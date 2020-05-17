package lt.bit.covid19;

import java.io.File;
import java.io.IOException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author rimid
 */
public class FileWriter {
    
    /**
     * Writes given XML to the new created file in the c:/temp/ directory
     *
     * @param dataValue
     * @throws IOException
     * @throws javax.xml.transform.TransformerException
     */
    public static void writeToFile(WriteDataValueModel dataValue) throws IOException, TransformerException {

        DOMSource source = dataValue.source;
        String fileName = dataValue.fileName;
        Transformer transformer = dataValue.transformer;

        String filePath = "c://temp//" + fileName + ".xml";

        // write the content into xml file
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);

        System.out.println("Please find " + fileName + " in: c:/temp/" + fileName + ".xml");
    }
}
