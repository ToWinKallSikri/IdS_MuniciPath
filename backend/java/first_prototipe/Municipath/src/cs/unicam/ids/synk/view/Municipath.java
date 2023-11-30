package cs.unicam.ids.synk.view;

import cs.unicam.ids.synk.controller.CityData;
import cs.unicam.ids.synk.controller.LogHandler;
import cs.unicam.ids.synk.controller.ResponseMaker;
import cs.unicam.ids.synk.controller.command.Command;
import cs.unicam.ids.synk.controller.command.CommandMaker;
import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.Response;

public class Municipath {
	
	private CityData data;
	private LogHandler log;
	private NavigationState state;
	private String lastCity, lastPost;
	private Position lastPosition;
	private CommandMaker cmdMaker;
	private ResponseMaker rspMaker;
	
	public Municipath() {
		this.log = new LogHandler();
		this.data = CityData.getCityData();
		this.state = NavigationState.HOME;
		this.lastCity = "null";
		this.lastPost = "null";
		this.lastPosition = new Position(0,0);
		this.cmdMaker = new CommandMaker();
		this.rspMaker = new ResponseMaker();
	}
	
	public String apply(String operation) {
		Command cmd = cmdMaker.makeCommand(log.getLoggedUser(), lastCity, lastPost, lastPosition, state, operation);
		return apply(cmd);
	}
	
	private String apply(Command cmd) {
		Response response = getResponse(cmd);
		this.lastCity = response.getLastCity();
		this.lastPosition = response.getPosition();
		this.lastPost = response.getLastPost();
		this.state = response.getState();
		return response.getMessage();
	}
	
	private Response getResponse(Command cmd) {
		return switch(cmd.getType()) {
		case LOGIN -> login(cmd);
		case LOGOUT -> logout(cmd);
		case REGISTRATION -> registration(cmd);
		case NAVIGATION -> this.data.apply(cmd);
		case ERROR -> rspMaker.make("Comando non valido.", cmd);
		};
	}

	

	private Response login(Command cmd) {
		String info;
		if(this.log.logIn(cmd.getInfo()[0], cmd.getInfo()[1]))
				info = "Login eseguito.";
		else info = "Login fallito.";
		return rspMaker.make(info, cmd);
	}
	

	private Response logout(Command cmd) {
		String info;
		if(this.log.logOut())
				info = "Logout eseguito.";
		else info = "Logout fallito.";
		return rspMaker.make(info, cmd);
	}
	
	private Response registration(Command cmd) {
		String info;
		if(this.log.signIn(cmd.getInfo()[0], cmd.getInfo()[1]))
				info = "Registrazione eseguita.";
		else info = "Registrazione fallita.";
		return rspMaker.make(info, cmd);
	}
	
}
