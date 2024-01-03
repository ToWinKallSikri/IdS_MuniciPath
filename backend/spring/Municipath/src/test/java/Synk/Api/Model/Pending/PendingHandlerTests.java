package Synk.Api.Model.Pending;
import Synk.Api.Model.Group.Group;
import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PendingHandlerTests {

	@Autowired
	private PendingHandler penh;

	@PostConstruct
	public void init() {
		MuniciPathMediator mediator = new MuniciPathMediator();
		penh.setMediator(mediator);
		mediator.setPending(penh);
	}

	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new PendingHandler());
	}

	@Test
	void testAddRequest() {
		PendingRequest pr = new PendingRequest("1");
		assertDoesNotThrow(() -> penh.addRequest("1"));
	}

	
	void testAddPostRequest() {
		Position p1 = new Position(1, 1);
		Point point1 = new Point("1", p1, "1");
		Post post1 = new Post("Punto Panoramico", PostType.TOURISTIC,
				"Ottimo luogo per ammirare la skyline della città", "Shaun Murphy",
				p1, "1", point1.getNewPostId(), null, false, null, null, false);
		PendingRequest pr = new PendingRequest();
		pr.setId(post1.getPostId());
		pr.setTitle(post1.getTitle());
		pr.setText(post1.getText());
		pr.setPersistence(post1.isPersistence());
		pr.setType(post1.getType());
		pr.setData(post1.getMultimediaData());
		pr.setStartTime(post1.getStartTime());
		pr.setEndTime(post1.getEndTime());
		assertDoesNotThrow(() -> penh.addPostRequest(pr.getId(), pr.getTitle(), pr.getType(), pr.getText(), pr.getData(),
				pr.getStartTime(), pr.getEndTime(), pr.isPersistence()));
		assertTrue(penh.judge(pr.getId(), true, "ok"));
	}

	
	void testAddGroupRequest() {
		Position p1 = new Position(1, 1);
		Point point1 = new Point("1", p1, "1");
		Post post1 = new Post("Punto Panoramico", PostType.TOURISTIC,
				"Ottimo luogo per ammirare la skyline della città", "Shaun Murphy",
				p1, "1", point1.getNewPostId(), null, false, null, null, false);
		Post post2 = new Post("Punto Storico", PostType.TOURISTIC,
				"Luogo simbolico della città, offre la possibilità di conoscere il background" +
						"storico della città", "Aaron Glassman",
				p1, "1", point1.getNewPostId(), null, false, null, null, false);
		List<String> postIds = new ArrayList<>();
		postIds.add(post1.getPostId());
		postIds.add(post2.getPostId());
		Group g1 = new Group();
		g1.setId("1.g.1");
		g1.setTitle("Gruppo 1");
		g1.setSorted(false);
		g1.setPersistence(false);
		g1.setAuthor("Lea DiLallo");
		g1.setCityId("1");
		g1.setPublished(false);
		g1.setStartTime(null);
		g1.setEndTime(null);
		PendingRequest pr2 = new PendingRequest();
		pr2.setId(g1.getId());
		pr2.setTitle(g1.getTitle());
		pr2.setSorted(g1.isSorted());
		pr2.setPersistence(g1.isPersistence());
		pr2.setData(g1.getPosts());
		pr2.setStartTime(g1.getStartTime());
		pr2.setEndTime(g1.getEndTime());
		assertDoesNotThrow(() -> penh.addGroupRequest(pr2.getId(), pr2.getTitle(), pr2.isSorted(), pr2.getData(),
				pr2.getStartTime(), pr2.getEndTime(), pr2.isPersistence()));
		assertTrue(penh.judge(pr2.getId(), true, "ok"));
	}
}
