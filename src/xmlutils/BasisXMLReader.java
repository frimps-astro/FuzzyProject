package xmlutils;

import exceptions.EmptyNodesException;
import main.Basis;
import main.HeytingAlgebra;
import org.w3c.dom.Node;
import storage.HeytingAlgebraStorage;

import java.util.List;

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
        HeytingAlgebra heytingAlgebra = HeytingAlgebraStorage.getInstance().load(getStringAttribute(childNodes.get(0),"name"));
        int[][] star  = getMatrix(childNodes.get(1).getTextContent().trim().strip().split("\n"));
        int[][] plus  = getMatrix(childNodes.get(2).getTextContent().trim().strip().split("\n"));

        return new Basis(heytingAlgebra, star, plus);
    }

    private int[][] getMatrix(String[] data){
        try{
            if (data == null || data[0].isBlank())
                throw new EmptyNodesException("Basis XML cannot have an empty Star/Plus node");

            int s = data.length;
            int t = data[0].trim().split(",").length;
            int[][] matrix = new int[s][t];

            for (int j = 0; j < s; j++) {
                String[] starData = data[j].trim().strip().split(",");
                for (int i = 0; i < t; i++) {
                    matrix[j][i] = Integer.parseInt(starData[i]);
                }
            }
        return matrix;
        } catch (EmptyNodesException ex){
            throw new RuntimeException(ex);
        }
    }
}