package Synk.Api.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({"/", "/map", "/createCity/{id:\\w+}"})
    public String index() {
        return "index";
    }
	
    
}