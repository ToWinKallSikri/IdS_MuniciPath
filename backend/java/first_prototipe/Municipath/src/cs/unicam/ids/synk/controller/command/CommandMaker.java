package cs.unicam.ids.synk.controller.command;

import java.util.List;
import java.util.stream.IntStream;

import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.UserLog;

public class CommandMaker {
	
	UserLog log;
	String city, post;
	Position pos;
	NavigationState state;
	
	
	public synchronized Command makeCommand(UserLog user, String lastCity, String lastPost,
			Position lastPosition, NavigationState state, String input) {
		this.log = user;
		this.city = lastCity;
		this.post = lastPost;
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
			return navigation(input, true);
		case "signin":
			return signin(input);
		case "login":
			return login(input);
		case "logout":
			return logout(input);
		default:
			return navigation(input, false);
		}
	}
	
	
	private Command navigation(String[] input, boolean back) {
		if(back && input.length != 1) back = false;
		return new NavigationCommand(log, city, post,  pos, state, input, back);
	}
	
	private Command create(String[] input) {
		if(input[1].toLowerCase().equals("post") && input.length > 4) 
			return createPost(input);
		if(input[1].toLowerCase().equals("city") && input.length == 7) 
			return createCity(input);
		return navigation(input, false);
	}

	private Command createPost(String[] input) {
		if(this.state == NavigationState.HOME || isNotDouble(input[3]) || isNotDouble(input[4]))
			return error();
		List<String> data = IntStream.range(2,input.length)
				.boxed().map(n -> input[n]).toList();
		return new CreatePostCommand(log, city, post,  pos, state, inArray(data));
	}
	
	private Command createCity(String[] input) {
		if(isNotDouble(input[4]) || isNotDouble(input[5]))
			return error();
		List<String> data = IntStream.range(2, 7)
				.boxed().map(n -> input[n]).toList();
		return new CreateCityCommand(log, city, post,  pos, state, inArray(data));
		
	}
	
	private String[] inArray(List<String> data) {
		String [] array = new String [data.size()];
		for(int i = 0; i < data.size(); i++) {
			array[i] = data.get(i);
		}
		return array;
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
				: new ManagePostCommand(log, city, post,  pos, state, new String[]{b ? "y" : "n"});
	}

	private Command signin(String[] input) {
		if(input.length != 3) 
			return error();
		String user = input[1], password = input[2];
		return new LogCommand(log, city, post, pos, state, new String[] {user, password}, CommandType.REGISTRATION);
	}

	private Command login(String[] input) {
		if(input.length != 3)
			return error();
		String user = input[1], password = input[2];
		return new LogCommand(log, city, post, pos, state, new String[] {user, password}, CommandType.LOGIN);
	}

	private Command logout(String[] input) {
		return input.length != 1 ? error() : new LogCommand(log, city, post, pos, state, null, CommandType.LOGOUT);
	}
	
	private Command error() {
		return new ErrorCommand(log, city, post, pos, state, new String[0]);
	}
	
}

