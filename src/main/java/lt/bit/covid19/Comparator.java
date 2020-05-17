package lt.bit.covid19;

import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rimid
 */
public class Comparator {
    
    
    public static void compareCountryDataValueModel(List<CountryDataValueModel> countryDeathTolls) {

        Collections.sort(countryDeathTolls, (CountryDataValueModel xmlObjectA, CountryDataValueModel xmlObjectB) -> {
            int compare = 0;
            try {
                int keyA = xmlObjectA.deaths;
                int keyB = xmlObjectB.deaths;

                compare = Integer.compare(keyB, keyA);
            } catch (JSONException e) {
                System.out.println("Please check if JSON is valid!!!");
            }
            return compare;
        });
    }

    public static void compareJSON(List<JSONObject> countriesData) {

        Collections.sort(countriesData, (JSONObject jsonObjectA, JSONObject jsonObjectB) -> {
            int compare = 0;
            try {
                int keyA = jsonObjectA.getInt("latitude");
                int keyB = jsonObjectB.getInt("latitude");

                compare = Integer.compare(keyB, keyA);
            } catch (JSONException e) {
                System.out.println("Please check if JSON is valid!!!");
            }
            return compare;
        });
    }
}
