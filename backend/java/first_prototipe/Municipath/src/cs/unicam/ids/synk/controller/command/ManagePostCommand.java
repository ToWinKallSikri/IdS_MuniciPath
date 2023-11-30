package cs.unicam.ids.synk.controller.command;

import cs.unicam.ids.synk.controller.CityData;
import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.Response;
import cs.unicam.ids.synk.model.UserLog;

public class ManagePostCommand extends Command {
	
	public ManagePostCommand(UserLog userLog, String lastCity, String lastPost, Position position,
			NavigationState state, String[] info) {
		super(userLog, lastCity, lastPost, position, state, info);
		this.type = CommandType.NAVIGATION;
	}
	
	@Override
	public Response execute(CityData data) {
		boolean approved = getInfo()[0].equals("y");
		String rsp;
		if (data.managePending(getUserLog(), getLastPost(), approved)) 
			rsp = approved ? "Post approvato." : "Post non approvato.";
		else rsp = "Comando non valido.";
		return new Response(rsp, getLastCity(), getLastPost(), getPosition(), getState());
	}
	
}