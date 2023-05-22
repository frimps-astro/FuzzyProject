import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class XMLWriter {
    public static XMLStreamWriter createXmlDocument(String filename) {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();

        FileOutputStream fos;
        XMLStreamWriter writer = null;
        try {
            fos = new FileOutputStream(filename);
            writer = factory.createXMLStreamWriter(fos, "UTF-8");
        }
        catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }

        return writer;
    }
}
