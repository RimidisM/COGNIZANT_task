package lt.bit.covid19;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rimid
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, TransformerConfigurationException, TransformerException {

        CountriesClient countriesClient = new CountriesClient();
        List<JSONObject> countriesData = countriesClient.getListOfCountries();
         
//Task No.: 1
        FileWriter.writeToFile(countriesDataToXml(countriesData));

//Task No.: 2
        FileWriter.writeToFile(sortCountriesXml(countriesData));

//Task No.: 3 and 4        
        ArrayList<String> countriesCodes = new ArrayList<>();
        countriesCodes.add("lt");
        countriesCodes.add("lv");
        countriesCodes.add("se");
        countriesCodes.add("no");
        countriesCodes.add("it");

        FileWriter.writeToFile(getLastDaysDataToXml(countriesClient.formCountriesReport(countriesCodes)));
//Task No.: 5  
        XmlParser xmlParser = new XmlParser();
        WriteDataValueModel WorstDayData = getWorstDayDataToXml(xmlParser.parseData());
        FileWriter.writeToFile(getWorstDayDataToXml(xmlParser.parseData()));

//Task No.: 6
        SendAttachmentInEmail sendEmail = new SendAttachmentInEmail();
        sendEmail.sendEmail(WorstDayData);
    }

    /**
     * Forms fetched data to XML
     *
     * @param countriesData JSON object with data about countries
     * @return data model with necessary information for data writing to file
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    private static WriteDataValueModel countriesDataToXml(List<JSONObject> countriesData) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException {

        String fileName = "task_1";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Create root element 
        Element rootElement = doc.createElement("countries");
        doc.appendChild(rootElement);

        for (int i = 0; i < countriesData.size(); i++) {

            // Create country element
            Element country = doc.createElement("country");
            rootElement.appendChild(country);
            // country code element
            Element countryCode = doc.createElement("countryCode");
            countryCode.appendChild(doc.createTextNode(countriesData.get(i).get("alpha2code").toString()));
            country.appendChild(countryCode);
            // Create country name element
            Element countryName = doc.createElement("countryName");
            countryName.appendChild(doc.createTextNode(countriesData.get(i).get("name").toString()));
            country.appendChild(countryName);
            //Create longitude element 
            Element longitude = doc.createElement("longitude");
            longitude.appendChild(doc.createTextNode(countriesData.get(i).get("longitude").toString()));
            country.appendChild(longitude);
            // Create latitude element
            Element latitude = doc.createElement("latitude");
            latitude.appendChild(doc.createTextNode(countriesData.get(i).get("latitude").toString()));
            country.appendChild(latitude);
        }

        return getWriteDataValueModel(fileName, doc);
    }

    /**
     * Forms XML from sorted data
     *
     * @param countriesData JSON object with data about countries
     * @return data model with necessary information for data writing to file
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    private static WriteDataValueModel sortCountriesXml(List<JSONObject> countriesData) throws IOException, TransformerConfigurationException, TransformerException, ParserConfigurationException {

        String fileName = "task_2";

        Comparator.compareJSON(countriesData);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Create root element 
        Element rootElement = doc.createElement("countries");
        doc.appendChild(rootElement);

        for (int i = 0; i < 15; i++) {

            // Create country element
            Element country = doc.createElement("country");
            rootElement.appendChild(country);
            // Create country name element
            Element countryName = doc.createElement("countryName");
            countryName.appendChild(doc.createTextNode(countriesData.get(i).get("name").toString()));
            country.appendChild(countryName);
            // Create latitude element
            Element latitude = doc.createElement("latitude");
            latitude.appendChild(doc.createTextNode(countriesData.get(i).get("latitude").toString()));
            country.appendChild(latitude);
        }
        
        return getWriteDataValueModel(fileName, doc);
    }

    /**
     * Forms XML from daily data about countries
     *
     * @param countriesReport JSON object with daily data about countries
     * @return data model with necessary information for data writing to file
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    private static WriteDataValueModel getLastDaysDataToXml(List<JSONObject> countriesReport) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException {

        String fileName = "task_3_4";

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Create root element 
        Element rootElement = doc.createElement("countries");
        doc.appendChild(rootElement);

        for (int i = 0; i < countriesReport.size(); i++) {

            String numberOfConfirmed = countriesReport.get(i).getJSONArray("provinces").getJSONObject(0).get("confirmed").toString();
            String numberODeaths = countriesReport.get(i).getJSONArray("provinces").getJSONObject(0).get("deaths").toString();
            String numberOfRecovered = countriesReport.get(i).getJSONArray("provinces").getJSONObject(0).get("recovered").toString();

            // Create country element
            Element country = doc.createElement("country");
            rootElement.appendChild(country);
            // Create country name element
            Element countryName = doc.createElement("countryName");
            countryName.appendChild(doc.createTextNode(countriesReport.get(i).get("country").toString()));
            country.appendChild(countryName);
            // date element
            Element date = doc.createElement("date");
            date.appendChild(doc.createTextNode(countriesReport.get(i).get("date").toString()));
            country.appendChild(date);
            // Create confirmed element
            Element confirmed = doc.createElement("confirmed");
            confirmed.appendChild(doc.createTextNode(numberOfConfirmed));
            country.appendChild(confirmed);
            //Create deaths element 
            Element deaths = doc.createElement("deaths");
            deaths.appendChild(doc.createTextNode(numberODeaths));
            country.appendChild(deaths);
            // Create recovered element
            Element recovered = doc.createElement("recovered");
            recovered.appendChild(doc.createTextNode(numberOfRecovered));
            country.appendChild(recovered);
        }
        
        return getWriteDataValueModel(fileName, doc);
    }

    /**
     * Forms XML from sorted data
     *
     * @param countriesData JSON object with data about countries
     * @return data model with necessary information for data writing to file
     * and email generation
     * @param countriesResults parsed XML data from file
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    private static WriteDataValueModel getWorstDayDataToXml(List<CountryDataValueModel> countriesResults) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException {

        String fileName = "task_5";

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Create root element 
        Element rootElement = doc.createElement("countries");
        doc.appendChild(rootElement);
        
        List<CountryDataValueModel> countryDeathTolls = new ArrayList();
        String countryNameA = countriesResults.get(0).countryName;
        for (int i = 0; i < countriesResults.size(); i++) {

            CountryDataValueModel cdvm = new CountryDataValueModel();
            String countryNameB = countriesResults.get(i).countryName;

            if (countryNameA.equals(countryNameB)) {

                cdvm.countryName = countriesResults.get(i).countryName;
                cdvm.date = countriesResults.get(i).date;
                cdvm.deaths = countriesResults.get(i).deaths;
                countryDeathTolls.add(cdvm);
            } else {

                countryNameA = countryNameB;
                
                rootElement.appendChild(createCountryElement(countryDeathTolls, doc));

                if (i < countriesResults.size()) {

                    countryDeathTolls.clear();
                }
                if (i == countriesResults.size() - 1) {
                    i = countriesResults.size();
                } else {

                    i = i - 1;
                }
            }
        }

        rootElement.appendChild(createCountryElement(countryDeathTolls, doc));
        
        return getWriteDataValueModel(fileName, doc);
    }

    /**
     * Gives list with sorted and calculated death toll for country
     * @param countryDeathTolls country daily data from API
     */
    private static void getBiggestDeathToll(List<CountryDataValueModel> countryDeathTolls) {

        for (int i = 0; i < countryDeathTolls.size(); i++) {
            if (i == countryDeathTolls.size() - 1) {
                countryDeathTolls.get(i).deaths = 0;
            } else {
                countryDeathTolls.get(i).deaths = countryDeathTolls.get(i).deaths - countryDeathTolls.get(i + 1).deaths;
            }
        }

        Comparator.compareCountryDataValueModel(countryDeathTolls);
    }

     /**
      * Transform Document to XML String
      * @return transformer object
      * @throws TransformerConfigurationException 
      */
    private static Transformer getTransformer() throws TransformerConfigurationException {
        // Transform Document to XML String
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        return transformer;
    }

     /**
      * Forms ready to write data
      * @param fileName
      * @param doc document object
      * @return ready to write data
      * @throws TransformerConfigurationException 
      */
    private static WriteDataValueModel getWriteDataValueModel(String fileName, Document doc) throws TransformerConfigurationException {
        WriteDataValueModel writeDataValueModel = new WriteDataValueModel();
        writeDataValueModel.fileName = fileName;
        writeDataValueModel.source = new DOMSource(doc);
        writeDataValueModel.transformer = getTransformer();

        return writeDataValueModel;
    }
    
    /**
     * Forms XML country element with inner elements
     * @param countryDeathTolls country data
     * @param doc document object
     * @return returns XML country element with inner elements
     */
    private static Element createCountryElement(List<CountryDataValueModel> countryDeathTolls, Document doc){
        getBiggestDeathToll(countryDeathTolls);

        //Create country element
        Element country = doc.createElement("country");
        // Create country name element
        Element countryName = doc.createElement("countryName");
        countryName.appendChild(doc.createTextNode(countryDeathTolls.get(0).countryName));
        country.appendChild(countryName);
        // date element
        Element date = doc.createElement("date");
        date.appendChild(doc.createTextNode(countryDeathTolls.get(0).date));
        country.appendChild(date);
        //Create deaths element 
        Element deaths = doc.createElement("deaths");
        deaths.appendChild(doc.createTextNode(Integer.toString(countryDeathTolls.get(0).deaths)));
        country.appendChild(deaths);
        
        return country;
    }
}
