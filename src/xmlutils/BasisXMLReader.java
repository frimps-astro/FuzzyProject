package xmlutils;

import exceptions.EmptyNodesException;
import exceptions.InvalidMeetJoinMatrixLengthException;
import main.Basis;
import main.HeytingAlgebra;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static classutils.LoadPaths.XSDPATH;
import static xmlutils.XMLTools.getChildNodes;
import static xmlutils.XMLTools.getStringAttribute;

public class BasisXMLReader extends XMLReader<Basis> implements XMLNodeConverter<Basis>{
    private static BasisXMLReader READER = null;

    public static BasisXMLReader getInstance() {
        if (READER == null) READER = new BasisXMLReader();
        return READER;
    }

    private BasisXMLReader() {
        setXMLSchema(XSDPATH + "basis.xsd");
        setXMLNodeConverter(this);
    }

    @Override
    public Basis convertXMLNode(Node node) {
        List<Node> childNodes = getChildNodes(node);
        HeytingAlgebra heytingAlgebra = HeytingAlgebra.load(getStringAttribute(childNodes.get(0),"name"));
        int[][] star  = star(childNodes.get(1).getTextContent().trim().strip().split("\n"));

        return new Basis(heytingAlgebra, star);
    }

    private int[][] star(String[] data){
        try{
            if (data == null || data[0].isBlank())
                throw new EmptyNodesException("Basis XML cannot have an empty Start node");

            int[][] starMatrix = new int[2][2];

            for (int j = 0; j < 2; j++) {
                String[] starData = data[j].strip().split(",");
                for (int i = 0; i < 2; i++) {
                    starMatrix[j][i] = Integer.parseInt(starData[i]);
                }
            }
        return starMatrix;
        } catch (EmptyNodesException ex){
            throw new RuntimeException(ex);
        }
    }
}