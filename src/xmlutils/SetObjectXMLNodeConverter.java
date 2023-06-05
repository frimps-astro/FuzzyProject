package xmlutils;

import exceptions.EmptyNodesException;
import main.HeytingAlgebra;
import main.SetObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class SetObjectXMLNodeConverter implements XMLNodeConverter{

    @Override
	public SetObject convertXMLNode(Node node) {
        int numElements = XMLTools.getIntAttribute(node, "size");
        String[] elementNames = node.getTextContent().trim().split(",");

        return new SetObject(numElements, elementNames);
    }

}