package Synk.Api.Controller.City;
import Synk.Api.Controller.City.Report.ReportHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Model.City.Report.Report;
import Synk.Api.Model.Post.PostType;
import Synk.Api.View.ViewModel.ProtoPost;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.City;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.City.Role.RoleRequest;
import Synk.Api.Model.Post.Position;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityHandlerTests {
	
	@Autowired
    private CityHandler ch;
	@Autowired
    private UserHandler uh;
    @Autowired
    private PointHandler poh;
    @Autowired
    private ReportHandler rh;
	
	
	@Test
	void testInitialization() {
		assertDoesNotThrow(() -> new CityHandler());
	}
	
	@Test
	void testCreateDeleteCity() {
		assertFalse(ch.createCity("tokyo", 12345, "naruto", new Position(1, 2)));
		uh.addUser("naruto", "password");
		uh.userValidation("naruto");
		String id = "" + ("tokyo"+12345).hashCode();
		assertFalse(ch.deleteCity(id));
		assertTrue(ch.createCity("tokyo", 12345, "naruto", new Position(1, 2)));
		uh.addUser("sasuke", "password");
		uh.userValidation("sasuke");
		assertFalse(ch.createCity("tokyo", 12345, "sasuke", new Position(1, 2)));
		assertTrue(ch.deleteCity(id));
		uh.removeUser("sasuke");
		uh.removeUser("naruto");
	}
	
	@Test
	void testGetCity() {
		uh.addUser("naruto", "password");
		uh.userValidation("naruto");
		String id = "" + ("tokyo"+12345).hashCode();
		assertTrue(ch.getCity(id) == null);
		ch.createCity("tokyo", 12345, "naruto", new Position(1, 2));
		assertFalse(ch.getCity(id) == null);
		City city = ch.getCity(id);
		assertEquals(ch.getCities("Tokyo").get(0), city);
		ch.deleteCity(id);
		uh.removeUser("naruto");
	}
	
	@Test
	void testUpdateCity() {
		uh.addUser("naruto", "password");
		uh.userValidation("naruto");
		uh.addUser("sasuke", "password");
		uh.userValidation("sasuke");
		String id = "" + ("tokyo"+12345).hashCode();
		Position p1 = new Position(1, 2);
		Position p2 = new Position(2, 1);
		assertFalse(ch.updateCity(id, "berlino", 54321, "sasuke", p2));
		ch.createCity("tokyo", 12345, "naruto", p1);
		City city = ch.getCity(id);
		assertEquals(city.getPos(), p1);
		assertEquals(city.getCap(), 12345);
		assertEquals(city.getName(), "tokyo");
		assertEquals(city.getCurator(), "naruto");
		assertTrue(ch.updateCity(id, "berlino", 54321, "sasuke", p2));
		city = ch.getCity(id);
		assertEquals(city.getPos(), p2);
		assertEquals(city.getCap(), 54321);
		assertEquals(city.getName(), "berlino");
		assertEquals(city.getCurator(), "sasuke");
		ch.deleteCity(id);
		uh.removeUser("naruto");
		uh.removeUser("sasuke");
	}
	
	@Test
	void testRoles() {
		uh.addUser("naruto", "password");
		uh.userValidation("naruto");
		uh.addUser("sasuke", "password");
		uh.userValidation("sasuke");
		uh.addUser("sakura", "password");
		uh.userValidation("sakura");
		uh.addUser("jiraya", "password");
		uh.userValidation("jiraya");
		String id = "" + ("tokyo"+12345).hashCode();
		Position pos = new Position(1, 2);
		ch.createCity("tokyo", 12345, "naruto", pos);
		assertEquals(ch.getRole("naruto", id), Role.CURATOR);
		assertEquals(ch.getRole("naruto", id), Role.CURATOR);
		assertFalse(ch.setRole("sakura", id, Role.CURATOR));
		assertFalse(ch.setRole("sakura", id, Role.MODERATOR));
		assertTrue(ch.setRole("sakura", id, Role.CONTR_NOT_AUTH));
		assertTrue(ch.setRole("jiraya", id, Role.CONTR_AUTH));
		assertEquals(ch.getRole("sakura", id), Role.CONTR_NOT_AUTH);
		assertNotEquals(ch.getRole(id, "sasuke"), Role.CONTR_AUTH);
		assertEquals(ch.getRole("jiraya", id), Role.CONTR_AUTH);
		assertFalse(ch.addModerator("naruto", id));
		assertTrue(ch.addModerator("sasuke", id));
		assertTrue(ch.removeModerator("sasuke", id));
		assertFalse(ch.removeModerator("sasuke", id));
		ch.deleteCity(id);
		uh.removeUser("naruto");
		uh.removeUser("sasuke");
		uh.removeUser("jiraya");
		uh.removeUser("sakura");
	}
	
	@Test
	void testRequests() {
		uh.addUser("naruto", "password");
		uh.userValidation("naruto");
		uh.addUser("sasuke", "password");
		uh.userValidation("sasuke");
		uh.addUser("sakura", "password");
		uh.userValidation("sakura");
		String id = "" + ("tokyo"+12345).hashCode();
		Position pos = new Position(1, 2);
		ch.createCity("tokyo", 12345, "naruto", pos);
		ch.setRole("sasuke", id, Role.CONTR_NOT_AUTH);
		assertEquals(ch.getRole("sasuke", id), Role.CONTR_NOT_AUTH);
		assertEquals(ch.getRole("sakura", id), Role.TOURIST);
		assertFalse(ch.addRequest("sasuke", "konoa"));
		assertFalse(ch.addRequest("obito", id));
		assertTrue(ch.addRequest("sasuke", id));
		assertTrue(ch.addRequest("sakura", id));
		List<RoleRequest> list = ch.getRequests(id);
		assertEquals(list.size(), 2);
		assertFalse(ch.judge("uicfbwquicwqo", true));
		assertTrue(ch.judge(list.get(0).getRequestId(), true));
		assertTrue(ch.judge(list.get(1).getRequestId(), false));
		list = ch.getRequests(id);
		assertTrue(list.isEmpty());
		assertEquals(ch.getRole("sasuke", id), Role.CONTR_AUTH);
		assertEquals(ch.getRole("sakura", id), Role.TOURIST);
		ch.deleteCity(id);
		uh.removeUser("naruto");
		uh.removeUser("sasuke");
		uh.removeUser("sakura");
	}
	
	@Test
	void testReport() {
        String id = "" + ("tokyo"+12345).hashCode();
        String user = "naruto";
        uh.addUser(user, "password");
        uh.userValidation(user);
        String user2 = "sasuke";
        uh.addUser(user2, "password");
        uh.userValidation(user2);
        String user3 = "sakura";
        uh.addUser(user3, "password");
        uh.userValidation(user3);
        ch.createCity("tokyo", 12345, user, new Position(1, 2));
        Position pos = new Position(10, 10);
        List<String> empty = new ArrayList<>();
        ProtoPost data1 = new ProtoPost();
        String postId2 = "655823757.75498433.1";
        data1.setTitle("parole");
        data1.setText("blablabla");
        data1.setType(PostType.SOCIAL);
        data1.setPersistence(true);
        data1.setMultimediaData(empty);
        poh.createPost(user, pos, id, data1);
        Position pos1 = new Position(10, 10);
        List<String> ReportableContent = new ArrayList<>();
        ProtoPost data2 = new ProtoPost();
        data2.setTitle("CONTENUTO ESPLICITO");
        data2.setText("CONTENUTO ESPLICITO");
        data2.setType(PostType.SOCIAL);
        data2.setPersistence(true);
        data2.setMultimediaData(ReportableContent);
        poh.createPost(user, pos1, id, data2);
        assertTrue(ch.reportContent(user2, postId2, "contenuto inappropriato"));
        assertTrue(ch.reportContent(user3,postId2,"è improponibile lasciare un contenuto del genere su MuniciPath!!" ));
        List<Report> reports = new ArrayList<>();
                reports.add(rh.getReport("sasuke.655823757.75498433.1"));
                reports.add(rh.getReport("sakura.655823757.75498433.1"));
        List<Report> actualReports = rh.getReports(id);
        assertTrue(sameList(reports, actualReports));
        ch.deleteCity(id);
        uh.removeUser(user);
        uh.removeUser(user2);
        uh.removeUser(user3);
	}

    private boolean sameList(List<Report> list1, List<Report> list2){
        if(list1.size() != list2.size())
            return false;
        for (int i = 0; i < list1.size(); i++) {
            if(!list1.get(i).getId().equals(list2.get(i).getId())){
                return false;
            }
        }
        return true;
    }
}
