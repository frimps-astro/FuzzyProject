package main;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ProductComponent extends SetObject<ProductObject>{
    private int numElements;
    private String[] elementNames;

    private String position;

    public void setPosition(String position) {
        this.position = position;
    }

    public ProductComponent() {
    }

    public ProductComponent(int numElements, String[] elementNames) {
        this.numElements = numElements;
        this.elementNames = elementNames;
    }

    int getNumElements() {
        return numElements;
    }

    String[] getElementNames() {
        return elementNames;
    }

    void save(String filename) throws IOException {
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
    public String toXMLString(){
        StringBuilder setObjectXML = new StringBuilder();

        //Elements
        setObjectXML.append("\t<Component SetObject=\"").append(position).append("\" size=\"").append(numElements).append("\">\n\t\t");
        setObjectXML.append(Arrays.toString(elementNames)
                //remove extra characters and white spaces
                .replace("[","")
                .replace("]","")
                .replace(" ",""));
        setObjectXML.append("\n\t</Component>\n");

        return setObjectXML.toString();
    }

    @Override
    public ProductObject load(String filename, String type) {
        return null;
    }
}
