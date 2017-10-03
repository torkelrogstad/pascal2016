package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import static main.Main.verbose;

public class Library extends Block {
  TypeDecl bool;
  TypeDecl character;
  TypeDecl integer;

  types.Type boolType;
  types.Type charType;
  types.Type intType;

  ProcDecl write;

  ConstDecl eolDecl;
  ConstDecl trueDecl;
  ConstDecl falseDecl;

  Block outerScope = null;

  public void genCode(CodeFile file) {

  }

  public Library(int lineNum) {
    super(lineNum);

    boolType = new types.BoolType();
    charType = new types.CharType();
    intType = new types.IntType();

    bool = new TypeDecl("boolean", -1);
    bool.type = boolType;

    character = new TypeDecl("char", -1);
    character.type = charType;

    integer = new TypeDecl("integer", -1);
    integer.type = intType;

    eolDecl = new ConstDecl("eol", -1);
    eolDecl.type = charType;
    eolDecl.c = new Constant(-1);
    eolDecl.c.unsignedConstant = new CharLiteral((char) 10, -1);

    trueDecl = new ConstDecl("true", -1);
    trueDecl.type = boolType;
    trueDecl.c = new Constant(-1);
    trueDecl.c.unsignedConstant = new NumberLiteral(-1);
    trueDecl.c.unsignedConstant.constVal = 1;
    NumberLiteral nl = (NumberLiteral) trueDecl.c.unsignedConstant;
    nl.val = 1;

    falseDecl = new ConstDecl("false", -1);
    falseDecl.type = boolType;
    falseDecl.c = new Constant(-1);
    falseDecl.c.unsignedConstant = new NumberLiteral(-1);
    falseDecl.c.unsignedConstant.constVal = 0;

    write = new ProcDecl("write", -1);

    addDecl("eol", eolDecl);
    addDecl("true", trueDecl);
    addDecl("false", falseDecl);

    addDecl("boolean", bool);
    addDecl("integer", integer);
    addDecl("char", character);

    addDecl("write", write);

    declLevel = 0;
  }

  @Override
  public String identify() {
    return "<library>";
  }
}
