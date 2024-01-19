package Synk.Api.Controller.Analysis;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Synk.Api.Model.MetaData;

public class TopFiveContributorMonthAnalyzer implements TopFiveMonthAnalyzer {
	
	Map<String, Integer> publications;
	
	@Override
	public void StartAnalysis() {
		publications = new HashMap<>();
	}

	@Override
	public void acceptData(MetaData data) {
		int newP = this.publications.containsKey(data.getAuthor()) ?
				this.publications.get(data.getAuthor()) : 0;
		this.publications.put(data.getAuthor(), ++newP);
		
	}

	@Override
	public String[] getResult() {
		List<Entry<String, Integer>> list = this.publications.entrySet().stream().toList();
		list.sort(Comparator.comparing(Entry::getValue));
		List<String> authorList = list.subList(0, Math.min(5, list.size()))
				.stream().map(e -> e.getKey()).toList();
		String[] result = new String[5];
		for(int i = 0; i < authorList.size(); i++) {
			result[i] = authorList.get(i);
		}
		return result;
	}

}
