package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.verbose;

/* <assign statm> ::= <variable> ':=' <expression> */

class AssignStatm extends Statement {
  Variable var;
  Expression expr;
  types.Type leftType;
  types.Type rightType;

  AssignStatm(int lineNum) {
    super(lineNum);
  }

  @Override
  void genCode(CodeFile f){
    expr.genCode(f);

    // function call
    if (var.ref instanceof FuncDecl) {
      int i = -4 * (var.ref.declLevel);
      String arg = i + "(%ebp),%edx";
      f.verboseGenInstr("", "", "", "assign: func call");
      f.genInstr("", "movl", arg, "");
      f.genInstr("", "movl", "%eax, -32(%edx)", var.ref.name + " :=");
    }

    // normal variable, or array
    else {
      VarDecl varRef = (VarDecl) var.ref;

      if (var.isArray()) {
        f.verboseGenInstr("", "", "", "assign: arr-acc");
        f.genInstr("", "pushl", "%eax", "");
        var.expression.genCode(f);

        Type t = varRef.parserType;
        ArrayType at = (ArrayType) t;

        int low = at.low;
        if (low != 0) {
          f.genInstr("", "subl", "$" + low + ",%eax", "");
        }

        int i = var.ref.declLevel;
        f.verboseGenInstr("", "# i:", "# " + i, "assign: arr-acc II");
        f.genInstr("", "movl", (-4 * i) + "(%ebp),%edx", "");
        f.verboseGenInstr("", "var.ref:", var.ref.identify(), "");
        f.genInstr("", "leal", var.ref.declOffset + "(%edx),%edx", "");
        f.genInstr("", "popl", "%ecx", "");
        String arg = var.name + "[x] :=";
        //XXX : difference from ref.comp, missing '0' in front of (edx...
        f.genInstr("", "movl", "%ecx,(%edx,%eax,4)", arg);
      }

      else {
        f.verboseGenInstr("", "", "", "assign: 'normal' variable");

        int i = var.ref.declLevel;
        f.genInstr("", "movl", (-4 * i) + "(%ebp), %edx", "");
        String arg = "%eax," + var.ref.declOffset + "(%edx)";
        f.genInstr("", "movl", arg, var.name + " :=");
      }
    }
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    var.check(curScope, lib);
    var.ref.checkWhetherAssignable(this);

    // Left hand side is function name AND isn't found in the functions own
    // block. Functions can only be assigned to in the definition of said
    // function, and an error occurs
    if (var.ref instanceof FuncDecl) {
      FuncDecl fd = (FuncDecl) var.ref;
      if (curScope.outerScope.decls.get(fd.name) == null) {
        this.error("Assignment to " + fd.name + " only allowed inside function!");
      }
    }

    expr.check(curScope, lib);
    //expr.checkWhetherValue(this);

    leftType = var.type;
    rightType = expr.type;

    verbose(var.ref.identify());
    verbose(var.name);
    verbose("leftType: " + leftType.identify());
    verbose("rightType: " + rightType.identify());

    leftType.checkType(rightType, ":=", this, "Different types in assignment!");

    leaveVerboseBinder();
  }

  static AssignStatm parse(Scanner s) {
    enterParser("assign statm");

    AssignStatm as = new AssignStatm(s.curLineNum());

    as.var = Variable.parse(s);

    s.skip(assignToken);

    as.expr = Expression.parse(s);

    leaveParser("assign statm");
    return as;
  }

  @Override
  public void prettyPrint() {
    var.prettyPrint(); Main.log.prettyPrint(" := "); expr.prettyPrint();
  }

  @Override
  public String identify() {
    return "<assign statm> on line " + lineNum;
  }
}
