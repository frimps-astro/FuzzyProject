package main;

import xmlutils.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public abstract class SetObject<E> implements XMLObject {
    abstract int getNumElements();

    abstract String[] getElementNames();

    public E load(String filename, String type) {
        if (type.equals("heyting")) {
            //load from xml file
            XMLReader<E> reader = new XMLReader<>();
            reader.setXMLSchema(System.getProperty("user.dir") + "/src/data/heyting.xsd");
            reader.setXMLNodeConverter(new HeytingAlgebraXMLNodeConverter());

            return reader.readXML(new File(filename));
        } else if (type.equals("primitive")){
            //load from xml file
            XMLReader<E> reader = new XMLReader<>();
            reader.setXMLSchema(System.getProperty("user.dir") + "/src/data/set_object.xsd");
            reader.setXMLNodeConverter(new PrimitiveSetObjectXMLNodeConverter());

            return reader.readXML(new File(filename));
        } else{
            //load from xml file
            XMLReader<E> reader = new XMLReader<>();
            reader.setXMLSchema(System.getProperty("user.dir") + "/src/data/product_object.xsd");
            reader.setXMLNodeConverter(new ProductObjectXMLNodeConverter());

            return reader.readXML(new File(filename));
        }
    }

    abstract void save(String filename) throws IOException;

}
