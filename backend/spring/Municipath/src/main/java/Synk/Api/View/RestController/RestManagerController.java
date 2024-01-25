package Synk.Api.View.RestController;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.View.WebResponseCreator;
import Synk.Api.View.Auth.Authenticator;
import Synk.Api.View.Auth.Authorizer;
import Synk.Api.View.ViewModel.ProtoCity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestManagerController {
    @Autowired
    private UserHandler uh;
    @Autowired
    private CityHandler ch;
    @Autowired
    private Authorizer authorizer;
    private Authenticator authenticator;
    private WebResponseCreator wrc;
    
    public RestManagerController() {
    	this.authenticator = new Authenticator();
        this.wrc = new WebResponseCreator();
    }


    @DeleteMapping(value="/api/v1/manager/removeUser")
    public ResponseEntity<Object> removeUser(@RequestHeader(name="auth") String token,
                                             @RequestParam("toRemove") String toRemove) {
        String username = authenticator.getUsername(token);
        if(this.authorizer.isManager(username) &&  uh.removeUser(toRemove)) {
            return new ResponseEntity<Object>(wrc.make("Utente rimosso."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value="/api/v1/manager/convalidate")
    public ResponseEntity<Object> convalidateAccount(@RequestHeader(name="auth") String token, @RequestParam("toValidate") String toValidate){
        String username = authenticator.getUsername(token);
        if(this.authorizer.isManager(username) && uh.userValidation( toValidate)) {
            return new ResponseEntity<Object>(wrc.make("Account convalidato."), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping(value="/api/v1/manager/getUsersNotConvalidated")
    public ResponseEntity<Object> getUsersNotConvalidated(@RequestHeader(name="auth") String token) {
        String username = authenticator.getUsername(token);
        if(this.authorizer.isManager(username)) {
            List<String> list = this.uh.getNotConvalidatedUsers().stream().map(u -> u.getUsername()).toList();
            if (list != null) {
                return new ResponseEntity<Object>(list, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }



    @PutMapping(value="/api/v1/manager/addManager")
    public ResponseEntity<Object> addManager(@RequestHeader(name="auth") String token,
                                             @RequestParam("toManage") String toManage) {
        String username = authenticator.getUsername(token);
        if(this.authorizer.isManager(username)) {
            if (this.uh.manageManager(toManage, true)) {
                return new ResponseEntity<Object>(wrc.make("Manager aggiunto."), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }



    @PutMapping(value="/api/v1/manager/removeManager")
    public ResponseEntity<Object> removeManager(@RequestHeader(name="auth") String token,
                                                @RequestParam("toManage") String toManage) {
        String username = authenticator.getUsername(token);
        if(this.authorizer.isManager(username)) {
            if (this.uh.manageManager(toManage, false)) {
                return new ResponseEntity<Object>(wrc.make("Manager rimosso."), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value="/api/v1/manager/createCity")
    public ResponseEntity<Object> createCity(@RequestHeader(name="auth") String token,
                                             @RequestBody ProtoCity pc) {
        String username = authenticator.getUsername(token);
        if(this.authorizer.isManager(username)) {
            if (this.ch.createCity(pc.getCityName(), pc.getCap(), pc.getCurator(), pc.getPos())) {
                return new ResponseEntity<Object>(wrc.make("Comune " + pc.getCityName() + " creato."), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value="/api/v1/manager/updateCity")
    public ResponseEntity<Object> updateCity(@RequestHeader(name="auth") String token,
                                             @RequestParam("cityId") String cityId,
                                             @RequestBody ProtoCity pc) {
        String username = authenticator.getUsername(token);
        if(this.authorizer.isManager(username)) {
            if (this.ch.updateCity(cityId, pc.getCityName(), pc.getCap(), pc.getCurator(), pc.getPos())) {
                return new ResponseEntity<Object>(wrc.make("Comune aggiornato."), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value="/api/v1/manager/deleteCity")
    public ResponseEntity<Object> deleteCity(@RequestHeader(name="auth") String token,
                                             @RequestParam("cityId") String cityId) {
        String username = authenticator.getUsername(token);
        if (this.authorizer.isManager(username)) {
            if (this.ch.deleteCity(cityId)) {
                return new ResponseEntity<Object>(wrc.make("Comune eliminato."), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
