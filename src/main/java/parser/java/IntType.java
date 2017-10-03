package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class IntType extends Type {
  public IntType(int linenum) {
    super(linenum);
  }

  @Override
  void check(Block block, Library lib) {
    
  }

  static IntType parse(Scanner s) {
    IntType it = new IntType(s.curLineNum());
    return it;
  }

  @Override
  public String identify() {
    return "<int type> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {

  }
}
