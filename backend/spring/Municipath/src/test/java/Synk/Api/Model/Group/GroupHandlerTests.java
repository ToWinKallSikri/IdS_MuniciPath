package Synk.Api.Model.Group;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GroupHandlerTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new GroupHandler());
	}
	
}
