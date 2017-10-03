package types;

public class IntType extends Type {

  @Override public String identify() {
    return "Integer";
  }

  @Override public int size() {
    return 4;
  }
}
