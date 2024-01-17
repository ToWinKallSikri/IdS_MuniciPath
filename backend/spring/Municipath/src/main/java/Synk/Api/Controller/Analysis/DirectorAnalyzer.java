package Synk.Api.Controller.Analysis;

import java.util.List;

import Synk.Api.Model.MetaData;

public class DirectorAnalyzer {
	
	private MonthAnalyzer builder;
	
	public void setBuilder(MonthAnalyzer builder) {
		this.builder = builder;
	}
	
	public void make(List<MetaData> data) {
		this.builder.StartAnalysis();
		for(MetaData md : data) {
			this.builder.acceptData(md);
		}
	}
	
}
