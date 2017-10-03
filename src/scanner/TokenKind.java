package scanner;

// Note that tokens found in standard Pascal but not in Pascal2016
// have been commented out.

public enum TokenKind {
  nameToken("name"),
  intValToken("number"),
  charValToken("char"),

  addToken("+"),
  assignToken(":="),
  colonToken(":"),
  commaToken(","),
  /* divideToken("/"), */
  dotToken("."),
  equalToken("="),
  greaterToken(">"),
  greaterEqualToken(">="),
  leftBracketToken("["),
  leftParToken("("),
  lessToken("<"),
  lessEqualToken("<="),
  multiplyToken("*"),
  notEqualToken("<>"),
  rangeToken(".."),
  rightBracketToken("]"),
  rightParToken(")"),
  semicolonToken(";"),
  subtractToken("-"),
  /* upArrowToken("^"), */

  andToken("and"),
  arrayToken("array"),
  beginToken("begin"),
  /* caseToken("case"), */
  constToken("const"),
  divToken("div"),
  doToken("do"),
  /* downtoToken("downto"), */
  elseToken("else"),
  endToken("end"),
  /* fileToken("file"), */
  /* forToken("for"), */
  functionToken("function"),
  /* gotoToken("goto"), */
  ifToken("if"),
  /* inToken("in"), */
  /* labelToken("label"), */
  modToken("mod"),
  /* nilToken("nil"), */
  notToken("not"),
  ofToken("of"),
  orToken("or"),
  /* packedToken("packed"), */
  procedureToken("procedure"),
  programToken("program"),
  /* recordToken("record"), */
  /* repeatToken("repeat"), */
  /* setToken("set"), */
  thenToken("then"),
  /* toToken("to"), */
  /* typeToken("type"), */
  /* untilToken("until"), */
  varToken("var"),
  whileToken("while"),
  /* withToken("with"), */

  eofToken("e-o-f");

  private String image;

  TokenKind(String im) {
    image = im;
  }


  public String identify() {
    return image + " token";
  }

  @Override public String toString() {
    return image;
  }


  public boolean isFactorOpr() {
    return this==multiplyToken || this==divToken ||
    this==modToken || this==andToken;
  }

  public boolean isPrefixOpr() {
    return this==addToken || this==subtractToken;
  }

  public boolean isRelOpr() {
    return this==equalToken || this==notEqualToken ||
    this==lessToken || this==lessEqualToken ||
    this==greaterToken || this==greaterEqualToken;
  }

  public boolean isTermOpr() {
    return isPrefixOpr() || this==orToken;
  }
}
