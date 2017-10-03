package types;

import main.*;
import parser.PascalSyntax;

public abstract class Type {
  public abstract String identify();

  //tx = expected type, op = description of how to use type
  //where = where in the program the type is used
  //message = the message to be sent if there is a type error
  public void checkType(Type tx, String op, PascalSyntax where, String message) {
    Main.log.noteTypeCheck(this, op, tx, where);

    if (!this.isSameType(tx)) {
      where.error(message);
    }
  }

  private boolean isSameType(Type t) {
    return t.identify().equals(this.identify());
  }

  public abstract int size();
}
