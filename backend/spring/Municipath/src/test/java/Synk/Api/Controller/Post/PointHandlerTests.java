package Synk.Api.Controller.Post;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import Synk.Api.View.ViewModel.ProtoPost;

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
    private PendingHandler peh;
	
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new PointHandler());
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
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("parole");
		data1.setText("blablabla");
		data1.setType(PostType.SOCIAL);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		assertFalse(poh.createPost("sakura", pos, id, data1));
		assertFalse(poh.createPost("sasuke", pos, id, data1));
		data1.setType(PostType.EVENT);
		assertFalse(poh.createPost("naruto", pos, id, data1));
		data1.setEndTime( LocalDateTime.now());
		data1.setStartTime(LocalDateTime.now().plusDays(1));
		assertFalse(poh.createPost("naruto", pos, id, data1));
		data1.setEndTime( LocalDateTime.now().plusDays(1));
		data1.setStartTime(LocalDateTime.now());
		assertTrue(poh.createPost("naruto", pos, id, data1));
		List<Point> list = poh.getPoints(id, user);
		assertTrue(list.size() == 2);
		ch.setRole("sasuke", id, Role.CONTR_NOT_AUTH);
		ProtoPost data6 = new ProtoPost();
		data6.setTitle("urlo da battaglia");
		data6.setText("aaaaaaaaa");
		data6.setType(PostType.HEALTHandWELLNESS);
		data6.setPersistence(true);
		data6.setMultimediaData(empty);
		assertTrue(poh.createPost("sasuke", pos2, id, data6));
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
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("parole");
		data1.setText("blablabla");
		data1.setType(PostType.SOCIAL);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		poh.createPost(user, pos, id, data1);
		String primeId = "655823757.-32504895.0";
		ProtoPost data2 = new ProtoPost();
		data2.setTitle("title");
		data2.setText("blablabla");
		data2.setType(PostType.SOCIAL);
		data2.setPersistence(true);
		data2.setMultimediaData(empty);
		assertFalse(poh.editPost(primeId, data2));
		String postId = "655823757.75498433.0";
		ProtoPost data3 = new ProtoPost();
		data3.setTitle("title");
		data3.setText("mmmmm");
		data3.setType(PostType.SOCIAL);
		data3.setPersistence(true);
		data3.setMultimediaData(empty);
		assertTrue(poh.editPost(postId, data3));
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
		assertEquals(primeId, post.getId());
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
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("parole");
		data1.setText("blablabla");
		data1.setType(PostType.SOCIAL);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		poh.createPost(user, pos, id, data1);
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
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("parole");
		data1.setText("blablabla");
		data1.setType(PostType.EVENT);
		data1.setPersistence(false);
		data1.setMultimediaData(empty);
		data1.setStartTime(LocalDateTime.now().minusDays(5));
		data1.setEndTime(LocalDateTime.now().minusDays(3));
		poh.createPost(user, pos, id, data1);
		assertTrue(poh.createPost("naruto", pos, id, data1));
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
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("Sfida foto!");
		data1.setText("vediamo chi riesce a farsi la foto più bella in piazza!");
		data1.setType(PostType.CONTEST);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		data1.setEndTime( LocalDateTime.now().plusDays(3));
		assertTrue(poh.createPost(user, pos, id, data1));
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
