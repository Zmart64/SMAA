package utils_tests_tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import smaa_creation.AGG;

import java.util.List;

public class AGGTestEntity {

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

    public AGG getAgg() {
        return new AGG("test/resources/" + this.name);
    }

    public String getName() {
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
