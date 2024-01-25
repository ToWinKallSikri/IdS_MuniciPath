package Synk.Api.View.RestController;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.SavedContent.SavedContentHandler;
import Synk.Api.Model.City.City;
import Synk.Api.Model.Group.Group;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.Post.Post;
import Synk.Api.View.Auth.Authenticator;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@org.springframework.web.bind.annotation.RestController
public class RestViewController {
    @Autowired
    private PointHandler poh;
    @Autowired
    private CityHandler ch;
    @Autowired
    private GroupHandler gh;
    @Autowired
    private SavedContentHandler sch;
    private final Authenticator authenticator;

    public RestViewController() {
        this.authenticator = new Authenticator();
    }


    @GetMapping(value="/api/v1/cities")
    public ResponseEntity<Object> getCities(@PathParam("cityName") String cityName) {
        return new ResponseEntity<Object>(this.ch.getCities(cityName), HttpStatus.OK);
    }

    @GetMapping(value="/api/v1/city")
    public ResponseEntity<Object> getCity(@RequestParam("cityId") String cityId) {
        City city = this.ch.getCity(cityId);
        if(city != null)
            return new ResponseEntity<Object>(city, HttpStatus.OK);
        else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/api/v1/city/{cityId}/groups")
    public ResponseEntity<Object> viewGroup(@RequestParam("groupId") String groupId,
                                            @PathVariable("cityId") String cityId) {
        Group group = this.gh.viewGroup(groupId);
        if(group != null) {
            return new ResponseEntity<Object>(group, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value="/api/v1/viewGroups")
    public ResponseEntity<Object> viewGroups(@RequestHeader(name="groupIds") List<String> groupIds) {
        List<Group> list = this.gh.viewGroups(groupIds);
        if(list != null) {
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(value="/api/v1/viewPosts")
    public ResponseEntity<Object> viewPosts(@RequestHeader(name="postIds") List<String> postIds) {
        List<Post> list = this.poh.getPosts(postIds);
        if(list != null) {
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value="/api/v1/city/{cityId}/points")
    public ResponseEntity<Object> getPoints(@RequestHeader(name="auth") String token,
                                            @PathVariable("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        if(username.equals("unregistered_tourist") && (!token.equals("?")))
        	return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        List<Point> list = this.poh.getPoints(cityId, username);
        if(list != null) {
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value="/api/v1/city/{cityId}/point")
    public ResponseEntity<Object> viewPosts(@RequestHeader(name="auth") String token,
                                            @RequestParam("pointId") String pointId,
                                            @PathVariable("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        if(username.equals("unregistered_tourist") && (!token.equals("?")))
        	return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        List<Post> list = this.poh.viewPosts(pointId, username);
        if(list != null) {
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value="/api/v1/city/{cityId}/post")
    public ResponseEntity<Object> viewPost(@RequestHeader(name="auth") String token,
    										@RequestParam("postId") String postId,
                                           @PathVariable("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        if(username.equals("unregistered_tourist") && (!token.equals("?")))
        	return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        Post post = this.poh.getPost(postId, username);
        if(post != null) {
            return new ResponseEntity<Object>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value="/api/v1/saved/number")
    public ResponseEntity<Object> getNumberOfPartecipants(@PathVariable("cityId") String cityId,
                                                          @RequestParam("contentId") String contentId){
        int num = this.sch.getPartecipants(contentId).size();
        return new ResponseEntity<Object>(num, HttpStatus.OK);
    }

}
