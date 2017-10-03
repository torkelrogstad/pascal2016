package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.verbose;
import static main.Main.verboseLog;

public class ConstDecl extends PascalDecl {
  Constant c;


  public ConstDecl(String id, int linenum) {
    super(id, linenum);
  }

  @Override
  void genCode(CodeFile f){

    verbose(identify());
    c.unsignedConstant.genCode(f);
  }

  // used for testing, inspecting a block
  void verbosePrintContent() {
    verbose(identify() + ": " + name + " " + c.constVal);
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    c.check(curScope, lib);
    type = c.type;
    curScope.addDecl(name, this);

    leaveVerboseBinder();
  }

  static ConstDecl parse(Scanner s) {
    enterParser("const decl");

    ConstDecl cd = new ConstDecl(s.curToken.id, s.curLineNum());
    cd.name = s.curToken.id; s.readNextToken();
    s.skip(equalToken);
    cd.c = Constant.parse(s);
    s.skip(semicolonToken);

    leaveParser("const decl");
    return cd;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name + " = "); c.prettyPrint();
    Main.log.prettyPrintLn(";");
  }

  @Override
  public String identify() {
    String suffix = "";
    if (isInLibrary()) {
      suffix = " in the library";
    }

    else {
      suffix = " on line " + lineNum;
    }

    return "<const decl> " + name +  suffix;
  }
}
