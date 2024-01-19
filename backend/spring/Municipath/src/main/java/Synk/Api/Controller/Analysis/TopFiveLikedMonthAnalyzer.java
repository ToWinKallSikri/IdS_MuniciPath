package Synk.Api.Controller.Analysis;

import Synk.Api.Model.Feedback.Score;
import Synk.Api.Model.MetaData;

public class TopFiveLikedMonthAnalyzer implements TopFiveMonthAnalyzer {

    private String[] top5;
    private double [] topLiked;
    private final double Z = 1.96f;
	@Override
	public void StartAnalysis() {
		top5 = new String[5];
		topLiked = new double[5];
	}

	@Override
	public void acceptData(MetaData data) {
        double wScore = wilsonScore(data.getVote());
        if ( wScore > this.topLiked[4]) {
            this.topLiked[4] = wScore;
            this.top5[4] = data.getId();
            insertIntoTop(4);
        }
    }

        @Override
        public String[] getResult () {
            return top5;
        }

        private double wilsonScore (Score vote) {
            double p = vote.getVoteAverage() / 5;
            double n = vote.getVoteCount();
            double left = p + ((Z * Z) / (2 * n));
            double right = Z * Math.sqrt((p * (1 - p) / n) + ((Z * Z) / (4 * n * n)));
            double under = 1 + ((Z * Z) / n);
            return (left - right) / under;
        }

        private void insertIntoTop ( int index){
            if (index < 1)
                return;
            if (this.topLiked[index] > this.topLiked[index - 1]) {
                double likeTemp = this.topLiked[index - 1];
                String idTemp = this.top5[index - 1];
                this.topLiked[index - 1] = this.topLiked[index];
                this.top5[index - 1] = this.top5[index];
                this.topLiked[index] = likeTemp;
                this.top5[index] = idTemp;
                insertIntoTop(index - 1);
            }
        }


}
