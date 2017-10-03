package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class BoolType extends Type {
  public BoolType(int linenum) {
    super(linenum);
  }

  @Override
  void check(Block block, Library lib) {
    enterVerboseBinder();

    type = lib.boolType;

    leaveVerboseBinder();
  }

  static BoolType parse(Scanner s) {
    BoolType bt = new BoolType(s.curLineNum());
    return bt;
  }

  @Override
  public String identify() {
    return "<bool type> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {

  }
}
