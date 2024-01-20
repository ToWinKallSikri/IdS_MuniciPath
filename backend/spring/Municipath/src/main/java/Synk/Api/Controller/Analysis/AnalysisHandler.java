package Synk.Api.Controller.Analysis;
import java.util.List;

import org.springframework.stereotype.Service;

import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.MetaData;
import Synk.Api.Model.Analysis.Analysis;

@Service
public class AnalysisHandler {
	
	private DataMonthPartitioner partitioner;
	private DirectorAnalyzer director;
	private PublicationMonthAnalyzer publAnalyzer;
	private ViewMonthAnalyzer viewAnalyzer;
	private VoteAverageMonthAnalyzer voteAveAnalyzer;
	private TopFiveContributorMonthAnalyzer TFCAnalyzer;
	private TopFiveLikedMonthAnalyzer TFLAnalyzer;
	private TopFiveViewedMonthAnalyzer TFVAnalyzer;
	private MuniciPathMediator mediator;
	
	public AnalysisHandler() {
		this.partitioner = new DataMonthPartitioner();
		this.director = new DirectorAnalyzer();
		this.publAnalyzer = new PublicationMonthAnalyzer();
		this.viewAnalyzer = new ViewMonthAnalyzer();
		this.voteAveAnalyzer = new VoteAverageMonthAnalyzer();
		this.TFCAnalyzer = new TopFiveContributorMonthAnalyzer();
		this.TFLAnalyzer = new TopFiveLikedMonthAnalyzer();
		this.TFVAnalyzer = new TopFiveViewedMonthAnalyzer();
	}
	
	public void setMediator(MuniciPathMediator mediator) {
		this.mediator = mediator;
	}
	
	/**
	 * metodo per ottenre le analisi del comune
	 * @param cityId id del comune
	 * @param months numero di mesi da controllare
	 * @param onlyUsers se ignorare i dati dello staff del comune
	 * @return dati di analisi
	 */
	public synchronized Analysis getAnalysis(String cityId, int months, boolean onlyUsers) {
		if(months < 1 || months > 12 || cityId == null)
			return null;
		List<MetaData> data = this.mediator.getDataForAnalysis(cityId, months, onlyUsers);
		return getAnalysis(data, months);
	}
	
	/**
	 * questo metodo si occupa di dividere i mesi in base
	 * al numero richiesto.
	 * @param data dati di da analizzare
	 * @param months numero di mesi
	 * @return dati di analisi
	 */
	private Analysis getAnalysis(List<MetaData> data, int months) {
		if(data == null || data.isEmpty())
			return null;
		return getAnalysis(this.partitioner.divide(data, months));
	}
	
	/**
	 * usando gli analizzatori ottiene tutti i 
	 * dati necessari per comporre le analisi
	 * @param data dati da analizzare
	 * @return dati di analisi
	 */
	private Analysis getAnalysis(List<List<MetaData>> data) {
		long[] publs = getNumberResult(this.publAnalyzer, data);
		long[] views = getNumberResult(this.viewAnalyzer, data);
		long[] votes = getNumberResult(this.voteAveAnalyzer, data);
		String[][] contr = getTFResult(this.TFCAnalyzer, data);
		String[][] like = getTFResult(this.TFLAnalyzer, data);
		String[][] viewed = getTFResult(this.TFVAnalyzer, data);
		return new Analysis(publs, views, votes, contr, like, viewed);
	}

	private long[] getNumberResult(NumberMonthAnalyzer analyzer, List<List<MetaData>> data) {
		long[] result = new long[data.size()];
		this.director.setBuilder(analyzer);
		for(int i = 0; i < data.size(); i++) {
			director.make(data.get(i));
			result[i] = analyzer.getResult();
		}
		return result;
	}

	private String[][] getTFResult(TopFiveMonthAnalyzer analyzer, List<List<MetaData>> data) {
		String[][] result = new String[data.size()][5];
		this.director.setBuilder(analyzer);
		for(int i = 0; i < data.size(); i++) {
			director.make(data.get(i));
			result[i] = analyzer.getResult();
		}
		return result;
	}
	
}
