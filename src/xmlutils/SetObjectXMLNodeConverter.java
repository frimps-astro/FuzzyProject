package xmlutils;

import main.SetObject;
import org.w3c.dom.Node;

public class SetObjectXMLNodeConverter implements XMLNodeConverter{

    @Override
	public SetObject convertXMLNode(Node node) {
        int numElements = XMLTools.getIntAttribute(node, "size");
        String[] elementNames = node.getTextContent().trim().split(",");

        return new SetObject(numElements, elementNames);
    }

}