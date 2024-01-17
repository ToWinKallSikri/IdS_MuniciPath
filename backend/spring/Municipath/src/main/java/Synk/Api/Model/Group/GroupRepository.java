package Synk.Api.Model.Group;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, String> {
	List<Group> findByCityId(String cityId);
}
