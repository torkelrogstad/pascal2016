package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.*;

abstract class Factor extends PascalSyntax {
  types.Type type;

  public Factor(int linenum) {
    super(linenum);
  }

  static Factor parse(Scanner s) {
    enterParser("factor");
    verbose("\n<factor> on line " + s.curLineNum());
    verboseBlock(s);

    Factor factor = null;

    if (s.curToken.kind == nameToken && s.nextToken.kind == leftBracketToken) {
      factor = Variable.parse(s);
    }

    else if (s.curToken.kind == nameToken && s.nextToken.kind == leftParToken) {
      factor = FuncCall.parse(s);
    }

    else if (s.curToken.kind == notToken) {
      factor = Negation.parse(s);
    }

    else if (s.curToken.kind == leftParToken) {
      factor = InnerExpr.parse(s);
    }

    else if (s.curToken.kind == charValToken || s.curToken.kind == intValToken) {
      factor = UnsignedConstant.parse(s);
    }

    else {
      factor = Variable.parse(s);
    }

    leaveParser("factor");
    return factor;
  }

}
