package cs.unicam.ids.synk.controller.command;

import java.util.stream.IntStream;

import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.UserLog;

public class CommandMaker {
	
	UserLog log;
	String city;
	Position pos;
	NavigationState state;
	
	
	public synchronized Command makeCommand(UserLog user, String lastCity, 
			Position lastPosition, NavigationState state, String input) {
		this.log = user;
		this.city = lastCity;
		this.pos = lastPosition;
		this.state = state;
		if(input.isEmpty())
			return error();
		return makeCommand(input.split(" "));
	}
	
	private Command makeCommand(String[] input) {
		switch(input[0].toLowerCase()) {
		case "y":
			return manage(input, true);
		case "n":
			return manage(input, false);
		case "create":
			return create(input);
		case "back":
			return back(input);
		case "signin":
			return signin(input);
		case "login":
			return login(input);
		case "logout":
			return logout(input);
		default:
			return navigation(input);
		}
	}
	
	
	private Command navigation(String[] input) {
		return new NavigationCommand(log, city, pos, state, input);
	}
	

	private Command create(String[] input) {
		if(input.length == 1)
			return navigation(input);
		if(input[1].toLowerCase().equals("post") && input.length > 4) 
			return createPost(input);
		if(input[1].toLowerCase().equals("city") && input.length == 7) 
			return createCity(input);
		return navigation(input);
	}

	private Command createPost(String[] input) {
		if(this.state == NavigationState.HOME || isNotDouble(input[3]) || isNotDouble(input[4]))
			return error();
		String [] data = (String[]) IntStream.range(2,input.length)
				.boxed().map(n -> input[n]).toArray();
		return new CreatePostCommand(log, city, pos, state, data);
	}
	
	private Command createCity(String[] input) {
		if(isNotDouble(input[4]) || isNotDouble(input[5]))
			return error();
		String [] data = (String[]) IntStream.range(2, 7)
				.boxed().map(n -> input[n]).toArray();
		return new CreatePostCommand(log, city, pos, state, data);
		
	}
	
	private boolean isNotDouble(String s) {
		try {
			Double.parseDouble(s);
			return false;
		} catch(Exception e){
			return true;
		}
	}

	private Command manage(String[] input, boolean b) {
		return input.length != 1 || this.state != NavigationState.PENDINGPOST ? error() 
				: new ManagePostCommand(log, city, pos, state, null);
	}

	private Command back(String[] input) {
		return input.length != 1 ? error() : new BackCommand(log, city, pos, state, null);
	}

	private Command signin(String[] input) {
		if(input.length != 3)
			return error();
		String user = input[1], password = input[2];
		return new LogCommand(log, city, pos, state, new String[] {user, password}, CommandType.REGISTRATION);
	}

	private Command login(String[] input) {
		if(input.length != 3)
			return error();
		String user = input[1], password = input[2];
		return new LogCommand(log, city, pos, state, new String[] {user, password}, CommandType.LOGIN);
	}

	private Command logout(String[] input) {
		return input.length != 1 ? error() : new LogCommand(log, city, pos, state, null, CommandType.LOGOUT);
	}

	private Command error() {
		return new ErrorCommand(log, city, pos, state, null);
	}
	
}
