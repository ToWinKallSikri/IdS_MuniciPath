package Synk.Api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import Synk.Api.Model.MuniciPathModel;
import jakarta.websocket.server.PathParam;

@RestController
public class MuniciPathRestController {
	
	@Autowired
	private MuniciPathModel model;
	private Authenticator auth;
	private FileHandler fh;
	private Encoder encoder;
	
	public MuniciPathRestController() throws Exception {
		this.auth = new Authenticator();
		this.fh = new FileHandler();
		this.encoder = new Encoder();
	}
	
	
	@PostMapping(value="/api/v1/signin")
	public ResponseEntity<Object> signin(@PathParam("username") String username, @PathParam("password") String password) throws Exception{
		password = encoder.encode(password);
		if(this.model.addUser(username, password)) {
			return new ResponseEntity<Object>("Creazione account riuscito.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Creazione account fallito.", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@PutMapping(value="/api/v1/manager/convalidate")
	public ResponseEntity<Object> convalidateAccount(@RequestHeader(name="Authorization") String token, @PathParam("toValidate") String toValidate){
		String username = this.auth.getUsername(token);
		System.out.println(toValidate);
		if(this.model.userValidation(username, toValidate)) {
			return new ResponseEntity<Object>("Account convalidato.", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Convalidazione non riuscita.", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@GetMapping(value="/api/v1/login")
	public ResponseEntity<Object> login(@PathParam("username") String username, @PathParam("password") String password) throws Exception{
		if(this.model.isThePassword(username, password)) {
			String jwt = auth.createJwt(username);
			return new ResponseEntity<Object>(jwt, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Username o password errati", HttpStatus.BAD_REQUEST);
		}
	}
	
}
