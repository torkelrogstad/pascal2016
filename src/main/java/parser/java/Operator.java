package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public abstract class Operator extends PascalSyntax {

  Token opr;

  public Operator(int linenum) {
    super(linenum);
  }

  
}