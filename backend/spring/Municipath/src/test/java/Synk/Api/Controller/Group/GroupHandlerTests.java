package Synk.Api.Controller.Group;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.City;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.PostType;
import Synk.Api.View.Model.ProtoGroup;
import Synk.Api.View.Model.ProtoPost;
import jakarta.annotation.PostConstruct;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupHandlerTests {
	

	@Autowired
    private CityHandler ch;
	@Autowired
    private UserHandler uh;
	@Autowired
    private PointHandler poh;
	@Autowired
    private GroupHandler gh;
	@Autowired
	private PendingHandler peh;

	@PostConstruct
	public void init() {
		MuniciPathMediator mediator = new MuniciPathMediator();
		ch.setMediator(mediator);
		poh.setMediator(mediator);
		uh.setMediator(mediator);
		gh.setMediator(mediator);
		peh.setMediator(mediator);
		mediator.setCity(ch);
		mediator.setPoint(poh);
		mediator.setUser(uh);
		mediator.setGroup(gh);
		mediator.setPending(peh);
	}

	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new GroupHandler());
	}
	
	@Test
	void testCreateDeleteGroup() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		Position pos2 = new Position(11, 10);
		Position pos3 = new Position(12, 10);
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("statua");
		data1.setText("è bella.");
		data1.setType(PostType.TOURISTIC);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		poh.createPost(user, pos, id, data1);
		ProtoPost data2 = new ProtoPost();
		data2.setTitle("lago");
		data2.setText("è sporco.");
		data2.setType(PostType.TOURISTIC);
		data2.setPersistence(true);
		data2.setMultimediaData(empty);
		poh.createPost(user, pos2, id, data2);
		ProtoPost data3 = new ProtoPost();
		data3.setTitle("trattoria");
		data3.setText("si mangia.");
		data3.setType(PostType.HEALTHandWELLNESS);
		data3.setPersistence(true);
		data3.setMultimediaData(empty);
		poh.createPost(user, pos3, id, data3);
		City city = ch.getCity(id);
		List<String> postIds = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getId()).toList();
		ProtoGroup dataX = new ProtoGroup();
		dataX.setTitle("un giretto in centro");
		dataX.setPersistence(true);
		dataX.setSorted(true);
		dataX.setPosts(postIds);
		dataX.setStartTime(LocalDateTime.now());
		assertFalse(gh.createGroup(user, id, dataX));
		dataX.setStartTime(null);
		assertTrue(gh.createGroup(user, id, dataX));
		assertEquals(poh.getPost(postIds.get(0)).getGroups().get(0), "655823757.g.0");
		assertTrue(gh.removeGroup(user, id+".g.0"));
		ch.deleteCity(id);
		uh.removeUser(user);
	}
	
	@Test
	void testEditGroup() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		Position pos2 = new Position(11, 10);
		Position pos3 = new Position(12, 10);
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("statua");
		data1.setText("è bella.");
		data1.setType(PostType.TOURISTIC);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		poh.createPost(user, pos, id, data1);
		ProtoPost data2 = new ProtoPost();
		data2.setTitle("lago");
		data2.setText("è sporco.");
		data2.setType(PostType.TOURISTIC);
		data2.setPersistence(true);
		data2.setMultimediaData(empty);
		poh.createPost(user, pos2, id, data2);
		ProtoPost data3 = new ProtoPost();
		data3.setTitle("trattoria");
		data3.setText("si mangia.");
		data3.setType(PostType.HEALTHandWELLNESS);
		data3.setPersistence(true);
		data3.setMultimediaData(empty);
		poh.createPost(user, pos3, id, data3);
		City city = ch.getCity(id);
		List<String> postIds = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getId()).toList();
		ProtoGroup dataX = new ProtoGroup();
		dataX.setTitle("un giretto in centro");
		dataX.setPersistence(true);
		dataX.setSorted(true);
		dataX.setPosts(postIds);
		gh.createGroup(user, id, dataX);
		Position pos4 = new Position(13, 10);
		ProtoPost data4 = new ProtoPost();
		data4.setTitle("piazza nuovo");
		data4.setText("c'è un bel panorama.");
		data4.setType(PostType.SOCIAL);
		data4.setPersistence(true);
		data4.setMultimediaData(empty);
		poh.createPost(user, pos4, id, data4);
		postIds = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getId()).toList();
		assertTrue(gh.viewGroup(id+".g.0").getPosts().size() == 3);
		assertTrue(gh.viewGroup(id+".g.0").isSorted());
		dataX.setSorted(false);
		dataX.setPosts(postIds);
		assertTrue(gh.editGroup(id+".g.0", user, dataX));
		assertTrue(gh.viewGroup(id+".g.0").getPosts().size() == 4);
		assertFalse(gh.viewGroup(id+".g.0").isSorted());
		gh.removeGroup(user, id+".g.0");
		ch.deleteCity(id);
		uh.removeUser(user);
	}
	
	@Test
	void testEndingGroup() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		Position pos2 = new Position(11, 10);
		Position pos3 = new Position(12, 10);
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("statua");
		data1.setText("è bella.");
		data1.setType(PostType.TOURISTIC);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		poh.createPost(user, pos, id, data1);
		ProtoPost data2 = new ProtoPost();
		data2.setTitle("lago");
		data2.setText("è sporco.");
		data2.setType(PostType.TOURISTIC);
		data2.setPersistence(true);
		data2.setMultimediaData(empty);
		poh.createPost(user, pos2, id, data2);
		ProtoPost data3 = new ProtoPost();
		data3.setTitle("trattoria");
		data3.setText("si mangia.");
		data3.setType(PostType.HEALTHandWELLNESS);
		data3.setPersistence(true);
		data3.setMultimediaData(empty);
		poh.createPost(user, pos3, id, data3);
		City city = ch.getCity(id);
		List<String> postIds = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getId()).toList();
		ProtoGroup dataX = new ProtoGroup();
		dataX.setTitle("un giretto in centro");
		dataX.setPersistence(false);
		dataX.setSorted(true);
		dataX.setPosts(postIds);
		dataX.setStartTime(LocalDateTime.now().minusDays(5));
		dataX.setEndTime( LocalDateTime.now().minusDays(3));
		gh.createGroup(user, id, dataX);
		assertEquals(gh.viewGroup("655823757.g.0").getId(), "655823757.g.0");
		gh.checkEndingGroups();
		assertEquals(gh.viewGroup("655823757.g.0"), null);
		ch.deleteCity(id);
		uh.removeUser(user);
	}

}
