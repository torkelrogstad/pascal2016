package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;
import static main.Main.*;

public class Term extends PascalSyntax {
  ArrayList<Factor> factors = new ArrayList<>();
  ArrayList<FactorOpr> factorOprs;
  types.Type type;

  public Term(int linenum) {
    super(linenum);
  }

  @Override
  void genCode(CodeFile f) {
    FactorOpr factorOpr;
    Factor factor = factors.get(0);
    factor.genCode(f);

    for(int i = 1; i < factors.size(); i++) {
      factor = factors.get(i);
      factorOpr = factorOprs.get(i-1);

      f.genInstr("", "pushl", "%eax", "");
      factor.genCode(f);
      f.genInstr("", "movl", "%eax,%ecx", "");
      f.genInstr("", "popl", "%eax", "");

      if(factorOpr.name == andToken.toString()){
        f.genInstr("", "andl", "%ecx,%eax", "  " + andToken.toString());
      } else if(factorOpr.name == multiplyToken.toString()) {
        f.genInstr("", "imull", "%ecx,%eax", " * ");
      } else {
        f.genInstr("", "cdq", "", "");
        if(factorOpr.name == modToken.toString()){
          f.genInstr("", "idivl", "%ecx", "");
          f.genInstr("", "movl", "%edx,%eax", " mod ");
        }

        else {
          f.genInstr("", "idivl", "%ecx", " / ");
        }
      }
    }
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    factors.get(0).check(curScope, lib);
    type = factors.get(0).type;

    for (int i = 1; i < factors.size(); i++) {
      Factor factor = factors.get(i);
      FactorOpr factorOpr = factorOprs.get(i-1);

      factorOpr.check(curScope, lib);
      factor.check(curScope, lib);

      verbose(factorOpr.getClass().getSimpleName());
      verbose(factor.getClass().getSimpleName());

      // type.checkType(factor.type, "'" + factorOpr.name + "' operand", this, "Elements on both sides of '" + factorOpr.name + "' needs to be of same type.");

      type.checkType(factor.type, "'" + factorOpr.name + "' operand", this, "Left operand to " + factorOpr.name + " is not a " + factor.type.identify() + "!");

      factor.type.checkType(type, "'" + factorOpr.name + "' operand", this, "Right operand to " + factorOpr.name + " is not a " + type.identify() + "!");
    }

    leaveVerboseBinder();
  }

  static Term parse(Scanner s) {
    enterParser("term");
    verbose("\n<term> on line " + s.curLineNum());
    verboseBlock(s);

    Term term = new Term(s.curLineNum());

    term.factors.add(Factor.parse(s));

    if (s.curToken.kind.isFactorOpr()) {
      term.factorOprs = new ArrayList<>();
    }

    while(s.curToken.kind.isFactorOpr()) {
      term.factorOprs.add(FactorOpr.parse(s));
      term.factors.add(Factor.parse(s));
    }

    leaveParser("term");
    return term;
  }

  @Override
  public String identify() {
    return "<term> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    factors.get(0).prettyPrint();
    for (int i = 1; i < factors.size(); i++) {
      Main.log.prettyPrint(" "); factorOprs.get(i-1).prettyPrint();
      Main.log.prettyPrint(" "); factors.get(i).prettyPrint();
    }
  }
}
