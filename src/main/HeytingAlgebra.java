package main;

import exceptions.InvalidXYvalueException;
import xmlutils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HeytingAlgebra implements XMLObject {
    private final  int numElements;
    private  final String[] elementNames;
    private  final int[][] meet;
    private  final int[][] join;
    private  int[][] impl;
    private  int bot;
    private  int top;

    public int getNumElements() {
        return numElements;
    }

    public String[] getElementNames() {
        return elementNames;
    }

    public int getMeet(int x, int y) {
        return meet[x][y];
    }

    public int getJoin(int x, int y) {
        return join[x][y];
    }

    public int getImpl(int x, int y) {
        return impl[x][y];
    }

    public int getBot() {
        return bot;
    }

    public int getTop() {
        return top;
    }

    public HeytingAlgebra(int numElements, String[] elementNames, int[][] meet, int[][] join) {
        this.numElements = numElements;
        this.elementNames = elementNames;
        this.meet = meet;
        this.join = join;

        //call to compute impl, bot and top values
        computeImpl();
        computeBotTop();
    }

    public static HeytingAlgebra load(String filename) {
        //load from xml file
        XMLReader<HeytingAlgebra> reader = new XMLReader<>();
        reader.setXMLSchema(System.getProperty("user.dir") + "/src/data/heyting.xsd");
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

    private boolean leq(int x, int y){
        //check whether the two elements satisfy meet[x][y] =x
       try {
           if ((x < -1 || x > numElements) || (y < -1 || y > numElements))
               throw new InvalidXYvalueException("Invalid x or y values");

           return meet[x][y] == x;
       }catch (Exception ex){
           System.out.println("leq method exception message: " + ex.getMessage());
       }
       return false;
    }

    @Override
    public String toXMLString() {
        StringBuilder joinStr = new StringBuilder();
        StringBuilder meetStr = new StringBuilder();
        List<String> elementLists = Arrays.stream(elementNames).toList();

        for (int i = 0; i < numElements; i++) {
            for (int j = 0; j < numElements; j++) {
                joinStr.append(elementLists.get(join[i][j])).append(",");
                meetStr.append(elementLists.get(meet[i][j])).append(",");
            }
            joinStr.append("\n\t\t");
            meetStr.append("\n\t\t");
        }

        StringBuilder heytingXML = new StringBuilder();
        //start root node
        heytingXML.append("<HeytingAlgebra size=\"").append(numElements).append("\">\n");

        //Elements
        heytingXML.append("\t<Elements>\n\t\t");
        heytingXML.append(Arrays.toString(elementNames)
                //remove extra characters and white spaces
                .replace("[","")
                .replace("]","")
                .replace(" ",""));
        heytingXML.append("\n\t</Elements>\n");

        //Meet :remove trailing ","
        meetStr = new StringBuilder(meetStr.deleteCharAt(meetStr.lastIndexOf(",")));
        heytingXML.append("\t<Meet>\n\t\t");
        heytingXML.append(meetStr.toString().strip());
        heytingXML.append("\n\t</Meet>\n");

        //Join :remove trailing ","
        joinStr = new StringBuilder(joinStr.deleteCharAt(joinStr.lastIndexOf(",")));
        heytingXML.append("\t<Join>\n\t\t");
        heytingXML.append(joinStr.toString().strip());
        heytingXML.append("\n\t</Join>\n");

        heytingXML.append("</HeytingAlgebra>"); //end root node

        return heytingXML.toString();
    }
    public String toString() {
        StringBuilder joinStr = new StringBuilder();
        StringBuilder meetStr = new StringBuilder();
        StringBuilder implStr = new StringBuilder();
        for (int i = 0; i < numElements; i++) {
            for (int j = 0; j < numElements; j++) {
                joinStr.append(join[i][j]).append(",");
                meetStr.append(meet[i][j]).append(",");
                implStr.append(impl[i][j]).append(",");
            }
            joinStr.append("\n");
            meetStr.append("\n");
            implStr.append("\n");
        }
        return "Number of Elements: " + numElements + " elements: " + Arrays.toString(elementNames)
                + "\nmeet:\n" + meetStr + "\njoin:\n" + joinStr
                + "\nimpl:\n" + implStr
                + "\nbot: " + bot + " top: " + top;
    }

    private void computeBotTop(){
        //computing bot and top
        List<String> elementLists = Arrays.stream(elementNames).toList();

        //loop through all elements
        //take 'index' of each; assume as bot or top
        for (String str : elementLists) {
            int index = elementLists.indexOf(str);
            boolean isBot = true;
            boolean isTop = true;

            //loop through elements and take their indexes(i)
            //check join array using 'index' and each 'i'
            for (String s : elementLists) {
                int i = elementLists.indexOf(s);
                if (join[index][i] != i) {
                    isBot = false;
                    break;
                }
            }

            //check meet array using 'index' and each 'i'
            for (String s : elementLists) {
                int i = elementLists.indexOf(s);
                if (meet[index][i] != i) {
                    isTop = false;
                    break;
                }
            }
            if (isBot) {
                bot = index;
            }
            if (isTop) {
                top = index;
            }
        }
    }
    public void computeImpl(){
        impl = new int[numElements][numElements];

        for (int x = 0; x < numElements; x++) {
            for (int y = 0; y < numElements; y++) {
                int result = bot;
                for (int z = 0; z < numElements; z++) {
                    if (leq(meet[z][x], y)){
                        result = join[result][z];
                    }
                }
                impl[x][y] = result;
            }
        }
    }
}

