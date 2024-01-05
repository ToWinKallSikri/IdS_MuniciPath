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

@RestController
public class MuniciPathRestController {
	
	@Autowired
	private MuniciPathController controller;
	private Authenticator auth;
	private FileHandler fh;
	private final String TURIST = "unregistered_tourist";
	
	public MuniciPathRestController() throws Exception {
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
											  @PathParam("cityId") String cityID, @PathParam("postIds") List<String> postIds,
											  @PathParam("start") LocalDateTime start, @PathParam("end") LocalDateTime end,
											  @PathParam("persistence") boolean persistence, @PathVariable String cityId) {
		String username = getUsernameFromToken(token);
		if(this.controller.createGroup(username, title, sorted, cityID, postIds, start, end, persistence)) {
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
















	
	
	/*
	
	
	
//	public boolean createGroup(String title, String author, boolean sorted, String cityId,
//			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
//		if(title == null || author == null || cityId == null || postIds == null)
//			return false;
//		return this.gh.createGroup(title, author, sorted, cityId, postIds, start, end, persistence);
//	}

//	public boolean editGroup(String groupId, String title, String author, boolean sorted,
//			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
//		if(groupId == null || title == null || author == null || postIds == null)
//			return false;
//		return this.gh.editGroup(groupId, title, author, sorted, postIds, start, end, persistence);
//	}
	
	
//	public boolean editGroupFromStaff(String username, String groupId, String title, boolean sorted,
//			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
//		if(username == null || groupId == null ||  title == null
//				|| postIds == null || (!checkStaff(username, idManager.getCityId(groupId))))
//			return false;
//		return this.gh.editGroup(groupId, title, sorted, postIds, start, end, persistence);
//	}
	
//	public boolean removeGroup(String author, String groupId) {
//		if(author == null || groupId == null)
//			return false;
//		return this.gh.removeGroup(author, groupId);
//	}
	
//	public boolean removeGroup(String groupId) {
//		if(groupId == null)
//			return false;
//		return this.gh.removeGroup(groupId);
//	}
	
//	public Group viewGroup(String groupId) {
//		if(groupId == null)
//			return null;
//		return this.gh.viewGroup(groupId);
//	}
	
//	public List<Group> viewGroups(List<String> groupIds) {
//		if(groupIds == null)
//			return null;
//		return this.gh.viewGroups(groupIds);
//	}
	
    
//    public boolean createPost(String title, PostType type, String text, String author, Position pos,
//            String cityId, ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
//    	if(title == null || type == null || text == null || author == null
//    			|| pos == null || cityId == null || data == null)
//    		return false;
//    	return this.poh.createPost(title, type, text, author, pos, cityId, data, start, end, persistence);
//    }
    
//    public boolean editPost(String postId, String title, PostType type, String text,
//    		String author, String cityId, ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
//    	if(postId == null || title == null || type == null || text == null
//    			|| author == null ||  cityId == null || data == null)
//    		return false;
//        return this.poh.editPost(postId, title, type, text, author, cityId, data, start, end, persistence);
//    }
    
    
//    public boolean editPostFromStaff(String username, String postId, String title, PostType type, String text,
//    		ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
//		if(username == null || postId == null || (!checkStaff(username, idManager.getCityId(postId))))
//			return false;
//    	if(title == null || type == null || text == null || data == null)
//    		return false;
//        return this.poh.editPost(postId, title, type, text, data, start, end, persistence);
//    }
    
    public List<Point> getPoints (String cityId, String username) {
    	if(cityId == null || username == null)
    		return null;
        return this.poh.getPoints(cityId, username);
      
    }
    
    public List<Post> viewPosts (String pointId, String username) {
    	if(pointId == null || username == null)
    		return null;
        return this.poh.viewPosts(pointId, username);
    }


    public List<Post> viewPosts (List<String> postIds) {
    	if(postIds == null)
    		return null;
        return this.poh.getPosts(postIds);
    }
    
    public Post viewPost(String postId, String username) {
    	if(postId == null || username == null)
    		return null;
        return this.poh.getPost(postId, username);
    }
    
    public boolean deletePost (String postId, String author) {
    	if(postId == null || author == null)
    		return false;
    	return this.poh.deletePost(postId, author);
    }
    
    
    public boolean deletePostFromStaff (String username, String postId) {
		if(username == null || postId == null || (!checkStaff(username, idManager.getCityId(postId))))
			return false;
    	return this.poh.deletePost(postId);
    }
    
    
    public List<Contribute> getContributes(String username, String postId){
    	if(postId == null || username == null)
    		return null;
		return this.poh.getContributes(username, postId);
	}
    
    
    public boolean addContentToContest(String contestAuthor, String contestId, List<String> content) {
    	if(contestAuthor == null || contestId == null || content == null)
    		return false;
		return this.poh.addContentToContest(contestAuthor, contestId, content);
	}
	
	
	public boolean declareWinner(String author, String contestId, String winnerId) {
		if(author == null || contestId == null || winnerId == null)
			return false;
		return this.poh.declareWinner(author, contestId, winnerId);
	}
    
    
    public boolean judge(String username, String pendingId, boolean outcome, String motivation) {
		if(username == null || pendingId == null || (!checkStaff(username, idManager.getCityId(pendingId))))
			return false;
    	if(motivation == null)
    		return false;
		return this.peh.judge(pendingId, outcome, motivation);
	}
	
    
	public List<PendingRequest> getAllRequest(String username, String cityId){
		if(username == null || (!checkStaff(username, cityId)))
			return null;
		if(cityId == null)
			return null;
		return this.peh.getAllRequest(cityId);
	}
	
	public PendingRequest getRequest(String requestId) {
		if(requestId == null)
			return null;
		return this.peh.getRequest(requestId);
	}
	
	
	public boolean removeUser(String username, String toRemove) {
		if(username == null || (!checkManager(username)))
    		return false;
		if(toRemove == null)
			return false;
		return this.uh.removeUser(toRemove);
	}
	
	
	public boolean changePassowrd(String username, String password) {
		if(username == null || password == null)
			return false;
		return this.uh.changePassowrd(username, password);
	}
	
	
	public boolean manageManager(String username, String toManage, boolean auth) {
		if(username == null || (!checkManager(username)))
    		return false;
		if(toManage == null)
			return false;
		return this.uh.manageManager(toManage, auth);
	}
    
    
    public List<User> getUsersNotConvalidated(String username){
    	if(username == null || (!checkManager(username)))
    		return null;
    	return this.uh.getNotConvalidatedUsers();
    }
    */
	
}
