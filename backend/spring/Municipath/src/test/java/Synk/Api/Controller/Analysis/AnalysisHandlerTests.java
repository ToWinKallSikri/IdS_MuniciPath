package Synk.Api.Controller.Analysis;

import static org.junit.jupiter.api.Assertions.*;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import Synk.Api.Controller.ContentTimeModifier;
import Synk.Api.Controller.ContentTimeModifier.TimeType;
import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Feedback.FeedbackHandler;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import Synk.Api.View.ViewModel.ProtoPost;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnalysisHandlerTests {

	@Autowired
	AnalysisHandler ah;
	@Autowired
	PointHandler ph;
	@Autowired
	GroupHandler gh;
	@Autowired
	ContentTimeModifier ctm;
	@Autowired
	CityHandler ch;
	@Autowired
	UserHandler uh;
	@Autowired
	FeedbackHandler fh;
	private final String user = "naruto000", userBase = "naruto";
	private final Position pos = new Position(1, 2);
	private final List<String> empty = new ArrayList<>();
	private final String cityId = "" + ("tokyo"+12345).hashCode();
	private Random rnd = new Random();
	private final String pointId = "655823757.-32504895";
	
	@Test
	public void testDoAnalysis() {
				uh.addUser(user, "password");
				uh.userValidation(user);
				ch.createCity("tokyo", 12345, user, pos);
				createUsers();
				makePosts();
				viewAndVote();
				assertDoesNotThrow(() -> ah.getAnalysis(cityId, 6, false));
				assertEquals(ah.getAnalysis(cityId, -1, true), null);
				assertEquals(ah.getAnalysis("dfujoiwebjfwpd", 3, true), null);
				assertNotEquals(ah.getAnalysis(cityId, 3, true), null);
				ch.deleteCity(cityId);
				removeUsers();
	}



	private void createUsers() {
		String userN;
		System.out.print("Preparation for analysis tests: ");
		for(int i = 1; i < 100; i++) {
			userN = i < 10 ? userBase+"00"+i : userBase+"0"+i;
			uh.addUser(userN, "password");
			uh.userValidation(userN);
			if(i < 10) ch.addModerator(userN, cityId);
			else ch.setRole(userN, cityId, Role.CONTR_AUTH);
			if(i%30==0)
				System.out.print("[]");
		}
	}
	
	private void makePosts() {
		ProtoPost data = new ProtoPost();
		data.setText("");
		data.setType(PostType.SOCIAL);
		data.setPersistence(true);
		data.setMultimediaData(empty);
		String userN = "";
		for(int i = 0; i < 100; i++) {
			userN = i < 10 ? userBase+"00"+i : i < 100 ? userBase+"0"+i : userBase+i;
			for(int j = 0; j < i/11; j++){
				data.setTitle("TestPost."+i+"."+j);
				ph.createPost(userN, pos, cityId, data);
			}
			if(i%40==0 || i == 99)
				System.out.print("[]");
		}
	}
	
	
	private void viewAndVote() {
		List<Post> posts = ph.viewPosts(pointId, user);
		String userN,  post;
		for(int i = 0; i < 100; i++) {
			userN = i < 10 ? userBase+"00"+i :  userBase+"0"+i;
			post = posts.get(getRnd(0, 405)).getId();
			ph.getPost(post, userN);
			fh.valute(userN, post, getRnd(1, 5));
			ctm.modifyTime(post, getRnd(0, 5), ChronoUnit.MONTHS, TimeType.PUBL);
			if(i%40==0)
				System.out.print("[]");
		}
		System.out.println();
	}
	
	
	private void removeUsers() {
		String userN;
		for(int i = 0; i < 100; i++) {
			userN = i < 10 ? userBase+"00"+i :  userBase+"0"+i;
			uh.removeUser(userN);
		}
	}
	
	private int getRnd(int from, int to) {
		if(from == to) 
			return from;
		if(from < to)
			return from + this.rnd.nextInt(to - from + 1);
		else 
			return getRnd(to, from);
	}
	
}
