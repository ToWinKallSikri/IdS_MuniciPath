package Synk.Api.Model.User;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class UserHandlerTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new UserHandler(null));
	}
	
}