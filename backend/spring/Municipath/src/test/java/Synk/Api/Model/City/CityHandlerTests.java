package Synk.Api.Model.City;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CityHandlerTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new CityHandler());
	}
	
}
