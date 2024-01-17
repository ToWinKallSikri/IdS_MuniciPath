package Synk.Api.Controller.Analytics;
import java.util.List;

import org.springframework.stereotype.Service;

import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.MetaData;
import Synk.Api.Model.Analytics.Analytics;

@Service
public class AnalyticsHandler {
	
	private DataMonthPartitioner partitioner;
	private DirectorAnalyzer director;
	private PublicationMonthAnalyzer publAnalyzer;
	private ViewMonthAnalyzer viewAnalyzer;
	private VoteAverageMonthAnalyzer voteAveAnalyzer;
	private TopFiveContributorMonthAnalyzer TFCAnalyzer;
	private TopFiveLikedMonthAnalyzer TFLAnalyzer;
	private TopFiveViewedMonthAnalyzer TFVAnalyzer;
	private MuniciPathMediator mediator;
	
	public AnalyticsHandler() {
		this.partitioner = new DataMonthPartitioner();
		this.director = new DirectorAnalyzer();
		this.publAnalyzer = new PublicationMonthAnalyzer();
		this.viewAnalyzer = new ViewMonthAnalyzer();
		this.voteAveAnalyzer = new VoteAverageMonthAnalyzer();
		this.TFCAnalyzer = new TopFiveContributorMonthAnalyzer();
		this.TFLAnalyzer = new TopFiveLikedMonthAnalyzer();
		this.TFVAnalyzer = new TopFiveViewedMonthAnalyzer();
	}
	
	public synchronized Analytics getAnalytics(String cityId, int months, boolean onlyUsers) {
		if(months < 1 || months > 12 || cityId == null)
			return null;
		List<MetaData> data = this.mediator.getDataForAnalysis(cityId, months, onlyUsers);
		return getAnalytics(data, months);
	}
	
	private Analytics getAnalytics(List<MetaData> data, int months) {
		if(data == null || data.isEmpty())
			return null;
		return getAnalytics(this.partitioner.divide(data, months));
	}
	
	private Analytics getAnalytics(List<List<MetaData>> data) {
		long[] publs = getNumberResult(this.publAnalyzer, data);
		long[] views = getNumberResult(this.viewAnalyzer, data);
		long[] votes = getNumberResult(this.voteAveAnalyzer, data);
		String[][] contr = getTFResult(this.TFCAnalyzer, data);
		String[][] like = getTFResult(this.TFLAnalyzer, data);
		String[][] viewed = getTFResult(this.TFVAnalyzer, data);
		return new Analytics(publs, views, votes, contr, like, viewed);
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
