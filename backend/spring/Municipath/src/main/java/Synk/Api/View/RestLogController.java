package Synk.Api.View;

import Synk.Api.Controller.User.UserHandler;
import Synk.Api.View.Auth.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
public class RestLogController {
    @Autowired
    private UserHandler uh;
    private final Authenticator authenticator;
    private final WebResponseCreator wrc;

    public RestLogController() {
        this.authenticator = new Authenticator();
        this.wrc = new WebResponseCreator();
    }

    @PostMapping(value="/api/v1/signin")
    public ResponseEntity<Object> signin(@RequestParam("username") String username, @RequestParam("password") String password) {
        if(uh.addUser(username, password)) {
            return new ResponseEntity<Object>(wrc.make("Creazione account riuscito."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value="/api/v1/changePassword")
    public ResponseEntity<Object> changePassword(@RequestHeader(name="auth") String token,
                                                 @RequestParam("newPassword") String newPassword) {
        String username = authenticator.getUsername(token);
        if(this.uh.changePassword(username, newPassword)) {
            return new ResponseEntity<Object>(wrc.make("Password cambiata."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping(value="/api/v1/login")
    public ResponseEntity<Object> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        if(this.uh.isThePassword(username, password)) {
            String jwt = authenticator.createJwt(username);
            return new ResponseEntity<Object>(wrc.make(jwt), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
}
