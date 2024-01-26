package Synk.Api.Model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Repository;

@Repository
public class PostPointRepository {
	
	private PointRepository pointRepo;
	private PostRepository postRepo;
	
	public PostPointRepository(PointRepository pointRepo, PostRepository postRepo) {
		this.pointRepo = pointRepo;
		this.postRepo = postRepo;
	}
	
	public void save(Point point) {
		this.pointRepo.save(point);
	}
	
	public void deleteById(String pointId) {
		this.pointRepo.deleteById(pointId);
	}
	
	public void delete(Point point) {
		this.pointRepo.delete(point);
	}
	

	
	public void deleteAll(List<Point> list) {
		this.pointRepo.deleteAll(list);
	}
	
	public Point findById(String pointId) {
		Point point = this.pointRepo.findById(pointId).orElse(null);
		if(point == null)
			return null;
		addAllPosts(point);
		return point;
	}
	
	public List<Point> findByCityId(String cityId){
		List<Point> list = this.pointRepo.findByCityId(cityId);
		list.forEach(p -> addAllPosts(p));
		return list;
	}
	
	public List<Point> findAll(){
		return StreamSupport.stream(pointRepo.findAll().spliterator(), true)
				.map(p -> {
					addAllPosts(p);
					return p;
		}).toList();
	}
	
	private void addAllPosts(Point point) {
		point.setPosts(new ArrayList<>(this.postRepo.findByPointId(point.getPointId())));
	}
	
}
