package Synk.Api.Model.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Contribute.Contribute;
import Synk.Api.Model.Post.Contribute.ContributeHandler;
import Synk.Api.Model.Post.Meteo.WeatherForecastProxy;
import Synk.Api.Model.Post.Meteo.WeatherService;
import jakarta.annotation.PostConstruct;

@Repository
public class PointHandler {

    private Map<String, List<Point>> points;
    private WeatherService weather;
    private MuniciPathMediator mediator;
    
    
    @Autowired
	private PostRepository postRepository;
	@Autowired
	private PointRepository pointRepository;
	@Autowired
    private ContributeHandler contributes;
    
    public PointHandler() {
    	points = new HashMap<>();
        weather = new WeatherForecastProxy();
    }
    
    public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }
    
    @PostConstruct
    public void loadData() {
    	this.points = StreamSupport.stream(pointRepository.findAll().spliterator(), false)
    			.map(p -> {
    				p.setPosts(StreamSupport.stream(postRepository.findAll().spliterator(), false)
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
            String cityId, ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	if(!(this.mediator.isAuthorizedToPost(cityId, author) && checkTiming(type, start, end, persistence)))
    		return false;
    	boolean published = this.mediator.canPublish(cityId, author);
        Post post = new Post(title, type, text, author, pos, cityId, null, data, published, start, end, persistence);
        Point point = getPoint(pos, cityId);
        post.setPostId(point.getNewPostId());
        if(!checkContest(post.getId(), type, published))
        	return false;
    	this.pointRepository.save(point);
        this.postRepository.save(post);
        point.getPosts().add(post);
        if(!published)
        	this.mediator.addPending(post.getId());
        return true;
    }

	public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, List<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	Post post = this.getPost(postId);
    	if(post == null|| (!post.getAuthor().equals(author)) || (!checkTiming(type, start, end, persistence)) || isPrime(post))
    		return false;
    	boolean published = this.mediator.canPublish(cityId, author);
        if(!checkContest(post.getId(), post.getType(), type, published))
        	return false;
    	if(published) {
    		post.updateInfo(title, type, text, data, start, end, persistence);
            this.postRepository.save(post);
    	}
    	else this.mediator.addPostPending(postId, title, type, text, data, start, end, persistence);
        return true;
    }
    
    public boolean editPost(PendingRequest request) {
    	Post post = getPost(request.getId());
    	post.updateInfo(request);
        this.postRepository.save(post);
        return true;
    }

	private boolean checkContest(String id, PostType type, boolean published) {
		if(type == PostType.CONTEST) {
			if(!published)
				return false;
			this.contributes.addContest(id);
		}
		return true;
	}

	private boolean checkContest(String id, PostType type, PostType newType, boolean published) {
		if(type == PostType.CONTEST || newType == PostType.CONTEST ) {
			if(!published)
				return false;
			if(type != PostType.CONTEST || newType == PostType.CONTEST )
				this.contributes.addContest(id);
			else if (type == PostType.CONTEST || newType != PostType.CONTEST)
				this.contributes.removeContest(id);
		}
		return true;
	}

	private boolean checkTiming(PostType type, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(isNotTemp(type) && persistence && start == null && end == null)
			return true;
		if(type == PostType.EVENT && start != null && end != null && start.isBefore(end))
			return true;
		if(type == PostType.CONTEST && start == null && end != null && persistence)
			return true;
		return false;
	}
    
    private boolean isNotTemp(PostType type) {
    	return type != PostType.EVENT && type != PostType.CONTEST;
    }
    
    private Point getPoint(Position pos, String cityId) {
    	return this.points.get(cityId).stream().filter(p -> p.getPos().equals(pos))
    			.findFirst().orElse(makeNewPoint(pos, cityId));
    }
    
    private Point makeNewPoint(Position pos, String cityId) {
    	Point point = new Point(cityId+"."+pos, pos, cityId);
    	this.points.get(cityId).add(point);
    	this.pointRepository.save(point);
    	return point;
    }
    
    public List<Point> getPoints (String cityId, String username) {
          return this.points.get(cityId).stream()
        		  .filter( p -> p.getPosts().stream().anyMatch(po -> toShow(po, username))).toList();
    }
    
    private Post updatePost(Post post) {
    	post.setMeteo(this.weather.getWeather(post.getPos(), post.getMeteoDate()));
    	post.setGroups(this.mediator.viewGroupFrom(post));
    	return post;
    }
    
    public void deleteCityPoints (String cityId) {
        List<Point> ps = this.points.get(cityId);
        ps.forEach(p -> {
        	p.getPosts().forEach( po -> deletePost(po.getPostId()));
        	this.postRepository.deleteAll(p.getPosts());
        });
        this.pointRepository.deleteAll(ps);
        this.points.remove(cityId);
        this.mediator.removeAllCityGroups(cityId);
    }
    
    public List<Post> viewPosts (String pointId, String username) {
        return searchPoint(pointId).getPosts().stream()
        		.filter(p -> toShow(p, username)).toList();
    }
    
    private boolean toShow(Post post, String username) {
    	if(post.getAuthor().equals(username))
    		return true;
    	if(!post.isPublished())
    		return false;
    	return post.getType() != PostType.EVENT || post.getEndTime().isAfter((LocalDateTime.now()));
    	
    }
    
    public Post getPost(String postId) {
    	Point point = searchPointFromPost(postId);
    	Post post = point.getPosts().stream()
    			.filter(p -> p.getId().equals(postId))
    			.findFirst().orElse(null);
    	return post == null ? null : updatePost(post);
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
    	this.postRepository.delete(post);
    	if(point.getPosts().isEmpty()) {
    		this.points.get(point.getCityId()).remove(point);
        	this.pointRepository.delete(point);
    	}
    	this.mediator.removeFromAllGroups(post.getPostId());
    	return true;
    }

	private boolean isPrime(Post post) {
		Position posCity = this.mediator.getCity(post.getCityID()).getPos();
		Point point = searchPoint(post.getCityID(), posCity);
		return post.getPostId().equals(point.getPointId()+".0");
	}
	
	public List<Post> getPosts(List<String> postIds){
		String cityId = postIds.get(0).split(".")[0];
		return this.points.get(cityId).stream()
				.map(p -> p.getPosts()).flatMap(List::stream).
				filter(post -> postIds.contains(post.getId()))
			    .collect(Collectors.toList());
	}
	
	public List<Post> getPostsIfAllExists(List<String> postIds) {
		List<Post> posts = getPosts(postIds);
		return posts.size() == postIds.size() ? posts : null;
	}
	
	public boolean approvePost(String postId) {
		Post post = getPost(postId);
		if(post == null || post.isPublished())
			return false;
		post.setPublished(true);
		return true;
	}
	
	public List<Contribute> getContributes(String username, String postId){
		if(getPost(postId).getAuthor().equals(username))
			return null;
		return this.contributes.getContributes(postId);
	}
	
	public boolean addContestToContest(String contestAuthor, String contestId, List<String> content) {
		if(!this.mediator.usernameExists(contestAuthor))
			return false;
		return this.contributes.addContributeToContest(contestAuthor, contestId, content);
	}
	
	
	public boolean declareWinner(String author, String contestId, String winnerId) {
		Post post = getPost(contestId);
		if(post == null || !post.getAuthor().equals(author))
			return false;
		List<String> winnercontent = this.contributes.declareWinner(contestId, winnerId);
		if(winnercontent == null)
			return false;
		PendingRequest edit = new PendingRequest(post.getId(), post.getTitle(), "", 
				true, PostType.SOCIAL, winnercontent, null, null);
		editPost(edit);
		post.setAuthor(winnerId);
		return true;
	}
	
	
	public void checkEndingPosts() {
		LocalDateTime date = LocalDateTime.now();
		this.points.values().forEach(l -> l.forEach( p -> p.getPosts()
				.stream()
				.filter(po -> ! po.isPersistence())
				.filter(po -> po.getType() == PostType.EVENT)
				.forEach(po -> {
					if(po.getEndTime().isBefore(date))
						p.getPosts().remove(po); })
		));
	}

	public String getAuthor(String pendingId) {
		return this.getPost(pendingId).getAuthor();
	}
    
}
