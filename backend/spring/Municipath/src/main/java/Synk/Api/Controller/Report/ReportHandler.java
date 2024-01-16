package Synk.Api.Controller.Report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.Report.Report;

public class ReportHandler {

    private MuniciPathMediator mediator;
    private List<Report> reports;
    private IdentifierManager identifierManager;

    public ReportHandler(){
        this.reports = new ArrayList<>();
        this.identifierManager = new IdentifierManager();
    }

    public void SetMediator (MuniciPathMediator mediator) {
        this.mediator = mediator;
    }

	public boolean reportContent(String username, String contentId, String motivation) {
        if(!(mediator.usernameExists(username) && mediator.contentExist(contentId)))
            return false;
        String cityId =  identifierManager.getCityId(contentId);
        String Id = username + "." + contentId;
        if(reports.stream().anyMatch(r -> (r.getId()).equals(Id)))
            return false;
		Report r1 = new Report(username, LocalDateTime.now(), motivation, contentId, false, cityId, Id);
        return reports.add(r1);
	}
	
	public List<Report> getReports(String cityId){
		return reports.stream().filter(r -> (r.getCityId()).equals(cityId)).toList();
	}
	
}
