package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class InnerExpr extends Factor {
  Expression expression;

  public InnerExpr(int lineNum) {
    super(lineNum);
  }

  @Override
  void genCode(CodeFile f){
    expression.genCode(f);
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    expression.check(curScope, lib);
    type = expression.type;

    leaveVerboseBinder();
  }

  static InnerExpr parse(Scanner s) {
    enterParser("inner expr");

    InnerExpr innerExpr = new InnerExpr(s.curLineNum());
    s.skip(leftParToken);
    innerExpr.expression = Expression.parse(s);
    s.skip(rightParToken);

    leaveParser("inner expr");
    return innerExpr;
  }

  public String identify() {
    return "<inner expr> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint("("); expression.prettyPrint();
    Main.log.prettyPrint(")");
  }
}
