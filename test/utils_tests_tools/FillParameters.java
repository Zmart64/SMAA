package utils_tests_tools;

import smaa_calculation.Utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class FillParameters {
    public Collection<Object[]> fillParameters(String type) throws Exception {
        //initialize parser
        JsonParser parser = new JsonParser();
        List<AGGTestEntity> entities = parser.readAGGEntitiesFromJson("test/resources/agg_calculation_results.json");

        Object[][] result = new Object[entities.size()][3];

        switch (type) {
            case "ranking":
                for (int i = 0; i < entities.size(); i++) {
                    result[i][0] = entities.get(i).getName();
                    result[i][1] = Utils.calculateRanking(entities.get(i).getAgg());
                    result[i][2] = entities.get(i).getRanking();
                }
                break;
            case "rai":
                for (int i = 0; i < entities.size(); i++) {
                    result[i][0] = entities.get(i).getName();
                    result[i][1] = Utils.calculateRAI(entities.get(i).getAgg(), 10000);
                    result[i][2] = entities.get(i).getRai();
                }
                break;
            case "totalPoints":
                for (int i = 0; i < entities.size(); i++) {
                    result[i][0] = entities.get(i).getName();
                    result[i][1] = Utils.calculateTotalPoints(Utils.calculateRAI(entities.get(i).getAgg(), 10000));
                    result[i][2] = entities.get(i).getTotalPoints();
                }
                break;
            case "percentageDifference":
                for (int i = 0; i < entities.size(); i++) {
                    result[i][0] = entities.get(i).getName();
                    result[i][1] = Utils.getPercentageDifference(Utils.calculateRAI(entities.get(i).getAgg(), 10000));
                    result[i][2] = entities.get(i).getPercentageDifference();
                }
                break;
            case "exclusion":
                for (int i = 0; i < entities.size(); i++) {
                    result[i][0] = entities.get(i).getName();
                    result[i][1] = Utils.decideExclusion(Utils.getPercentageDifference(Utils.calculateRAI(entities.get(i).getAgg(), 10000)), 20);
                    result[i][2] = entities.get(i).getExclusion();
                }
                break;
            default:
                throw new NoSuchElementException("Please look at your input for the filling parameter.");
        }

        return Arrays.asList(result);
    }
}
