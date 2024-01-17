package Synk.Api.Model.Feedback;


public class Score {
	
	private float voteAverage;
	private int voteCount;
	
	public Score(float voteAverage, int voteCount) {
		this.voteAverage = voteAverage;
		this.voteCount = voteCount;
	}

	public Score() { }

	public float getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage(float voteAverage) {
		this.voteAverage = voteAverage;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(voteAverage);
		result = prime * result + voteCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Score))
			return false;
		Score other = (Score) obj;
		if (Float.floatToIntBits(voteAverage) != Float.floatToIntBits(other.voteAverage))
			return false;
		if (voteCount != other.voteCount)
			return false;
		return true;
	}

}
