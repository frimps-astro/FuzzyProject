package xmlutils;

import static classutils.LoadPaths.XSDPATH;
import java.util.List;

import main.*;
import org.w3c.dom.Node;
import main.SetObject;
import storage.BasisStorage;
import storage.SetObjectStorage;

import static xmlutils.XMLTools.getChildNodes;
import static xmlutils.XMLTools.getStringAttribute;

public class SetObjectXMLReader extends XMLReader<SetObject> implements XMLNodeConverter<SetObject> {
    
    private static SetObjectXMLReader READER = null;
    
    public static SetObjectXMLReader getInstance() {
        if (READER == null) READER = new SetObjectXMLReader();
        return READER;
    }
    
    private SetObjectXMLReader() {
        setXMLSchema(XSDPATH + "set_object.xsd");
        setXMLNodeConverter(this);
    }  

    @Override
    public SetObject convertXMLNode(Node node) {
        switch (node.getNodeName()) {
            case "PrimitiveSetObject" -> {
                String[] elementNames = node.getTextContent().split(",");
                for(int i=0; i<elementNames.length; i++) {
                    elementNames[i] = elementNames[i].trim();
                }
                return new PrimitiveSetObject(elementNames);
            }
            case "ProductSetObject" -> {
                List<Node> clist = getChildNodes(node);
                SetObject left = SetObjectStorage.getInstance().load(getStringAttribute(clist.get(0),"SetObject"));
                SetObject right = SetObjectStorage.getInstance().load(getStringAttribute(clist.get(1),"SetObject"));
                return new ProductSetObject(left,right);
            }
            case "SumSetObject" -> {
                List<Node> clist = getChildNodes(node);
                SetObject left = SetObjectStorage.getInstance().load(getStringAttribute(clist.get(0),"SetObject"));
                SetObject right = SetObjectStorage.getInstance().load(getStringAttribute(clist.get(1),"SetObject"));
                return new SumSetObject(left,right);
            }
            case "PowerSetObject" -> {
                List<Node> clist = getChildNodes(node);
                SetObject body = SetObjectStorage.getInstance().load(getStringAttribute(clist.get(0),"body"));
                Basis basis = BasisStorage.getInstance().load(getStringAttribute(clist.get(1),"basis"));
                return new PowerSetObject(body, basis);
            }
        }
        return null;
    }
}
