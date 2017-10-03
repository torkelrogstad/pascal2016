package types;

import parser.PascalSyntax;

public class ArrayType extends Type {
  public Type elemType, indexType;
  public int lowLimit, highLimit;

  public ArrayType(Type element, Type index, int low, int high) {
    elemType = element;
    indexType = index;  lowLimit = low;  highLimit = high;
  }

  @Override public String identify() {
    return "type array [" + lowLimit + ".." + highLimit + ": " +
    indexType.identify() + "] of " + elemType.identify();
  }

  @Override public void checkType(Type tx, String op,
  PascalSyntax where, String message) {
    if (tx instanceof ArrayType) {
      ArrayType txa = (ArrayType)tx;
      indexType.checkType(txa.indexType, "array index", where, message);
      elemType.checkType(txa.elemType, op, where, message);
    } else {
      where.error(message);
    }
  }


  @Override public int size() {
    return (highLimit-lowLimit+1)*elemType.size();
  }
}
