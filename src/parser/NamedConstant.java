package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class NamedConstant extends UnsignedConstant {
  String name;
  PascalDecl decl;

  public NamedConstant(int linenum) {
    super(linenum);
  }

  @Override
  void genCode(CodeFile f){
    decl.genCode(f);
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    decl = curScope.findDecl(name, this);
    ConstDecl cd = (ConstDecl) decl;
    type = decl.type;
    constVal = cd.c.constVal;

    leaveVerboseBinder();
  }

  static NamedConstant parse(Scanner s) {
    enterParser("named constant");

    NamedConstant namedConstant = new NamedConstant(s.curLineNum());
    namedConstant.name = s.curToken.id; s.readNextToken();


    leaveParser("named constant");

    return namedConstant;
  }

  public String identify() {
    return "<named constant> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name);
  }
}
