package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class NumberLiteral extends UnsignedConstant {
  int val;

  public NumberLiteral(int linenum) {
    super(linenum);
  }

  @Override
  void genCode(CodeFile f){
    f.verboseGenInstr("", "", "", "number literal");
    f.genInstr("", "movl", "$" + val + ",%eax", "  " + val);
  }

  @Override
  void check(Block block, Library lib) {
    enterVerboseBinder();

    type = lib.intType;
    constVal = val;

    leaveVerboseBinder();
  }

  static NumberLiteral parse(Scanner s) {
    enterParser("number literal");

    NumberLiteral nl = new NumberLiteral(s.curLineNum());

    nl.val = s.curToken.intVal; s.readNextToken();

    leaveParser("number literal");
    return nl;
  }

  public String identify() {
    return "<number literal> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(Integer.toString(val));
  }
}
