package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class CharType extends Type {
  public CharType(int linenum) {
    super(linenum);
  }

  @Override
  void check(Block block, Library lib) {

  }

  static CharType parse(Scanner s) {
    CharType ct = new CharType(s.curLineNum());
    return ct;
  }

  @Override
  public String identify() {
    return "<char type> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {

  }
}
