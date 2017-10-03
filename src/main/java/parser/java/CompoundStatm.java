package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <Compound statm> ::= <variable> ':=' <expression> */

class CompoundStatm extends Statement {
  StatmList statmList;

  CompoundStatm(int lineNum) {
    super(lineNum);
  }

  @Override
  void genCode(CodeFile f){
    statmList.genCode(f);
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    statmList.check(curScope, lib);

    leaveVerboseBinder();
  }

  @Override
  public String identify() {
    return "<compound statm> on line " + lineNum;
  }

  static CompoundStatm parse(Scanner s) {
    enterParser("compound statm");

    CompoundStatm compoundStatm = new CompoundStatm(s.curLineNum());

    s.skip(beginToken);
    compoundStatm.statmList = StatmList.parse(s);
    s.skip(endToken);

    leaveParser("compound statm");
    return compoundStatm;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrintLn("begin"); Main.log.prettyIndent();
    statmList.prettyPrint(); Main.log.prettyOutdent();
    Main.log.prettyPrintLn(); Main.log.prettyPrint("end");
  }
}
