package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;
import static main.Main.*;

public class StatmList extends PascalSyntax {
  ArrayList<Statement> statements = new ArrayList<>();

  public StatmList(int linenum) {
    super(linenum);
  }

  // used for testing, inspecting a block
  void verbosePrintContent() {
    for (Statement s : statements) {
      s.verbosePrintContent();
    }
  }

  @Override
  void genCode(CodeFile f){
    for (Statement st: statements){
      st.genCode(f);
    }
  }

  @Override
  void check(Block block, Library lib) {
    enterVerboseBinder();

    for (Statement st : statements) {
      st.check(block, lib);
    }

    leaveVerboseBinder();
  }

  static StatmList parse(Scanner s) {
    enterParser("statm list");

    StatmList statmList = new StatmList(s.curLineNum());
    statmList.statements.add(Statement.parse(s));

    verbose(statmList.identify());
    verboseBlock(s);

    while(s.curToken.kind == semicolonToken) {
      s.skip(semicolonToken);
      statmList.statements.add(Statement.parse(s));
    }

    leaveParser("statm list");
    return statmList;
  }

  @Override
  public String identify() {
    return "<statm list> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    statements.get(0).prettyPrint();
    for (int i = 1; i < statements.size(); i++) {
      Main.log.prettyPrint(";"); Main.log.prettyPrintLn();
      statements.get(i).prettyPrint();
    }
    Main.log.prettyPrintLn();
  }
}
