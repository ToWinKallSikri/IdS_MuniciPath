package Synk.Api.Model.City.Report;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report, String> {
	
	public List<Report> findByCityId(String cityId);
	
}
