package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class FuncDecl extends ProcDecl {
  // ParamDeclList paramDeclList = null;
  TypeName typeName;

  public FuncDecl(String id, int linenum) {
    super(id, linenum);
  }

  @Override
  void genCode(CodeFile file) {
    if (block.procDecls != null) {
      for (ProcDecl pd : block.procDecls) {
        pd.genCode(file);
      }
    }

    label = file.getLabel("func$" + name);
    file.genInstr(label, "", "", "");

    String arg = "$" + (32 + block.declByteSize()) + ",$" + declLevel;
    file.genInstr("", "enter", arg, "Start of " + name);

    block.statementList.genCode(file);

    file.genInstr("", "movl", "-32(%ebp),%eax", "Fetch return value");
    file.genInstr("", "leave", "", "End of " + name);
    file.genInstr("", "ret", "", "");
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    curScope.addDecl(name, this);

    if (paramDeclList != null) {
      paramDeclList.check(curScope, lib);
    }

    typeName.check(curScope, lib);
    type = typeName.type;

    block.check(curScope, lib);

    leaveVerboseBinder();
  }

  static FuncDecl parse(Scanner s) {
    enterParser("func decl");

    FuncDecl fd = new FuncDecl(s.curToken.id, s.curLineNum());
    s.skip(functionToken);
    fd.name = s.curToken.id; s.readNextToken();

    if (s.curToken.kind != colonToken) {
      fd.paramDeclList = ParamDeclList.parse(s);
    }

    s.skip(colonToken);
    fd.typeName = TypeName.parse(s);
    s.skip(semicolonToken);
    fd.block = Block.parse(s);
    s.skip(semicolonToken);

    leaveParser("func decl");
    return fd;
  }

  @Override
  public String identify() {
    return "<func decl> " + name + " on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint("function " + name);
    if (paramDeclList != null) paramDeclList.prettyPrint();
    Main.log.prettyPrint(": "); typeName.prettyPrint();
    Main.log.prettyPrintLn(";");
    Main.log.prettyIndent();
    block.prettyPrint(); Main.log.prettyOutdent();
    Main.log.prettyPrintLn("; {" + name + "}\n");
  }

  @Override
  void checkWhetherAssignable(PascalSyntax where) {}
}
