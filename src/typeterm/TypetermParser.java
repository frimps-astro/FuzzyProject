package typeterm;

import org.jparsec.OperatorTable;
import org.jparsec.Parser;
import org.jparsec.Scanners;
import org.jparsec.Terminals;

import static org.jparsec.Parser.newReference;
import org.jparsec.Parsers;

public class TypetermParser {
    private static final String[] SYMBOLS = { "(", ")", "+", "*", "\uD835\uDCDF"};
    private static final Terminals operators = Terminals.operators(SYMBOLS);
    private static TypetermParser parser;

    public static TypetermParser getTypeParser() {
        if (parser == null) {
            parser = new TypetermParser();
        }
        return parser;
    }
    private TypetermParser() {
    }

    public Parser<Typeterm> getParser(Terminals operators) {
        Parser.Reference<Typeterm> ref = newReference();
        Parser<Typeterm> term =
                ref.lazy().between(operators.token("("), operators.token(")"))
                        .or(Parsers.sequence(operators.token("\uD835\uDCDF"),ref.lazy().between(operators.token("("),
                                operators.token(")")),(t,p) -> new Power(p)))
                        .or(Terminals.Identifier.PARSER.map(TypeVariable::new));
        Parser<Typeterm> parser = new OperatorTable<Typeterm>()
                .infixr(operators.token("*").retn(Product::new), 20)
                .infixr(operators.token("+").retn(Sum::new), 10)
                .build(term);
        ref.set(parser);
        return parser;
    }

    public Typeterm parse(CharSequence source) {
        return getParser(operators)
                .from(operators.tokenizer().cast().or(Terminals.Identifier.TOKENIZER),Scanners.WHITESPACES.skipMany())
                .parse(source);
    }
}


