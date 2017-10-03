package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.*;
import static main.Main.verbose;

public class ProcDecl extends PascalDecl {
  ParamDeclList paramDeclList = null;
  TypeName typeName = null;
  Block block;

  public ProcDecl(String id, int linenum) {
    super(id, linenum);
  }

  void setDeclLevel(int outerLevel) {
    declLevel = outerLevel + 1;
    if (paramDeclList != null) {
      paramDeclList.setDeclLevel(declLevel);
    }
    block.setDeclLevel(declLevel);
  }

  // used for testing, inspecting a block
  void verbosePrintContent() {
    verbose(identify() + ": " + name);
  }

  String view() {
    String s = (identify() + " " + name + ", declLevel: " + declLevel) + "\n";
    if (paramDeclList != null) {
      s += paramDeclList.view();
    }

    s += block.view();

    return s;
  }

  @Override
  void genCode(CodeFile file) {

    if (block.procDecls != null) {
      for (ProcDecl pd : block.procDecls) {
        pd.genCode(file);
      }
    }

    label = file.getLabel("proc$" + name);
    file.genInstr(label, "", "", "");

    String arg = "$" + (32 + block.declByteSize()) + ",$" + (declLevel);
    file.genInstr("", "enter", arg, "Start of " + name);

    block.statementList.genCode(file);

    file.genInstr("", "leave", "", "End of " + name);
    file.genInstr("", "ret", "", "");
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    curScope.addDecl(name, this);

    if (paramDeclList != null) {
      this.block.outerScope = curScope;
      paramDeclList.check(this.block, lib);
    }

    block.check(curScope, lib);

    leaveVerboseBinder();
  }

  static ProcDecl parse(Scanner s) {
    ProcDecl procDecl;

    if (s.curToken.kind == procedureToken) {
      enterParser("proc decl");

      procDecl = new ProcDecl(s.curToken.id, s.curLineNum());
      s.skip(procedureToken);
      procDecl.name = s.curToken.id; s.readNextToken();

      if (s.curToken.kind == leftParToken) {
        procDecl.paramDeclList = ParamDeclList.parse(s);
      }

      s.skip(semicolonToken);
      procDecl.block = Block.parse(s);
      s.skip(semicolonToken);

      leaveParser("proc decl");
    }

    else {
      procDecl = FuncDecl.parse(s);
    }

    return procDecl;
  }

  @Override
  public String identify() {
    if (lineNum < 0) {
      return "<proc decl> " + name + " in the library";
    }

    else {
      return "<proc decl> " + name + " on line " + lineNum;
    }
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint("procedure " + name);
    if (paramDeclList != null) paramDeclList.prettyPrint();
    Main.log.prettyPrintLn(";");
    Main.log.prettyIndent();
    block.prettyPrint(); Main.log.prettyOutdent();
    Main.log.prettyPrintLn("; {" + name + "}\n");
  }
}
