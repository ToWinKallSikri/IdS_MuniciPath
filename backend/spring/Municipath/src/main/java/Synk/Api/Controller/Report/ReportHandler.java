package Synk.Api.Controller.Report;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.Report.Report;

@Service
public class ReportHandler {
	
	
    private MuniciPathMediator mediator;
    private List<Report> reports;
    private IdentifierManager identifierManager;

    public ReportHandler(){
        this.reports = new ArrayList<>();
        this.identifierManager = new IdentifierManager();
    }

    public void setMediator (MuniciPathMediator mediator) {
        this.mediator = mediator;
    }

	public boolean reportContent(String username, String contentId, String motivation) {
        if(!(mediator.usernameExists(username) && mediator.contentExist(contentId)))
            return false;
        String cityId =  identifierManager.getCityId(contentId);
        String Id = username + "." + contentId;
        if(reports.stream().anyMatch(r -> (r.getId()).equals(Id)))
            return false;
		Report r1 = new Report(username, motivation, contentId, cityId);
        return reports.add(r1);
	}
	
	public List<Report> getReports(String cityId){
		return reports.stream().filter(r -> (r.getCityId()).equals(cityId)).toList();
	}
	
	public void deleteAllReportOf(String cityId) {
		List<Report> list = this.reports.stream()
				.filter(r -> r.getCityId().equals(cityId)).toList();
		this.reports.removeAll(list);
	}
	
}
