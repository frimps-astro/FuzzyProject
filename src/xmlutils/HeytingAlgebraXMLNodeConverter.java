package xmlutils;

import exceptions.EmptyNodesException;
import exceptions.InvalidMeetJoinMatrixLengthException;
import main.HeytingAlgebra;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeytingAlgebraXMLNodeConverter implements XMLNodeConverter{

	@Override
    public HeytingAlgebra convertXMLNode(Node node) {
        Map<String, Object> heytingAlgebra = elementsMeetJoin(node);

        int numElements = (int) heytingAlgebra.get("numElements");
        String[] elementNames = (String[]) heytingAlgebra.get("elementNames");
        int[][] meet = (int[][]) heytingAlgebra.get("meet");
        int[][] join = (int[][]) heytingAlgebra.get("join");

        return new HeytingAlgebra(numElements, elementNames, meet, join);
    }

	private Map<String, Object> elementsMeetJoin(Node node){
        String[] meet_ = null;
        String[] join_ = null;
        String[] elements = null;
        int numElements = XMLTools.getIntAttribute(node, "size");

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            if (n.getNodeName().equalsIgnoreCase("elements")) {
                elements = n.getTextContent().trim().strip().split(",");
            }

            //for meet and join split using \n to get each line
            if (n.getNodeName().equalsIgnoreCase("meet")) {
                meet_ = n.getTextContent().trim().strip().split("\n");
            }
            if (n.getNodeName().equalsIgnoreCase("join")) {
                join_ = n.getTextContent().trim().strip().split("\n");
            }
        }

		//Map to store numElements, elementNames, meet and join
		Map<String, Object> heytingAlgebraMap = new HashMap<>();

        heytingAlgebraMap.put("numElements", numElements);
        heytingAlgebraMap.put("elementNames", elements);


        //instantiate class field variables
        int[][] meet = new int[numElements][numElements];
        int[][] join = new int[numElements][numElements];

        try{
            if (meet_ == null || meet_[0].isBlank() || join_ == null || join_[0].isBlank()
                    || elements == null || elements[0].isEmpty())
                throw new EmptyNodesException("HeytingAlgebra XML cannot have Elements, Meet or Join empty nodes");

            if (meet_.length != numElements || join_.length != numElements)
                throw new InvalidMeetJoinMatrixLengthException(
                        "Meet or Join matrix length must be equal to the number of elements");

            //convert elements to a string list for easy index computations
            List<String> elementLists = Arrays.stream(elements).toList();

            for (int j = 0; j < numElements; j++) {
                String[] meetData = meet_[j].strip().split(",");
                String[] joinData = join_[j].strip().split(",");
                for (int i = 0; i < numElements; i++) {
                    meet[j][i] = elementLists.indexOf(meetData[i]);
                    join[j][i] = elementLists.indexOf(joinData[i]);
                }
            }

			heytingAlgebraMap.put("meet", meet);
			heytingAlgebraMap.put("join", join);
        } catch (EmptyNodesException | InvalidMeetJoinMatrixLengthException ex){
            throw new RuntimeException(ex);
        }

        return heytingAlgebraMap;
    }
}