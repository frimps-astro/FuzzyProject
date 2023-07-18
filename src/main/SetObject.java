package main;

import xmlutils.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public abstract class SetObject implements XMLObject {

    abstract int getNumElements();

    abstract String[] getElementNames();

    public static SetObject load(String filename){
        XMLReader<SetObject> reader = new XMLReader<>();
        //load from xml file
        reader.setXMLSchema(System.getProperty("user.dir") + "/src/data/set_object.xsd");
        reader.setXMLNodeConverter(new SetObjectXMLNodeConverter());

        return reader.readXML(new File(filename));
    }

    abstract void save(String filename) throws IOException;

}
