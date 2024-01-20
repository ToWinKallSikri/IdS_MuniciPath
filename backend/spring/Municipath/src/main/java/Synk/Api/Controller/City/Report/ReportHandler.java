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
    
    /**
     * metodo per segnalare un contenuto
     * @param username nome utente del segnalatore
     * @param contentId id del contenuto
     * @param motivation motivazione della segnalazione
     * @return true se la sengnalazione 
     * è andata a buon fine, false altrimenti
     */
	public boolean reportContent(String username, String contentId, String motivation) {
        String cityId =  identifierManager.getCityId(contentId);
        String Id = username + "." + contentId;
        if(reportRepository.existsById(Id))
            return false;
		Report report = new Report(username, motivation, contentId, cityId);
        reportRepository.save(report);
        return true;
	}
	
	/**
	 * metodo per ottenre le sengalazioni di un comune
	 * @param cityId id del comune
	 * @return segnalazioni di quel comune
	 */
	public List<Report> getReports(String cityId){
		return this.reportRepository.findByCityId(cityId);
	}
	
	/**
	 * metodo per ottenre una segnalazione di un comune
	 * @param reportId id della segnalazione
	 * @return regnalazione
	 */
	public Report getReport(String reportId){
		Report report = reportRepository.findById(reportId).orElse(null);
		if(report != null) {
			report.setRead(true);
			this.reportRepository.save(report);
		}
		return report;
	}
	
	/**
	 * metodo per eliminare tutte le 
	 * segnalazioni di un comune destinato
	 * alla eliminazione
	 * @param cityId id del comune
	 */
	public void deleteAllReportOf(String cityId) {
		this.reportRepository.deleteAll(getReports(cityId));
	}
	
}
