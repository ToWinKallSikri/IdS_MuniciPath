package Synk.Api.Model.Post;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Synk.Api.Model.City.CityHandler;
import Synk.Api.Model.Group.GroupHandler;


public class PointHandler {

    private Map<String, List<Point>> points;
    private WeatherForecast weather;
    private CityHandler cityHandler;
    private GroupHandler groupHandler;
    private ContestHandler contributes;

    public PointHandler(CityHandler ch, GroupHandler gh) {
    	points = new HashMap<>();
        weather = new WeatherForecast();
        this.cityHandler = ch;
        this.groupHandler = gh;
        this.contributes = new ContestHandler();
    }
    
    public void loadData(ArrayList<Point> points, ArrayList<Post> posts) {
    	this.points = points.stream()
    			.map(p -> {
    				p.setPosts(posts.stream()
    					.filter(po -> po.getPos().equals(p.getPos()))
    					.toList()); return p; })
    			.collect(Collectors.groupingBy(Point::getCityId));
    	
    }
    
    public boolean addNewCity(String cityId) {
    	if(this.points.containsKey(cityId))
    		return false;
    	this.points.put(cityId, new ArrayList<Point>());
    	return true;
    }
    
    
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, ArrayList<String> data, boolean published, Date start, Date end, boolean persistence) {
    	if(! this.cityHandler.isAuthorized(cityId, author))
    		return false;
        Post post = new Post(title, type, text, author, pos, cityId, null, data, published, start, end, persistence);
        Point point = getPoint(pos, cityId);
        post.setPostId(point.getNewPostId());
        point.getPosts().add(post);
        return true;
    }
    
    public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, ArrayList<String> data) {
    	Post post = this.getPost(postId);
    	if(!(post != null && post.getAuthor().equals(author)))
    		return false;
    	post.updateInfo(title, type, text, data);
        return true;
    }
    
    private Point getPoint(Position pos, String cityId) {
    	return this.points.get(cityId).stream().filter(p -> p.getPos().equals(pos))
    			.findFirst().orElse(makeNewPoint(pos, cityId));
    }
    
    private Point makeNewPoint(Position pos, String cityId) {
    	Point point = new Point(cityId+"."+pos, pos, cityId);
    	this.points.get(cityId).add(point);
    	return point;
    }
    
    public List<Point> getPoints (String cityId) {
          return this.points.get(cityId);
      
    }
    
    private Post updateMeteo(Post post) {
    	post.setMeteo(this.weather.getWeather(post.getPos(), post.getMeteoDate()));
    	return post;
    }
    
    public void deleteCityPoints (String cityId) {
        List<Point> ps = this.points.get(cityId);
        ps.forEach(p -> p.getPosts().forEach(po -> deletePost(po.getPostId())));
        this.points.remove(cityId);
        this.groupHandler.removeAllFromCity(cityId);
    }
    
    public List<Post> getPosts (String pointId) {
        return searchPoint(pointId).getPosts().stream()
        		.map(p -> updateMeteo(p)).toList();
    }
    
    private Point searchPointFromPost(String postId) {
    	String[] parts = postId.split(".");
    	return searchPoint(parts[0]+"."+parts[1]);
    }
    
    private String searchCityIdFromPoint(String pointId) {
    	return pointId.split(".")[0];
    }
    
    private Point searchPoint (String pointId) {
        return this.points.get(searchCityIdFromPoint(pointId))
        		.stream().filter(p -> p.getPointId().equals(pointId))
        		.findFirst().orElse(new Point());
    }
    
    private Point searchPoint (String cityId,Position positon) {
        return this.points.get(cityId).stream()
        		.filter(p -> p.getPos().equals(positon))
        		.findFirst().orElse(new Point());
    }
    
    public Post getPost(String postId) {
    	Point point = searchPointFromPost(postId);
    	Post post = point.getPosts().stream()
    			.filter(p -> p.getId().equals(postId))
    			.findFirst().orElse(null);
    	return updateMeteo(post);
    }
    
    public boolean deletePost (String postId, String author) {
    	Post post = getPost(postId);
    	if(!(post != null && post.getAuthor().equals(author)))
    		return false;
    	return deletePost(post);
    }
    
    public boolean deletePost (String postId) {
    	Post post = getPost(postId);
    	if(post == null)
    		return false;
    	return deletePost(post);
    }
    
    private boolean deletePost(Post post) {
    	if(isPrime(post))
    		return false;
    	Point point = searchPoint(post.getId());
    	point.getPosts().remove(post);
    	this.groupHandler.removeFromAll(post);
    	return true;
    }

	private boolean isPrime(Post post) {
		Position posCity = this.cityHandler.getCity(post.getCityID()).getPos();
		Point point = searchPoint(post.getCityID(), posCity);
		return post.getPostId().equals(point.getPointId()+".0");
	}
    
}
