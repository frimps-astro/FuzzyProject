import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.text.MessageFormat;
import java.util.Arrays;

public class SetObject implements XMLObject, XMLNodeConverter {
    private int numElements;
    private String[] elementNames;

    public static SetObject load(String filename) {
        //load from xml file
        XMLReader<SetObject> reader = new XMLReader<>();
        reader.setXMLSchema(System.getProperty("user.dir") + "/src/data/set_object.xsd");
        reader.setXMLNodeConverter(new SetObject());

        return reader.readXML(new File(filename));
    }

    public void save(String filename) throws IOException {
        //save as xml
        XMLStreamWriter writer = XMLWriter.createXmlDocument(filename);
        try {
            writer.writeStartDocument();
            writer.writeCharacters("\n");
            writer.writeStartElement("SetObject");
            writer.writeAttribute("size", MessageFormat.format("{0}", numElements));

            //Write Elements node
            writer.writeCharacters("\n\t");
            writer.writeStartElement("Elements");
            writer.writeCharacters(Arrays.stream(elementNames)
                    .reduce("%s,%s"::formatted).orElse(""));

            writer.writeCharacters("\t");
            writer.writeEndElement(); //end Elements node
            writer.writeCharacters("\n");

            writer.writeEndElement(); //End SetObject node
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    public int getNumElements() {
        return numElements;
    }

    public String[] getElementNames() {
        return elementNames;
    }

    @Override
    public String toXMLString() {
        return "Number of Elements: "+numElements+" elements: "+Arrays.toString(elementNames);
    }

    @Override
    public SetObject convertXMLNode(Node node) {
        SetObject setObject = new SetObject();
        setObject.numElements = XMLTools.getIntAttribute(node, "size");
        setObject.elementNames = node.getTextContent().split(",");

        return setObject;
    }
}


