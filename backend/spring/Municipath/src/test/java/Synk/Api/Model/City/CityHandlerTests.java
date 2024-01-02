package Synk.Api.Model.City;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.Group.GroupHandler;
import Synk.Api.Model.Post.PointHandler;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.User.UserHandler;
import jakarta.annotation.PostConstruct;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityHandlerTests {
	
	@Autowired
    private CityHandler ch;
	@Autowired
    private UserHandler uh;
	@Autowired
    private PointHandler ph;
	@Autowired
    private GroupHandler gh;
	
	@PostConstruct
	public void init() {
		MuniciPathMediator mediator = new MuniciPathMediator();
		ch.setMediator(mediator);
		ph.setMediator(mediator);
		uh.setMediator(mediator);
		gh.setMediator(mediator);
		mediator.setCity(ch);
		mediator.setPoint(ph);
		mediator.setUser(uh);
		mediator.setGroup(gh);
	}
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new CityHandler());
	}
	
	@Test
	void testCreateDeleteCity() {
		assertFalse(ch.createCity("civitanova", 62012, "twinkal", new Position(1, 2)));
		uh.addUser("twinkal", "password");
		uh.userValidation("twinkal");
		String id = "" + ("civitanova"+62012).hashCode();
		assertFalse(ch.deleteCity(id));
		assertTrue(ch.createCity("civitanova", 62012, "twinkal", new Position(1, 2)));
		ch.getCities().forEach(c -> System.out.println( c.getId() ));
		assertTrue(ch.deleteCity(id));
	}
	
}
