package lt.bit.covid19;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author rimid
 */
public class XmlParser {

    public XmlParser() {
    }
    
    /**
     * Reads and creates data model from XML file
     * @return Data model of country with country name, date and death.
     * @throws IOException 
     */
    public List<CountryDataValueModel> parseData() throws IOException {

        List<CountryDataValueModel> results = new ArrayList();
        String fileName = "task_3_4";
        try {

            File inputFile = new File("c://temp//" + fileName + ".xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("country");

            for (int i = 0; i < nodes.getLength(); i++) {
                CountryDataValueModel cdvm = new CountryDataValueModel();
                Node node = nodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    cdvm.countryName = getValue("countryName", element);
                    cdvm.date = (getValue("date", element));
                    cdvm.deaths = Integer.parseInt(getValue("deaths", element));
                }
                results.add(cdvm);
            }
        } catch (IOException | ParserConfigurationException | org.xml.sax.SAXException ex) {
            System.out.println("Serious configuration error.");
        }
        return results;
    }

    /**
     * Method for value extraction from XML object
     * @param tag information unit name
     * @param element XML element
     * @return node value
     */
    private static String getValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        return node.getNodeValue();
    }
}
