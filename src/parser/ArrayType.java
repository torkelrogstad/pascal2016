package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class ArrayType extends Type {
  Constant constant1;
  Constant constant2;
  Type parserType;
  int low; int high; int diff;

  public ArrayType(int linenum) {
    super(linenum);
  }

  @Override
  void check(Block curScope, Library lib) {
    enterVerboseBinder();

    constant1.check(curScope, lib);
    constant2.check(curScope, lib);
    parserType.check(curScope, lib);

    type = parserType.type;

    diff = constant1.constVal - constant2.constVal;
    high = Math.max(constant1.constVal, constant2.constVal);
    low = Math.min(constant1.constVal, constant2.constVal);

    constant1.type.checkType(constant2.type, "array limits", this, "Array limits must be of same type.");



    leaveVerboseBinder();
  }

  static ArrayType parse(Scanner s) {
    enterParser("array-type");

    ArrayType arrayType = new ArrayType(s.curLineNum());
    s.skip(arrayToken);
    s.skip(leftBracketToken);

    arrayType.constant1 = Constant.parse(s);
    s.skip(rangeToken);
    arrayType.constant2 = Constant.parse(s);

    s.skip(rightBracketToken);
    s.skip(ofToken);
    arrayType.parserType = Type.parse(s);

    leaveParser("array-type");
    return arrayType;
  }

  @Override
  public String identify() {
    String s = this.getClass().getName();
    return "<" + s + "> on line " + lineNum;
  }

  @Override
  public void prettyPrint() {
    Main.log.prettyPrint("array ["); constant1.prettyPrint();
    Main.log.prettyPrint(".."); constant2.prettyPrint();
    Main.log.prettyPrint("] of "); parserType.prettyPrint();
  }

  int size() {
    return (high-low+1)*parserType.type.size();
  }

}
