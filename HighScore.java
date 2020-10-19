/**
 * Created by jot38 on 26/04/2018.
 */
public class HighScore implements Comparable<HighScore> {
    private int points;
    private String name;

    public HighScore(int points, String name) {
        this.points = points;
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return points + "," +
                name;
    }


    @Override
    public int compareTo(HighScore o) {
        return Integer.compare(this.points, o.points);
    }
}
