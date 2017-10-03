package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;
import static main.Main.verbose;

public class ParamDeclList extends PascalSyntax {
  ArrayList<ParamDecl> paramDecls = new ArrayList<>();

  public ParamDeclList(int linenum) {
    super(linenum);
  }

  String view() {
    String s = "";
    for (ParamDecl pd : paramDecls) {
      s += pd.view();
    }

    return s;
  }

  void setDeclLevel(int declLevel) {
    for (ParamDecl pd : paramDecls) {
      pd.declLevel = declLevel;
    }
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    for (ParamDecl pd : paramDecls) {
      pd.check(curScope, lib);
    }

    // traversing the list, calculating the offset
    int offset = 8;
    for (ParamDecl pd : paramDecls) {
      pd.declOffset = offset;
      offset += 4;
    }

    leaveVerboseBinder();
  }

  static ParamDeclList parse(Scanner s) {
    enterParser("param decl list");

    ParamDeclList paramDeclList = new ParamDeclList(s.curLineNum());
    s.skip(leftParToken);
    paramDeclList.paramDecls.add(ParamDecl.parse(s));

    while(s.curToken.kind != rightParToken) {
      s.skip(semicolonToken);
      paramDeclList.paramDecls.add(ParamDecl.parse(s));
    }

    s.skip(rightParToken);

    leaveParser("param decl list");
    return paramDeclList;
  }

  public String identify() {
    return "<param decl list> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(" (");

    paramDecls.get(0).prettyPrint();
    for(int i = 1; i < paramDecls.size(); i++) {
      Main.log.prettyPrint("; "); paramDecls.get(i).prettyPrint();
    }
    Main.log.prettyPrint(")");
  }
}
