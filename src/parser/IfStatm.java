package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.verboseLog;
import static main.Main.verboseLog;

class IfStatm extends Statement {
  Expression expression;
  Statement statement1;
  Statement statement2 = null;

  types.Type type;

  IfStatm (int lnum) {
    super(lnum);
  }

  @Override
  void genCode(CodeFile f){

    String elseLabel, endLabel;
    f.genInstr("", "", "", "Start if-statement");

    expression.genCode(f);

    if(statement2 == null){

      endLabel = f.getLocalLabel();

      f.genInstr("", "cmpl", "$0, %eax", "");
      f.genInstr("", "je", endLabel, "");
      statement1.genCode(f);
      f.genInstr(endLabel, "", "", "");

    } else {
      elseLabel = f.getLocalLabel();
      endLabel = f.getLocalLabel();

      f.genInstr("", "cmpl", "$0, %eax", "");
      f.genInstr("", "je", elseLabel, "");
      statement1.genCode(f);
      f.genInstr("", "jmp", endLabel, "");
      f.genInstr(elseLabel, "", "", "");
      statement2.genCode(f);
      f.genInstr(endLabel, "", "", "");
    }

    f.genInstr("", "", "", "End if-statement");

  }


  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    type = lib.boolType;

    expression.check(curScope, lib);
    statement1.check(curScope, lib);
    if (statement2 != null)
      statement2.check(curScope, lib);

    type.checkType(expression.type, "if-test", this, "If-tests require boolean expressions.");

    leaveVerboseBinder();
  }

  static IfStatm parse(Scanner s) {
    enterParser("if-statm");

    IfStatm ifStatm = new IfStatm(s.curLineNum());
    s.skip(ifToken);
    ifStatm.expression = Expression.parse(s);
    s.skip(thenToken);
    ifStatm.statement1 = Statement.parse(s);
    if (s.curToken.kind == elseToken) {
      s.skip(elseToken);
      ifStatm.statement2 = Statement.parse(s);
    }

    leaveParser("if-statm");
    return ifStatm;
  }

  @Override
  public String identify() {
    return "<if-statm> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint("if "); expression.prettyPrint();
    Main.log.prettyPrint(" then"); Main.log.prettyPrintLn();
    Main.log.prettyIndent();

    statement1.prettyPrint();
    Main.log.prettyOutdent();
    if (statement2 != null) {
      Main.log.prettyPrintLn();
      Main.log.prettyPrint("else"); Main.log.prettyIndent();
      Main.log.prettyPrintLn(); statement2.prettyPrint();
      Main.log.prettyOutdent();
    }
  }
}
