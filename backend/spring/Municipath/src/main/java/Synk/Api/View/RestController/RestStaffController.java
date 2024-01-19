package Synk.Api.View.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Synk.Api.Controller.Analysis.AnalysisHandler;
import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Model.Analysis.Analysis;
import Synk.Api.Model.City.Report.Report;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.City.Role.RoleRequest;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.View.WebResponseCreator;
import Synk.Api.View.Auth.Authenticator;
import Synk.Api.View.Auth.Authorizer;

@RestController
public class RestStaffController {

	@Autowired
	private AnalysisHandler ah;
	@Autowired
    private CityHandler ch;
	@Autowired
	private PendingHandler peh;
    @Autowired
    private Authorizer authorizer;
    private Authenticator authenticator;
    private WebResponseCreator wrc;
    
    public RestStaffController(){
    	this.authenticator = new Authenticator();
    	this.wrc = new WebResponseCreator();
    }
	
	@PostMapping(value="/api/v1/city/{cityId}/staff/analysis")
	public ResponseEntity<Object> getAnalysis(@RequestHeader(name="auth") String token,
											  @PathVariable("cityId") String cityId,
											  @RequestParam("months") int months, 
											  @RequestParam("onlyUsers")boolean onlyUsers) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isCurator(username, cityId)) {
			Analysis data = this.ah.getAnalysis(cityId, months, onlyUsers);
			if(data != null)
				return new ResponseEntity<Object>(data, HttpStatus.OK);
			else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping(value="/api/v1/city/{cityId}/staff/setModerator")
	public ResponseEntity<Object> setModerator(@RequestHeader(name="auth") String token,
												@RequestParam("toSet") String toSet, 
												@RequestParam("action") boolean action, 
												@PathVariable("cityId") String cityId) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isCurator(username, cityId)) {
			boolean result;
			if(action) 
				result = this.ch.addModerator(toSet, cityId);
			else result = this.ch.removeModerator(toSet, cityId);
			if(result) {
				return new ResponseEntity<Object>(wrc.make("Moderatore aggiunto."), HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/setRole")
	public ResponseEntity<Object> setRole(@RequestHeader(name="auth") String token,
											@PathVariable("cityId") String cityId,
											@RequestParam("toSet") String toSet, 
											@RequestParam("role") String role) {
		String username = authenticator.getUsername(token);
		Role _role = Role.safeValueOf(role);
		if(this.authorizer.isStaff(username, cityId)) {
			if(this.ch.setRole(toSet, cityId, _role)) {
				return new ResponseEntity<Object>(wrc.make("Ruovo modificato."), HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
	
	@GetMapping(value="/api/v1/city/{cityId}/staff/roleRequests")
	public ResponseEntity<Object> getRoleRequests(@RequestHeader(name="auth") String token,
													@PathVariable("cityId") String cityId) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isStaff(username, cityId)) {
			List<RoleRequest> list = this.ch.getRequests(cityId);
			if(list != null) {
				return new ResponseEntity<Object>(list, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(value="/api/v1/city/{cityId}/staff/manageRoleRequest")
	public ResponseEntity<Object> manageRoleRequest(@RequestHeader(name="auth") String token,
													@PathVariable("cityId") String cityId, 
													@RequestParam("requestId") String requestId,
													@RequestParam("judge") boolean judge) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isStaff(username, cityId)) {
			if(this.ch.judge(requestId, judge)) {
				return new ResponseEntity<Object>(wrc.make("Richiesta accettata"), HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	


	@GetMapping(value="/api/v1/city/{cityId}/staff/getPendingRequests")
	public ResponseEntity<Object> getAllRequest(@RequestHeader(name="auth") String token,
												@PathVariable String cityId) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isStaff(username, cityId)) {
			List<PendingRequest> list = this.peh.getAllRequest(cityId);
			if(list != null) {
				return new ResponseEntity<Object>(list, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value="/api/v1/city/{cityId}/staff/getPendingRequest")
	public ResponseEntity<Object> getRequest(@RequestHeader(name="auth") String token,
											 @RequestParam("requestId") String requestId,
											 @PathVariable String cityId) {
		String username = authenticator.getUsername(token);
		if(this.authorizer.isStaff(username, cityId)) {
			PendingRequest request = this.peh.getRequest(requestId);
			if(request != null) {
				return new ResponseEntity<Object>(request, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	

	@GetMapping(value="/api/v1/city/{cityId}/staff/reports")
	public ResponseEntity<Object> getReports(@RequestHeader(name="auth") String token,
												@PathVariable("cityId") String cityId) {
    	String username = authenticator.getUsername(token);
    	if(this.authorizer.isStaff(username, cityId)) {
	    	List<Report> list = ch.getReports(cityId);
	    	if(list != null)
	    		return new ResponseEntity<Object>(list, HttpStatus.OK);
		    else
		    	return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    	} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
	
	@GetMapping(value="/api/v1/city/{cityId}/staff/report")
	public ResponseEntity<Object> getReport(@RequestHeader(name="auth") String token,
												@PathVariable("cityId") String cityId,
												@RequestParam("id") String id) {
    	String username = authenticator.getUsername(token);
    	if(this.authorizer.isStaff(username, cityId)) {
	    	Report report = ch.getReport(id);
	    	if(report != null)
	    		return new ResponseEntity<Object>(report, HttpStatus.OK);
		    else
		    	return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    	} else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
