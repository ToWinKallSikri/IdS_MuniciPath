package cs.unicam.ids.synk.controller.command;

import cs.unicam.ids.synk.controller.CityData;
import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.Response;
import cs.unicam.ids.synk.model.UserLog;

public class CreateCityCommand extends Command {

	public CreateCityCommand(UserLog userLog, String lastCity, String lastPost, Position position,
			NavigationState state, String[] info) {
		super(userLog, lastCity, lastPost, position, state, info);
		this.type = CommandType.NAVIGATION;
	}

	@Override
	public Response execute(CityData data) {
		Position pos = new Position(Double.parseDouble(getInfo()[2]), Double.parseDouble(getInfo()[3]));
		String rsp;
		if (data.addCity(getUserLog(), getInfo()[0], getInfo()[1], pos, getInfo()[4])) 
			rsp = "Comune "+getInfo()[0]+" creato con successo";
		else rsp = "Creazione del comune non riuscita.";
		return new Response(rsp, getLastCity(), getLastPost(), getPosition(), getState());
	}

}
