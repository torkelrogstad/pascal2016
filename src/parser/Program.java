package parser;

import main.*;
import static main.Main.verbose;
import static main.Main.verboseLog;
import scanner.*;
import static scanner.TokenKind.*;

/* <program> ::= <name> ';' <block> '.' */

public class Program extends PascalDecl {
  Block block;
  String name;

  Program(String id, int lineNum) {
    super(id, lineNum);
  }

  // for development use in part 4
  // views a block's level, as well as offset for all params/var in the block
  public void viewBlocks() {
    System.out.println("\n" + block.view());
  }

  // taken (mostly) from the compendium
  @Override
  public void genCode(CodeFile file) {
    String progLabel = file.getLabel(name);
    file.genInstr("", ".globl", "main", "");

    file.genInstr("main", "", "", "");
    file.genInstr("", "call", "prog$" + progLabel, "Start program");
    file.genInstr("", "movl", "$0,%eax", "Set status 0 and");
    file.genInstr("", "ret", "", "terminate the program");

    int size = block.declByteSize();

    if (block.procDecls != null) {
      for (ProcDecl pd : block.procDecls) {
        pd.genCode(file);
      }
    }

    file.genInstr("prog$" + progLabel, "", "", "");

    verbose("size of block " + block.identify() + ": " + size);
    block.verbosePrintContent();

    file.genInstr("", "enter", "$" + (size + 32) + ",$1", "Start of " + name);

    block.statementList.genCode(file);

    file.genInstr("", "leave", "", "End of " + name);
    file.genInstr("", "ret", "", "");
  }

  @Override
  public void check(Block curScope, Library lib) {
    enterVerboseBinder();

    declLevel = 1;

    block.check(curScope, lib);

    block.setDeclLevel(declLevel);

    leaveVerboseBinder();
  }

  @Override
  public String identify() {
    return "<program> on line " + lineNum;
  }
  @Override
  public void prettyPrint() {
    Main.log.prettyPrintLn("program " + name + ";");
    block.prettyPrint(); Main.log.prettyPrintLn(".");
  }

  public static Program parse(Scanner s) {
    enterParser("program");

    Program p = new Program(s.curToken.id, s.curLineNum());

    s.skip(programToken);
    p.name = s.curToken.id; s.readNextToken();
    s.skip(semicolonToken);
    p.block = Block.parse(s);
    s.skip(dotToken);

    leaveParser("program");
    return p;
  }
}
