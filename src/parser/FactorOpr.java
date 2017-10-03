package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class FactorOpr extends Operator {
  String name;
  public FactorOpr(int linenum) {
    super(linenum);
  }

  @Override
  void genCode(CodeFile f) {}

  @Override
  void check(Block block, Library lib) {
    enterVerboseBinder();
    
    leaveVerboseBinder();
  }

  static FactorOpr parse(Scanner s) {
    enterParser("factor opr");

    FactorOpr factorOpr = new FactorOpr(s.curLineNum());
    factorOpr.name = s.curToken.kind.toString(); s.readNextToken();

    leaveParser("factor opr");
    return factorOpr;
  }

  @Override
  public String identify() {
    return "<factor opr> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name);
  }
}
