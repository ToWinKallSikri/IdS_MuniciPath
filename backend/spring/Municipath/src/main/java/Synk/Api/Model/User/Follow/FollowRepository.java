package Synk.Api.Model.User.Follow;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FollowRepository extends CrudRepository<Follow, String> {

	List<Follow> findByUsername(String username);

	List<Follow> findByFollowed(String followed);
	
}
