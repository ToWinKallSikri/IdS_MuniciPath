package Synk.Api.Controller.Feedback;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.PostType;
import Synk.Api.View.ViewModel.ProtoPost;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedbackHandlerTests {

	@Autowired
	FeedbackHandler fh;
    @Autowired
    UserHandler uh;
    @Autowired
    CityHandler ch;
    @Autowired
    PointHandler poh;

    @Test
    void testInitialization() {
        assertDoesNotThrow(() -> new FeedbackHandler());
    }

    @Test
	public void testGiveFeedback() {
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
        data1.setTitle("parole");
        data1.setText("blablabla");
        data1.setType(PostType.SOCIAL);
        data1.setPersistence(true);
        data1.setMultimediaData(empty);
        poh.createPost(user, pos, id, data1);
        String postId = "655823757.75498433.0";
        fh.valute(user2, postId, 4);
        fh.valute(user3, postId, 2);
        fh.valute(user, postId, 5);
        assertEquals(3.6666667461395264, fh.getFeedback(postId).getVoteAverage());
        assertEquals(3, fh.getFeedback(postId).getVoteCount());
        assertNotEquals(6, fh.getFeedback(postId).getVoteCount());
        assertNotEquals(2, fh.getFeedback(postId).getVoteAverage());
        ch.deleteCity(id);
        uh.removeUser(user);
        uh.removeUser(user2);
        uh.removeUser(user3);
	}
	
}
