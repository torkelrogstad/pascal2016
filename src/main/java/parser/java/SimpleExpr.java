package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;
import static main.Main.*;

public class SimpleExpr extends PascalSyntax {
  PrefixOpr preOpr = null;
  ArrayList<Term> terms = new ArrayList<>();
  ArrayList<TermOpr> termOprs;
  types.Type type;

  public SimpleExpr(int linenum) {
    super(linenum);
  }

  @Override
  void genCode(CodeFile f) {
    TermOpr termOpr;
    Term term = terms.get(0);
    term.genCode(f);

    if(preOpr != null){
      if(preOpr.name == subtractToken.toString())
        f.genInstr("", "negl", "%eax", " - (prefix)");
    }

    for (int i = 1; i < terms.size(); i++) {
      term = terms.get(i);
      termOpr = termOprs.get(i-1);

      f.genInstr("", "pushl", "%eax", "");
      term.genCode(f);
      f.genInstr("", "movl", "%eax,%ecx", "");
      f.genInstr("", "popl", "%eax", "");

      if(termOpr.name == addToken.toString()){
        f.genInstr("", "addl", "%ecx,%eax", "  " + addToken.toString());
      } else if(termOpr.name == subtractToken.toString()){
        // why is this included? can't be found anywhere in the compendium
        f.genInstr("", "subl", "%ecx,%eax", "  " + subtractToken.toString());
      } else if(termOpr.name == orToken.toString()){
        f.genInstr("", "orl", "%ecx,%eax", "  " + orToken.toString());
      }
    }
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    Term firstTerm = terms.get(0);

    firstTerm.check(curScope, lib);
    type = firstTerm.type;

    if (preOpr != null) {
      preOpr.check(curScope, lib);

      type.checkType(lib.intType, "prefix '" + preOpr.name + "' operand", this, "'" + preOpr.name + "' operand takes an integer.");
    }

    for (int i = 1; i < terms.size(); i++) {
      Term term = terms.get(i);
      TermOpr termOpr = termOprs.get(i-1);

      term.check(curScope, lib);
      termOpr.check(curScope, lib);

      type.checkType(term.type, "left '" + termOpr.name + "' operand", this, "Left operand to " + termOpr.name + " is not a " + term.type.identify() + "!");

      term.type.checkType(type, "'" + termOpr.name + "' operand", this, "Right operand to " + termOpr.name + " is not a " + type.identify() + "!");
    }

    leaveVerboseBinder();
  }


  static SimpleExpr parse(Scanner s) {
    enterParser("simple expr");

    SimpleExpr simpleExpr = new SimpleExpr(s.curLineNum());

    verbose("\n" + simpleExpr.identify());
    verboseBlock(s);

    if (s.curToken.kind.isPrefixOpr() && s.nextToken.kind.isPrefixOpr()) {
      s.testError("value");
    }

    if (s.curToken.kind.isPrefixOpr()) {
      simpleExpr.preOpr = PrefixOpr.parse(s);
    }

    simpleExpr.terms.add(Term.parse(s));

    if(s.curToken.kind.isTermOpr()) {
      simpleExpr.termOprs = new ArrayList<>();
    }

    while (s.curToken.kind.isTermOpr()) {
      simpleExpr.termOprs.add(TermOpr.parse(s));
      simpleExpr.terms.add(Term.parse(s));
    }

    leaveParser("simple expr");
    return simpleExpr;
  }

  @Override
  public String identify() {
    return "<simple expr> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    if (preOpr != null) {
      preOpr.prettyPrint(); Main.log.prettyPrint(" ");
    }

    terms.get(0).prettyPrint();
    for (int i = 1; i < terms.size(); i++) {
      Main.log.prettyPrint(" ");
      termOprs.get(i-1).prettyPrint();
      Main.log.prettyPrint(" ");
      terms.get(i).prettyPrint();
    }
  }
}
