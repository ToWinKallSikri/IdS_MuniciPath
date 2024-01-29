package Synk.Api.View.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Synk.Api.Controller.User.UserHandler;
import Synk.Api.View.WebResponseCreator;
import Synk.Api.View.Auth.Authenticator;
import Synk.Api.View.Auth.Authorizer;

@RestController
public class RestCheckController {
	
    @Autowired
    private Authorizer authorizer;
    @Autowired
    private UserHandler uh;
    private Authenticator authenticator;
    private WebResponseCreator wrc;
    
    public RestCheckController() {
    	this.authenticator = new Authenticator();
        this.wrc = new WebResponseCreator();
    }
    
    @GetMapping(value="/api/v1/check/role")
	public ResponseEntity<Object> getRole(@RequestHeader(name="auth") String token,
			  									@RequestParam("cityId") String cityId) {
		String username = authenticator.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.authorizer.getRole(username, cityId)), HttpStatus.OK);
	}
    
	@GetMapping(value="/api/v1/check/isManager")
	public ResponseEntity<Object> isManager(@RequestHeader(name="auth") String token) {
		String username = authenticator.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.authorizer.isManager(username)), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/isAuthor")
	public ResponseEntity<Object> isAuthor(@RequestHeader(name="auth") String token,
										   @RequestParam("contentId") String contentId) {
		String username = authenticator.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.authorizer.isAuthorOf(username, contentId)), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/havePowerWithIt")
	public ResponseEntity<Object> havePowerWithIt(@RequestHeader(name="auth") String token,
												  @RequestParam("contentId") String contentId) {
		String username = authenticator.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.authorizer.havePowerWithIt(username, contentId)), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/usernameExists")
	public ResponseEntity<Object> usernameExists(@RequestParam("username") String username) {
		return new ResponseEntity<Object>(this.uh.usernameExists(username), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/isNotLimited")
	public ResponseEntity<Object> isNotLimited(@RequestHeader(name="auth") String token,
												  @RequestParam("cityId") String cityId) {
		String username = authenticator.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.authorizer.isNotLimited(username, cityId)), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/follow/content")
	public ResponseEntity<Object> alreadyFollowingContent(@RequestHeader(name="auth") String token,
													@RequestParam("contentId")	String contentId) {
		String username = authenticator.getUsername(token);
		return new ResponseEntity<Object>(this.uh.alreadyFollowing(username, contentId), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/follow/city")
	public ResponseEntity<Object> alreadyFollowingCity(@RequestHeader(name="auth") String token,
														@RequestParam("cityId") String cityId) {
		String username = authenticator.getUsername(token);
		return new ResponseEntity<Object>(this.uh.alreadyFollowing(username, cityId), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/follow/contributor")
	public ResponseEntity<Object> alreadyFollowingContributor(@RequestHeader(name="auth") String token,
														@RequestParam("contributor") String contributor) {
		String username = authenticator.getUsername(token);
		return new ResponseEntity<Object>(this.uh.alreadyFollowingContributor(username, contributor), HttpStatus.OK);
	}
}
