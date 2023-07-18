package main;

import xmlutils.XMLReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ProductSetObject extends SetObject{
    private SetObject left, right;
    private String leftFilename, rightFilename;

    public ProductSetObject(SetObject left, SetObject right, String leftFilename, String rightFilename) {
        this.left = left;
        this.right = right;
        this.leftFilename = leftFilename;
        this.rightFilename = rightFilename;
    }

    public ProductSetObject() {
    }


    public SetObject getLeft() {
        return left;
    }

    public SetObject getRight() {
        return right;
    }

    @Override
    int getNumElements() {
        return 2;
    }

    @Override
    String[] getElementNames() {
        String[] elements = new String[2];
        elements[0] = "left";
        elements[1] = "right";
        return elements;
    }

    @Override
    public void save(String filename) throws IOException {
        //save as xml
        FileWriter writer = new FileWriter(filename);

        try {
            writer.write(toXMLString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //save product objects
        this.left.save("src/data/left_set_object_out.xml");
        this.right.save("src/data/right_set_object_out.xml");
    }

    @Override
    public String toXMLString() {
        StringBuilder setObjectXML = new StringBuilder();

        //Elements
        setObjectXML.append("<ProductObject>\n");

        setObjectXML.append("\t<Component SetObject=\"").append(this.leftFilename).append("\"></Component>\n");
        setObjectXML.append("\t<Component SetObject=\"").append(this.rightFilename).append("\"></Component>");

        setObjectXML.append("\n</ProductObject>");

        return setObjectXML.toString();
    }

    public String toString() {
        return "\nNumber of Left Elements: " + left.getNumElements() + " Left Elements: " + Arrays.toString(left.getElementNames())
                +"\nNumber of Right Elements: " + right.getNumElements() + " Right Elements: " + Arrays.toString(right.getElementNames());
    }
}
