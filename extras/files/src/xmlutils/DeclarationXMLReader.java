package xmlutils;

import org.w3c.dom.Node;
import relterm.Declaration;
import relterm.DeclarationParser;

import static classutils.LoadPaths.XSDPATH;

public class DeclarationXMLReader extends XMLReader<Declaration> implements XMLNodeConverter<Declaration>{
    private static DeclarationXMLReader READER = null;

    public static DeclarationXMLReader getInstance() {
        if (READER == null) READER = new DeclarationXMLReader();
        return READER;
    }

    private DeclarationXMLReader() {
        setXMLSchema(XSDPATH + "declaration.xsd");
        setXMLNodeConverter(this);
    }

    @Override
    public Declaration convertXMLNode(Node node) {
        return DeclarationParser.getTypeParser().parse(node.getTextContent().strip());
    }

}