package Synk.Api.Controller.Analysis;

import Synk.Api.Model.MetaData;

public class PublicationMonthAnalyzer implements NumberMonthAnalyzer {
	
	private long count;

	@Override
	public void StartAnalysis() {
		this.count = 0;
		
	}

	@Override
	public void acceptData(MetaData data) {
		this.count++;
	}

	@Override
	public long getResult() {
		return this.count;
	}

}
