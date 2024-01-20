package Synk.Api.Model.Post;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, String> {
	
	List<Post> findByCityId(String cityId);
	
	List<Post> findByPointId(String pointId);
}
