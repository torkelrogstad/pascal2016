package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.*;

public abstract class Type extends PascalSyntax {
  public Type(int linenum) {
    super(linenum);
  }

  types.Type type;

  static Type parse(Scanner s) {
    enterParser("type");

    Type t = null;

    if (s.curToken.kind == arrayToken) {
      t = ArrayType.parse(s);
    }

    else {
      t = TypeName.parse(s);
    }

    leaveParser("type");
    return t;
  }
}
