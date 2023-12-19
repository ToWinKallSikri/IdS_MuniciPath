package Synk.Api.Model;
import java.util.ArrayList;
import java.util.List;


public class PointHandler {

    List<Point> points;
    private WeatherForecast weather;
    private CityHandler cityHandler;
    private GroupHandler groupHandler;

    public PointHandler(CityHandler ch, GroupHandler gh) {
    	points = new ArrayList<>();
        weather = new WeatherForecast();
        this.cityHandler = ch;
        this.groupHandler = gh;
    }
    
    public void loadData(ArrayList<Point> points, ArrayList<Post> posts) {
    	this.points = points.stream()
    			.map(p -> {
    				p.setPosts(posts.stream()
    					.filter(po -> po.getPos().equals(p.getPos()))
    					.toList()); return p; }).toList();
    }
    
    
    
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, ArrayList<String> data, boolean published) {
    	if(! this.cityHandler.isAuthorized(cityId, author))
    		return false;
        Post post = new Post(title, type, text, author, pos, cityId, null, data, published);
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
    	return this.points.stream().filter(p -> p.getPos().equals(pos))
    			.findFirst().orElse(makeNewPoint(pos, cityId));
    }
    
    private Point makeNewPoint(Position pos, String cityId) {
    	Point point = new Point(cityId+"."+pos, pos, cityId);
    	this.points.add(point);
    	return point;
    }
    
    public List<Point> getPoints (String cityID) {
          return this.points.stream()
        		  .filter(p -> p.getCityId().equals(cityID)).toList();
      
    }
    
    private Post updateMeteo(Post post) {
    	post.setMeteo(this.weather.getWeather(post.getPos(), post.getDateTime()));
    	return post;
    }
    
    public void deleteCityPoints (String cityId) {
        List<Point> ps = this.points.stream()
        		.filter(p -> p.getCityId().equals(cityId)).toList();
        ps.forEach(p -> p.getPosts().forEach(po -> deletePost(po.getPostId())));
        this.points.removeAll(ps);
    }
    
    public List<Post> getPosts (String pointId) {
        return searchPoint(pointId).getPosts().stream()
        		.map(p -> updateMeteo(p)).toList();
    }
    
    private Point searchPointFromPost(String postId) {
    	String[] parts = postId.split(".");
    	return searchPoint(parts[0]+"."+parts[1]);
    }
    
    private Point searchPoint (String pointId) {
        return this.points.stream().filter(p -> p.getPointId().equals(pointId))
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
    	Point point = searchPoint(post.getId());
    	point.getPosts().remove(post);
    	this.groupHandler.removeFromAll(post);
    	return true;
    }
    
}
