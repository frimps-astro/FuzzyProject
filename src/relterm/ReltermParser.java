package relterm;

import org.jparsec.OperatorTable;
import org.jparsec.Parser;
import org.jparsec.Scanners;
import org.jparsec.Terminals;
import relterm.leaves.*;
import typeterm.Typeterm;

import java.util.Map;

import static org.jparsec.Parser.newReference;

public class ReltermParser {

    private static final String[] SYMBOLS = { "(", ")", "'", ";", "\u02D8","\ua71c","\u2192","\u2294","\\","\u2293","/","*",
            ";*", "\u2192*","\\*","/*","syQ*","syQ","\ua71b","\u2aeb",
            "\u03B5","\u2261","\u0399","\u039A","\u03C0","\u03C1","\u2aea"};


    private static final Terminals operators = Terminals.operators(SYMBOLS);
	  
    private static ReltermParser parser;

    public static ReltermParser getTypeParser() {
        if (parser == null) {
            parser = new ReltermParser();
        }
        return parser;
    }
    private ReltermParser() {
    }

    public Parser<Relterm> getParser(Terminals operators) {
        Parser.Reference<Relterm> ref = newReference();
        Parser<Relterm> term =
                ref.lazy().between(operators.token("("), operators.token(")"))
                        .or(operators.token("ε").retn(new Epsi()))
                        .or(operators.token("⫫").retn(new Bot()))
                        .or(operators.token("Κ").retn(new Kappa()))
                        .or(operators.token("≡").retn(new Identity()))
                        .or(operators.token("\u0399").retn(new Iota()))
                        .or(operators.token("ρ").retn(new Rho()))
                        .or(operators.token("π").retn(new Pi()))
                        .or(operators.token("⫪").retn(new Top()))
                .or(Terminals.Identifier.PARSER.map(RelVariable::new));
        Parser<Relterm> parser = new OperatorTable<Relterm>()
                .postfix(operators.token("˘").retn(Converse::new), 50)
                .infixr(operators.token(";").retn(Composition::new), 40)
                .postfix(operators.token("ꜛ").retn(Up::new), 50)
                .postfix(operators.token("'").retn(Complement::new), 50)
                .infixr(operators.token("⊔").retn(Join::new), 40)
                .infixr(operators.token("\\").retn(LeftResidual::new), 30)
                .infixr(operators.token("syQ*").retn(StarSyq::new), 0)
                .infixr(operators.token("\\*").retn(StarLeftResidual::new), 30)
                .infixr(operators.token("⊓").retn(Meet::new), 20)
                .infixr(operators.token("syQ").retn(Syq::new), 0)
                .infixr(operators.token("/").retn(RightResidual::new), 30)
                .postfix(operators.token("ꜜ").retn(Down::new), 50)
                .infixr(operators.token("→").retn(Impl::new), 10)
                .infixr(operators.token("→*").retn(StarImpl::new), 10)
                .infixr(operators.token("*").retn(Star::new), 20)
                .infixr(operators.token("/*").retn(StarRightResidual::new), 30)
                .infixr(operators.token(";*").retn(StarComposition::new), 40)
            .build(term);
        ref.set(parser);
        return parser;
    }

    public Relterm parse(CharSequence source) {
        return getParser(operators)
                .from(operators.tokenizer().cast().or(Terminals.Identifier.TOKENIZER),Scanners.WHITESPACES.skipMany())
                .parse(source);
    }
}


