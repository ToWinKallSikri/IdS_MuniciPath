package Synk.Api.Model.Post.Contribute;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContributeHandlerTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new ContributeHandler());
	}
	
}
