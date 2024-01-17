package Synk.Api.Controller.Analysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import Synk.Api.Model.MetaData;

public class DataMonthPartitioner {
	
	private int numberOfMonths;
	
	/**
	 * Metodo per dividere i dati in base al mese
	 * @param data dati da dividere
	 * @return dati divisi
	 */
	public ArrayList<List<MetaData>> divide(List<MetaData> data, int months){
		this.numberOfMonths = months;
		ArrayList<List<MetaData>> array = generateArray();
		data.sort(Comparator.comparing(MetaData::getPublicationTime));
		if(!data.isEmpty()) 
			assignData(array, data);
		return array;
	}
	
	/**
	 * genera un nuovo array inizializzando le liste che contiene
	 * @return nuovo array
	 */
	private ArrayList<List<MetaData>> generateArray(){
		ArrayList<List<MetaData>> array = new ArrayList<List<MetaData>>();
		for(int i = 0; i < 6; i++) {
			array.add(new ArrayList<MetaData>());
		}
		return array;
	}
	
	/**
	 * metodo per assegnare i dati in base al mese
	 * @param array array dove posizionare i dati
	 * @param data dati da posizionare
	 */
	private void assignData(ArrayList<List<MetaData>> array, List<MetaData> data) {
		int count = 0, month = data.get(0).getPublicationTime().getMonth().getValue();
		for(MetaData md : data) {
			if(md.getPublicationTime().getMonth().getValue() != month) {
				//se il mese cambia, lo salvo e aumento il counter di 1
				month = md.getPublicationTime().getMonth().getValue();
				count++;
			}
			//se supero i sei mesi mi fermo
			if(count > this.numberOfMonths - 1)
				break;
			array.get(count).add(md);
		}
	}
	
}
