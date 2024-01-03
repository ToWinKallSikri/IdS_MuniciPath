package Synk.Api.Model.User;

import static org.junit.jupiter.api.Assertions.*;

import Synk.Api.Model.City.City;
import Synk.Api.Model.Post.Position;
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
		assertTrue(uh.changePassowrd("naruto", "newPassword"));
		assertFalse(uh.changePassowrd("sasuke", "newPassword"));
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
		Position p = new Position(1, 2);
		String id = "" + ("tokyo"+12345).hashCode();
		City city = new City(id, "tokyo", p, null, 12345);
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
		Position p = new Position(1, 2);
		String id = "" + ("tokyo"+12345).hashCode();
		City city = new City(id, "tokyo", p, null, 12345);
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

	
}