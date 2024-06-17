package relterm;

import relterm.Declaration;
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
            "\u03B5","\u2261","\u0399","\u039A","\u03C0","\u03C1","\u2aea", "(", ")", "+", "*", "P", ":", "->", "=", ","};

    private static final Terminals operators = Terminals.operators(SYMBOLS);

    private static final ReltermParser reltermParser = ReltermParser.getTypeParser();
    private static final TypetermParser typetermParser = TypetermParser.getTypeParser();
    private static final Parser<String> identifier_parser = Terminals.Identifier.PARSER;

    private Declaration declaration;


    public static DeclarationParser getTypeParser() {
        if (parser == null) {
            parser = new DeclarationParser();
        }
        return parser;
    }
    public Parser<Declaration> getParser() {
        Parser.Reference<Declaration> ref = newReference();

        Parser<Declaration> decParser = Parsers.sequence(getIdentifier(), Parsers.between(term("("), getVars(), term(")")).followedBy(term(":")),
                Parsers.sequence(getTypeterm().followedBy(term("->")),
                        getTypeterm().followedBy(term("=")), RelationType::new), getRelterm(),
                (name, vars, resultType, relterm) -> {
                    declaration.name = name;
                    declaration.vars = relTypesMap(vars);
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

    private Parser<String> getVars() {
        return Parsers.sequence(getIdentifier().followedBy(term(":")),getIdentifier().followedBy(term("->")),
                        getIdentifier()).sepBy1(term(",")).source();
    }
    private LinkedHashMap<String, RelationType> relTypesMap(String data) {
        LinkedHashMap<String, RelationType> map = new LinkedHashMap<>();
        String[] vars = data.split(",");
        for (String var : vars) {
            var entry = varEntry(var);
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    private Map.Entry<String, RelationType> varEntry(String var) {
        return Parsers.sequence(getIdentifier().followedBy(term(":")),
                Parsers.sequence(getTypeterm().followedBy(term("->")),
                        getTypeterm(), RelationType::new), Map::entry)
                .from(operators.tokenizer().cast().or(Terminals.Identifier.TOKENIZER),Scanners.WHITESPACES.skipMany()).parse(var);
    }

    public Declaration parse(CharSequence source) {
        declaration = new Declaration();
        declaration.setDeclaration(source.toString());
        return getParser()
                .from(operators.tokenizer().cast().or(Terminals.Identifier.TOKENIZER),Scanners.WHITESPACES.skipMany())
                .parse(source);
    }
}
