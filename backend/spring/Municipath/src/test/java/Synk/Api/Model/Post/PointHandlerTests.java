package Synk.Api.Model.Post;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PointHandlerTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new PointHandler());
	}
	
}
