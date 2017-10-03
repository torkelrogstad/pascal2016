package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.verboseLog;
import static main.Main.verboseLog;

public class Expression extends PascalSyntax {
  SimpleExpr leftOp;
  RelOpr relOpr = null;
  SimpleExpr rightOp = null;
  types.Type type;

  public Expression(int lineNum) {
    super(lineNum);
  }

  @Override
  void genCode(CodeFile f){

    leftOp.genCode(f);

    if(rightOp != null){

      f.genInstr("", "pushl", "%eax", "");
      rightOp.genCode(f);
      f.genInstr("", "popl", "%ecx", "");

      if(relOpr.name == equalToken.toString()){
        // =
        f.genInstr("", "cmpl", "%eax,%ecx", "");
        f.genInstr("", "movl", "$0,%eax", "");
        f.genInstr("", "sete", "%al", " Test " + equalToken.toString());
      } else if(relOpr.name == notEqualToken.toString()){
        // !=
        f.genInstr("", "cmpl", "%eax,%ecx", "");
        f.genInstr("", "movl", "$0,%eax", "");
        f.genInstr("", "setne", "%al", " Test " + notEqualToken.toString());
      } else if(relOpr.name == lessToken.toString()){
        // <
        f.genInstr("", "cmpl", "%eax,%ecx", "");
        f.genInstr("", "movl", "$0,%eax", "");
        f.genInstr("", "setl", "%al", " Test " + lessToken.toString());
      } else if(relOpr.name == lessEqualToken.toString()){
        // <=
        f.genInstr("", "cmpl", "%eax,%ecx", "");
        f.genInstr("", "movl", "$0,%eax", "");
        f.genInstr("", "setle", "%al", " Test " + lessEqualToken.toString());

      } else if(relOpr.name == greaterToken.toString()){
        // >
        f.genInstr("", "cmpl", "%eax,%ecx", "");
        f.genInstr("", "movl", "$0,%eax", "");
        f.genInstr("", "setg", "%al", " Test " + greaterToken.toString());
      } else if(relOpr.name == greaterEqualToken.toString()){
        // >=
        f.genInstr("", "cmpl", "%eax,%ecx", "");
        f.genInstr("", "movl", "$0,%eax", "");
        f.genInstr("", "setge", "%al", " Test " + greaterEqualToken.toString());
      }
    }
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    leftOp.check(curScope, lib);
    type = leftOp.type;

    if (rightOp != null) {
      relOpr.check(curScope, lib);
      rightOp.check(curScope, lib);

      String oprName = relOpr.opr.kind.toString();

      type.checkType(leftOp.type, oprName + " operands", this, "Operands to " + oprName + " are of different types");

      type = lib.boolType;
    }

    leaveVerboseBinder();
  }

  static Expression parse(Scanner s) {
    enterParser("expression");

    Expression e = new Expression(s.curLineNum());
    e.leftOp = SimpleExpr.parse(s);

    if (s.curToken.kind.isRelOpr()) {
      e.relOpr = RelOpr.parse(s);
      e.rightOp = SimpleExpr.parse(s);
    }

    leaveParser("expression");
    return e;
  }

  @Override
  public String identify() {
    return "<expression> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    leftOp.prettyPrint();
    if (relOpr != null) {
      Main.log.prettyPrint(" "); relOpr.prettyPrint();
      Main.log.prettyPrint(" "); rightOp.prettyPrint();
    }
  }
}
