// Note that this class is taken in its entirety from lecture slides
// Other classes are inspired by this structure.

package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.verbose;

abstract class Statement extends PascalSyntax {
  Statement (int lnum) {
    super(lnum);
  }

  void verbosePrintContent() {
    verbose(identify());
  }

  static Statement parse(Scanner s) {
    enterParser("statement");

    Statement statement = null;
    switch (s.curToken.kind) {
      case beginToken:
      statement = CompoundStatm.parse(s); break;

      case ifToken:
      statement = IfStatm.parse(s); break;

      case nameToken:
      switch (s.nextToken.kind) {
        case assignToken:
        //statement = AssignStatm.parse(s); break; //implied (why?)

        case leftBracketToken:
        statement = AssignStatm.parse(s); break;

        default:
        statement = ProcCall.parse(s); break;

      } break;

      case whileToken:
      statement = WhileStatm.parse(s); break;

      default:
      statement = EmptyStatm.parse(s); break;
    }

    leaveParser("statement");
    return statement;
  }
}
