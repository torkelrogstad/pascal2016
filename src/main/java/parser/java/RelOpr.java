package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class RelOpr extends Operator {
  String name;
  types.Type type;

  public RelOpr(int linenum) {
    super(linenum);
  }

  @Override
  void genCode(CodeFile f){
    
  }

  @Override
  void check(Block block, Library lib) {
    enterVerboseBinder();

    type = null;

    leaveVerboseBinder();
  }

  static RelOpr parse(Scanner s) {
    enterParser("rel opr");

    RelOpr ro = new RelOpr(s.curLineNum());
    ro.name = s.curToken.kind.toString();
    ro.opr = s.curToken; s.readNextToken();

    leaveParser("rel opr");
    return ro;
  }

  @Override
  public String identify() {
    return "<rel opr> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name);
  }
}
