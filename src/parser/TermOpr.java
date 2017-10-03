package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class TermOpr extends Operator {
  String name;

  public TermOpr(int linenum) {
    super(linenum);
  }

  @Override
  void genCode(CodeFile f) {}

  @Override
  void check(Block block, Library lib) {}

  static TermOpr parse(Scanner s) {
    enterParser("term opr");

    TermOpr termOpr = new TermOpr(s.curLineNum());
    termOpr.name = s.curToken.kind.toString(); s.readNextToken();

    leaveParser("term opr");
    return termOpr;
  }

  @Override
  public String identify() {
    return "<term opr> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name);
  }
}
