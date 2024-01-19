package Synk.Api.View.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Feedback.FeedbackHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.SavedContent.SavedContentHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.User.Notification.Notification;
import Synk.Api.View.WebResponseCreator;
import Synk.Api.View.Auth.Authenticator;
import Synk.Api.View.Auth.Authorizer;
import Synk.Api.View.ViewModel.WebLog;

@RestController
public class RestTouristContoller {
	
	@Autowired
	private PointHandler poh;
	@Autowired
	private UserHandler uh;
	@Autowired
    private CityHandler ch;
	@Autowired
    private SavedContentHandler sch;
	@Autowired
    private FeedbackHandler fh;
    @Autowired
    private Authorizer authorizer;
    private Authenticator authenticator;
    private WebResponseCreator wrc;
    
    public RestTouristContoller(){
    	this.authenticator = new Authenticator();
    	this.wrc = new WebResponseCreator();
    }
    
    @PostMapping(value="/api/v1/signin")
    public ResponseEntity<Object> signin(@RequestBody WebLog log) {
        if(uh.addUser(log.getUsername(), log.getPassword())) {
            return new ResponseEntity<Object>(wrc.make("Creazione account riuscito."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping(value="/api/v1/login")
    public ResponseEntity<Object> login(@RequestBody WebLog log) {
        if(this.uh.isThePassword(log.getUsername(), log.getPassword())) {
            String jwt = authenticator.createJwt(log.getUsername());
            return new ResponseEntity<Object>(wrc.make(jwt), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping(value="/api/v1/city/{cityId}/addRoleRequest")
	public ResponseEntity<Object> addRoleRequest(@RequestHeader(name="authenticator") String token,
													@PathVariable("cityId") String cityId) {
		String username = this.authenticator.getUsername(token);
		if(this.authorizer.isNotLimited(username, cityId)) {
			if(this.ch.addRequest(username, cityId)) {
				return new ResponseEntity<Object>(wrc.make("Richiesta inviata."), HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	


	@PostMapping(value="/api/v1/city/{cityId}/posts/{postId}/addContribute")
	public ResponseEntity<Object> addContribute(@RequestHeader(name="authenticator") String token,
												@PathVariable("postId") String postId, 
												@RequestBody List<String> content,
												@PathVariable("cityId") String cityId) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isNotLimited(username, cityId)) {
			if(this.poh.addContentToContest(username, postId, content)) {
				return new ResponseEntity<Object>(wrc.make("Contenuto aggiunto."), HttpStatus.OK);
			} else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	

	@PutMapping(value="/api/v1/saved/save")
    public ResponseEntity<Object> saveSavedContent(@RequestHeader(name="authenticator") String token, 
    											   @RequestParam("contentId") String contentId,
    											   @RequestParam("action") boolean action) {
		String username = authenticator.getUsername(token);
		boolean result;
		if(action) result = this.sch.saveSavedContent(username, contentId);
		else result = this.sch.removeSavedContent(username, contentId);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
	
	@GetMapping(value="/api/v1/saved/all")
    public ResponseEntity<Object> getSavedContent(@RequestHeader(name="authenticator") String token) {
		String username = authenticator.getUsername(token);
		List<String> list = this.sch.getSavedContent(username);
		if(list != null)
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }
    
	@GetMapping(value="/api/v1/msgs")
    public ResponseEntity<Object> getMyMessages(@RequestHeader(name="authenticator") String token){
		String username = authenticator.getUsername(token);
    	List<Notification> list = this.uh.getMyMessages(username);
    	if(list != null)
    		return new ResponseEntity<Object>(list, HttpStatus.OK);
    	else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value="/api/v1/msg/{msgId}")
	public ResponseEntity<Object> getMyMessage(@RequestHeader(name="authenticator") String token,
												@PathVariable("msgId") String msgId) {
		String username = authenticator.getUsername(token);
		Notification msg = this.uh.getMyMessage(username, msgId);
		if(msg != null)
    		return new ResponseEntity<Object>(msg, HttpStatus.OK);
		else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(value="/api/v1/follows")
	public ResponseEntity<Object> getAllFollowed(@RequestHeader(name="authenticator") String token) {
		String username = authenticator.getUsername(token);
		List<String> list = this.uh.getAllFollowed(username);
		if(list != null)
    		return new ResponseEntity<Object>(list, HttpStatus.OK);
	    else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}
	

	@PutMapping(value="/api/v1/city/{cityId}/follow/contributor")
	public ResponseEntity<Object> followUnfollowContributor(@RequestHeader(name="authenticator") String token,
													@RequestParam("contributor")	String contributor,
	    											@PathVariable("cityId") String cityId,
	    											@RequestParam("action") boolean action) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isNotLimited(username, cityId)) {
			boolean result;
			if(action)
				result = this.uh.followContributor(username, contributor);
			else result = this.uh.unfollowContributor(username, contributor);
			if(result)
				return new ResponseEntity<Object>(wrc.make("Azione eseguita."), HttpStatus.OK);
			else
		    	return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		} return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(value="/api/v1/city/{cityId}/follow/city")
	public ResponseEntity<Object> followUnfollowCity(@RequestHeader(name="authenticator") String token,
	    											@PathVariable("cityId") String cityId,
	    											@RequestParam("action") boolean action) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isNotLimited(username, cityId)) {
			boolean result;
			if(action)
				result = this.uh.followCity(username, cityId);
			else result = this.uh.unfollowCity(username, cityId);
			if(result)
				return new ResponseEntity<Object>(wrc.make("Azione eseguita."), HttpStatus.OK);
			else
		    	return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		} return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	

	
	@PutMapping(value="/api/v1/city/{cityId}/follow/content")
	public ResponseEntity<Object> followUnfollowContent(@RequestHeader(name="authenticator") String token,
	    											@PathVariable("cityId") String cityId,
													@RequestParam("contentId")	String contentId,
	    											@RequestParam("action") boolean action) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isNotLimited(username, cityId)) {
			boolean result;
			if(action)
				result = this.uh.follow(username, contentId);
			else result = this.uh.unfollow(username, contentId);
			if(result)
				return new ResponseEntity<Object>(wrc.make("Azione eseguita."), HttpStatus.OK);
			else
		    	return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		} return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);	
	}
	

	@PostMapping(value="/api/v1/city/{cityId}/report")
	public ResponseEntity<Object> reportContent(@RequestHeader(name="authenticator") String token,
												@PathVariable("cityId") String cityId,
												@RequestParam("contentId") String contentId,
												@RequestBody String motivation) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isNotLimited(username, cityId)) {
			this.ch.reportContent(username, contentId, motivation);
	        return new ResponseEntity<Object>(wrc.make("Segnalazione inviata."), HttpStatus.OK);
		} return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);	
    }
	

    
	@PostMapping(value="/api/v1/city/{cityId}/valute")
    public ResponseEntity<Object> valute(@RequestHeader(name="authenticator") String token,
    						@PathVariable("cityId") String cityId,
    						@RequestParam("contentId") String contentId, 
    						@RequestParam("vote") int vote) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isNotLimited(username, cityId)) {
			if(this.fh.valute(username, contentId, vote))
	    		return new ResponseEntity<Object>(wrc.make("Valutazione inviata."), HttpStatus.OK);
		    else
		    	return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		} return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);	
	}
}
