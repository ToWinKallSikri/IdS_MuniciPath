package Synk.Api.Model.User;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



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


	
}