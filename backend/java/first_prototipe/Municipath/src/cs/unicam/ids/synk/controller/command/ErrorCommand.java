package cs.unicam.ids.synk.controller.command;

import cs.unicam.ids.synk.controller.CityData;
import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.Response;
import cs.unicam.ids.synk.model.UserLog;

public class ErrorCommand extends Command {

	public ErrorCommand(UserLog userLog, String lastCity, Position position, NavigationState state, String[] info) {
		super(userLog, lastCity, position, state, info);
	}

	@Override
	public Response execute(CityData data) {
		return null;
	}

}
