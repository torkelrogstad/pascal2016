package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class CharLiteral extends UnsignedConstant {
  char c;

  public CharLiteral(char c, int linenum) {
    super(linenum);

    this.c = c;
    constVal = (int) c;
  }

  @Override
  void genCode(CodeFile f){
    // XXX: DIFFERENT BEHAVIOR COMP. TO REF. COMPILATOR:
    // if c == 10 (\n), then 'eol' is printed. Else, value of char.
    // ref. compilator chose to print int value of char, this makes
    // understanding the .s files harder
    String comment;
    if (c == 10) comment = "eol";
    else comment = String.valueOf(c);
    f.genInstr("", "movl", "$" + (int) c + ",%eax", "  '" + comment + "'");
  }

  static CharLiteral parse(Scanner s) {
    enterParser("char literal");

    CharLiteral cl = new CharLiteral(s.curToken.charVal, s.curLineNum());
    s.readNextToken();

    leaveParser("char literal");

    return cl;
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    type = lib.charType;

    leaveVerboseBinder();
  }

  @Override
  public String identify() {
    return "<char literal> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint("\'" + Character.toString(c) + "\'");
  }
}
