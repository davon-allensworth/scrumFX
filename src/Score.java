public class Score implements Comparable<Score> {
    protected String player;
    protected int value;

    public Score(int value, String player){
        this.value = value;
        this.player = player;
    }

    public int getValue() {
        return value;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public int compareTo(Score compareScore) {
        int otherScore = compareScore.getValue();
        return otherScore - this.value;
    }
}
