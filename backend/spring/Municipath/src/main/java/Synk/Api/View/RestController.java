package Synk.Api.View;

import Synk.Api.Model.Post.PostType;
import org.springframework.web.bind.annotation.*;

import Synk.Api.Controller.MuniciPathController;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.City.Role.RoleRequest;
import Synk.Api.Model.Post.Position;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.websocket.server.PathParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	private MuniciPathController controller;
	private Authenticator auth;
	private FileHandler fh;
	private final String TURIST = "unregistered_tourist";
	
	public RestController() throws Exception {
		this.auth = new Authenticator();
		this.fh = new FileHandler();
	}
	
	
	private String getUsernameFromToken(String token) {
		String username = this.auth.getUsername(token);
		return username == null ? TURIST : username;
	}
	


	@GetMapping(value="/api/v1/isManager")
	public ResponseEntity<Object> isManager(@RequestHeader(name="Authorization") String token) {
		String username = getUsernameFromToken(token);
		return new ResponseEntity<Object>(this.controller.checkManager(username), HttpStatus.OK);
	}

	@GetMapping(value="/api/v1/role")
	public ResponseEntity<Object> getRole(@RequestHeader(name="Authorization") String token, @PathParam("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		return new ResponseEntity<Object>(this.controller.getRole(username, cityId), HttpStatus.OK);
	}
	
	
	@PostMapping(value="/api/v1/signin")
	public ResponseEntity<Object> signin(@PathParam("username") String username, @PathParam("password") String password) {
		if(this.controller.addUser(username, password)) {
			return new ResponseEntity<Object>("Creazione account riuscito.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Creazione account fallito.", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PutMapping(value="/api/v1/manager/convalidate")
	public ResponseEntity<Object> convalidateAccount(@RequestHeader(name="Authorization") String token, @PathParam("toValidate") String toValidate){
		String username = getUsernameFromToken(token);
		if(this.controller.userValidation(username, toValidate)) {
			return new ResponseEntity<Object>("Account convalidato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Convalidazione non riuscita.", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@GetMapping(value="/api/v1/login")
	public ResponseEntity<Object> login(@PathParam("username") String username, @PathParam("password") String password) {
		if(this.controller.isThePassword(username, password)) {
			String jwt = auth.createJwt(username);
			return new ResponseEntity<Object>(jwt, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Username o password errati", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/api/v1/manager/createCity")
	public ResponseEntity<Object> createCity(@RequestHeader(name="Authorization") String token,
			@PathParam("cityName") String cityName, @PathParam("cap") int cap, @PathParam("curator") String curator,
			@PathParam("lat") double lat,  @PathParam("lng") double lng) {
		String username = getUsernameFromToken(token);
		Position pos = new Position(lat, lng);
		if(this.controller.createCity(username, cityName, cap, curator, pos)) {
			return new ResponseEntity<Object>("Comune creato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Creazione fallita.", HttpStatus.BAD_REQUEST);
		}
    }
	
	@PutMapping(value="/api/v1/manager/updateCity")
	public ResponseEntity<Object> updateCity(@RequestHeader(name="Authorization") String token,
			@PathParam("cityId") String cityId, @PathParam("cityName") String cityName, @PathParam("cap") int cap,
			@PathParam("curator") String curator, @PathParam("lat") double lat,  @PathParam("lng") double lng) {
		String username = getUsernameFromToken(token);
		Position pos = new Position(lat, lng);
		if(this.controller.updateCity(username, cityId, cityName, cap, curator, pos)) {
			return new ResponseEntity<Object>("Comune aggiornato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Aggiornamento fallito.", HttpStatus.BAD_REQUEST);
		}
    }
	
	@DeleteMapping(value="/api/v1/manager/deleteCity")
	public ResponseEntity<Object> deleteCity(@RequestHeader(name="Authorization") String token, @PathParam("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.deleteCity(username, cityId)) {
			return new ResponseEntity<Object>("Comune eliminato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Eliminazione fallita.", HttpStatus.BAD_REQUEST);
		}
    }
	
	@GetMapping(value="/api/v1/cities")
	public ResponseEntity<Object> getCities(@PathParam("cityName") String cityName) {
		return new ResponseEntity<Object>(this.controller.searchCity(cityName), HttpStatus.OK);
    }
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/setRole")
	public ResponseEntity<Object> setRole(@RequestHeader(name="Authorization") String token,
			@PathParam("toSet") String toSet, @PathVariable("cityId") String cityId, @PathParam("role") String role) {
		String username = getUsernameFromToken(token);
		Role _role = Role.safeValueOf(role);
		if(this.controller.setRole(username, toSet, cityId, _role)) {
			return new ResponseEntity<Object>("Ruovo modificato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Operazione fallita.", HttpStatus.BAD_REQUEST);
		}
    }

	@PutMapping(value="/api/v1/city/{cityId}/staff/addModerator")
	public ResponseEntity<Object> addModerator(@RequestHeader(name="Authorization") String token,
			@PathParam("toSet") String toSet, @PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.addModerator(username, toSet, cityId)) {
			return new ResponseEntity<Object>("Moderatore aggiunto.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Operazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/staff/removeModerator")
	public ResponseEntity<Object> removeModerator(@RequestHeader(name="Authorization") String token,
			@PathParam("toSet") String toSet, @PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.removeModerator(username, toSet, cityId)) {
			return new ResponseEntity<Object>("Moderatore rimosso.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Operazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/api/v1/city/{cityId}/addRoleRequest")
	public ResponseEntity<Object> addRoleRequest(@RequestHeader(name="Authorization") String token,
			@PathVariable("cityId") String cityId) {
		String username = this.auth.getUsername(token);
		if(this.controller.addRequest(username, cityId)) {
			return new ResponseEntity<Object>("Richiesta inviata.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Operazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value="/api/v1/city/{cityId}/staff/roleRequests")
	public ResponseEntity<Object> getRoleRequests(@RequestHeader(name="Authorization") String token,
			@PathVariable("cityId") String cityId) {
		String username = getUsernameFromToken(token);
		List<RoleRequest> list = this.controller.getRequests(username, cityId);
		if(list != null) {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Richieste non trovate.", HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/acceptRoleRequest")
	public ResponseEntity<Object> acceptRoleRequest(@RequestHeader(name="Authorization") String token,
			@PathVariable("cityId") String cityId, @PathParam("requestId") String requestId) {
		String username = getUsernameFromToken(token);
		if(this.controller.judge(username, requestId, true)) {
			return new ResponseEntity<Object>("Richiesta accettata", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Richieste non trovate.", HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/rejectRoleRequest")
	public ResponseEntity<Object> rejectRoleRequest(@RequestHeader(name="Authorization") String token,
			@PathVariable("cityId") String cityId, @PathParam("requestId") String requestId) {
		String username = getUsernameFromToken(token);
		if(this.controller.judge(username, requestId, false)) {
			return new ResponseEntity<Object>("Richiesta rifiutata", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Richieste non trovate.", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value="/api/v1/city/{cityId}/groups/createGroup")
	public ResponseEntity<Object> createGroup(@RequestHeader(name="Authorization") String token,
											  @PathParam("title") String title, @PathParam("sorted") boolean sorted,
											  @PathParam("postIds") List<String> postIds,
											  @PathParam("start") LocalDateTime start, @PathParam("end") LocalDateTime end,
											  @PathParam("persistence") boolean persistence, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.createGroup(username, title, sorted, cityId, postIds, start, end, persistence)) {
			return new ResponseEntity<Object>("Gruppo creato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Creazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/groups/editGroup")
	public ResponseEntity<Object> editGroup(@RequestHeader(name="Authorization") String token,
											@PathParam("groupId") String groupId, @PathParam("title") String title,
											@PathParam("sorted") boolean sorted, @PathParam("postIds") List<String> postIds,
											@PathParam("start") LocalDateTime start, @PathParam("end") LocalDateTime end,
											@PathParam("persistence") boolean persistence, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.editGroup(username, groupId, title, sorted, postIds, start, end, persistence)) {
			return new ResponseEntity<Object>("Gruppo modificato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Modifica fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/staff/editGroupFromStaff")
	public ResponseEntity<Object> editGroupFromStaff(@RequestHeader(name="Authorization") String token,
											@PathParam("groupId") String groupId, @PathParam("title") String title,
											@PathParam("sorted") boolean sorted, @PathParam("postIds") List<String> postIds,
											@PathParam("start") LocalDateTime start, @PathParam("end") LocalDateTime end,
											@PathParam("persistence") boolean persistence, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.editGroupFromStaff(username, groupId, title, sorted, postIds, start, end, persistence)) {
			return new ResponseEntity<Object>("Gruppo modificato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Modifica fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value="/api/v1/city/{cityId}/groups/removeGroup")
	public ResponseEntity<Object> removeGroup(@RequestHeader(name="Authorization") String token,
											  @PathParam("groupId") String groupId, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.removeGroup(username, groupId)) {
			return new ResponseEntity<Object>("Gruppo eliminato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Eliminazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value="/api/v1/city/{cityId}/staff/removeGroupFromStaff")
	public ResponseEntity<Object> removeGroupFromStaff(@RequestHeader(name="Authorization") String token,
											  @PathParam("groupId") String groupId, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.removeGroupFromStaff(username, groupId)) {
			return new ResponseEntity<Object>("Gruppo eliminato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Eliminazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/groups/viewGroup")
	public ResponseEntity<Object> viewGroup(@PathParam("groupId") String groupId, @PathVariable String cityId) {
		if(this.controller.viewGroup(groupId) != null) {
			return new ResponseEntity<Object>(this.controller.viewGroup(groupId), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Gruppo non trovato.", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/groups/viewGroups")
	public ResponseEntity<Object> viewGroups(@PathParam("groupIds") List<String> groupIds, @PathVariable String cityId) {
		if(this.controller.viewGroups(groupIds) != null) {
			return new ResponseEntity<Object>(this.controller.viewGroups(groupIds), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Gruppi non trovati.", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value="/api/v1/city/{cityId}/points/createPost")
	public ResponseEntity<Object> createPost(@RequestHeader(name="Authorization") String token,
											 @PathParam("title") String title, @PathParam("type") PostType type,
											 @PathParam("text") String text,
											 @PathParam("lat") double lat, @PathParam("lng") double lng,
											 @PathParam("data") List<String> data, @PathParam("start") LocalDateTime start,
											 @PathParam("end") LocalDateTime end, @PathParam("persistence") boolean persistence,
											 @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.createPost(title, type, text, username, new Position(lat, lng), cityId, (ArrayList<String>) data, start, end, persistence)) {
			return new ResponseEntity<Object>("Post creato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Creazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/points/editPost")
	public ResponseEntity<Object> editPost(@RequestHeader(name="Authorization") String token, @PathParam("postId") String postId,
										   @PathParam("title") String title, @PathParam("type") PostType type,
										   @PathParam("text") String text, @PathParam("data") List<String> data,
										   @PathParam("start") LocalDateTime start, @PathParam("end") LocalDateTime end,
										   @PathParam("persistence") boolean persistence, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.editPost(postId, title, type, text, username, cityId, (ArrayList<String>) data, start, end, persistence)) {
			return new ResponseEntity<Object>("Post modificato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Modifica fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/staff/editPostFromStaff")
	public ResponseEntity<Object> editPostFromStaff(@RequestHeader(name="Authorization") String token, @PathParam("postId") String postId,
										   @PathParam("title") String title, @PathParam("type") PostType type,
										   @PathParam("text") String text, @PathParam("data") List<String> data,
										   @PathParam("start") LocalDateTime start, @PathParam("end") LocalDateTime end,
										   @PathParam("persistence") boolean persistence, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.editPostFromStaff(username, postId, title, type, text, (ArrayList<String>) data, start, end, persistence)) {
			return new ResponseEntity<Object>("Post modificato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Modifica fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/points/getPoints")
	public ResponseEntity<Object> getPoints(@RequestHeader(name="Authorization") String token,
											@PathVariable String cityId) {
	String username = getUsernameFromToken(token);
		if(this.controller.getPoints(cityId, username) != null) {
			return new ResponseEntity<Object>(this.controller.getPoints(cityId, username), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Punti non trovati.", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/points/viewPosts")
	public ResponseEntity<Object> viewPosts(@RequestHeader(name="Authorization") String token,
											@PathParam("pointId") String pointId, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.viewPosts(pointId, username) != null) {
			return new ResponseEntity<Object>(this.controller.viewPosts(pointId, username), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Post non trovati.", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/points/viewPost")
	public ResponseEntity<Object> viewPost(@RequestHeader(name="Authorization") String token,
										   @PathParam("postId") String postId, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.viewPost(postId, username) != null) {
			return new ResponseEntity<Object>(this.controller.viewPost(postId, username), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Post non trovato.", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="/api/v1/city/{cityId}/points/deletePost")
	public ResponseEntity<Object> deletePost(@RequestHeader(name="Authorization") String token,
										   @PathParam("postId") String postId, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.deletePost(postId, username)) {
			return new ResponseEntity<Object>("Post eliminato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Eliminazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value="/api/v1/city/{cityId}/staff/deletePostFromStaff")
	public ResponseEntity<Object> deletePostFromStaff(@RequestHeader(name="Authorization") String token,
													  @PathParam("postId") String postId, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if (this.controller.deletePostFromStaff(username, postId)) {
			return new ResponseEntity<Object>("Post eliminato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Eliminazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value="/api/v1/city/{cityId}/points/contest/getContributes")
	public ResponseEntity<Object> getContributes(@RequestHeader(name="Authorization") String token,
												 @PathParam("postId") String postId, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.getContributes(username, postId) != null) {
			return new ResponseEntity<Object>(this.controller.getContributes(username, postId), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Nessun contributo trovato", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value="/api/v1/city/{cityId}/points/contest/addContentToContest")
	public ResponseEntity<Object> addContentToContest(@RequestHeader(name="Authorization") String token,
													  @PathParam("contestId") String contestId, List<String> content,
													  @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.addContentToContest(username, contestId, content)) {
			return new ResponseEntity<Object>("Contenuto aggiunto.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Aggiunta fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value="/api/v1/city/{cityId}/points/contest/declareWinner")
	public ResponseEntity<Object> declareWinner(@RequestHeader(name="Authorization") String token,
												@PathParam("contestId") String contestId, @PathParam("winnerId") String winnerId,
												@PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.declareWinner(username, contestId, winnerId)) {
			return new ResponseEntity<Object>("Vincitore dichiarato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Dichiarazione fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/city/{cityId}/staff/judge")
	public ResponseEntity<Object> judge(@RequestHeader(name="Authorization") String token,
										@PathParam("pendingId") String pendingId, @PathParam("outcome") boolean outcome,
										@PathParam("motivation") String motivation, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.judge(username, pendingId, outcome, motivation)) {
			return new ResponseEntity<Object>("Giudizio emesso.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Giudizio fallito.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/staff/getAllRequest")
	public ResponseEntity<Object> getAllRequest(@RequestHeader(name="Authorization") String token,
												@PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.getAllRequest(username, cityId) != null) {
			return new ResponseEntity<Object>(this.controller.getAllRequest(username, cityId), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Richieste non trovate.", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value="/api/v1/city/{cityId}/staff/getRequest")
	public ResponseEntity<Object> getRequest(@RequestHeader(name="Authorization") String token,
											 @PathParam("requestId") String requestId, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.getRequest(username, requestId) != null) {
			return new ResponseEntity<Object>(this.controller.getRequest(username, requestId), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Richiesta non trovata.", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="/api/v1/manager/removeUser")
	public ResponseEntity<Object> removeUser(@RequestHeader(name="Authorization") String token,
											 @PathParam("toRemove") String toRemove) {
		String username = getUsernameFromToken(token);
		if(this.controller.removeUser(username, toRemove)) {
			return new ResponseEntity<Object>("Utente rimosso.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Rimozione fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value="/api/v1/changePassword")
	public ResponseEntity<Object> changePassword(@RequestHeader(name="Authorization") String token,
												 @PathParam("newPassword") String newPassword) {
		String username = getUsernameFromToken(token);
		if(this.controller.changePassword(username, newPassword)) {
			return new ResponseEntity<Object>("Password cambiata.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Cambio fallito.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/api/v1/manager/manageManager")
	public ResponseEntity<Object> manageManager(@RequestHeader(name="Authorization") String token,
											@PathParam("toManage") String toManage,
											@PathParam("auth") boolean auth) {
		String username = getUsernameFromToken(token);
		if(this.controller.manageManager(username, toManage, auth)) {
			return new ResponseEntity<Object>("Manager modificato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Modifica fallita.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/api/v1/manager/getUsersNotConvalidated")
	public ResponseEntity<Object> getUsersNotConvalidated(@RequestHeader(name="Authorization") String token) {
		String username = getUsernameFromToken(token);
		if(this.controller.getUsersNotConvalidated(username) != null) {
			return new ResponseEntity<Object>(this.controller.getUsersNotConvalidated(username), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Utenti non trovati.", HttpStatus.NOT_FOUND);
		}
	}

}
