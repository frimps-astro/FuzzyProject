package main;

import xmlutils.PrimitiveSetObjectXMLNodeConverter;
import xmlutils.XMLReader;


import java.io.*;
import java.util.Arrays;

public class PrimitiveSetObject extends SetObject<PrimitiveSetObject> {
    private int numElements;
    private String[] elementNames;

    public PrimitiveSetObject(int numElements, String[] elementNames) {
        this.numElements = numElements;
        this.elementNames = elementNames;
    }

    public PrimitiveSetObject(){};

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

    @Override
    public String toXMLString() {
        StringBuilder setObjectXML = new StringBuilder();

        setObjectXML.append("<PrimitiveSetObject size=\"").append(getNumElements()).append("\">\n");

        //Elements
        setObjectXML.append("\t<Elements>\n\t\t");
        setObjectXML.append(Arrays.toString(getElementNames())
                //remove extra characters and white spaces
                .replace("[","")
                .replace("]","")
                .replace(" ",""));
        setObjectXML.append("\n\t</Elements>\n");

        setObjectXML.append("</PrimitiveSetObject>"); //end root node

        return setObjectXML.toString();

    }

    public int getNumElements() {
        return numElements;
    }

    public String[] getElementNames() {
        return elementNames;
    }

    public String toString() {
        return "Number of Elements: " + numElements + " elements: " + Arrays.toString(elementNames);
    }
}


