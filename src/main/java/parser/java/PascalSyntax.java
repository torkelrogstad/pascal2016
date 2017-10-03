package parser;

import main.*;

public abstract class PascalSyntax {
  String className = this.getClass().getSimpleName();

  public int lineNum;

  PascalSyntax(int n) {
    lineNum = n;
  }

  boolean isInLibrary() {
    return lineNum < 0;
  }

  // Del 3:
  abstract void check(Block curScope, Library lib);

  // Del 4:
  //default implementation, so that project will compile for now
  void genCode(CodeFile f) {}

  abstract public String identify();

  abstract void prettyPrint();

  public void error(String message) {
    Main.error("Error at line " + lineNum + ": " + message);
  }

  static void enterParser(String nonTerm) {
    Main.log.enterParser(nonTerm);
  }

  static void leaveParser(String nonTerm) {
    Main.log.leaveParser(nonTerm);
  }

  void enterVerboseBinder() {
    Main.log.enterVerboseBinder(className);
  }

  void leaveVerboseBinder() {
    Main.log.leaveVerboseBinder(className);
  }
}
