package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.verbose;

class Variable extends Factor {
  String name;
  Expression expression = null;
  //VarDecl varRef; ConstDecl constRef; ParamDecl paramRef;
  PascalDecl ref; //FuncDecl funcRef;


  Variable (int lnum) {
    super(lnum);
  }

  boolean isArray() {
    return expression != null;
  }

  @Override
  void genCode(CodeFile f) {
    if (ref instanceof FuncDecl) {
      f.genInstr("", "call", ref.progProcFuncName, "  " + name);
    } else if(ref instanceof ConstDecl){
      ref.genCode(f);
      // accessing a place in an array
    } else if (isArray()) {
      expression.genCode(f);

      int i = 0;

      // there HAS to be a more elegant way of doing this?
      // anyways, this is the index name accessing the array
      Variable v = (Variable) expression.leftOp.terms.get(0).factors.get(0);

      VarDecl varRef = (VarDecl) ref;
      Type t = varRef.parserType;
      ArrayType at = (ArrayType) t;
      i = at.low;
      if (i != 0) f.genInstr("", "subl", "$" + i + ",%eax", "");

      f.verboseGenInstr("", "# ref.identify():", ref.identify(), "");

      i = ref.declLevel;
      String s = Integer.toString(-4 * i);
      f.verboseGenInstr("", "# i:", s, "var: arr-acc II");
      f.genInstr("", "movl", s + "(%ebp),%edx", "");

      i = ref.declOffset;
      f.genInstr("", "leal", i + "(%edx),%edx", "");

      // XXX: difference from ref.comp., missing '0' in front of (%edx...
      f.genInstr("", "movl", "(%edx,%eax,4),%eax", name + "[...]");
    } else {
      f.verboseGenInstr("", "# ref.identify()", ref.identify(), "");

      int i = ref.declLevel;
      f.verboseGenInstr("", "", "# i: " + i, "var: 'normal'");
      f.genInstr("", "movl", (-4 * i) + "(%ebp),%edx", "");
      f.genInstr("", "movl", ref.declOffset + "(%edx),%eax", "  " + name);
    }
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    verbose("Looking for decl " + name);

    ref = curScope.findDecl(name, this);

    // if (ref instanceof VarDecl) {
    //   VarDecl vd = (VarDecl) ref;
    //   vd.checkWhetherAssignable(this);
    // }
    //
    // else if (ref instanceof ConstDecl) {
    //   ConstDecl cd = (ConstDecl) ref;
    //   cd.checkWhetherAssignable(this);
    // }
    //
    // else if (ref instanceof ParamDecl) {
    //   ParamDecl pd = (ParamDecl) ref;
    //   pd.checkWhetherAssignable(this);
    // }
    //
    // else if (ref instanceof FuncDecl) {
    //   FuncDecl fd = (FuncDecl) ref;
    //   fd.checkWhetherAssignable(this);
    // }

    type = ref.type;

    verbose(ref.getClass().getSimpleName());
    verbose(ref.identify());

    // if (type == null) {
    //   ConstDecl cd = (ConstDecl) ref;
    //   verbose(cd.c.identify());
    // }


    // array-acessing
    // var[expr]
    if (expression != null) {
      expression.check(curScope, lib);

      verbose("name: " + name + ", type: " + type.identify());
      verbose("exprtype: " + expression.type.identify());

      lib.intType.checkType(expression.type, "array index", this, "Array indexes must be of type integer.");
    }

    leaveVerboseBinder();
  }

  static Variable parse(Scanner s) {
    enterParser("variable");

    Variable v = new Variable(s.curLineNum());
    v.name = s.curToken.id; s.readNextToken();

    if (s.curToken.kind == leftBracketToken) {
      s.skip(leftBracketToken);
      v.expression = Expression.parse(s);
      s.skip(rightBracketToken);
    }

    leaveParser("variable");
    return v;
  }

  @Override
  public String identify() {
    String s = "";
    if (name != null) {
      s = name;
    }
    return "<variable> " + name + " on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name);
    if (expression != null) {
      Main.log.prettyPrint("["); expression.prettyPrint();
      Main.log.prettyPrint("]");
    }
  }
}
