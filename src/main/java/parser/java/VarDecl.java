package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.verboseLog;
import static main.Main.verbose;

public class VarDecl extends PascalDecl {
  String name;
  Type parserType;

  public VarDecl(String id, int linenum) {
    super(id, linenum);
  }

  String view() {
    String s = identify() + ", offset: " + declOffset + " block level: " + declLevel + "\n";
    return s;
  }

  // to calculate when entering blocks
  int byteSize() {
    int size = 0;
    if (parserType instanceof ArrayType) {
      ArrayType at = (ArrayType) parserType;
      size = at.size();
    }

    else size = 4;
    return size;
  }

  // used for testing, inspecting a block
  void verbosePrintContent() {
    verbose(identify() + ": " + name);
  }

  @Override
  void genCode(CodeFile f) {}

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    //not array
    if (parserType instanceof TypeName) {
      TypeName tn = (TypeName) parserType;
      tn.check(curScope, lib);
      typeRef = (TypeDecl) curScope.findDecl(tn.name, this);
      type = typeRef.type;
    }

    else if (parserType instanceof ArrayType) {
      ArrayType at = (ArrayType) parserType;
      at.check(curScope, lib);
      type = at.type;
    }

    curScope.addDecl(name, this);

    leaveVerboseBinder();
  }

  static VarDecl parse(Scanner s) {
    enterParser("var decl");

    VarDecl vd = new VarDecl(s.curToken.id, s.curLineNum());
    vd.name = s.curToken.id; s.readNextToken();
    s.skip(colonToken);
    vd.parserType = Type.parse(s);
    s.skip(semicolonToken);

    leaveParser("var decl");
    return vd;
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

    return "<var decl> " + name +  suffix;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name + ": "); parserType.prettyPrint();
    Main.log.prettyPrintLn(";");
  }

  @Override
  public void checkWhetherAssignable(PascalSyntax where) {

  }

}
