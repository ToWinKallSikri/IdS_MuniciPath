package Synk.Api.Controller.Post;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import jakarta.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PointHandlerTests {
	
	
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
		assertDoesNotThrow(() -> new PointHandler());
	}
	
	@Test
	void testCityCreation() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		assertTrue(poh.addNewCity(id));
		assertFalse(poh.addNewCity(id));
		assertDoesNotThrow(() -> poh.deleteCityPoints(id));
		assertTrue(poh.addNewCity(id));
		assertFalse(poh.addNewCity(id));
		poh.deleteCityPoints(id);
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<Point> list = poh.getPoints(id, user);
		assertFalse(list.isEmpty());
		ch.deleteCity(id);
		uh.removeUser(user);
	}

	@Test
	void testPostCreation() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		uh.addUser("sasuke", "password");
		uh.userValidation("sasuke");
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		Position pos2 = new Position(20, 10);
		assertFalse(poh.createPost("parole", PostType.SOCIAL, "blablabla", "sakura", pos, id, empty, null, null, true));
		assertFalse(poh.createPost("parole", PostType.SOCIAL, "blablabla", "sasuke", pos, id, empty, null, null, true));
		assertFalse(poh.createPost("parole", PostType.EVENT, "blablabla", "naruto", pos, id, empty, null, null, true));
		assertFalse(poh.createPost("parole", PostType.EVENT, "blablabla", "naruto", pos, id, empty, LocalDateTime.now().plusDays(1), LocalDateTime.now(), true));
		assertTrue(poh.createPost("parole", PostType.EVENT, "blablabla", "naruto", pos, id, empty, LocalDateTime.now(), LocalDateTime.now().plusDays(1), true));
		List<Point> list = poh.getPoints(id, user);
		assertTrue(list.size() == 2);
		ch.setRole("sasuke", id, Role.CONTR_NOT_AUTH);
		assertTrue(poh.createPost("urlo da battaglia", PostType.HEALTHandWELLNESS, "aaaaaaaaa", "sasuke", pos2, id, empty, null, null, true));
		list = poh.getPoints(id, user);
		assertTrue(list.size() == 2);
		String pendingId = peh.getAllRequest(id).get(0).getId();
		peh.judge(pendingId, false, "");
		ch.deleteCity(id);
		uh.removeUser(user);
		uh.removeUser("sasuke");
	}
	
	@Test
	void testPostEditing() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		Position pos = new Position(10, 10);
		List<String> empty = new ArrayList<>();
		poh.createPost("parole", PostType.SOCIAL, "blablabla", user, pos, id, empty, null, null, true);
		String primeId = "655823757.-32504895.0";
		assertFalse(poh.editPost(primeId, "title", PostType.SOCIAL, "blablabla", user, id, empty, null, null, true));
		String postId = "655823757.75498433.0";
		assertTrue(poh.editPost(postId, "title", PostType.SOCIAL, "mmmmm", user, id, empty, null, null, true));
		Post post = poh.getPost(postId);
		assertEquals(post.getTitle(), "title");
		assertEquals(post.getText(), "mmmmm");
		ch.deleteCity(id);
		uh.removeUser(user);
	}
	
	@Test
	void testPostGetting() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		String primeId = "655823757.-32504895.0";
		Post post = this.poh.getPost(primeId);
		assertEquals(primeId, post.getPostId());
		ch.deleteCity(id);
		uh.removeUser(user);
	}
	
	@Test
	void testPostRemoving() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		Position pos = new Position(10, 10);
		List<String> empty = new ArrayList<>();
		poh.createPost("parole", PostType.SOCIAL, "blablabla", user, pos, id, empty, null, null, true);
		String postId = "655823757.75498433.0";
		uh.addUser("sasuke", "password");
		uh.userValidation("sasuke");
		assertFalse(poh.deletePost(postId, "obito"));
		assertFalse(poh.deletePost(postId, "sasuke"));
		assertTrue(poh.deletePost(postId, user));
		ch.deleteCity(id);
		uh.removeUser(user);
	}
	
	@Test
	void testCheckEnding() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		assertTrue(poh.createPost("parole", PostType.EVENT, "blablabla", "naruto", pos, id, empty, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3), false));
		List<Point> list = poh.getPoints(id, user);
		assertEquals(list.size(), 2);
		poh.checkEndingPosts();
		list = poh.getPoints(id, user);
		assertEquals(list.size(), 1);
		ch.deleteCity(id);
		uh.removeUser(user);
	}
	
	@Test
	void testContest() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto", user2 = "sasuke";
		uh.addUser(user, "password");
		uh.userValidation(user);
		uh.addUser(user2, "password");
		uh.userValidation(user2);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		assertTrue(poh.createPost("Sfida foto!", PostType.CONTEST, "vediamo chi riesce a farsi la foto più bella in piazza!",
				user, pos, id, empty, null, LocalDateTime.now().plusDays(3), true));
		String contestId = "655823757.75498433.0";
		List<String> content = new ArrayList<>();
		content.add("foto1.png");
		content.add("foto2.png");
		content.add("foto3.png");
		assertTrue(poh.addContentToContest(user2, contestId, content));
		List<String> content2 = new ArrayList<>();
		content.add("foto4.png");
		content.add("foto5.png");
		content.add("foto6.png");
		assertFalse(poh.addContentToContest(user2, contestId, content2));
		poh.deletePost(contestId);
		ch.deleteCity(id);
		uh.removeUser(user);
		uh.removeUser(user2);
	}
	
	
	
	
	
}
