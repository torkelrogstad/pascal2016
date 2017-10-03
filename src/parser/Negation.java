package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class Negation extends Factor {
  Factor factor;

  public Negation(int lineNum) {
    super(lineNum);
  }

  @Override
  void genCode(CodeFile f){
    factor.genCode(f);
    f.genInstr("", "xorl", "$0x1,%eax", " not ");
  }

  @Override
  void check(Block block, Library lib) {
    enterVerboseBinder();

    factor.check(block, lib);

    type = lib.boolType;
    type.checkType(factor.type, "'not' operand", this, "Negation (using 'not') needs a boolean value.");

    leaveVerboseBinder();
  }

  static Negation parse(Scanner s) {
    enterParser("negation");

    Negation negation = new Negation(s.curLineNum());
    s.skip(notToken);
    negation.factor = Factor.parse(s);

    leaveParser("negation");
    return negation;
  }

  @Override
  public String identify() {
    return "<negation> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint("not "); factor.prettyPrint();
  }
}
