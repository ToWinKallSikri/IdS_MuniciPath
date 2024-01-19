package Synk.Api.Controller.Analysis;

import Synk.Api.Model.MetaData;

public class VoteAverageMonthAnalyzer implements NumberMonthAnalyzer {
	
	float count, sum;
	
	@Override
	public void StartAnalysis() {
		count = 0f;
		sum = 0f;
	}

	@Override
	public void acceptData(MetaData data) {
		this.sum += data.getVote().getVoteAverage();
		this.count++;
	}

	@Override
	public long getResult() {
		return Math.round(sum/count);
	}

}
