package cs.unicam.ids.synk.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import cs.unicam.ids.synk.controller.command.Command;
import cs.unicam.ids.synk.model.City;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.Post;
import cs.unicam.ids.synk.model.Response;
import cs.unicam.ids.synk.model.UserLog;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class CityData {
	
	private ArrayList<City> cities;
	private ArrayList<Post> posts;
	private UserData users;
	private static CityData cityData;
	
	private CityData(){
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<City>>() {}.getType();
		cities = gson.fromJson(getJson("city"), type);
		type = new TypeToken<ArrayList<Post>>() {}.getType();
		posts = gson.fromJson(getJson("post"), type);
		users = UserData.getUserData();
	}
	
	private String getJson(String name) {
		try {
			File file = new File(name + ".json");
		    @SuppressWarnings("resource")
			Scanner myReader = new Scanner(file);
		    return myReader.nextLine();
		} catch (FileNotFoundException e) {
			return "[]";
		}
	}
	
	public static synchronized CityData getCityData() {
		if(cityData == null)
			cityData = new CityData();
		return cityData;
	}
	
	public synchronized List<City> getCities(){
		return cities;
	}
	
	public synchronized List<Post> getPosts(){
		return posts;
	}
	
	public synchronized List<Position> getPoints(String cityID){
		return this.posts.stream().parallel()
				.filter(p -> p.getCityID().equals(cityID)).filter(Post::getApproved)
				.map(Post::getPosition).distinct().toList();
	}
	
	public synchronized List<Post> getPosts(String cityID, Position position){
		return this.posts.stream()
				.parallel().filter(p -> p.getPosition().equals(position))
				.filter(p -> p.getCityID().equals(cityID)).filter(Post::getApproved).toList();
	}

    public synchronized Post getPost(String postID) {
        Optional<Post> Opost = getFromAllPost(postID);
        return Opost.isPresent() && Opost.get().getApproved() ? Opost.get() : null;
    }

    public synchronized Optional<Post> getFromAllPost(String postID) {
        return this.posts.stream().parallel().filter(p -> p.getID()
                .equals(postID)).findFirst();
    }
	
	public synchronized boolean addCity(UserLog manager, String cityName, 
			String CAP, Position position, String newCurator) {
		if(manager.isPlatformManager() && isNewCity(cityName+CAP)) {
			City city = new City(position, cityName+CAP, cityName, CAP, newCurator);
			if(this.users.makeCurator(manager, city, newCurator)) {
				this.cities.add(city);
				saveAll();
				return true;
			}
		}
		return false;
	}
	
	private boolean isNewCity(String ID) {
		return getCity(ID).isEmpty();
	}
	
	private Optional<City> getCity(String ID){
		return this.cities.stream().parallel()
				.filter(city -> city.getID().equals(ID)).findFirst();
	}
	
	public synchronized boolean addPost(UserLog author, String cityID, 
			String title, Position position, String text) {
		String postID = ""+this.posts.size();
		Post post = new Post(postID, title, text, 
				author.getUsername(), cityID, position, false);
		Optional<City> city = getCity(cityID);
		if(this.users.addPostToUser(author.getUsername(), ""+this.posts.size()) && city.isPresent()) {
			city.get().getPostIDs().add(postID);
			this.posts.add(post);
			saveAll();
			return true;
		}
		return false;
	}
	
	public synchronized boolean managePending(UserLog curator, String postID, boolean approved) {
		Optional<Post> post = getFromAllPost(postID);
		System.out.println(postID);
		System.out.println(isCuratorOf(curator, post.get()));
		if(post.isPresent() && isCuratorOf(curator, post.get())) {
			if(approved) post.get().setApproved(true);
			else posts.remove(post.get());
			saveAll();
			return true;
		}
		return false;
	}

    private boolean isCuratorOf(UserLog curator, Post post){
        return post.getCityID().equals(curator.getCityCurator());
    }
	
	public synchronized Response apply(Command cmd) {
		return cmd.execute(this);
	}
	
	private void saveAll() {
		Gson gson = new Gson(); 
		saveFile("post", gson.toJson(this.posts));
		saveFile("city", gson.toJson(this.cities));
	}
	
	private void saveFile(String name, String data) {
	    try {
			File file = new File(name + ".json");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(data);
			writer.close();
	    } catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
