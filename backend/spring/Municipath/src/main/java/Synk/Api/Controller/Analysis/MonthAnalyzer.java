package Synk.Api.Controller.Analysis;

import Synk.Api.Model.MetaData;

/**
 * interfaccia per le analisi di un mese di dati
 */
public interface MonthAnalyzer {
	
	/**
	 * metodo per inizializzare
	 * le analisi
	 */
	public void StartAnalysis();
	
	/**
	 * metodo per accettare un nuovo dato
	 * @param data nuovo dato
	 */
	public void acceptData(MetaData data);
	
}
