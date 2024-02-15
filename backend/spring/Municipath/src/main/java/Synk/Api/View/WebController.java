package Synk.Api.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({"/", "/makecity/{lat:[-+]?[0-9]*\\.?[0-9]+}/{lng:[-+]?[0-9]*\\.?[0-9]+}", "/updatecity/{id:\\w+}", "/deletecity/{id:\\w+}",
    	"/log/login", "log/signin", "/accountValidation", "/deletepost", "/updatepost", 
    	"/makepost/{cityId:\\w+}/{lat:[-+]?[0-9]*\\.?[0-9]+}/{lng:[-+]?[0-9]*\\.?[0-9]+}", "/city/{id:\\w+}", "/city/{id:\\w+}/staff"})
    public String index() {
        return "index";
    }
}