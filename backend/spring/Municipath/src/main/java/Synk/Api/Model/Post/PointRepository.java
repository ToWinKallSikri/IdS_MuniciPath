package Synk.Api.Model.Post;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PointRepository extends CrudRepository<Point, String> {
	
	List<Point> findByCityId(String cityId);
	
}
