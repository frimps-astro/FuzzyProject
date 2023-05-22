import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLTools {
	
    public static List<Node> getChildNodes(Node node) {
        List<Node> result = new LinkedList<Node>();
        NodeList list = node.getChildNodes();
        for(int i=0; i<list.getLength(); i++) {
            if (!list.item(i).getNodeName().equals("#text")) {
                result.add(list.item(i));
            }
        }
        return result;		
    }

    public static int getIntAttribute(Node node, String name) {
        Attr attr = (Attr) node.getAttributes().getNamedItem(name);
        return Integer.valueOf(attr.getValue());
    }

    public static boolean getBoolAttribute(Node node, String name) {
        Attr attr = (Attr) node.getAttributes().getNamedItem(name);
        return Boolean.valueOf(attr.getValue());
    }

    public static String getStringAttribute(Node node, String name) {
        Attr attr = (Attr) node.getAttributes().getNamedItem(name);
        return attr.getValue();
    }

    public static Enum getEnumAttribute(Class c, Node node, String name) {
        Attr attr = (Attr) node.getAttributes().getNamedItem(name);
        Class[] array = new Class[1];
        array[0] = String.class;
        Object obj = null;
        try {
            obj = c.getMethod("valueOf",array).invoke(null,attr.getValue());
        } catch (IllegalAccessException e) {} 
          catch (IllegalArgumentException e) {} 
          catch (NoSuchMethodException e) {} 
          catch (SecurityException e) {} 
          catch (InvocationTargetException e) {}
        Enum result = null;
        if (obj instanceof Enum) {
            result = (Enum) obj;
        }
        return result;
    }	
}