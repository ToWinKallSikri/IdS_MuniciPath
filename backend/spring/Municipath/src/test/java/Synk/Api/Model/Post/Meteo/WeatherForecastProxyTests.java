package Synk.Api.Model.Post.Meteo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WeatherForecastProxyTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new WeatherForecastProxy());
	}
	
}
