package cs.unicam.ids.synk.controller.command;

import java.util.stream.IntStream;

import cs.unicam.ids.synk.controller.CityData;
import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.Response;
import cs.unicam.ids.synk.model.UserLog;

public class CreatePostCommand extends Command {

	public CreatePostCommand(UserLog userLog, String lastCity, String lastPost, Position position,
			NavigationState state, String[] info) {
		super(userLog, lastCity, lastPost, position, state, info);
		this.type = CommandType.NAVIGATION;
	}

	@Override
	public Response execute(CityData data) {
		Position pos = new Position(Double.parseDouble(getInfo()[1]), Double.parseDouble(getInfo()[2]));
		String rsp;
		if (data.addPost(getUserLog(), getLastCity(), getInfo()[0], pos, getContent(getInfo()))) 
			rsp = "Post "+getInfo()[0]+" creato con successo";
		else rsp = "Post non valido.";
		return new Response(rsp, getLastCity(), getLastPost(), getPosition(), getState());
	}

	private String getContent(String[] info) {
		if(info.length == 3)
			return "";
		return IntStream.range(3, info.length).boxed()
				.map(n -> info[n]).reduce("", (a, b) -> a + " " + b);
	}

	

}
