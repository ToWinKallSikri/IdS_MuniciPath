package Synk.Api.Controller.SavedContent;


import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.PostType;
import Synk.Api.View.ViewModel.ProtoPost;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SavedContentHandlerTests {

	@Autowired
	SavedContentHandler sh;
	@Autowired
	private UserHandler uh;
	@Autowired
	private CityHandler ch;
	@Autowired
	private PointHandler poh;
	
	@Test
	public void testSaveUnsaveContent() {
		String cityId = "" + ("tokyo"+12345).hashCode();
		String user = "naruto", user2 = "sasuke";
		uh.addUser(user, "password");
		uh.addUser(user2, "password");
		uh.userValidation(user);
		uh.userValidation(user2);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		Position pos = new Position(10, 10);
		List<String> empty = new ArrayList<>();
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("parole");
		data1.setText("blablabla");
		data1.setType(PostType.SOCIAL);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		String postId = "655823757.75498433.0";
		assertTrue(sh.getSavedContent(user2).isEmpty());
		assertFalse(sh.saveContent(user2, postId));
		poh.createPost(user, pos, cityId, data1);
		assertTrue(sh.saveContent(user2, postId));
		assertFalse(sh.getSavedContent(user2).isEmpty());
		assertEquals(sh.getPartecipants(postId).get(0), user2);
		ch.deleteCity(cityId);
		uh.removeUser(user);
		uh.removeUser(user2);
	}
	
	@Test
	public void testNotifyEvent() {
		String cityId = "" + ("tokyo"+12345).hashCode();
		String user = "naruto", user2 = "sasuke";
		uh.addUser(user, "password");
		uh.addUser(user2, "password");
		uh.userValidation(user);
		uh.userValidation(user2);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		Position pos = new Position(10, 10);
		List<String> empty = new ArrayList<>();
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("Circo Divertente");
		data1.setText("Tante attrazioni! ;)");
		data1.setType(PostType.EVENT);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		data1.setStartTime(LocalDateTime.now().plusDays(10));
		data1.setEndTime(LocalDateTime.now().plusDays(20));
		String postId = "655823757.75498433.0";
		poh.createPost(user, pos, cityId, data1);
		sh.saveContent(user2, postId);
		uh.notifyEvent(user, "Accorrete numerosi!", postId);
		assertFalse(uh.getMyMessages(user2).isEmpty());
		ch.deleteCity(cityId);
		uh.removeUser(user);
		uh.removeUser(user2);
	}
}
