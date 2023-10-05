package xmlutils;

import static classutils.LoadPaths.XSDPATH;
import static xmlutils.XMLTools.getChildNodes;
import static xmlutils.XMLTools.getStringAttribute;

import exceptions.EmptyNodesException;
import main.*;
import org.w3c.dom.Node;
import storage.BasisStorage;
import storage.SetObjectStorage;

import java.util.List;


public class RelationXMLReader extends XMLReader<Relation> implements XMLNodeConverter<Relation> {

    private static RelationXMLReader READER = null;

    public static RelationXMLReader getInstance() {
        if (READER == null) READER = new RelationXMLReader();
        return READER;
    }

    private RelationXMLReader() {
        setXMLSchema(XSDPATH + "relation.xsd");
        setXMLNodeConverter(this);
    }

    @Override
    public Relation convertXMLNode(Node node) {
        List<Node> clist = getChildNodes(node);

        Basis basis = BasisStorage.getInstance().load(getStringAttribute(clist.get(0), "name"));

        SetObject source = SetObjectStorage.getInstance().load(getStringAttribute(clist.get(1),"Source"));
        SetObject target = SetObjectStorage.getInstance().load(getStringAttribute(clist.get(1),"Target"));

        int[][] matrix  = matrix(clist.get(2).getTextContent().trim().strip().split("\n"),
                source.getNumElements(), target.getNumElements());

        return new Relation(source, target, basis, matrix);
    }

    private int[][] matrix(String[] data, int s, int t){
        try{
            if (data == null || data[0].isBlank())
                throw new EmptyNodesException("Relation XML cannot have an empty Matrix node");

            int[][] starMatrix = new int[s][t];

            for (int j = 0; j < s; j++) {
                String[] starData = data[j].strip().split(",");
                for (int i = 0; i < t; i++) {
                    starMatrix[j][i] = Integer.parseInt(starData[i]);
                }
            }
            return starMatrix;
        } catch (EmptyNodesException ex){
            throw new RuntimeException(ex);
        }
    }
}
