package Synk.Api.Model.Pending;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PendingHandlerTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new PendingHandler(null));
	}
	
}
