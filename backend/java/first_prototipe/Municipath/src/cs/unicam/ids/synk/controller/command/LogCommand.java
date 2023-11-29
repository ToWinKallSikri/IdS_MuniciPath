package cs.unicam.ids.synk.controller.command;

import cs.unicam.ids.synk.controller.CityData;
import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.Response;
import cs.unicam.ids.synk.model.UserLog;

public class LogCommand extends Command {

	public LogCommand(UserLog userLog, String lastCity, Position position, NavigationState state,
			String[] info, CommandType type) {
		super(userLog, lastCity, position, state, info);
		this.type = type;
	}

	@Override
	public Response execute(CityData data) {
		return null;
	}

}