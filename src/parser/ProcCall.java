package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;
import static main.Main.verboseBlock;

public class ProcCall extends Statement {
  ArrayList<Expression> params = null;
  String name;


  public ProcCall(int linenum) {
    super(linenum);
  }

  @Override
  void genCode(CodeFile file) {

    // write procedure for printing, not redefined
    if (procRef.isInLibrary() && procRef.name.equals("write")) {
      for (Expression param : Main.safe(params)) {

        param.genCode(file);
        file.genInstr("", "pushl", "%eax", "Push next param.");
        String t = "";

        if (param.type instanceof types.IntType) {
          t = "int";
        }

        else if (param.type instanceof types.CharType) {
          t = "char";
        }

        else if (param.type instanceof types.BoolType) {
          t = "bool";
        }

        file.genInstr("", "call", "write_" + t, "");
        file.genInstr("", "addl", "$4,%esp", "Pop param.");
      }
    }

    else {
      int bytes = 0;
      if (params != null) {
        for(int i = params.size()-1; i>= 0; i--){
          params.get(i).genCode(file);
          file.genInstr("", "pushl", "%eax", "Push param #" + (i + 1) + ".");
          bytes += 4;
        }
      }
      file.genInstr("", "call", procRef.label, "");

      if (params != null) {
        file.genInstr("", "addl", "$" + bytes + ",%esp", "Pop params.");
      }
    }
  }

  ProcDecl procRef;

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    PascalDecl decl = curScope.findDecl(name, this);

    if (decl instanceof FuncDecl) {
      this.error(name + " is a function, not a procedure.");
    }

    procRef = (ProcDecl) decl;

    if (procRef == lib.write) {
      for (Expression param : params) {
        param.check(curScope, lib);
      }
    }

    else if (params != null) {
      ArrayList<ParamDecl> formalParams = procRef.paramDeclList.paramDecls;

      if (formalParams.size() < params.size()) {
        this.error("Too many parameters in call on " + name + "!");
      }

      else if (formalParams.size() > params.size()) {
        this.error("Too few parameters in call on " + name + "!");
      }

      for (int i = 0; i < params.size(); i++) {
        params.get(i).check(curScope, lib);

        types.Type actParamType = params.get(i).type;
        types.Type formParamType = formalParams.get(i).type;

        actParamType.checkType(formParamType, "param #"+(i+1), this, "Illegal type of parameter #" + (i+1) + "!");
      }
    }

    leaveVerboseBinder();
  }

  static ProcCall parse(Scanner s) {
    enterParser("proc call");

    ProcCall procCall = new ProcCall(s.curLineNum());
    procCall.name = s.curToken.id; s.readNextToken();

    if (s.curToken.kind == leftParToken) {
      s.skip(leftParToken);
      procCall.params = new ArrayList<>();
      procCall.params.add(Expression.parse(s));

      verboseBlock(s);

      while(s.curToken.kind == commaToken) {
        s.skip(commaToken);
        procCall.params.add(Expression.parse(s));
      }

      s.skip(rightParToken);
    }

    leaveParser("proc call");
    return procCall;
  }

  @Override
  public String identify() {
    return "<proc call> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name);

    if (params!= null) {
      Main.log.prettyPrint("("); params.get(0).prettyPrint();
      for (int i = 1; i < params.size(); i++) {
        Main.log.prettyPrint(", "); params.get(i).prettyPrint();
      }
      Main.log.prettyPrint(")");
    }
  }
}
