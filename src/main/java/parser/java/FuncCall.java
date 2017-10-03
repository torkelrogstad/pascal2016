package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;
import static main.Main.verboseBlock;
import static main.Main.verbose;

public class FuncCall extends Factor {
  String name;
  ArrayList<Expression> params = null;

  public FuncCall(int lineNum) {
    super(lineNum);
  }

  @Override
  void genCode(CodeFile file) {
    int bytes = 0;

    // litt mindre komplisert kode. Starter øverst og itererer nedover
    for(int i = params.size()-1; i>= 0; i--){
      params.get(i).genCode(file);
      file.genInstr("", "pushl", "%eax", "Push param #" + (i + 1));
      bytes += 4;
    }

    file.genInstr("", "call", decl.label, "");
    file.genInstr("", "addl", "$" + bytes + ",%esp", "Pop parameters");
  }

  PascalDecl decl;
  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    decl = curScope.findDecl(name, this);

    FuncDecl funcRef = (FuncDecl) decl;

    if (params != null) {
      ArrayList<ParamDecl> formalParams = funcRef.paramDeclList.paramDecls;

      for (int i = 0; i < params.size(); i++) {

        if (formalParams.size() < params.size()) {
          this.error("Too many parameters in call on " + name + "!");
        }

        else if (formalParams.size() > params.size()) {
          this.error("Too few parameters in call on " + name + "!");
        }

        params.get(i).check(curScope, lib);

        types.Type actParamType = params.get(i).type;
        types.Type formParamType = formalParams.get(i).type;

        actParamType.checkType(formParamType, "param #"+(i+1), this, "Illegal type of parameter #" + (i+1));
      }
    }

    type = decl.type;

    leaveVerboseBinder();
  }

  static FuncCall parse(Scanner s) {
    enterParser("func call");

    FuncCall funcCall = new FuncCall(s.curLineNum());
    funcCall.name = s.curToken.id; s.readNextToken();

    if (s.curToken.kind == leftParToken) {
      s.skip(leftParToken);
      funcCall.params = new ArrayList<>();
      funcCall.params.add(Expression.parse(s));

      verboseBlock(s);
      while (s.curToken.kind == commaToken) {
        s.skip(commaToken);
        funcCall.params.add(Expression.parse(s));
      }

      s.skip(rightParToken);
    }


    leaveParser("func call");
    return funcCall;
  }

  public String identify() {
    return "<func call> on line " + lineNum;
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
