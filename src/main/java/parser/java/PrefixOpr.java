package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.*;

public class PrefixOpr extends Operator {
  String name;

  public PrefixOpr(int linenum) {
    super(linenum);
  }

  @Override
  void check(Block block, Library lib) {
    enterVerboseBinder();

    leaveVerboseBinder();
  }

  static PrefixOpr parse(Scanner s) {
    enterParser("prefix opr");

    PrefixOpr po = new PrefixOpr(s.curLineNum());
    verbose("\n" + po.identify());
    verboseBlock(s);

    po.name = s.curToken.kind.toString();
    po.opr = s.curToken; s.readNextToken();

    leaveParser("prefix opr");

    return po;
  }

  @Override
  public String identify() {
    return "<prefix opr> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint(name);
  }
}
