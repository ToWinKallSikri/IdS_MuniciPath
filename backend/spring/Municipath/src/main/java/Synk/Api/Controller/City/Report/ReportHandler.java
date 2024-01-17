package Synk.Api.Controller.City.Report;


import java.util.List;

import org.springframework.stereotype.Service;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Model.City.Report.Report;
import Synk.Api.Model.City.Report.ReportRepository;

@Service
public class ReportHandler {
	
    private IdentifierManager identifierManager;
    private ReportRepository reportRepository;

    public ReportHandler(ReportRepository reportRepository){
    	this.reportRepository = reportRepository;
        this.identifierManager = new IdentifierManager();
    }

	public boolean reportContent(String username, String contentId, String motivation) {
        String cityId =  identifierManager.getCityId(contentId);
        String Id = username + "." + contentId;
        if(reportRepository.existsById(Id))
            return false;
		Report report = new Report(username, motivation, contentId, cityId);
        reportRepository.save(report);
        return true;
	}
	
	public List<Report> getReports(String cityId){
		return this.reportRepository.findByCityId(cityId);
	}
	
	public Report getReport(String reportId){
		Report report = reportRepository.findById(reportId).orElse(null);
		if(report != null) {
			report.setRead(true);
			this.reportRepository.save(report);
		}
		return report;
	}
	
	public void deleteAllReportOf(String cityId) {
		this.reportRepository.deleteAll(getReports(cityId));
	}
	
}
