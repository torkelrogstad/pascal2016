package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class VarDeclPart extends PascalSyntax {
  ArrayList<VarDecl> varDecls = new ArrayList<>();

  public VarDeclPart(int linenum) {
    super(linenum);
  }

  String view() {
    String s = "";
    for (VarDecl vd : varDecls) {
      s += vd.view();
    }

    return s;
  }

  // used when entering a new block
  int declByteSize() {
    int size = 0;

    for (VarDecl decl : varDecls) {
      size += decl.byteSize();
    }

    return size;
  }

  @Override
  void genCode(CodeFile f) {

  }

  // used for testing, inspecting a block
  void verbosePrintContent() {
    for (VarDecl vd : varDecls) {
      vd.verbosePrintContent();
    }
  }

  @Override
  void check(Block block, Library lib) {
    enterVerboseBinder();

    for (VarDecl decl : varDecls) {
      decl.check(block, lib);
    }

    // for use in genCode()
    int offset = -32;
    for(VarDecl decl : varDecls){
      offset -= decl.byteSize();
      decl.declOffset = offset;
    }

    leaveVerboseBinder();
  }

  static VarDeclPart parse(Scanner s) {
    enterParser("var decl part");

    VarDeclPart varDeclPart = new VarDeclPart(s.curLineNum());
    s.skip(varToken);

    while(s.curToken.kind == nameToken) {
      VarDecl temp = VarDecl.parse(s);
      varDeclPart.varDecls.add(temp);
    }

    leaveParser("var decl part");
    return varDeclPart;
  }

  @Override
  public String identify() {
    return "<var decl part> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrintLn("var"); Main.log.prettyIndent();
    for (VarDecl vd : varDecls) {
      vd.prettyPrint();
    }
    Main.log.prettyOutdent();
  }
}
