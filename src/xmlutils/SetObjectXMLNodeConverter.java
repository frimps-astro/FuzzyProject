package xmlutils;

import exceptions.EmptyNodesException;
import main.PrimitiveSetObject;
//import main.ProductComponent;
import main.ProductSetObject;
import main.SetObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class SetObjectXMLNodeConverter implements XMLNodeConverter<SetObject>{
    private String leftFilename, rightFilename;

    @Override
	public SetObject convertXMLNode(Node node) {
        if (node.getNodeName().equals("SetObject")) {
            int numElements = XMLTools.getIntAttribute(node, "size");

            String[] elementNames = node.getTextContent().trim().split(",");

            try {
                if (elementNames[0].isEmpty())
                    throw new EmptyNodesException("Elements node in a SetObject cannot be empty");
            } catch (EmptyNodesException ex) {
                throw new RuntimeException(ex);
            }

            return new PrimitiveSetObject(numElements, elementNames);
        } else {
            Map<String, SetObject> comps = components(node);

            SetObject left = comps.get("left");
            SetObject right = comps.get("right");

            return new ProductSetObject(left, right, leftFilename, rightFilename);
        }
    }

    private Map<String, SetObject> components(Node node) {
        Map<String, SetObject> componentMap = new HashMap<>();

        NodeList nodeList = node.getChildNodes();
        int counter = 0; //track number of components

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            if (n.getNodeName().equals("Component") && counter == 0) {
                String filename = XMLTools.getStringAttribute(n, "SetObject");
                filename = "src/data/"+filename;
                leftFilename = filename;
                componentMap.put("left", SetObject.load(filename));
                counter += 1;
            }

            if (n.getNodeName().equals("Component") && counter == 1) {
                String filename = XMLTools.getStringAttribute(n, "SetObject");
                filename = "src/data/"+filename;
                rightFilename = filename;
                componentMap.put("right", SetObject.load(filename));
            }
        }
        return componentMap;
    }

}