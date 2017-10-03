package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;
import static main.Main.verboseLog;
import static main.Main.verboseLog;

public class ConstDeclPart extends PascalSyntax {
  ArrayList<ConstDecl> constDecls = new ArrayList<>();

  public ConstDeclPart(int linenum) {
    super(linenum);
  }

  // used when entering a block
  // arrays can't be constants, right?
  // ut i fra jernbanediagrammet for array-type ser det ut til at de kan være konstanter
  int declByteSize() {
    int size = 0;

    for (ConstDecl decl : constDecls) {
      size += 4;
    }

    return size;
  }

  @Override
  void genCode(CodeFile f){}

  // used for testing, inspecting a block
  void verbosePrintContent() {
    for (ConstDecl cd : constDecls) {
      cd.verbosePrintContent();
    }
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    for (ConstDecl constDecl : constDecls) {
      constDecl.check(curScope, lib);
    }

    leaveVerboseBinder();
  }

  static ConstDeclPart parse(Scanner s) {
    enterParser("const decl part");

    ConstDeclPart constDeclPart = new ConstDeclPart(s.curLineNum());
    s.skip(constToken);

    while(s.curToken.kind != varToken) {
      ConstDecl temp = ConstDecl.parse(s);
      constDeclPart.constDecls.add(temp);
    }

    leaveParser("const decl part");
    return constDeclPart;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrintLn("const"); Main.log.prettyIndent();
    for (ConstDecl cd : constDecls) {
      cd.prettyPrint();
    }
    Main.log.prettyOutdent();
  }

  public String identify() {
    return "<const decl part> on line " + lineNum;
  }
}
