package xmlutils;

import exceptions.EmptyNodesException;
import main.SetObject;
import org.w3c.dom.Node;

public class SetObjectXMLNodeConverter implements XMLNodeConverter{

    @Override
	public SetObject convertXMLNode(Node node) {
        int numElements = XMLTools.getIntAttribute(node, "size");

//        if ()
        String[] elementNames = node.getTextContent().trim().split(",");

        try {
            if (elementNames[0].isEmpty())
                throw new EmptyNodesException("Elements node in SetObject cannot be empty");
        } catch (EmptyNodesException ex) {
            throw new RuntimeException(ex);
        }

        return new SetObject(numElements, elementNames);
    }

}