package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class Constant extends PascalSyntax {
  PrefixOpr prefixOpr = null;
  UnsignedConstant unsignedConstant;

  types.Type type;
  int constVal;

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    unsignedConstant.check(curScope, lib);
    type = unsignedConstant.type;
    constVal = unsignedConstant.constVal;

    if (prefixOpr != null) {
      String oprName = prefixOpr.opr.kind.toString();
      unsignedConstant.type.checkType(lib.intType, "'prefix " + oprName + "'", this, "Prefix + or - may only be applied to integers.");

      if (prefixOpr.opr.kind == subtractToken) {
        constVal = -constVal;
      }
    }

    leaveVerboseBinder();
  }

  public Constant(int lineNum) {
    super(lineNum);
  }

  static Constant parse(Scanner s) {
    enterParser("constant");

    Constant c = new Constant(s.curLineNum());

    if (s.curToken.kind.isPrefixOpr()) {
      c.prefixOpr = PrefixOpr.parse(s);
    }

    c.unsignedConstant = UnsignedConstant.parse(s);

    leaveParser("constant");

    return c;
  }

  @Override
  public void prettyPrint() {
    if (prefixOpr != null) {
      prefixOpr.prettyPrint();
    }
    unsignedConstant.prettyPrint();
  }

  public String identify() {
    return "<constant> on line " + lineNum;
  }
}
