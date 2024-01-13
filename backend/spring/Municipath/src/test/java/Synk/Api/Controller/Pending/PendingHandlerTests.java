package Synk.Api.Controller.Pending;
import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.City;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.PostType;
import Synk.Api.View.Model.ProtoPost;
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
		assertDoesNotThrow(() -> new PendingHandler());
	}

	@Test
	void testAcceptRequest() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		uh.addUser("sasuke", "password");
		uh.userValidation("sasuke");
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		ch.setRole("sasuke", id, Role.CONTR_NOT_AUTH);
		ProtoPost data1 = new ProtoPost();
		data1.title = "urlo da battaglia";
		data1.text = "aaaaaaaaa";
		data1.type = PostType.HEALTHandWELLNESS;
		data1.persistence = true;
		data1.multimediaData = empty;
		poh.createPost("sasuke", pos, id, data1);
		assertTrue(this.peh.getAllRequest(id).size() == 1);
		String pendingId = peh.getAllRequest(id).get(0).getId();
		peh.judge(pendingId, true, "");
		assertTrue(this.peh.getAllRequest(id).isEmpty());
		assertTrue(this.poh.getPost(pendingId).isPublished());
		ch.deleteCity(id);
		uh.removeUser(user);
		uh.removeUser("sasuke");
	}
	
	
	@Test
	void testRejectRequest() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		uh.addUser("sasuke", "password");
		uh.userValidation("sasuke");
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		ch.setRole("sasuke", id, Role.CONTR_NOT_AUTH);
		ProtoPost data1 = new ProtoPost();
		data1.title = "urlo da battaglia";
		data1.text = "aaaaaaaaa";
		data1.type = PostType.HEALTHandWELLNESS;
		data1.persistence = true;
		data1.multimediaData = empty;
		poh.createPost("sasuke", pos, id, data1);
		assertTrue(this.peh.getAllRequest(id).size() == 1);
		String pendingId = peh.getAllRequest(id).get(0).getId();
		peh.judge(pendingId, false, "");
		assertTrue(this.peh.getAllRequest(id).isEmpty());
		assertEquals(this.poh.getPost(pendingId), null);
		ch.deleteCity(id);
		uh.removeUser(user);
		uh.removeUser("sasuke");
	}

	@Test
	void testAddPostRequest() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		uh.addUser("sasuke", "password");
		uh.userValidation("sasuke");
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		ch.setRole("sasuke", id, Role.CONTR_NOT_AUTH);
		ProtoPost data1 = new ProtoPost();
		data1.title = "urlo da battaglia";
		data1.text = "aaaaaaaaa";
		data1.type = PostType.HEALTHandWELLNESS;
		data1.persistence = true;
		data1.multimediaData = empty;
		poh.createPost("sasuke", pos, id, data1);
		String pendingId = peh.getAllRequest(id).get(0).getId();
		peh.judge(pendingId, true, "");
		ProtoPost data2 = new ProtoPost();
		data2.title = "Urlo da Battaglia!";
		data2.text = "AAAAAAAAAA";
		data2.type = PostType.SOCIAL;
		data2.persistence = true;
		data2.multimediaData = empty;
		poh.editPost(pendingId, "sasuke", id, data2);
		assertEquals(this.poh.getPost(pendingId).getText(), "aaaaaaaaa");
		assertEquals(this.poh.getPost(pendingId).getType(), PostType.HEALTHandWELLNESS);
		assertEquals(this.poh.getPost(pendingId).getTitle(), "urlo da battaglia");
		assertTrue(this.peh.getAllRequest(id).size() == 1);
		peh.judge(pendingId, true, "");
		assertTrue(this.peh.getAllRequest(id).isEmpty());
		assertEquals(this.poh.getPost(pendingId).getText(), "AAAAAAAAAA");
		assertEquals(this.poh.getPost(pendingId).getType(), PostType.SOCIAL);
		assertEquals(this.poh.getPost(pendingId).getTitle(), "Urlo da Battaglia!");
		ch.deleteCity(id);
		uh.removeUser(user);
		uh.removeUser("sasuke");
	}

	@Test
	void testAddGroupRequest() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto", user2 = "sasuke";
		uh.addUser(user, "password");
		uh.userValidation(user);
		uh.addUser(user2, "password");
		uh.userValidation(user2);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		ch.setRole(user2, id, Role.CONTR_NOT_AUTH);
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		Position pos2 = new Position(11, 10);
		Position pos3 = new Position(12, 10);
		ProtoPost data1 = new ProtoPost();
		data1.title = "statua";
		data1.text = "è bella.";
		data1.type = PostType.TOURISTIC;
		data1.persistence = true;
		data1.multimediaData = empty;
		poh.createPost(user, pos, id, data1);
		ProtoPost data2 = new ProtoPost();
		data2.title = "lago";
		data2.text = "è sporco.";
		data2.type = PostType.TOURISTIC;
		data2.persistence = true;
		data2.multimediaData = empty;
		poh.createPost(user, pos2, id, data2);
		ProtoPost data3 = new ProtoPost();
		data3.title = "trattoria";
		data3.text = "si mangia.";
		data3.type = PostType.HEALTHandWELLNESS;
		data3.persistence = true;
		data3.multimediaData = empty;
		poh.createPost(user, pos3, id, data3);
		City city = ch.getCity(id);
		List<String> postIds = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getPostId()).toList();
		gh.createGroup("un giretto in centro", user2, true, id, postIds, null, null, true);
		assertTrue(poh.getPost(postIds.get(0)).getGroups().isEmpty());
		peh.judge(id+".g.0", true, "");
		assertFalse(poh.getPost(postIds.get(0)).getGroups().isEmpty());
		Position pos4 = new Position(13, 10);
		ProtoPost data4 = new ProtoPost();
		data4.title = "piazza nuovo";
		data4.text = "c'è un bel panorama.";
		data4.type = PostType.SOCIAL;
		data4.persistence = true;
		data4.multimediaData = empty;
		poh.createPost(user, pos4, id, data4);
		List<String> postIds2 = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getPostId()).toList();
		assertTrue(gh.viewGroup(id+".g.0").getPosts().size() == 3);
		assertTrue(gh.viewGroup(id+".g.0").isSorted());
		assertTrue(gh.editGroup(id+".g.0", "un giretto in centro", user2, false, postIds2, null, null, true));
		assertTrue(gh.viewGroup(id+".g.0").getPosts().size() == 3);
		assertTrue(gh.viewGroup(id+".g.0").isSorted());
		peh.judge(id+".g.0", true, "");
		assertFalse(gh.viewGroup(id+".g.0").isSorted());
		assertTrue(gh.viewGroup(id+".g.0").getPosts().size() == 4);
		gh.removeGroup(user, id+".g.0");
		ch.deleteCity(id);
		uh.removeUser(user);
		uh.removeUser(user2);
	}
}



