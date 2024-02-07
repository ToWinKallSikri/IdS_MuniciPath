package Synk.Api.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({"/", "/makecity/{lat:\\w+}/{lng:\\w+}", "/updatecity/{id:\\w+}", "/deletecity/{id:\\w+}",
    	"/log/login", "log/signin", "/accountValidation", "/deletepost", "/updatepost", 
    	"/makepost/{id:\\w+}/{lat:\\w+}/{lng:\\w+}", "/city/{id:\\w+}", "/city/{id:\\w+}/staff"})
    public String index() {
        return "index";
    }
	
    
}