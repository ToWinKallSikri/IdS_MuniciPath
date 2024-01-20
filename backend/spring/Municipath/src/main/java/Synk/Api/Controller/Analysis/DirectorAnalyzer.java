package Synk.Api.Controller.Analysis;

import java.util.List;

import Synk.Api.Model.MetaData;

public class DirectorAnalyzer {
	
	private MonthAnalyzer builder;
	
	/**
	 * imposta un nuovo builder per i dati
	 * @param builder nuovo builder
	 */
	public void setBuilder(MonthAnalyzer builder) {
		this.builder = builder;
	}
	
	/**
	 * prende i dati e li somministra 
	 * iterativamente al builder
	 * @param data dati da somministrare
	 */
	public void make(List<MetaData> data) {
		this.builder.StartAnalysis();
		for(MetaData md : data) {
			this.builder.acceptData(md);
		}
	}
	
}
