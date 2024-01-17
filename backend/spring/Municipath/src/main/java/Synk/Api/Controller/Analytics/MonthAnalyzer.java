package Synk.Api.Controller.Analytics;

import Synk.Api.Model.MetaData;

public interface MonthAnalyzer {
	
	public void StartAnalysis();
	
	public void acceptData(MetaData data);
	
}
