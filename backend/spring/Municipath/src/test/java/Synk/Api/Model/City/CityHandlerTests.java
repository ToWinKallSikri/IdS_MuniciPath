package Synk.Api.Model.City;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;

import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.Post.PointHandler;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.User.UserHandler;

@SpringBootTest
public class CityHandlerTests {
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new CityHandler());
	}
	
	void testCreateCity() {
		MuniciPathMediator mediator = new MuniciPathMediator();
		CityHandler ch = new CityHandler();
		ch.setMediator(mediator);
		PointHandler ph = new PointHandler();
		ph.setMediator(mediator);
		UserHandler uh = new UserHandler(mediator);
		mediator.setCity(ch);
		mediator.setPoint(ph);
		mediator.setUser(uh);
		assertFalse(ch.createCity("civitanova", 62012, "twinkal", new Position(1, 2)));
	}
	
}
