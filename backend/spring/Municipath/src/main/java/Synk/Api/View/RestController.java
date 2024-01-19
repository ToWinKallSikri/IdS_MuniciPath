package Synk.Api.View;

import Synk.Api.Model.Post.Contribute.Contribute;
import Synk.Api.Model.User.User;
import Synk.Api.Model.User.Notification.Notification;
import Synk.Api.View.Auth.Authenticator;
import Synk.Api.View.File.FileHandler;
import Synk.Api.View.ViewModel.ProtoCity;
import Synk.Api.View.ViewModel.ProtoGroup;
import Synk.Api.View.ViewModel.ProtoPost;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import Synk.Api.Controller.MuniciPathController;
import Synk.Api.Model.Analysis.Analysis;
import Synk.Api.Model.City.City;
import Synk.Api.Model.City.Report.Report;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.City.Role.RoleRequest;
import Synk.Api.Model.Group.Group;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	private MuniciPathController controller;
	private Authenticator auth;
	private FileHandler fh;
	private WebResponseCreator wrc;
	
	public RestController() throws Exception {
		this.auth = new Authenticator();
		this.fh = new FileHandler();
		this.wrc = new WebResponseCreator();
	}
	
	//--------------------------CHECK--------------------------------

	@GetMapping(value="/api/v1/check/isManager")
	public ResponseEntity<Object> isManager(@RequestHeader(name="auth") String token) {
		String username = auth.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.controller.checkManager(username)), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/isAuthor")
	public ResponseEntity<Object> isAuthor(@RequestHeader(name="auth") String token,
										   @RequestParam("contentId") String contentId) {
		String username = auth.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.controller.checkAuthor(username, contentId)), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/havePowerWithIt")
	public ResponseEntity<Object> havePowerWithIt(@RequestHeader(name="auth") String token,
												  @RequestParam("contentId") String contentId) {
		String username = auth.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.controller.havePowerWithIt(username, contentId)), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/check/isLimited")
	public ResponseEntity<Object> isLimited(@RequestHeader(name="auth") String token,
												  @RequestParam("cityId") String cityId) {
		String username = auth.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.controller.isLimited(username, cityId)), HttpStatus.OK);
	}

	@GetMapping(value="/api/v1/check/role")
	public ResponseEntity<Object> getRole(@RequestHeader(name="auth") String token, @RequestParam("cityId") String cityId) {
		String username = auth.getUsername(token);
		return new ResponseEntity<Object>(wrc.make(this.controller.getRole(username, cityId)), HttpStatus.OK);
	}
	

	
	@GetMapping(value="/api/v1/city/{cityId}/check/follow/content")
	public ResponseEntity<Object> alreadyFollowingContent(@RequestHeader(name="auth") String token,
													@PathVariable("cityId") String cityId,
													@RequestParam("contentId")	String contentId) {
		String username = auth.getUsername(token);
		return new ResponseEntity<Object>(this.controller.alreadyFollowing(username, contentId), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/city/{cityId}/check/follow/city")
	public ResponseEntity<Object> alreadyFollowingCity(@RequestHeader(name="auth") String token,
														@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		return new ResponseEntity<Object>(this.controller.alreadyFollowing(username, cityId), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/city/{cityId}/check/follow/contributor")
	public ResponseEntity<Object> alreadyFollowingContributor(@RequestHeader(name="auth") String token,
														@PathVariable("cityId") String cityId,
														@RequestParam("contributor") String contributor) {
		String username = auth.getUsername(token);
		return new ResponseEntity<Object>(this.controller.alreadyFollowingContributor(username, contributor), HttpStatus.OK);
	}
	
	//----------------USER LOG-----------------
	
	@PostMapping(value="/api/v1/signin")
	public ResponseEntity<Object> signin(@RequestParam("username") String username, @RequestParam("password") String password) {
		if(this.controller.addUser(username, password)) {
			return new ResponseEntity<Object>(wrc.make("Creazione account riuscito."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value="/api/v1/changePassword")
	public ResponseEntity<Object> changePassword(@RequestHeader(name="auth") String token,
												 @RequestParam("newPassword") String newPassword) {
		String username = auth.getUsername(token);
		if(this.controller.changePassword(username, newPassword)) {
			return new ResponseEntity<Object>(wrc.make("Password cambiata."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	

	
	@GetMapping(value="/api/v1/login")
	public ResponseEntity<Object> login(@RequestParam("username") String username, 
										@RequestParam("password") String password) {
		if(this.controller.isThePassword(username, password)) {
			String jwt = auth.createJwt(username);
			return new ResponseEntity<Object>(wrc.make(jwt), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	//--------------------------FOR NOT LIMITED--------------------------
	
	@PostMapping(value="/api/v1/city/{cityId}/addRoleRequest")
	public ResponseEntity<Object> addRoleRequest(@RequestHeader(name="auth") String token,
													@PathVariable("cityId") String cityId) {
		String username = this.auth.getUsername(token);
		if(this.controller.addRequest(username, cityId)) {
			return new ResponseEntity<Object>(wrc.make("Richiesta inviata."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	


	@PostMapping(value="/api/v1/city/{cityId}/posts/{postId}/addContribute")
	public ResponseEntity<Object> addContribute(@RequestHeader(name="auth") String token,
												@PathVariable("postId") String postId, 
												@RequestBody List<String> content,
												@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.addContentToContest(username, postId, content)) {
			return new ResponseEntity<Object>(wrc.make("Contenuto aggiunto."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	

	@PutMapping(value="/api/v1/saved/save")
    public ResponseEntity<Object> saveSavedContent(@RequestHeader(name="auth") String token, 
    											   @RequestParam("contentId") String contentId,
    											   @RequestParam("action") boolean action) {
		String username = auth.getUsername(token);
		boolean result;
		if(action) result = this.controller.saveSavedContent(username, contentId);
		else result = this.controller.removeSavedContent(username, contentId);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
	
	@GetMapping(value="/api/v1/saved/all")
    public ResponseEntity<Object> getSavedContent(@RequestHeader(name="auth") String token) {
		String username = auth.getUsername(token);
		List<String> list = this.controller.getSavedContent(username);
		if(list != null)
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }
    
	@GetMapping(value="/api/v1/msgs")
    public ResponseEntity<Object> getMyMessages(@RequestHeader(name="auth") String token){
		String username = auth.getUsername(token);
    	List<Notification> list = this.controller.getMyMessages(username);
    	if(list != null)
    		return new ResponseEntity<Object>(list, HttpStatus.OK);
    	else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value="/api/v1/msg/{msgId}")
	public ResponseEntity<Object> getMyMessage(@RequestHeader(name="auth") String token,
												@PathVariable("msgId") String msgId) {
		String username = auth.getUsername(token);
		Notification msg = this.controller.getMyMessage(username, msgId);
		if(msg != null)
    		return new ResponseEntity<Object>(msg, HttpStatus.OK);
		else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(value="/api/v1/follows")
	public ResponseEntity<Object> getAllFollowed(@RequestHeader(name="auth") String token) {
		String username = auth.getUsername(token);
		List<String> list = this.controller.getAllFollowed(username);
		if(list != null)
    		return new ResponseEntity<Object>(list, HttpStatus.OK);
	    else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	

	@PutMapping(value="/api/v1/city/{cityId}/follow/contributor")
	public ResponseEntity<Object> followUnfollowContributor(@RequestHeader(name="auth") String token,
													@RequestParam("contributor")	String contributor,
	    											@PathVariable("cityId") String cityId,
	    											@RequestParam("action") boolean action) {
		String username = auth.getUsername(token);
		boolean result;
		if(action)
			result = this.controller.followContributor(username, contributor);
		else result = this.controller.unfollowContributor(username, contributor);
		if(result)
			return new ResponseEntity<Object>(wrc.make("Azione eseguita."), HttpStatus.OK);
		else
	    	return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(value="/api/v1/city/{cityId}/follow/city")
	public ResponseEntity<Object> followUnfollowCity(@RequestHeader(name="auth") String token,
	    											@PathVariable("cityId") String cityId,
	    											@RequestParam("action") boolean action) {
		String username = auth.getUsername(token);
		boolean result;
		if(action)
			result = this.controller.followCity(username, cityId);
		else result = this.controller.unfollowCity(username, cityId);
		if(result)
			return new ResponseEntity<Object>(wrc.make("Azione eseguita."), HttpStatus.OK);
		else
	    	return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	

	
	@PutMapping(value="/api/v1/city/{cityId}/follow/content")
	public ResponseEntity<Object> followUnfollowContent(@RequestHeader(name="auth") String token,
	    											@PathVariable("cityId") String cityId,
													@RequestParam("contentId")	String contentId,
	    											@RequestParam("action") boolean action) {
		String username = auth.getUsername(token);
		boolean result;
		if(action)
			result = this.controller.follow(username, contentId);
		else result = this.controller.unfollow(username, contentId);
		if(result)
			return new ResponseEntity<Object>(wrc.make("Azione eseguita."), HttpStatus.OK);
		else
	    	return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	

	@PostMapping(value="/api/v1/city/{cityId}/report")
	public ResponseEntity<Object> reportContent(@RequestHeader(name="auth") String token,
												@PathVariable("cityId") String cityId,
												@RequestParam("contentId") String contentId,
												@RequestBody String motivation) {
		String username = auth.getUsername(token);
		this.controller.reportContent(username, contentId, motivation);
        return new ResponseEntity<Object>(wrc.make("Segnalazione inviata."), HttpStatus.OK);
    }
	

    
	@PostMapping(value="/api/v1/city/{cityId}/valute")
    public ResponseEntity<Object> valute(@RequestHeader(name="auth") String token,
    						@PathVariable("cityId") String cityId,
    						@RequestParam("contentId") String contentId, 
    						@RequestParam("vote") int vote) {
		String username = auth.getUsername(token);
		if(this.controller.valute(username, contentId, vote))
    		return new ResponseEntity<Object>(wrc.make("Valutazione inviata."), HttpStatus.OK);
	    else
	    	return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	//----------------------MANAGER----------------

	@DeleteMapping(value="/api/v1/manager/removeUser")
	public ResponseEntity<Object> removeUser(@RequestHeader(name="auth") String token,
											 @RequestParam("toRemove") String toRemove) {
		String username = auth.getUsername(token);
		if(this.controller.removeUser(username, toRemove)) {
			return new ResponseEntity<Object>(wrc.make("Utente rimosso."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PutMapping(value="/api/v1/manager/convalidate")
	public ResponseEntity<Object> convalidateAccount(@RequestHeader(name="auth") String token, @RequestParam("toValidate") String toValidate){
		String username = auth.getUsername(token);
		if(this.controller.userValidation(username, toValidate)) {
			return new ResponseEntity<Object>(wrc.make("Account convalidato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	


	@GetMapping(value="/api/v1/manager/getUsersNotConvalidated")
	public ResponseEntity<Object> getUsersNotConvalidated(@RequestHeader(name="auth") String token) {
		String username = auth.getUsername(token);
		List<User> list = this.controller.getUsersNotConvalidated(username);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}



	@PutMapping(value="/api/v1/manager/addManager")
	public ResponseEntity<Object> addManager(@RequestHeader(name="auth") String token,
											 @RequestParam("toManage") String toManage) {
		String username = auth.getUsername(token);
		if(this.controller.manageManager(username, toManage, true)) {
			return new ResponseEntity<Object>(wrc.make("Manager aggiunto."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	

	@PutMapping(value="/api/v1/manager/removeManager")
	public ResponseEntity<Object> removeManager(@RequestHeader(name="auth") String token,
												@RequestParam("toManage") String toManage) {
		String username = auth.getUsername(token);
		if(this.controller.manageManager(username, toManage, false)) {
			return new ResponseEntity<Object>(wrc.make("Manager rimosso."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping(value="/api/v1/manager/createCity")
	public ResponseEntity<Object> createCity(@RequestHeader(name="auth") String token,
											@RequestBody ProtoCity pc) {
		String username = auth.getUsername(token);
		if(this.controller.createCity(username, pc.getCityName(), pc.getCap(), pc.getCurator(), pc.getPos())) {
			return new ResponseEntity<Object>(wrc.make("Comune "+pc.getCityName()+" creato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
    }
	
	@PutMapping(value="/api/v1/manager/updateCity")
	public ResponseEntity<Object> updateCity(@RequestHeader(name="auth") String token,
											@RequestParam("cityId") String cityId,
											@RequestBody ProtoCity pc) {
		String username = auth.getUsername(token);
		if(this.controller.updateCity(username, cityId, pc.getCityName(), pc.getCap(), pc.getCurator(), pc.getPos())) {
			return new ResponseEntity<Object>(wrc.make("Comune aggiornato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
    }
	
	@DeleteMapping(value="/api/v1/manager/deleteCity")
	public ResponseEntity<Object> deleteCity(@RequestHeader(name="auth") String token, 
											@RequestParam("cityId") String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.deleteCity(username, cityId)) {
			return new ResponseEntity<Object>(wrc.make("Comune eliminato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
    }
	
	//-----------------------VIEW---------------
	
	@GetMapping(value="/api/v1/cities")
	public ResponseEntity<Object> getCities(@PathParam("cityName") String cityName) {
		return new ResponseEntity<Object>(this.controller.searchCity(cityName), HttpStatus.OK);
    }
	
	@GetMapping(value="/api/v1/city")
	public ResponseEntity<Object> getCity(@RequestParam("cityId") String cityId) {
		City city = this.controller.getCity(cityId);
		if(city != null)
			return new ResponseEntity<Object>(city, HttpStatus.OK);
		else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }
	
	@GetMapping(value="/api/v1/city/{cityId}/groups/{groupId}")
	public ResponseEntity<Object> viewGroup(@PathVariable("groupId") String groupId, 
											@PathVariable("cityId") String cityId) {
		Group group = this.controller.viewGroup(groupId);
		if(group != null) {
			return new ResponseEntity<Object>(group, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value="/api/v1/viewGroups")
	public ResponseEntity<Object> viewGroups(@RequestHeader(name="groupIds") List<String> groupIds) {
		List<Group> list = this.controller.viewGroups(groupIds);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}


	@GetMapping(value="/api/v1/city/{cityId}/points")
	public ResponseEntity<Object> getPoints(@RequestHeader(name="auth") String token,
											@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		List<Point> list = this.controller.getPoints(cityId, username);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/points/{pointId}")
	public ResponseEntity<Object> viewPosts(@RequestHeader(name="auth") String token,
											@PathVariable("pointId") String pointId, 
											@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		List<Post> list = this.controller.viewPosts(pointId, username);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/posts/{postId}")
	public ResponseEntity<Object> viewPost(@RequestHeader(name="auth") String token,
											@PathVariable("postId") String postId, 
											@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		Post post = this.controller.viewPost(postId, username);
		if(post != null) {
			return new ResponseEntity<Object>(post, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping(value="/api/v1/saved/number")
    public ResponseEntity<Object> getNumberOfPartecipants(@PathVariable("cityId") String cityId,
    														@RequestParam("contentId") String contentId){
		int num = this.controller.getNumberOfPartecipants(contentId);
		return new ResponseEntity<Object>(num, HttpStatus.OK);
    }
	
	//-----------------------CURATOR------------------------
	

	
	@PostMapping(value="/api/v1/city/{cityId}/staff/analysis")
	public ResponseEntity<Object> getAnalysis(@RequestHeader(name="auth") String token,
											  @PathVariable("cityId") String cityId,
											  @RequestParam("months") int months, 
											  @RequestParam("onlyUsers")boolean onlyUsers) {
		String username = auth.getUsername(token);
		Analysis data = this.controller.getAnalysis(username, cityId, months, onlyUsers);
		if(data != null)
			return new ResponseEntity<Object>(data, HttpStatus.OK);
		else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	//-------------------------STAFF-----------------------

	@PutMapping(value="/api/v1/city/{cityId}/staff/addModerator")
	public ResponseEntity<Object> addModerator(@RequestHeader(name="auth") String token,
												@RequestParam("toSet") String toSet, 
												@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.addModerator(username, toSet, cityId)) {
			return new ResponseEntity<Object>(wrc.make("Moderatore aggiunto."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/staff/removeModerator")
	public ResponseEntity<Object> removeModerator(@RequestHeader(name="auth") String token,
													@RequestParam("toSet") String toSet, 
													@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.removeModerator(username, toSet, cityId)) {
			return new ResponseEntity<Object>(wrc.make("Moderatore rimosso."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/setRole")
	public ResponseEntity<Object> setRole(@RequestHeader(name="auth") String token,
											@PathVariable("cityId") String cityId,
											@RequestParam("toSet") String toSet, 
											@RequestParam("role") String role) {
		String username = auth.getUsername(token);
		Role _role = Role.safeValueOf(role);
		if(this.controller.setRole(username, toSet, cityId, _role)) {
			return new ResponseEntity<Object>(wrc.make("Ruovo modificato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
    }
	
	@GetMapping(value="/api/v1/city/{cityId}/staff/roleRequests")
	public ResponseEntity<Object> getRoleRequests(@RequestHeader(name="auth") String token,
													@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		List<RoleRequest> list = this.controller.getRequests(username, cityId);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/manageRoleRequest")
	public ResponseEntity<Object> manageRoleRequest(@RequestHeader(name="auth") String token,
													@PathVariable("cityId") String cityId, 
													@RequestParam("requestId") String requestId,
													@RequestParam("judge") boolean judge) {
		String username = auth.getUsername(token);
		if(this.controller.judge(username, requestId, judge)) {
			return new ResponseEntity<Object>(wrc.make("Richiesta accettata"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}
	


	@GetMapping(value="/api/v1/city/{cityId}/staff/getPendingRequests")
	public ResponseEntity<Object> getAllRequest(@RequestHeader(name="auth") String token,
												@PathVariable String cityId) {
		String username = auth.getUsername(token);
		List<PendingRequest> list = this.controller.getAllRequest(username, cityId);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/staff/getPendingRequest")
	public ResponseEntity<Object> getRequest(@RequestHeader(name="auth") String token,
											 @RequestParam("requestId") String requestId,
											 @PathVariable String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.getRequest(username, requestId) != null) {
			return new ResponseEntity<Object>(this.controller.getRequest(username, requestId), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}
	

	@GetMapping(value="/api/v1/city/{cityId}/staff/reports")
	public ResponseEntity<Object> getReports(@RequestHeader(name="auth") String token,
												@PathVariable("cityId") String cityId) {
    	String username = auth.getUsername(token);
    	List<Report> list = controller.getReports(username, cityId);
    	if(list != null)
    		return new ResponseEntity<Object>(list, HttpStatus.OK);
	    else
	    	return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }
	
	@GetMapping(value="/api/v1/city/{cityId}/staff/report")
	public ResponseEntity<Object> getReport(@RequestHeader(name="auth") String token,
												@PathVariable("cityId") String cityId,
												@RequestParam("id") String id) {
    	String username = auth.getUsername(token);
    	Report report = controller.getReport(username, cityId, id);
    	if(report != null)
    		return new ResponseEntity<Object>(report, HttpStatus.OK);
	    else
	    	return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }
	
	//-------------------------------CONTRIBUTORS------------------------

	
	@GetMapping(value="/api/v1/city/{cityId}/posts/{postId}/contributes")
	public ResponseEntity<Object> getContributes(@RequestHeader(name="auth") String token,
												@PathVariable("postId") String postId, 
												@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		List<Contribute> list = this.controller.getContributes(username, postId);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/posts/{postId}/declareWinner")
	public ResponseEntity<Object> declareWinner(@RequestHeader(name="auth") String token,
												@PathVariable("postId") String postId, 
												@RequestParam("winnerId") String winnerId,
												@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.declareWinner(username, postId, winnerId)) {
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
		String username = auth.getUsername(token);
		if(this.controller.judge(username, pendingId, judge, motivation)) {
			return new ResponseEntity<Object>(wrc.make("Pending accettato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	

	@PostMapping(value="/api/v1/city/{cityId}/groups/createGroup")
	public ResponseEntity<Object> createGroup(@RequestHeader(name="auth") String token,
											    @PathVariable("cityId") String cityId,
												@RequestBody ProtoGroup data) {
		String username = auth.getUsername(token);
		if(this.controller.createGroup(username, cityId, data)) {
			return new ResponseEntity<Object>(wrc.make("Gruppo creato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/groups/{groupId}/editGroup")
	public ResponseEntity<Object> editGroup(@RequestHeader(name="auth") String token,
											@PathVariable("groupId") String groupId,
											@RequestBody ProtoGroup data, 
											@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.editGroup(username, groupId, data)) {
			return new ResponseEntity<Object>(wrc.make("Gruppo modificato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value="/api/v1/city/{cityId}/groups/{groupId}/removeGroup")
	public ResponseEntity<Object> removeGroup(@RequestHeader(name="auth") String token,
											@PathVariable("groupId") String groupId, 
											@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.removeGroup(username, groupId)) {
			return new ResponseEntity<Object>(wrc.make("Gruppo eliminato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	

	@PostMapping(value="/api/v1/city/{cityId}/posts/createPost")
	public ResponseEntity<Object> createPost(@RequestHeader(name="auth") String token,
											 @RequestParam("lat") double lat,
											 @RequestParam("lng") double lng,
											 @RequestBody ProtoPost data,
											 @PathVariable("cityId") String cityId)  {
		String username = auth.getUsername(token);
		Position pos = new Position(lat, lng);
		if(this.controller.createPost(username, pos, cityId, data)) {
			return new ResponseEntity<Object>(wrc.make("Post creato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/posts/{postId}/editPost")
	public ResponseEntity<Object> editPost(@RequestHeader(name="auth") String token,
											@PathVariable("postId") String postId,
			 								@RequestBody ProtoPost data,
			 								@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.editPost(postId, username, cityId, data)) {
			return new ResponseEntity<Object>(wrc.make("Post modificato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value="/api/v1/city/{cityId}/posts/{postId}/deletePost")
	public ResponseEntity<Object> deletePost(@RequestHeader(name="auth") String token,
											@PathVariable("postId") String postId, 
											@PathVariable("cityId") String cityId) {
		String username = auth.getUsername(token);
		if(this.controller.deletePost(postId, username)) {
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
		String username = auth.getUsername(token);
		this.controller.notifyEvent(username, message, contentId);
		return new ResponseEntity<Object>(wrc.make("Evento notificato."), HttpStatus.OK);
    }

	
	//-----------------------MEDIA--------------------
	
	@GetMapping(value="/api/v1/media")
	public ResponseEntity<Object> getMedia(@RequestHeader(name="paths") List<String> paths) {
		List<Resource> list = paths.stream().map(s -> this.fh.getFile(s)).toList();
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
	
	@PostMapping(value="/api/v1/path")
	public ResponseEntity<Object> getPath(@RequestBody List<MultipartFile> files) {
		List<String> list = files.stream().map(f -> this.fh.getName(f)).toList();
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
	

}
