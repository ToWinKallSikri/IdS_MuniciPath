package Synk.Api.Model.Analytics;

public class Analytics {
    
	//il numero delle pubblicazioni nel suo comune negli ultimi sei mesi
	private long[] lastSixMonthsPublications;
	//il numero di visualizzazioni dei contenuti pubblicati nel suo comune negli ultimi sei mesi
	private long[] lastSixMonthsViews;
	//la media delle valutazioni fatte nei contenuti pubblicati negli ultimi 6 mesi
	private long[] lastSixMonthsVoteAverage;
	//i 5 contributors con l'indice di pubblicazione più alto negli ultimi sei mesi
	private String[][] lastSixMonthsTopFiveContributors;
	//i 5 contenuti più visitati pubblicati negli ultimi sei mesi
	private String[][] lastSixMonthsTopFiveViewedContents;
	//i 5 contenuti più piaciuti pubblicati negli ultimi 6 mesi
	private String[][] lastSixMonthsTopFiveLikedContents;
	
	public Analytics(long[] lastSixMonthsPublications, long[] lastSixMonthsViews, long[] lastSixMonthsVoteAverage,
			String[][] lastSixMonthsTopFiveContributors, String[][] lastSixMonthsTopFiveViewedContents,
			String[][] lastSixMonthsTopFiveLikedContents) {
		this.lastSixMonthsPublications = lastSixMonthsPublications;
		this.lastSixMonthsViews = lastSixMonthsViews;
		this.lastSixMonthsVoteAverage = lastSixMonthsVoteAverage;
		this.lastSixMonthsTopFiveContributors = lastSixMonthsTopFiveContributors;
		this.lastSixMonthsTopFiveViewedContents = lastSixMonthsTopFiveViewedContents;
		this.lastSixMonthsTopFiveLikedContents = lastSixMonthsTopFiveLikedContents;
	}

	public Analytics() { }

	public long[] getLastSixMonthsPublications() {
		return lastSixMonthsPublications;
	}

	public void setLastSixMonthsPublications(long[] lastSixMonthsPublications) {
		this.lastSixMonthsPublications = lastSixMonthsPublications;
	}

	public long[] getLastSixMonthsViews() {
		return lastSixMonthsViews;
	}

	public void setLastSixMonthsViews(long[] lastSixMonthsViews) {
		this.lastSixMonthsViews = lastSixMonthsViews;
	}

	public long[] getLastSixMonthsVoteAverage() {
		return lastSixMonthsVoteAverage;
	}

	public void setLastSixMonthsVoteAverage(long[] lastSixMonthsVoteAverage) {
		this.lastSixMonthsVoteAverage = lastSixMonthsVoteAverage;
	}

	public String[][] getLastSixMonthsTopFiveContributors() {
		return lastSixMonthsTopFiveContributors;
	}

	public void setLastSixMonthsTopFiveContributors(String[][] lastSixMonthsTopFiveContributors) {
		this.lastSixMonthsTopFiveContributors = lastSixMonthsTopFiveContributors;
	}

	public String[][] getLastSixMonthsTopFiveViewedContents() {
		return lastSixMonthsTopFiveViewedContents;
	}

	public void setLastSixMonthsTopFiveViewedContents(String[][] lastSixMonthsTopFiveViewedContents) {
		this.lastSixMonthsTopFiveViewedContents = lastSixMonthsTopFiveViewedContents;
	}

	public String[][] getLastSixMonthsTopFiveLikedContents() {
		return lastSixMonthsTopFiveLikedContents;
	}

	public void setLastSixMonthsTopFiveLikedContents(String[][] lastSixMonthsTopFiveLikedContents) {
		this.lastSixMonthsTopFiveLikedContents = lastSixMonthsTopFiveLikedContents;
	}
	
}
