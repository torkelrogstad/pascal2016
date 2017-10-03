//Taken from oppgaver 38.txt

package parser;

import main.CodeFile;
import scanner.*;

/* <empty statm> ::= */

class EmptyStatm extends Statement {
  EmptyStatm(int lNum) {
    super(lNum);
  }

  @Override
  void genCode(CodeFile f){}


  @Override
  void check(Block block, Library lib) {}

  @Override
  public String identify() {
    return "<empty statm> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {}

  static EmptyStatm parse(Scanner s) {
    enterParser("empty statm");

    EmptyStatm ess = new EmptyStatm(s.curLineNum());

    leaveParser("empty statm");
    return ess;
  }
}
