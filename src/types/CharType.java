package types;

public class CharType extends Type {

  @Override public String identify() {
    return "Char";
  }

  @Override public int size() {
    return 4;
  }
}
