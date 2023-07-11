package xmlutils;

import exceptions.EmptyNodesException;
import main.ProductComponent;
import main.ProductObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class ProductObjectXMLNodeConverter implements XMLNodeConverter {

    @Override
    public ProductObject convertXMLNode(Node node) {
        ProductComponent left = components(node).get("left");
        ProductComponent right = components(node).get("right");

        return new ProductObject(left, right);
    }


    private Map<String, ProductComponent> components(Node node) {
        ProductComponent left = new ProductComponent();
        ProductComponent right = new ProductComponent();

        Map<String, ProductComponent> componentMap = new HashMap<>();

        NodeList nodeList = node.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            try {
            if (n.getNodeName().equals("Component") && XMLTools.getStringAttribute(n, "SetObject").equals("left")) {
                String[] leftElements = n.getTextContent().trim().strip().split(",");

                if (leftElements[0].isBlank())
                        throw new EmptyNodesException("Left Component node in ProductObject must contain elements");

                int leftNum = XMLTools.getIntAttribute(n, "size");
                componentMap.put("left", new ProductComponent(leftNum, leftElements));
            }

            if (n.getNodeName().equals("Component") && XMLTools.getStringAttribute(n, "SetObject").equals("right")){
                String[] rightElements = n.getTextContent().trim().strip().split(",");

                if (rightElements[0].isBlank())
                    throw new EmptyNodesException("Right Component node in ProductObject must contain elements");

                int rightNum = XMLTools.getIntAttribute(n, "size");
                componentMap.put("right", new ProductComponent(rightNum, rightElements));
            }
            } catch (EmptyNodesException e) {
                throw new RuntimeException(e);
            }
        }
        return componentMap;
    }
}

