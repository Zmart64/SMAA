package utilsTestsTools;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import smaa_creation.AGG;

public class AGG_Test_Entity {

    private AGG agg;
    @JsonProperty("name")
    public String name;
    @JsonProperty("ranking")
    private int[] ranking;
    @JsonProperty("rai")
    private double[][] rai;
    @JsonProperty("totalPoints")
    private double[] totalPoints;
    @JsonProperty("percentageDifference")
    private double[] percentageDifference;
    @JsonProperty("exclusion")
    private List<Integer> exclusion;

    public AGG_Test_Entity() {
    }

    public AGG getAgg() {
        this.agg = new AGG("test/resources/" + this.name);
        return agg;
    }

    public String getName(){
        return name;
    }

    public int[] getRanking() {
        return ranking;
    }

    public double[][] getRai() {
        return rai;
    }

    public double[] getTotalPoints() {
        return totalPoints;
    }

    public double[] getPercentageDifference() {
        return percentageDifference;
    }

    public List<Integer> getExclusion() {
        return exclusion;
    }
}
