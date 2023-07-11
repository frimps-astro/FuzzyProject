package main;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ProductObject extends SetObject<ProductObject>{
    private ProductComponent left, right;

    public ProductObject(ProductComponent left, ProductComponent right) {
        this.left = left;
        this.right = right;
    }

    public ProductObject() {
    }

    public SetObject<ProductObject> getLeft() {
        return left;
    }

    public SetObject<ProductObject> getRight() {
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
    }

    @Override
    public String toXMLString() {
        StringBuilder setObjectXML = new StringBuilder();

        //Elements
        setObjectXML.append("<ProductObject>\n");

        left.setPosition("left");
        right.setPosition("right");

        setObjectXML.append(left.toXMLString());
        setObjectXML.append(right.toXMLString());

        setObjectXML.append("</ProductObject>");

        return setObjectXML.toString();
    }

    public String toString() {
        return "\nNumber of Left Elements: " + left.getNumElements() + " Left Elements: " + Arrays.toString(left.getElementNames())
                +"\nNumber of Right Elements: " + right.getNumElements() + " Right Elements: " + Arrays.toString(right.getElementNames());
    }
}
