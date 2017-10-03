package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class TypeDecl extends PascalDecl {
  public TypeDecl(String id, int linenum) {
    super(id, linenum);
  }

  @Override
  void check(Block block, Library lib) {
    enterVerboseBinder();

    leaveVerboseBinder();
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

    return "<type decl> " + name +  suffix;
  }
}
