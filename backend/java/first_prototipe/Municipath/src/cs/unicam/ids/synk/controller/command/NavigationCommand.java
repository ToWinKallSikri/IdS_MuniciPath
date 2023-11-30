package cs.unicam.ids.synk.controller.command;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import cs.unicam.ids.synk.controller.CityData;
import cs.unicam.ids.synk.model.City;
import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.Post;
import cs.unicam.ids.synk.model.Response;
import cs.unicam.ids.synk.model.UserLog;

public class NavigationCommand extends Command {
	
	private boolean back;
	private CityData data;

	public NavigationCommand(UserLog userLog, String lastCity, String lastPost, Position position,
			NavigationState state, String[] info, boolean back) {
		super(userLog, lastCity, lastPost, position, state, info);
		this.type = CommandType.NAVIGATION;
		this.back = back;
	}

	@Override
	public Response execute(CityData data) {
		this.data = data;
		if(this.getInfo()[0].equals("pending"))
			return goPending();
		return switch(getState()) {
			case CITY -> back ? goCities() : goPoint();
			case HOME -> back ? goCities() : goCity();
			case PENDING -> back ? goCities() : goPendingPost();
			case PENDINGPOST -> back ? goPending() : error();
			case POSITION -> back ? goCity() : goPost();
			case POST -> back ? goPoint() : error();
		};
	}

	private Response goCities() {
		String result = getCities("").stream().map(City::getName)
				.reduce((a, b) -> a + "\n" + b).orElse("Nessun comune attualmente registrato.");
		return new Response(result, getLastCity(), getLastPost(), getPosition(), NavigationState.HOME);
	}

	private Response goCity() {
		List<City> list = getCities(back ? getLastCity() : getInfo()[0]);
		if(list.size() != 1) {
			String result = makeText(list.stream().map(City::getID), "Nessun comune attualmente registrato.");
			return new Response(result, getLastCity(), getLastPost(), getPosition(), NavigationState.HOME);
		}
		String city = list.get(0).getID();
		String result = makeText(data.getPoints(city).stream().map(Position::toString), "Nessun punto attualmente registrato.");
		return new Response(result, city, getLastPost(), getPosition(), NavigationState.CITY);
	}

	private List<City> getCities(String value){ 
		return data.getCities().stream()
				.filter( c -> c.getID().startsWith(value)).toList();
	}

	private Response goPoint() {
		Position position = back ? getPosition() : makeNewPosition();
		if (position == null)
				return error();
		String result = makeText(data.getPosts(getLastCity(), position).stream().map(Post::getTitle), "ERRORE DI POSIZIONI");
		return new Response(result, getLastCity(), getLastPost(), position, NavigationState.POSITION);
	}

	private Position makeNewPosition() {
		try {
			return new Position(Double.parseDouble(getInfo()[0]),
					Double.parseDouble(getInfo()[1]));
		} catch(Exception e) {
			return null;
		}
	}
	
	private Response goPost() {
		List<Post> posts = data.getPosts(getLastCity(), getPosition()).stream()
				.filter(p -> p.getTitle().startsWith(getInfo()[0])).toList();
		if(posts.isEmpty()) return error();
		String result = posts.stream().map(post -> makePost(post)).reduce((a, b) -> a + "\n\n" + b).get();
		return new Response(result, getLastCity(), posts.get(0).getID(), getPosition(), NavigationState.POST);
	}

	private String makePost(Post post) {
		String result = post.getTitle().toUpperCase();
		result += "\nAutore: " + post.getAuthor();
		result += "\n\n" + post.getText();
		return result;
	}

	private Response goPending() {
		if( getState() != NavigationState.HOME 
				&& getLastCity().equals(getUserLog().getCityCurator())) {
			String result = makeText(getPendingPosts().map(Post::getID), "Nessun post in fase di approvazione");
			return new Response(result, getLastCity(), getLastPost(), getPosition(), NavigationState.PENDING);
		}
		return error();
	}

	private Response goPendingPost() {
		Optional<Post> oPost = getPendingPosts().filter(post -> post.getID().equals(getInfo()[0])).findFirst();
		if(oPost.isPresent())
			return new Response(makePost(oPost.get()), getLastCity(), oPost.get().getID(), getPosition(), NavigationState.PENDINGPOST);
		return error();
	}
	
	private Stream<Post> getPendingPosts(){
		return data.getPosts().stream()
				.filter(post -> post.getCityID().equals(getLastCity()))
				.filter(post -> ! post.getApproved());
	}
	
	private Response error() {
		return new Response("Comando non valido.", getLastCity(), getLastPost(), getPosition(), getState());
	}
	
	private String makeText(Stream<String> stream, String _else) {
		return stream.reduce((a, b) -> a + "\n" + b).orElse(_else);
	}
	
}
