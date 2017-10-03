// Note that this class is taken in its entirety from figure 4.9 in the
// provided compendium. Other classes are inspired by this structure.

package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <while-statm> ::= 'while' <expression> 'do' <statement> */

public class WhileStatm extends Statement {
  Expression expr;
  Statement body;

  WhileStatm(int lineNum) {
    super(lineNum);
  }

  @Override
  void genCode(CodeFile f) {
    String testLabel = f.getLocalLabel();
    String endLabel = f.getLocalLabel();

    f.genInstr(testLabel, "", "", "Start while-statement");
    expr.genCode(f);
    f.genInstr("", "cmpl", "$0,%eax", "");
    f.genInstr("", "je", endLabel, "");
    body.genCode(f);
    f.genInstr("", "jmp", testLabel, "");
    f.genInstr(endLabel, "", "", "End while-statement");

  }

  @Override
  public String identify() {
    return "<while-statm> on line " + lineNum;
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    expr.check(curScope, lib);
    expr.type.checkType(lib.boolType, "while-test", this, "While-test is not Boolean.");
    body.check(curScope, lib);

    leaveVerboseBinder();
  }

  static WhileStatm parse(Scanner s) {
    enterParser("while-statm");

    WhileStatm ws = new WhileStatm(s.curLineNum());
    s.skip(whileToken);

    ws.expr = Expression.parse(s);
    s.skip(doToken);
    ws.body = Statement.parse(s);

    leaveParser("while-statm");
    return ws;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint("while "); expr.prettyPrint(); Main.log.prettyIndent();
    Main.log.prettyPrintLn(" do "); body.prettyPrint();
    Main.log.prettyOutdent();
  }
}
