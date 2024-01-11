package Synk.Api.Controller.Group;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.Post.PostHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.City;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.PostType;
import jakarta.annotation.PostConstruct;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupHandlerTests {
	

	@Autowired
    private CityHandler ch;
	@Autowired
    private UserHandler uh;
	@Autowired
    private PostHandler poh;
	@Autowired
    private GroupHandler gh;
	@Autowired
	private PendingHandler peh;

	@PostConstruct
	public void init() {
		MuniciPathMediator mediator = new MuniciPathMediator();
		ch.setMediator(mediator);
		poh.setMediator(mediator);
		uh.setMediator(mediator);
		gh.setMediator(mediator);
		peh.setMediator(mediator);
		mediator.setCity(ch);
		mediator.setPoint(poh);
		mediator.setUser(uh);
		mediator.setGroup(gh);
		mediator.setPending(peh);
	}

	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new GroupHandler());
	}
	
	@Test
	void testCreateDeleteGroup() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		Position pos2 = new Position(11, 10);
		Position pos3 = new Position(12, 10);
		poh.createPost("statua", PostType.TOURISTIC, "è bella.", user, pos, id, empty, null, null, true);
		poh.createPost("lago", PostType.TOURISTIC, "è sporco.", user, pos2, id, empty, null, null, true);
		poh.createPost("trattoria", PostType.HEALTHandWELLNESS, "si mangia.", user, pos3, id, empty, null, null, true);
		City city = ch.getCity(id);
		List<String> postIds = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getPostId()).toList();
		assertFalse(gh.createGroup("un giretto in centro", user, true, id, postIds, LocalDateTime.now(), null, true));
		assertTrue(gh.createGroup("un giretto in centro", user, true, id, postIds, null, null, true));
		assertEquals(poh.getPost(postIds.get(0)).getGroups().get(0), "655823757.g.0");
		assertTrue(gh.removeGroup(user, id+".g.0"));
		ch.deleteCity(id);
		uh.removeUser(user);
	}
	
	@Test
	void testEditGroup() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		Position pos2 = new Position(11, 10);
		Position pos3 = new Position(12, 10);
		poh.createPost("statua", PostType.TOURISTIC, "è bella.", user, pos, id, empty, null, null, true);
		poh.createPost("lago", PostType.TOURISTIC, "è sporco.", user, pos2, id, empty, null, null, true);
		poh.createPost("trattoria", PostType.HEALTHandWELLNESS, "si mangia.", user, pos3, id, empty, null, null, true);
		City city = ch.getCity(id);
		List<String> postIds = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getPostId()).toList();
		gh.createGroup("un giretto in centro", user, true, id, postIds, null, null, true);
		Position pos4 = new Position(13, 10);
		poh.createPost("piazza nuovo", PostType.SOCIAL, "c'è un bel panorama.", user, pos4, id, empty, null, null, true);
		postIds = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getPostId()).toList();
		assertTrue(gh.viewGroup(id+".g.0").getPosts().size() == 3);
		assertTrue(gh.viewGroup(id+".g.0").isSorted());
		assertTrue(gh.editGroup(id+".g.0", "un giretto in centro", user, false, postIds, null, null, true));
		assertTrue(gh.viewGroup(id+".g.0").getPosts().size() == 4);
		assertFalse(gh.viewGroup(id+".g.0").isSorted());
		gh.removeGroup(user, id+".g.0");
		ch.deleteCity(id);
		uh.removeUser(user);
	}
	
	@Test
	void testEndingGroup() {
		String id = "" + ("tokyo"+12345).hashCode(), user = "naruto";
		uh.addUser(user, "password");
		uh.userValidation(user);
		ch.createCity("tokyo", 12345, user, new Position(1, 2));
		List<String> empty = new ArrayList<>();
		Position pos = new Position(10, 10);
		Position pos2 = new Position(11, 10);
		Position pos3 = new Position(12, 10);
		poh.createPost("statua", PostType.TOURISTIC, "è bella.", user, pos, id, empty, null, null, true);
		poh.createPost("lago", PostType.TOURISTIC, "è sporco.", user, pos2, id, empty, null, null, true);
		poh.createPost("trattoria", PostType.HEALTHandWELLNESS, "si mangia.", user, pos3, id, empty, null, null, true);
		City city = ch.getCity(id);
		List<String> postIds = this.poh.getPoints(id, user).stream()
				.filter(p -> !p.getPos().equals(city.getPos()))
				.map(p -> p.getPosts().get(0).getPostId()).toList();
		gh.createGroup("un giretto in centro", user, true, id, postIds, 
				LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3), false);
		assertEquals(gh.viewGroup("655823757.g.0").getId(), "655823757.g.0");
		gh.checkEndingGroups();
		assertEquals(gh.viewGroup("655823757.g.0"), null);
		ch.deleteCity(id);
		uh.removeUser(user);
	}

}
