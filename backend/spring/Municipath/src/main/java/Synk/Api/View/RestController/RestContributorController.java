package Synk.Api.View.RestController;

import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.Post.Contribute.Contribute;
import Synk.Api.Model.Post.Position;
import Synk.Api.View.WebResponseCreator;
import Synk.Api.View.Auth.Authenticator;
import Synk.Api.View.Auth.Authorizer;
import Synk.Api.View.ViewModel.ProtoGroup;
import Synk.Api.View.ViewModel.ProtoPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestContributorController {

    @Autowired
    private PointHandler poh;
    @Autowired
    private UserHandler uh;
    @Autowired
    private GroupHandler gh;
    @Autowired
    private PendingHandler peh;
    @Autowired
    private Authorizer authorizer;
    private final Authenticator authenticator;
    private final WebResponseCreator wrc;

    public RestContributorController() {
        this.authenticator = new Authenticator();
        this.wrc = new WebResponseCreator();
    }

    @GetMapping(value="/api/v1/city/{cityId}/contributes")
    public ResponseEntity<Object> getContributes(@RequestHeader(name="auth") String token,
    											@RequestParam("postId") String postId,
                                                 @PathVariable("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        List<Contribute> list = this.poh.getContributes(username, postId);
        if(list != null) {
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/api/v1/city/{cityId}/declareWinner")
    public ResponseEntity<Object> declareWinner(@RequestHeader(name="auth") String token,
    											@RequestParam("postId") String postId,
                                                @RequestParam("winnerId") String winnerId,
                                                @PathVariable("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        if(this.poh.declareWinner(username, postId, winnerId)) {
            return new ResponseEntity<Object>(wrc.make("Vincitore dichiarato."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
    



    @PutMapping(value="/api/v1/city/{cityId}/staff/managePending")
    public ResponseEntity<Object> managePending(@RequestHeader(name="auth") String token,
                                                @RequestParam("pendingId") String pendingId,
                                                @RequestParam("motivation") String motivation,
                                                @RequestParam("judge") boolean judge,
                                                @PathVariable String cityId) {
        String username = authenticator.getUsername(token);
        if(this.authorizer.isStaff(username, cityId)) {
            if (this.peh.judge(pendingId, judge, motivation)) {
                return new ResponseEntity<Object>(wrc.make("Pending accettato."), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value="/api/v1/city/{cityId}/groups")
    public ResponseEntity<Object> createGroup(@RequestHeader(name="auth") String token,
                                              @PathVariable("cityId") String cityId,
                                              @RequestBody ProtoGroup data) {
        String username = authenticator.getUsername(token);
        if(this.gh.createGroup(username, cityId, data)) {
            return new ResponseEntity<Object>(wrc.make("Gruppo creato."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value="/api/v1/city/{cityId}/groups")
    public ResponseEntity<Object> editGroup(@RequestHeader(name="auth") String token,
                                            @RequestParam("groupId") String groupId,
                                            @RequestBody ProtoGroup data,
                                            @PathVariable("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        boolean result;
        if(authorizer.isStaff(username, cityId))
			result = this.gh.editGroup(groupId, data);
        else result = this.gh.editGroup(groupId, username, data);
        if(result) {
            return new ResponseEntity<Object>(wrc.make("Gruppo modificato."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/api/v1/city/{cityId}/groups")
    public ResponseEntity<Object> removeGroup(@RequestHeader(name="auth") String token,
    										  @RequestParam("groupId") String groupId,
                                              @PathVariable("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        boolean result;
        if(authorizer.isStaff(username, cityId))
			result = this.gh.removeGroup(groupId);
        else result = this.gh.removeGroup(username, groupId);
        if(result) {
            return new ResponseEntity<Object>(wrc.make("Gruppo eliminato."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value="/api/v1/city/{cityId}/posts")
    public ResponseEntity<Object> createPost(@RequestHeader(name="auth") String token,
                                             @RequestParam("lat") double lat,
                                             @RequestParam("lng") double lng,
                                             @RequestBody ProtoPost data,
                                             @PathVariable("cityId") String cityId)  {
        String username = authenticator.getUsername(token);
        Position pos = new Position(lat, lng);
        if(this.poh.createPost(username, pos, cityId, data)) {
            return new ResponseEntity<Object>(wrc.make("Post creato."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value="/api/v1/city/{cityId}/posts")
    public ResponseEntity<Object> editPost(@RequestHeader(name="auth") String token,
                                           @RequestParam("postId") String postId,
                                           @RequestBody ProtoPost data,
                                           @PathVariable("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        boolean result;
        if(authorizer.isStaff(username, cityId))
			result = this.poh.editPost(postId, data);
        else result = this.poh.editPost(postId, username, cityId, data);
        if(result) {
            return new ResponseEntity<Object>(wrc.make("Post modificato."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/api/v1/city/{cityId}/posts")
    public ResponseEntity<Object> deletePost(@RequestHeader(name="auth") String token,
    										 @RequestParam("postId") String postId,
                                             @PathVariable("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        boolean result;
        if(authorizer.isStaff(username, cityId))
			result = this.poh.deletePost(postId);
        else result = this.poh.deletePost(postId, username);
        if(result) {
            return new ResponseEntity<Object>(wrc.make("Post eliminato."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value="/api/v1/city/{cityId}/notifyEvent")
    public ResponseEntity<Object> notifyEvent(@RequestHeader(name="auth") String token,
                                              @PathVariable("cityId") String cityId,
                                              @RequestParam("contentId") String contentId,
                                              @RequestBody String message) {
        String username = authenticator.getUsername(token);
        this.uh.notifyEvent(username, message, contentId);
        return new ResponseEntity<Object>(wrc.make("Evento notificato."), HttpStatus.OK);
    }
}
