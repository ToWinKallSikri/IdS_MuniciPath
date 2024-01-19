package Synk.Api.Controller.Analysis;

import Synk.Api.Model.MetaData;

public class ViewMonthAnalyzer implements NumberMonthAnalyzer {
	
	long count = 0;
	
	@Override
	public void StartAnalysis() {
		this.count = 0;
	}

	@Override
	public void acceptData(MetaData data) {
		this.count += data.getViewsCount();
	}

	@Override
	public long getResult() {
		return this.count;
	}
	
}
