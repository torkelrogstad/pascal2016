package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class TypeName extends Type {
  String name;

  public TypeName(int linenum) {
    super(linenum);
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    if (name.equals("integer")) {
      type = lib.intType;
    }

    else if (name.equals("boolean")) {
      type = lib.boolType;
    }

    else if (name.equals("false")) {
      type = lib.boolType;
    }

    else if (name.equals("true")) {
      type = lib.boolType;
    }

    else if (name.equals("char")) {
      type = lib.charType;
    }


    leaveVerboseBinder();
  }

  static TypeName parse(Scanner s) {
    enterParser("type name");

    TypeName tn = new TypeName(s.curLineNum());
    tn.name = s.curToken.id; s.readNextToken();

    leaveParser("type name");
    return tn;
  }

  @Override
  public String identify() {
    return "<type name> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name);
  }
}
