package types;

public class BoolType extends Type {
  @Override public String identify() {
    return "Boolean";
  }

  @Override public int size() {
    return 4;
  }
}
