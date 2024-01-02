package Synk.Api.Controller;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MuniciPathRestControllerTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new MuniciPathRestController());
	}
	
}
