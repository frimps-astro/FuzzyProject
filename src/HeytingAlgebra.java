import exceptions.InvalidXYvalueException;
import exceptions.EmptyNodesException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class HeytingAlgebra implements XMLObject, XMLNodeConverter {
    private  int numElements;
    private  String[] elementNames;
    private  int[][] meet;
    private  int[][] join;
    private  int[][] impl;
    private  int bot;
    private  int top;

    public int getNumElements() {
        return numElements;
    }

    public String[] getElementNames() {
        return elementNames;
    }

    public int[][] getMeet() {
        return meet;
    }

    public int[][] getJoin() {
        return join;
    }

    public static HeytingAlgebra load(String filename) {
        //load from xml file
        XMLReader<HeytingAlgebra> reader = new XMLReader<>();
        reader.setXMLSchema(System.getProperty("user.dir") + "/src/data/heyting.xsd");
        reader.setXMLNodeConverter(new HeytingAlgebra());

        return reader.readXML(new File(filename));
    }

    public void save(String filename) throws IOException {
        //save as xml
        XMLStreamWriter writer = XMLWriter.createXmlDocument(filename);
        List<String> elementLists = Arrays.stream(elementNames).toList();
        StringBuilder nodeText = new StringBuilder();

        try {
            writer.writeStartDocument();
            writer.writeCharacters("\n");
            writer.writeStartElement("HeytingAlgebra");
            writer.writeAttribute("size", MessageFormat.format("{0}", numElements));

            //Write Elements node
            writer.writeCharacters("\n\t");
            writer.writeStartElement("Elements");
            writer.writeCharacters("\n\t\t");
            writer.writeCharacters(Arrays.stream(elementNames).reduce("%s,%s"::formatted).orElse(""));

            writer.writeCharacters("\n\t");
            writer.writeEndElement(); //end Elements node

            //Write Meet node
            writeMeetJoin(writer, meet, "Meet", elementLists, nodeText);

            // Write Join node
            writeMeetJoin(writer, join, "Join", elementLists, nodeText);

            writer.writeCharacters("\n");
            writer.writeEndElement(); //End HeytingAlgebra node
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static void writeMeetJoin(XMLStreamWriter writer, int[][] dataArray, String node,
                                      List<String> elementLists, StringBuilder nodeText)
            throws XMLStreamException {

        writer.writeCharacters("\n\t");
        writer.writeStartElement(node);
        for (int[] data:dataArray) {
            Arrays.stream(data).forEach(i -> nodeText.append(elementLists.get(i)).append(","));
            writer.writeCharacters("\n\t\t" + nodeText);
            nodeText.setLength(0); //clear text for next iteration
        }
        writer.writeCharacters("\n\t");
        writer.writeEndElement(); //end Join node
    }

    public boolean leq(int x, int y){
        //check whether the two elements satisfy meet[x][y] =x
       try {
           if ((x < -1 || x > numElements) || (y < -1 || y > numElements))
               throw new InvalidXYvalueException("Invalid x or y values");

           return meet[x][y] == x;
       }catch (Exception ex){
           System.out.println("leq method exception message: "+ex.getMessage());
       }
       return false;
    }

    @Override
    public String toXMLString() {
        StringBuilder joinStr = new StringBuilder();
        StringBuilder meetStr = new StringBuilder();
        for (int i = 0; i < numElements; i++) {
            for (int j = 0; j < numElements; j++) {
                joinStr.append(join[i][j]).append(",");
                meetStr.append(meet[i][j]).append(",");
            }
            joinStr.append("\n");
            meetStr.append("\n");
        }
        return "Number of Elements: "+numElements+" elements: "+ Arrays.toString(elementNames)
                +"\nmeet:\n"+meetStr +"\njoin:\n"+joinStr
                +"\nbot: "+bot+" top: "+top;
    }

    @Override
    public HeytingAlgebra convertXMLNode(Node node) {
        HeytingAlgebra heytingAlgebra = new HeytingAlgebra();
        heytingAlgebra.numElements = XMLTools.getIntAttribute(node, "size");

        //convert elements, meet, and join to their corresponding int values
        elementsMeetJoin(node, heytingAlgebra);
        computeBotTop(heytingAlgebra);

        return heytingAlgebra;
    }

    private void elementsMeetJoin(Node node, HeytingAlgebra heytingAlgebra){
        String[] meet_ = null;
        String[] join_ = null;
        String[] elements = null;

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            if (n.getNodeName().equalsIgnoreCase("elements")) {
                elements = n.getTextContent().strip().split(",");
            }

            //for meet and join split using \n to get each line
            if (n.getNodeName().equalsIgnoreCase("meet")) {
                meet_ = n.getTextContent().strip().split("\n");
            }
            if (n.getNodeName().equalsIgnoreCase("join")) {
                join_ = n.getTextContent().strip().split("\n");
            }
        }

        heytingAlgebra.elementNames = elements;

        //instantiate class field variables
        heytingAlgebra.meet = new int[heytingAlgebra.numElements][heytingAlgebra.numElements];
        heytingAlgebra.join = new int[heytingAlgebra.numElements][heytingAlgebra.numElements];

        try{
            if (meet_ == null || meet_[0].isBlank() || join_ == null || join_[0].isBlank()
                    || elements == null || elements[0].isBlank())
                throw new EmptyNodesException("XML cannot have Elements, Meet or Join empty nodes");

            //convert elements to a string list for easy index computations
            List<String> elementLists = Arrays.stream(elements).toList();

            for (int j = 0; j < heytingAlgebra.numElements; j++) {
                String[] meetData = meet_[j].strip().split(",");
                String[] joinData = join_[j].strip().split(",");
                for (int i = 0; i < heytingAlgebra.numElements; i++) {
                    heytingAlgebra.meet[j][i] = elementLists.indexOf(meetData[i]);
                    heytingAlgebra.join[j][i] = elementLists.indexOf(joinData[i]);
                }
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void computeBotTop(HeytingAlgebra heytingAlgebra){
        //computing bot and top
        List<String> elementLists = Arrays.stream(heytingAlgebra.elementNames).toList();

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
                if (heytingAlgebra.join[index][i] != i) {
                    isBot = false;
                    break;
                }
            }

            //check meet array using 'index' and each 'i'
            for (String s : elementLists) {
                int i = elementLists.indexOf(s);
                if (heytingAlgebra.meet[index][i] != i) {
                    isTop = false;
                    break;
                }
            }
            if (isBot) {
                heytingAlgebra.bot = index;
            }
            if (isTop) {
                heytingAlgebra.top = index;
            }
        }
    }
    public void impl(){
    //impl[x,y] is the join of all elements z with leq(meet[z][x],y)
    //TODO
        //Need some clarification on implementing this
    }
}

//bot is the element so that join[bot,x] == x for all x.
//top is the element so that meet[top,x] == x for all x.
