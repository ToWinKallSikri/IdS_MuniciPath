package Synk.Api.Model.Analytics;

public class Analytics {
    
	//il numero delle pubblicazioni nel suo comune negli ultimi sei mesi
	private long[] lastMonthsPublications;
	//il numero di visualizzazioni dei contenuti pubblicati nel suo comune negli ultimi sei mesi
	private long[] lastMonthsViews;
	//la media delle valutazioni fatte nei contenuti pubblicati negli ultimi 6 mesi
	private long[] lastMonthsVoteAverage;
	//i 5 contributors con l'indice di pubblicazione più alto negli ultimi sei mesi
	private String[][] lastMonthsTopFiveContributors;
	//i 5 contenuti più visitati pubblicati negli ultimi sei mesi
	private String[][] lastMonthsTopFiveViewedContents;
	//i 5 contenuti più piaciuti pubblicati negli ultimi 6 mesi
	private String[][] lastMonthsTopFiveLikedContents;

	public Analytics() { }

	public Analytics(long[] lastMonthsPublications, long[] lastMonthsViews, long[] lastMonthsVoteAverage,
			String[][] lastMonthsTopFiveContributors, String[][] lastMonthsTopFiveViewedContents,
			String[][] lastMonthsTopFiveLikedContents) {
		this.lastMonthsPublications = lastMonthsPublications;
		this.lastMonthsViews = lastMonthsViews;
		this.lastMonthsVoteAverage = lastMonthsVoteAverage;
		this.lastMonthsTopFiveContributors = lastMonthsTopFiveContributors;
		this.lastMonthsTopFiveViewedContents = lastMonthsTopFiveViewedContents;
		this.lastMonthsTopFiveLikedContents = lastMonthsTopFiveLikedContents;
	}

	public long[] getLastMonthsPublications() {
		return lastMonthsPublications;
	}

	public void setLastMonthsPublications(long[] lastMonthsPublications) {
		this.lastMonthsPublications = lastMonthsPublications;
	}

	public long[] getLastMonthsViews() {
		return lastMonthsViews;
	}

	public void setLastMonthsViews(long[] lastMonthsViews) {
		this.lastMonthsViews = lastMonthsViews;
	}

	public long[] getLastMonthsVoteAverage() {
		return lastMonthsVoteAverage;
	}

	public void setLastMonthsVoteAverage(long[] lastMonthsVoteAverage) {
		this.lastMonthsVoteAverage = lastMonthsVoteAverage;
	}

	public String[][] getLastMonthsTopFiveContributors() {
		return lastMonthsTopFiveContributors;
	}

	public void setLastMonthsTopFiveContributors(String[][] lastMonthsTopFiveContributors) {
		this.lastMonthsTopFiveContributors = lastMonthsTopFiveContributors;
	}

	public String[][] getLastMonthsTopFiveViewedContents() {
		return lastMonthsTopFiveViewedContents;
	}

	public void setLastMonthsTopFiveViewedContents(String[][] lastMonthsTopFiveViewedContents) {
		this.lastMonthsTopFiveViewedContents = lastMonthsTopFiveViewedContents;
	}

	public String[][] getLastMonthsTopFiveLikedContents() {
		return lastMonthsTopFiveLikedContents;
	}

	public void setLastMonthsTopFiveLikedContents(String[][] lastMonthsTopFiveLikedContents) {
		this.lastMonthsTopFiveLikedContents = lastMonthsTopFiveLikedContents;
	}
	
}
