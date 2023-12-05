package relterm;

public class DeclarationParser {
    private final ReltermParser parser;

    public DeclarationParser(ReltermParser parser) {
        this.parser = parser;
    }

    public Relterm parseDeclaration(String dec){
        return parser.parse(dec);
    }
}
