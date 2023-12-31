package Synk.Api.Controller;

import org.springframework.web.bind.annotation.*;

import Synk.Api.Model.MuniciPathModel;

@RestController
public class MuniciPathRestController {
	
	private MuniciPathModel model;
	
	public MuniciPathRestController() {
		model = new MuniciPathModel();
	}
	
	
	
}	
