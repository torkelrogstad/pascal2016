package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ParamDecl extends PascalDecl {
  static int i = 0;
  String name;
  TypeName typeName;

  public ParamDecl(String id, int linenum) {
    super(id, linenum);
  }

  String view() {
    return ("    " + identify() + " " + name + ", offset: " + declOffset + ", declLevel: " + declLevel + "\n");
  }

  @Override
  void genCode(CodeFile f){}

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    typeName.check(curScope, lib);

    type = typeName.type;

    curScope.addDecl(name, this);

    leaveVerboseBinder();
  }

  static ParamDecl parse(Scanner s) {
    enterParser("param decl");

    ParamDecl pd = new ParamDecl(s.curToken.id, s.curLineNum());

    pd.name = s.curToken.id; s.readNextToken();
    s.skip(colonToken);
    pd.typeName = TypeName.parse(s);

    leaveParser("param decl");
    return pd;
  }

  public String identify() {
    return "<param decl> " + name + " on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name + ": "); typeName.prettyPrint();
  }
}
