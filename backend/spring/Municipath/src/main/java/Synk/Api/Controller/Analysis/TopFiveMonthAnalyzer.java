package Synk.Api.Controller.Analysis;

public interface TopFiveMonthAnalyzer extends MonthAnalyzer {
	
	/**
	 * metodo per avere una top 5 
	 * di risultati in base ad un
	 * analisi mensile
	 * @return top 5 di dati
	 */
	public String[] getResult();
	
}
