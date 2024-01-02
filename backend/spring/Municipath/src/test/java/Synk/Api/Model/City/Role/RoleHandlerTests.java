package Synk.Api.Model.City.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoleHandlerTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new RoleHandler());
	}
	
}
