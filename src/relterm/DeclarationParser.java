package relterm;

import org.jparsec.*;
import typeterm.RelationType;
import typeterm.Typeterm;
import typeterm.TypetermParser;

import java.util.*;

import static org.jparsec.Parser.newReference;


public class DeclarationParser {
    private static DeclarationParser parser;
    private static final String[] SYMBOLS = { "(", ")", "'", ";", "\u02D8","\ua71c","\u2192","\u2294","\\","\u2293","/","*",
            ";*", "\u2192*","\\*","/*","syQ*","syQ","\ua71b","\u2aeb",
            "\u03B5","\u2261","\u0399","\u039A","\u03C0","\u03C1","\u2aea", "(", ")", "+", "*", "P", ":", "->", "="};

    private static final Terminals operators = Terminals.operators(SYMBOLS);

    private static final ReltermParser reltermParser = ReltermParser.getTypeParser();
    private static final TypetermParser typetermParser = TypetermParser.getTypeParser();
    private static final Parser<String> identifier_parser = Terminals.Identifier.PARSER;

    public static DeclarationParser getTypeParser() {
        if (parser == null) {
            parser = new DeclarationParser();
        }
        return parser;
    }
    public Parser<Declaration> getParser() {
        Declaration declaration = new Declaration();
        Parser.Reference<Declaration> ref = newReference();

        Parser<Declaration> decParser = Parsers.sequence(getIdentifier().followedBy(term("(")), getRelterm().followedBy(term(":")),
                Parsers.sequence(getTypeterm().followedBy(term("->")),
                        getTypeterm(), RelationType::new).followedBy(term(")").followedBy(term(":"))),
                Parsers.sequence(getTypeterm().followedBy(term("->")),
                        getTypeterm().followedBy(term("=")), RelationType::new), getRelterm(),
                (name, key, vars, resultType, relterm) -> {
                    declaration.name = name;
                    declaration.vars = relTypesMap(key.toStringPrec(0), vars);
                    declaration.t = relterm;
                    declaration.resultType = resultType;

                    return declaration;
                });

        ref.set(decParser);

        return decParser;
    }

    private static Parser<String> getIdentifier() {
        return identifier_parser.map(String::new);
    }
    private static Parser<Token> term(String t) {
        return operators.token(t);
    }
    private static Parser<Relterm> getRelterm() {
        return reltermParser.getParser(operators);
    }
    private static Parser<Typeterm> getTypeterm() {
        return typetermParser.getParser(operators);
    }

    private LinkedHashMap<String, RelationType> relTypesMap(String key, RelationType type) {
        LinkedHashMap<String, RelationType> map = new LinkedHashMap<>();
        map.put(key, type);
        return map;
    }

    public Declaration parse(CharSequence source) {
        return getParser()
                .from(operators.tokenizer().cast().or(Terminals.Identifier.TOKENIZER),Scanners.WHITESPACES.skipMany())
                .parse(source);
    }
}
