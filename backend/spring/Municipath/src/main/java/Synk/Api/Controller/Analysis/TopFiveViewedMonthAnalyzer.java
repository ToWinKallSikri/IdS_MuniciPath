package Synk.Api.Controller.Analysis;

import Synk.Api.Model.MetaData;

public class TopFiveViewedMonthAnalyzer implements TopFiveMonthAnalyzer {
	
	private String [] top;
	private long [] topView;
	
	@Override
	public void StartAnalysis() {
		this.top = new String [5];
		this.topView = new long [5];
	}

	@Override
	public void acceptData(MetaData data) {
		if(data.getViewsCount() > this.topView[4]) {
			this.topView[4] = data.getViewsCount();
			this.top[4] = data.getId();
			insertIntoTop(4);
		}
	}

	private void insertIntoTop(int index) {
		if(index < 1)
			return;
		if(this.topView[index] > this.topView[index -1]) {
			long viewTemp = this.topView[index -1] ;
			String idTemp = this.top[index -1];
			this.topView[index -1] = this.topView[index];
			this.top[index -1] = this.top[index];
			this.topView[index] = viewTemp;
			this.top[index] = idTemp;
			insertIntoTop(index -1);
		}
	}

	@Override
	public String[] getResult() {
		return this.top;
	}

}
