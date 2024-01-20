package Synk.Api.Controller.User;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Model.City.City;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.PostType;
import Synk.Api.View.ViewModel.ProtoPost;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserHandlerTests {

	@Autowired
	private UserHandler uh;
	@Autowired
	private CityHandler ch;
	@Autowired
	private PointHandler poh;
	@Autowired
	private PendingHandler peh;

	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new UserHandler());
	}

	@Test
	void testAddRemoveUser () {
		assertTrue(uh.addUser("naruto", "password"));
		uh.userValidation("naruto");
		assertFalse(uh.addUser("naruto", "newPassword"));
		assertTrue(uh.removeUser("naruto"));
		uh.removeUser("naruto");
	}

	@Test
	void testChangePassword() {
		uh.addUser("naruto", "password");
		uh.userValidation("naruto");
		assertTrue(uh.changePassword("naruto", "newPassword"));
		assertFalse(uh.changePassword("sasuke", "newPassword"));
		uh.removeUser("naruto");
	}

	@Test
	void testUserValidation() {
		uh.addUser("naruto", "password");
		assertTrue(uh.userValidation("naruto"));
		assertFalse(uh.userValidation("sasuke"));
		uh.removeUser("naruto");
	}

	@Test
	void testManageManager() {
		uh.addUser("ronaldinho", "password");
		uh.addUser("sasuke", "password");
		uh.userValidation("ronaldinho");
		assertTrue(uh.manageManager("ronaldinho", true));
		assertFalse(uh.manageManager("sasuke", true));
		uh.removeUser("ronaldinho");
		uh.removeUser("sasuke");
	}

	@Test
	void testGetUser() {
		uh.addUser("ronaldinho", "password");
		uh.userValidation("ronaldinho");
		assertEquals(uh.getUser("ronaldinho").getUsername(), "ronaldinho");
		uh.removeUser("ronaldinho");
	}

	@Test
	void testGetConvalidatedUser() {
		uh.addUser("goku", "password");
		uh.userValidation("goku");
		assertEquals(uh.getConvalidatedUser("goku").getUsername(), "goku");
		uh.removeUser("goku");
	}

	@Test
	void testGetNotConvalidatedUsers() {
		uh.addUser("goku", "password");
		uh.addUser("vegeta", "password");
		uh.addUser("junior", "password");
		uh.userValidation("goku");
		assertEquals(uh.getNotConvalidatedUsers().size(), 2);
		uh.removeUser("goku");
		uh.removeUser("vegeta");
		uh.removeUser("junior");
	}

	@Test
	void testMatchCurator() {
		uh.addUser("goku", "password");
		uh.addUser("vegeta", "password");
		uh.userValidation("goku");
		assertTrue(uh.matchCurator("goku", "12345"));
		assertFalse(uh.matchCurator("goku", "12345"));
		assertFalse(uh.matchCurator("vegeta", "12345"));
		uh.removeUser("goku");
		uh.removeUser("vegeta");
	}

	@Test
	void testChangeCurator() {
		uh.addUser("goku", "password");
		uh.addUser("vegeta", "password");
		uh.userValidation("goku");
		uh.userValidation("vegeta");
		assertTrue(uh.matchCurator("goku", "12345"));
		assertFalse(uh.changeCurator("goku", "12345"));
		assertTrue(uh.changeCurator("vegeta", "12345"));
		uh.removeUser("goku");
		uh.removeUser("vegeta");
	}

	@Test
	void testDiscreditCurator() {
		uh.addUser("goku", "password");
		uh.userValidation("goku");
		Position p = new Position(1, 2);
		String id = "" + ("tokyo"+12345).hashCode();
		City city = new City(id, "tokyo", p, null, 12345);
		assertTrue(uh.matchCurator("goku", "12345"));
		uh.discreditCurator("12345");
        assertNull(city.getCurator());
		uh.removeUser("goku");
	}

	@Test
	void testUsernameExists() {
		uh.addUser("goku", "password");
		assertTrue(uh.usernameExists("goku"));
		assertFalse(uh.usernameExists("vegeta"));
		uh.removeUser("goku");
	}
	
	@Test
	void testFollowingUnfollowing() {
		String cityId = "" + ("tokyo"+12345).hashCode(), user = "naruto", user2 = "sasuke";
		uh.addUser(user, "password");
		uh.addUser(user2, "password");
		uh.userValidation(user);
		uh.userValidation(user2);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		String primeId = "655823757.-32504895.0";
		assertFalse(uh.alreadyFollowing(user2, primeId));
		assertTrue(uh.follow(user2, primeId));
		assertFalse(uh.follow(user2, primeId));
		assertTrue(uh.alreadyFollowing(user2, primeId));
		assertTrue(uh.alreadyFollowingCity(user2, cityId));
		assertFalse(uh.alreadyFollowingContributor(user2, user));
		assertTrue(uh.followContributor(user2, user));
		assertTrue(uh.unfollowCity(user2, cityId));
		assertFalse(uh.alreadyFollowing(user2, primeId));
		assertFalse(uh.alreadyFollowingCity(user2, cityId));
		assertTrue(uh.alreadyFollowingContributor(user2, user));
		ch.deleteCity(cityId);
		uh.removeUser(user);
		uh.removeUser(user2);
	}
	
	@Test
	public void testNotifyCreation() {
		String cityId = "" + ("tokyo"+12345).hashCode();
		String user = "naruto", user2 = "sasuke", user3 = "sakura";
		uh.addUser(user, "password");
		uh.addUser(user2, "password");
		uh.addUser(user3, "password");
		uh.userValidation(user);
		uh.userValidation(user2);
		uh.userValidation(user3);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		uh.followContributor(user3, user);
		uh.followCity(user2, cityId);
		Position pos = new Position(10, 10);
		List<String> empty = new ArrayList<>();
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("parole");
		data1.setText("blablabla");
		data1.setType(PostType.SOCIAL);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		assertTrue(uh.getMyMessages(user2).isEmpty());
		assertTrue(uh.getMyMessages(user3).isEmpty());
		poh.createPost(user, pos, cityId, data1);
		assertFalse(uh.getMyMessages(user2).isEmpty());
		assertTrue(uh.getMyMessages(user3).isEmpty());
		ch.deleteCity(cityId);
		uh.removeUser(user);
		uh.removeUser(user2);
		uh.removeUser(user3);
	}
	
	@Test
	void testNotification() {
		String cityId = "" + ("tokyo"+12345).hashCode();
		String user = "naruto", user2 = "sasuke";
		uh.addUser(user, "password");
		uh.addUser(user2, "password");
		uh.userValidation(user);
		uh.userValidation(user2);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		ch.setRole(user2, cityId, Role.CONTR_NOT_AUTH);
		Position pos = new Position(10, 10);
		List<String> empty = new ArrayList<>();
		ProtoPost data1 = new ProtoPost();
		data1.setTitle("parole");
		data1.setText("blablabla");
		data1.setType(PostType.SOCIAL);
		data1.setPersistence(true);
		data1.setMultimediaData(empty);
		poh.createPost(user2, pos, cityId, data1);
		String postId = "655823757.75498433.0";
		assertTrue(uh.getMyMessages(user2).isEmpty());
		peh.judge(postId, true, "Davvero bella idea!");
		assertFalse(uh.getMyMessages(user2).isEmpty());
		data1.setTitle("PAROLE");
		poh.editPost(postId, data1);
		assertEquals(uh.getMyMessages(user2).size(), 2);
		poh.deletePost(postId);
		assertEquals(uh.getMyMessages(user2).size(), 3);
		ch.deleteCity(cityId);
		uh.removeUser(user);
		uh.removeUser(user2);
	}

	
}