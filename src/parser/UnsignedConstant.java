package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.*;

public abstract class UnsignedConstant extends Factor {
  public UnsignedConstant(int linenum) {
    super(linenum);
  }

  int constVal;

  static UnsignedConstant parse(Scanner s) {
    enterParser("unsigned constant");
    verboseBlock(s);

    UnsignedConstant uc = null;

    if (s.curToken.kind == charValToken) {
      uc = CharLiteral.parse(s);
    }

    else if (s.curToken.kind == intValToken) {
      uc = NumberLiteral.parse(s);
    }

    else {
      uc = NamedConstant.parse(s);
    }

    leaveParser("unsigned constant");
    return uc;
  }
}
