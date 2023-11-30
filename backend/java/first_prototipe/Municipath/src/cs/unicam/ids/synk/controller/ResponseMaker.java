package cs.unicam.ids.synk.controller;

import cs.unicam.ids.synk.controller.command.Command;
import cs.unicam.ids.synk.model.Response;

public class ResponseMaker {
	
	public Response make(String info, Command cmd) {
		return new Response(info, cmd.getLastCity(), cmd.getLastPost(), cmd.getPosition(), cmd.getState());
	}
	
}
