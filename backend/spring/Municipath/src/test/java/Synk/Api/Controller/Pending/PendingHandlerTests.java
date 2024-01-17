package Synk.Api.Controller.Pending;
import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.City;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.PostType;
import Synk.Api.ViewModel.ProtoGroup;
import Synk.Api.ViewModel.ProtoPost;

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
		data1.setTitle("urlo da battaglia");
		data1.setText("aaaaaaaaa");
		data1.setType(PostType.HEALTHandWELLNESS);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
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
		data1.setTitle("urlo da battaglia");
		data1.setText("aaaaaaaaa");
		data1.setType(PostType.HEALTHandWELLNESS);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
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
		data1.setTitle("urlo da battaglia");
		data1.setText("aaaaaaaaa");
		data1.setType(PostType.HEALTHandWELLNESS);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		poh.createPost("sasuke", pos, id, data1);
		String pendingId = peh.getAllRequest(id).get(0).getId();
		peh.judge(pendingId, true, "");
		ProtoPost data2 = new ProtoPost();
		data2.setTitle("Urlo da Battaglia!");
		data2.setText("AAAAAAAAAA");
		data2.setType(PostType.SOCIAL);
		data2.setPersistence(true);
		data2.setMultimediaData(empty);
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
		gh.createGroup(user2, id, dataX);
		assertTrue(poh.getPost(postIds.get(0)).getGroups().isEmpty());
		peh.judge(id+".g.0", true, "");
		assertFalse(poh.getPost(postIds.get(0)).getGroups().isEmpty());
		Position pos4 = new Position(13, 10);
		ProtoPost data4 = new ProtoPost();
		data4.setTitle("piazza nuovo");
		data4.setText("c'è un bel panorama.");
		data4.setType(PostType.SOCIAL);
		data4.setPersistence(true);
		data4.setMultimediaData(empty);
		poh.createPost(user, pos4, id, data4);
		List<String> postIds2 = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getId()).toList();
		assertTrue(gh.viewGroup(id+".g.0").getPosts().size() == 3);
		assertTrue(gh.viewGroup(id+".g.0").isSorted());
		dataX.setPosts(postIds2);
		dataX.setSorted(false);
		assertTrue(gh.editGroup(id+".g.0", user2, dataX));
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



