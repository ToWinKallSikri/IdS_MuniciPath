package Synk.Api.Controller.Analysis;

public interface NumberMonthAnalyzer extends MonthAnalyzer {
	
	/**
	 * metodo per ottenere i 
	 * risultati numerici di un 
	 * mese di analisi
	 * @return risultato numerico
	 */
	public long getResult();
	
}
