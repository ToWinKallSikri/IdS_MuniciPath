package Synk.Api.View;

import Synk.Api.Model.Post.Contribute.Contribute;
import Synk.Api.Model.User.User;
import Synk.Api.View.Model.ProtoCity;
import Synk.Api.View.Model.ProtoGroup;
import Synk.Api.View.Model.ProtoPost;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import Synk.Api.Controller.MuniciPathController;
import Synk.Api.Model.City.City;
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
	private final String TURIST = "unregistered_tourist";
	
	public RestController() throws Exception {
		this.auth = new Authenticator();
		this.fh = new FileHandler();
		this.wrc = new WebResponseCreator();
	}
	
	
	private String getUsernameFromToken(String token) {
		String username = this.auth.getUsername(token);
		return username == null ? TURIST : username;
	}

	@GetMapping(value="/api/v1/isManager")
	public ResponseEntity<Object> isManager(@RequestHeader(name="auth") String token) {
		String username = getUsernameFromToken(token);
		return new ResponseEntity<Object>(wrc.make(this.controller.checkManager(username)), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/isAuthor")
	public ResponseEntity<Object> isAuthor(@RequestHeader(name="auth") String token,
										   @RequestParam("contentId") String contentId) {
		String username = getUsernameFromToken(token);
		return new ResponseEntity<Object>(wrc.make(this.controller.checkAuthor(username, contentId)), HttpStatus.OK);
	}
	
	@GetMapping(value="/api/v1/havePowerWithIt")
	public ResponseEntity<Object> havePowerWithIt(@RequestHeader(name="auth") String token,
												  @RequestParam("contentId") String contentId) {
		String username = getUsernameFromToken(token);
		return new ResponseEntity<Object>(wrc.make(this.controller.havePowerWithIt(username, contentId)), HttpStatus.OK);
	}

	@GetMapping(value="/api/v1/role")
	public ResponseEntity<Object> getRole(@RequestHeader(name="auth") String token, @RequestParam("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		return new ResponseEntity<Object>(wrc.make(this.controller.getRole(username, cityId)), HttpStatus.OK);
	}
	
	
	@PostMapping(value="/api/v1/signin")
	public ResponseEntity<Object> signin(@RequestParam("username") String username, @RequestParam("password") String password) {
		if(this.controller.addUser(username, password)) {
			return new ResponseEntity<Object>(wrc.make("Creazione account riuscito."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}


	@DeleteMapping(value="/api/v1/manager/removeUser")
	public ResponseEntity<Object> removeUser(@RequestHeader(name="auth") String token,
											 @RequestParam("toRemove") String toRemove) {
		String username = getUsernameFromToken(token);
		if(this.controller.removeUser(username, toRemove)) {
			return new ResponseEntity<Object>(wrc.make("Utente rimosso."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PutMapping(value="/api/v1/manager/convalidate")
	public ResponseEntity<Object> convalidateAccount(@RequestHeader(name="auth") String token, @RequestParam("toValidate") String toValidate){
		String username = getUsernameFromToken(token);
		if(this.controller.userValidation(username, toValidate)) {
			return new ResponseEntity<Object>(wrc.make("Account convalidato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	


	@GetMapping(value="/api/v1/manager/getUsersNotConvalidated")
	public ResponseEntity<Object> getUsersNotConvalidated(@RequestHeader(name="auth") String token) {
		String username = getUsernameFromToken(token);
		List<User> list = this.controller.getUsersNotConvalidated(username);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value="/api/v1/changePassword")
	public ResponseEntity<Object> changePassword(@RequestHeader(name="auth") String token,
												 @RequestParam("newPassword") String newPassword) {
		String username = getUsernameFromToken(token);
		if(this.controller.changePassword(username, newPassword)) {
			return new ResponseEntity<Object>(wrc.make("Password cambiata."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/manager/addManager")
	public ResponseEntity<Object> addManager(@RequestHeader(name="auth") String token,
											 @RequestParam("toManage") String toManage) {
		String username = getUsernameFromToken(token);
		if(this.controller.manageManager(username, toManage, true)) {
			return new ResponseEntity<Object>(wrc.make("Manager aggiunto."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	

	@PutMapping(value="/api/v1/manager/removeManager")
	public ResponseEntity<Object> removeManager(@RequestHeader(name="auth") String token,
												@RequestParam("toManage") String toManage) {
		String username = getUsernameFromToken(token);
		if(this.controller.manageManager(username, toManage, false)) {
			return new ResponseEntity<Object>(wrc.make("Manager rimosso."), HttpStatus.OK);
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
	
	@PostMapping(value="/api/v1/manager/createCity")
	public ResponseEntity<Object> createCity(@RequestHeader(name="auth") String token,
											@RequestBody ProtoCity pc) {
		String username = getUsernameFromToken(token);
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
		String username = getUsernameFromToken(token);
		if(this.controller.updateCity(username, cityId, pc.getCityName(), pc.getCap(), pc.getCurator(), pc.getPos())) {
			return new ResponseEntity<Object>(wrc.make("Comune aggiornato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
    }
	
	@DeleteMapping(value="/api/v1/manager/deleteCity")
	public ResponseEntity<Object> deleteCity(@RequestHeader(name="auth") String token, 
											@RequestParam("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.deleteCity(username, cityId)) {
			return new ResponseEntity<Object>(wrc.make("Comune eliminato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
    }
	
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
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/setRole")
	public ResponseEntity<Object> setRole(@RequestHeader(name="auth") String token,
											@PathVariable("cityId") String cityId,
											@RequestParam("toSet") String toSet, 
											@RequestParam("role") String role) {
		String username = getUsernameFromToken(token);
		Role _role = Role.safeValueOf(role);
		if(this.controller.setRole(username, toSet, cityId, _role)) {
			return new ResponseEntity<Object>(wrc.make("Ruovo modificato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
    }

	@PutMapping(value="/api/v1/city/{cityId}/staff/addModerator")
	public ResponseEntity<Object> addModerator(@RequestHeader(name="auth") String token,
												@RequestParam("toSet") String toSet, 
												@PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
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
		String username = getUsernameFromToken(token);
		if(this.controller.removeModerator(username, toSet, cityId)) {
			return new ResponseEntity<Object>(wrc.make("Moderatore rimosso."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
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
	
	@GetMapping(value="/api/v1/city/{cityId}/staff/roleRequests")
	public ResponseEntity<Object> getRoleRequests(@RequestHeader(name="auth") String token,
													@PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		List<RoleRequest> list = this.controller.getRequests(username, cityId);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/acceptRoleRequest")
	public ResponseEntity<Object> acceptRoleRequest(@RequestHeader(name="auth") String token,
													@PathVariable("cityId") String cityId, 
													@RequestParam("requestId") String requestId) {
		String username = getUsernameFromToken(token);
		if(this.controller.judge(username, requestId, true)) {
			return new ResponseEntity<Object>(wrc.make("Richiesta accettata"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/rejectRoleRequest")
	public ResponseEntity<Object> rejectRoleRequest(@RequestHeader(name="auth") String token,
													@PathVariable("cityId") String cityId, 
													@RequestParam("requestId") String requestId) {
		String username = getUsernameFromToken(token);
		if(this.controller.judge(username, requestId, false)) {
			return new ResponseEntity<Object>(wrc.make("Richiesta rifiutata"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value="/api/v1/city/{cityId}/groups/createGroup")
	public ResponseEntity<Object> createGroup(@RequestHeader(name="auth") String token,
											    @PathVariable("cityId") String cityId,
												@RequestBody ProtoGroup data) {
		String username = getUsernameFromToken(token);
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
		String username = getUsernameFromToken(token);
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
		String username = getUsernameFromToken(token);
		if(this.controller.removeGroup(username, groupId)) {
			return new ResponseEntity<Object>(wrc.make("Gruppo eliminato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
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

	@PostMapping(value="/api/v1/city/{cityId}/posts/createPost")
	public ResponseEntity<Object> createPost(@RequestHeader(name="auth") String token,
											 @RequestParam("lat") double lat,
											 @RequestParam("lng") double lng,
											 @RequestBody ProtoPost data,
											 @PathVariable("cityId") String cityId)  {
		String username = getUsernameFromToken(token);
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
		String username = getUsernameFromToken(token);
		if(this.controller.editPost(postId, username, cityId, data)) {
			return new ResponseEntity<Object>(wrc.make("Post modificato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/points")
	public ResponseEntity<Object> getPoints(@RequestHeader(name="auth") String token,
											@PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
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
		String username = getUsernameFromToken(token);
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
		String username = getUsernameFromToken(token);
		Post post = this.controller.viewPost(postId, username);
		if(post != null) {
			return new ResponseEntity<Object>(post, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="/api/v1/city/{cityId}/posts/{postId}/deletePost")
	public ResponseEntity<Object> deletePost(@RequestHeader(name="auth") String token,
											@PathVariable("postId") String postId, 
											@PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.deletePost(postId, username)) {
			return new ResponseEntity<Object>(wrc.make("Post eliminato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/posts/{postId}/contributes")
	public ResponseEntity<Object> getContributes(@RequestHeader(name="auth") String token,
												@PathVariable("postId") String postId, 
												@PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		List<Contribute> list = this.controller.getContributes(username, postId);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}
	
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

	@PostMapping(value="/api/v1/city/{cityId}/posts/{postId}/addContribute")
	public ResponseEntity<Object> addContribute(@RequestHeader(name="auth") String token,
												@PathVariable("postId") String postId, 
												@RequestBody List<String> content,
												@PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.addContentToContest(username, postId, content)) {
			return new ResponseEntity<Object>(wrc.make("Contenuto aggiunto."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/posts/{postId}/declareWinner")
	public ResponseEntity<Object> declareWinner(@RequestHeader(name="auth") String token,
												@PathVariable("postId") String postId, 
												@RequestParam("winnerId") String winnerId,
												@PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.declareWinner(username, postId, winnerId)) {
			return new ResponseEntity<Object>(wrc.make("Vincitore dichiarato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/staff/acceptPending")
	public ResponseEntity<Object> acceptPending(@RequestHeader(name="auth") String token,
												@RequestParam("pendingId") String pendingId,
												@RequestParam("motivation") String motivation,
												@PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.judge(username, pendingId, true, motivation)) {
			return new ResponseEntity<Object>(wrc.make("Pending accettato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/staff/rejectPending")
	public ResponseEntity<Object> rejectPending(@RequestHeader(name="auth") String token,
												@RequestParam("pendingId") String pendingId,
												@RequestParam("motivation") String motivation,
												@PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.judge(username, pendingId, false, motivation)) {
			return new ResponseEntity<Object>(wrc.make("Pending rifiutato."), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/staff/getPendingRequests")
	public ResponseEntity<Object> getAllRequest(@RequestHeader(name="auth") String token,
												@PathVariable String cityId) {
		String username = getUsernameFromToken(token);
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
		String username = getUsernameFromToken(token);
		if(this.controller.getRequest(username, requestId) != null) {
			return new ResponseEntity<Object>(this.controller.getRequest(username, requestId), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}

}
