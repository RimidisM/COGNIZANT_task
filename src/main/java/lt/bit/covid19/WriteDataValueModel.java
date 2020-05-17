package lt.bit.covid19;

import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;

/**
 * Data model for information transfer from generation method to file writing and email creation methods
 * @author rimid
 */
public class WriteDataValueModel {

    public DOMSource source;
    public String fileName;
    public Transformer transformer;
}
