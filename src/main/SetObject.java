package main;

import org.w3c.dom.Node;
import xmlutils.XMLNodeConverter;
import xmlutils.XMLObject;
import xmlutils.XMLReader;


import java.io.*;
import java.util.Arrays;

public class SetObject implements XMLObject {
    private final int numElements;
    private final String[] elementNames;

    public SetObject(int numElements, String[] elementNames) {
        this.numElements = numElements;
        this.elementNames = elementNames;
    }

    public static SetObject load(String filename) {
        //load from xml file
        XMLReader<SetObject> reader = new XMLReader<>();
        reader.setXMLSchema(System.getProperty("user.dir") + "/src/data/set_object.xsd");
        reader.setXMLNodeConverter(new XMLNodeConverter<>());

        return reader.readXML(new File(filename));
    }

    public void save(String filename) throws IOException {
        //save as xml
        FileWriter writer = new FileWriter(filename);

        try {
            writer.write(toXMLString());
            writer.close();
        } catch (FileNotFoundException e) {
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
        StringBuilder setObjectXML = new StringBuilder();

        setObjectXML.append("<SetObject size=\"").append(numElements).append("\">\n");

        //Elements
        setObjectXML.append("\t<Elements>\n\t\t");
        setObjectXML.append(Arrays.toString(elementNames)
                //remove extra characters and white spaces
                .replace("[","")
                .replace("]","")
                .replace(" ",""));
        setObjectXML.append("\n\t</Elements>\n");

        setObjectXML.append("</SetObject>"); //end root node

        return setObjectXML.toString();

    }

    public String toString() {
        return "Number of Elements: " + numElements + " elements: " + Arrays.toString(elementNames);
    }
}


