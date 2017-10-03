package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;
import java.util.HashMap;
import static main.Main.verboseBlock;
import static main.Main.verbose;

class Block extends PascalSyntax {
  ConstDeclPart constDeclPart = null;
  VarDeclPart varDeclPart = null;
  ArrayList<ProcDecl> procDecls = null;
  StatmList statementList;
  Block outerScope;
  int declLevel;
  HashMap<String, PascalDecl> decls = new HashMap<>();


  Block(int lineNum) {
    super(lineNum);
  }

  void setDeclLevel(int outerLevel) {
    declLevel = outerLevel;

    if (varDeclPart != null) {
      for (VarDecl vd : Main.safe(varDeclPart.varDecls)) {
        vd.declLevel = declLevel;
      }

    }

    for (ProcDecl pd : Main.safe(procDecls)) {
      pd.setDeclLevel(declLevel);
    }
  }

  String view() {
    String s =  (identify() + " at block level " + declLevel) + "\n";

    if (varDeclPart != null) {
      s += varDeclPart.view();
    }

    if (procDecls != null) {
      for (ProcDecl pd : procDecls) {
         s += pd.view();
      }
    }

    return s;
  }

  void genCode(CodeFile file) {
    if (procDecls != null) {
      for (ProcDecl decl : procDecls) {
        decl.genCode(file);
      }
    }

    statementList.genCode(file);
  }

  // only fires if verbose flag is on
  // used for testing, inspecting a block
  void verbosePrintContent() {
    if (constDeclPart != null) {
      constDeclPart.verbosePrintContent();
    }

    if (varDeclPart != null) {
      varDeclPart.verbosePrintContent();
    }

    if (procDecls != null) {
      for (ProcDecl pd : procDecls) {
        pd.verbosePrintContent();
      }
    }

    statementList.verbosePrintContent();
  }

  // method used to determine how much space must be put aside when entering a
  // new block
  int declByteSize() {
    int size = 0;

    if (varDeclPart != null) {
      size += varDeclPart.declByteSize();
    }

    return size;
  }

  @Override
  public void check(Block curScope, Library lib) {
    enterVerboseBinder();
    outerScope = curScope;

    if (constDeclPart != null) {
      constDeclPart.check(this, lib);
    }

    if (varDeclPart != null) {
      varDeclPart.check(this, lib);
    }

    for (ProcDecl pd : Main.safe(procDecls)) {
      pd.check(this, lib);
    }

    statementList.check(this, lib);

    // TODO: fix this
    // blockLevel();

    // sets declLevel, for use in codegenerating
    for (PascalDecl decl: decls.values()) {
      decl.declLevel = declLevel;
    }

    if (outerScope != null) {
      declLevel = outerScope.declLevel + 1;
    }

    leaveVerboseBinder();
  }

  PascalDecl findDecl(String id, PascalSyntax where) {
    // verbose("Looking for decl " + id + " in " + this.identify());
    // verbose("Outermost scope is " + outerMostBlock());

    PascalDecl decl = decls.get(id);
    if (decl != null) {
      Main.log.noteBinding(id, where, decl);
      // verbose("Found decl in " + this.identify() + "!");
      return decl;
    }

    if (outerScope != null) {
      return outerScope.findDecl(id, where);
    }

    where.error("Name " + id + " is unknown!");
    return null;
  }

  void addDecl(String id, PascalDecl decl) {
    if (decls.containsKey(decl)) {
      decl.error(id + " declared twice in the same block!");
    }

    Main.log.noteDeclaration(id, decl, decl);
    decls.put(id, decl);
  }

  static Block parse(Scanner s) {
    enterParser("block");

    Block b = new Block(s.curLineNum());

    if (s.curToken.kind == constToken) {
      b.constDeclPart = ConstDeclPart.parse(s);
    }

    if (s.curToken.kind == varToken) {
      b.varDeclPart = VarDeclPart.parse(s);
    }

    if (s.curToken.kind == functionToken || s.curToken.kind == procedureToken) {
      b.procDecls = new ArrayList<>();

      while(s.curToken.kind == functionToken || s.curToken.kind == procedureToken) {
        b.procDecls.add(ProcDecl.parse(s));

        verboseBlock(s);
      }
    }

    s.skip(beginToken);

    b.statementList = StatmList.parse(s);
    s.skip(endToken);

    leaveParser("block");
    return b;
  }

  @Override
  public String identify() {
    String suffix;

    if (lineNum < 0) {
      suffix = " in the library";
    }
    else {
      suffix = " on line " + lineNum;
    }

    return "<block> " + suffix;
  }

  @Override
  public void prettyPrint() {
    if (constDeclPart != null) constDeclPart.prettyPrint();
    if (varDeclPart != null) varDeclPart.prettyPrint();
    if (varDeclPart != null || constDeclPart != null) Main.log.prettyPrintLn();

    for (ProcDecl pd : Main.safe(procDecls)) {
      pd.prettyPrint();
    }

    Main.log.prettyPrintLn("begin"); Main.log.prettyIndent();
    statementList.prettyPrint();
    Main.log.prettyOutdent(); Main.log.prettyPrint("end");

  }
}
