package xmlutils;

import exceptions.EmptyNodesException;
import main.PrimitiveSetObject;
import org.w3c.dom.Node;

public class PrimitiveSetObjectXMLNodeConverter implements XMLNodeConverter{

    @Override
	public PrimitiveSetObject convertXMLNode(Node node) {
        int numElements = XMLTools.getIntAttribute(node, "size");

        String[] elementNames = node.getTextContent().trim().split(",");

        try {
            if (elementNames[0].isEmpty())
                throw new EmptyNodesException("Elements node in PrimitiveSetObject cannot be empty");
        } catch (EmptyNodesException ex) {
            throw new RuntimeException(ex);
        }

        return new PrimitiveSetObject(numElements, elementNames);
    }

}