package lt.bit.covid19;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author rimid
 */
public class CountriesClient {

    private final String HOST_HEADER = "x-rapidapi-host";
    private final String HOST_KEY_HEADER = "x-rapidapi-key";
    private final String HOST = "covid-19-data.p.rapidapi.com";
    private final String KEY = "Yours Covid-19 data API KEY";
    
    
    /**
     *
     * @return @throws IOException
     */
    @SuppressWarnings("ObjectEqualsNull")
    public List<JSONObject> getListOfCountries() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://covid-19-data.p.rapidapi.com/help/countries?format=json")
                .get()
                .addHeader(HOST_HEADER, HOST)
                .addHeader(HOST_KEY_HEADER, KEY)
                .build();

        Response response = client.newCall(request).execute();

        @SuppressWarnings("null")
        JSONArray jsonCountriesList = new JSONArray(response.body().string());

        List<JSONObject> countriesData = new ArrayList<>();
        for (int j = 0; j < jsonCountriesList.length(); j++) {

            if (jsonCountriesList.getJSONObject(j).get("latitude").equals(null)) {
                System.out.println(jsonCountriesList.getJSONObject(j).get("name")
                        + "was excluded, there was no information in data base!!!");
            } else {
                countriesData.add(jsonCountriesList.getJSONObject(j));
            }
        }

        return countriesData;
    }

    /**
     *
     * @param countryCode
     * @param date
     * @return @throws IOException
     */
    public JSONArray getDailyReportByCountryCode(String countryCode, String date) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://covid-19-data.p.rapidapi.com/report/country/code?format=json&date-format=YYYY-MM-DD&date=" + date + "&code=" + countryCode)
                .get()
                .addHeader(HOST_HEADER, HOST)
                .addHeader(HOST_KEY_HEADER, KEY)
                .build();

        Response response = client.newCall(request).execute();

        @SuppressWarnings("null")
        JSONArray dailyCountryReportArray = new JSONArray(response.body().string());

        return dailyCountryReportArray;
    }

    @SuppressWarnings("SleepWhileInLoop")
    public List<JSONObject> formCountriesReport(ArrayList<String> countriesCodes) throws IOException, InterruptedException {

        List<JSONObject> countriesReport = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < countriesCodes.size(); i++) {

            int recordNo = 0;
            int recordsLimit = 10;

            while (recordNo != recordsLimit) {
                String date = today.plusDays(-recordNo).toString();
                //Sleep due to free version API limitations, one call per second
                Thread.sleep(1100);
                JSONObject record = getDailyReportByCountryCode(countriesCodes.get(i), date).getJSONObject(0);

                int recordLenght = record.getJSONArray("provinces").getJSONObject(0).length();

                if (recordLenght < 5) {
                    recordNo++;
                    recordsLimit++;
                } else {

                    String confirmed = record.getJSONArray("provinces").getJSONObject(0).get("confirmed").toString();
                    String deaths = record.getJSONArray("provinces").getJSONObject(0).get("deaths").toString();
                    String recovered = record.getJSONArray("provinces").getJSONObject(0).get("recovered").toString();

                    if (confirmed.equals("null") || confirmed.isEmpty()
                            || deaths.equals("null") || deaths.isEmpty()
                            || recovered.equals("null") || recovered.isEmpty()) {

                        recordNo++;
                        recordsLimit++;
                    } else {

                        countriesReport.add(record);
                        recordNo++;
                    }
                }
            }
        }
        return countriesReport;
    }
}
