package scanner;

import main.Main;
import static scanner.TokenKind.*;
import static main.Main.*;

import java.io.*;

public class Scanner {
  public Token curToken = null, nextToken = null;

  private LineNumberReader sourceFile = null;
  private String sourceFileName, sourceLine = null;
  private int sourcePos = 0;

  public Scanner(String fileName) {
    sourceFileName = fileName;

    try {
      sourceFile = new LineNumberReader(new FileReader(fileName));
    } catch (FileNotFoundException e) {
      Main.error("Cannot read " + fileName + "!");
    }

    //the two tokens beging kept track of are uninitialized at the beginning,
    //therefore it is necessary to read two, not one
    readNextToken(); readNextToken();
  }


  public String identify() {
    return "Scanner reading " + sourceFileName;
  }


  public int curLineNum() {
    return curToken.lineNum;
  }


  private void error(String message) {
    Main.error("Scanner error" +
    (getFileLineNum()>0 ? " on line "+getFileLineNum() : "") +
    ": " + message);
  }


  public void readNextToken() {
    curToken = nextToken;  //nextToken = null;

    // Del 1 her:

    // beginning of scanning, no line read
    if (sourceLine == null && sourceFile != null) {
      readNextLine();
    }

    //come to end of file
    else if (sourceLine.equals("") && sourceFile == null) {
      nextToken = new Token(TokenKind.eofToken, getFileLineNum());
      Main.log.noteToken(nextToken);
      return;
    }

    // as long as the Reader hasn't reached the end of the
    // file and sourceLine is empty (save for the extra space)
    // this block skips empty lines
    while (sourceLine.equals(" ") && sourceFile != null) {
      readNextLine();
    }

    char c = sourceLine.charAt(sourcePos);

    //comment start
    if (c == '/') {
      sourcePos++;

      //line comment, skip line and read next
      if (sourceLine.charAt(sourcePos) == '/') {
        readNextLine();
        readNextToken();
        return;
      }

      //block comment, skip until '*/' occurs
      else if (sourceLine.charAt(sourcePos) == '*') {
        blockComment("*/");
      }
    }

    //comment start
    else if (c == '{') {
      blockComment("}");
    }

    //skipping whitespace
    else if (c == ' ' || c == '\t' /* tab */ ) {
      do {
        sourcePos++;
        //reached end of line
        if (sourcePos >= sourceLine.length()) {
          readNextLine();
          readNextToken();
          return;
        }
      } while (sourceLine.charAt(sourcePos) == ' ');
      readNextToken();
      return;
    }

    //digit start
    else if (isDigit(c)) {
      String digitString = "" + c;
      sourcePos++;
      //read until end of numbers is reached
      c = sourceLine.charAt(sourcePos);
      while (isDigit(c)) {
        digitString += c;
        sourcePos++;
        c = sourceLine.charAt(sourcePos);
      }
      nextToken = new Token(Integer.parseInt(digitString), getFileLineNum());
    }

    //text start
    else if (isLetterAZ(c)) {
      String textString = "" + c;
      sourcePos++;
      //read until end of letters or numbers is reached
      c = sourceLine.charAt(sourcePos);
      while (isLetterAZ(c) || isDigit(c)) {
        textString += c;
        sourcePos++;
        c = sourceLine.charAt(sourcePos);
      }
      nextToken = new Token(textString.toLowerCase(), getFileLineNum());
    }

    //semicolon
    else if (c == ';') {
      sourcePos++;
      nextToken = new Token(TokenKind.semicolonToken, getFileLineNum());
    }

    //left parenthesis
    else if (c == '(') {
      sourcePos++;
      nextToken = new Token(TokenKind.leftParToken, getFileLineNum());
    }

    //right parenthesis
    else if (c == ')') {
      sourcePos++;
      nextToken = new Token(TokenKind.rightParToken, getFileLineNum());
    }

    //left bracket
    else if (c == '[') {
      sourcePos++;
      nextToken = new Token(TokenKind.leftBracketToken, getFileLineNum());
    }

    //right bracket
    else if (c == ']') {
      sourcePos++;
      nextToken = new Token(TokenKind.rightBracketToken, getFileLineNum());
    }


    //char literal
    else if (c == '\'') {
      //special case, tick-literal
      if (sourceLine.charAt(sourcePos + 3) == '\'') {
        c = sourceLine.charAt(sourcePos);
        sourcePos += 4;
      }

      //error
      else if (sourceLine.charAt(sourcePos + 2) != '\'') {
        Main.illegalCharLiteralError(getFileLineNum());
      }
      //skipping to char value
      else {
        sourcePos++;
        c = sourceLine.charAt(sourcePos);
        //skips twice to go to letter after last backtick
        sourcePos++; sourcePos++;
      }

      nextToken = new Token(c, getFileLineNum());
    }

    //dot or range
    else if (c == '.') {
      sourcePos++;
      c = sourceLine.charAt(sourcePos);

      if (c == '.') {
        sourcePos++;
        nextToken = new Token(TokenKind.rangeToken, getFileLineNum());
      }

      else {
        nextToken = new Token(TokenKind.dotToken, getFileLineNum());
      }
    }

    //comma
    else if (c == ',') {
      sourcePos++;
      nextToken = new Token(TokenKind.commaToken, getFileLineNum());
    }

    //equal sign
    //this will never be confused with assignment or equality operators,
    //because this won't trigger if there's a ':' in front, and
    //'assignment-token' or comparators won't be reached if this is triggered
    //first
    else if (c == '=') {
      sourcePos++;
      nextToken = new Token(TokenKind.equalToken, getFileLineNum());
    }

    //colon as well as assignment
    else if (c == ':') {
      sourcePos++;
      c = sourceLine.charAt(sourcePos);

      if (c == '=') {
        sourcePos++;
        nextToken = new Token(TokenKind.assignToken, getFileLineNum());
      }

      else {
        nextToken = new Token(TokenKind.colonToken, getFileLineNum());
      }
    }

    //smaller than, not equal as well as smaller or equal
    else if (c == '<') {
      sourcePos++;
      c = sourceLine.charAt(sourcePos);

      if (c == '=') {
        sourcePos++;
        nextToken = new Token(TokenKind.lessEqualToken, getFileLineNum());
      }

      else if (c == '>') {
        sourcePos++;
        nextToken = new Token(TokenKind.notEqualToken, getFileLineNum());
      }

      else {
        nextToken = new Token(TokenKind.lessToken, getFileLineNum());
      }
    }

    //greater than as well as greater or equal
    else if (c == '>') {
      sourcePos++;
      c = sourceLine.charAt(sourcePos);

      if (c == '=') {
        sourcePos++;
        nextToken = new Token(TokenKind.greaterEqualToken, getFileLineNum());
      }

      else {
        nextToken = new Token(TokenKind.greaterToken, getFileLineNum());
      }
    }

    //+ operator
    else if (c == '+') {
      sourcePos++;
      nextToken = new Token(TokenKind.addToken, getFileLineNum());
    }

    //* operator
    else if (c == '*') {
      sourcePos++;
      nextToken = new Token(TokenKind.multiplyToken, getFileLineNum());
    }

    //- operator
    else if (c == '-') {
      sourcePos++;
      nextToken = new Token(TokenKind.subtractToken, getFileLineNum());
    }

    //if none of the tests passed it's an illegal character
    else {
      Main.illegalCharError(getFileLineNum(), c);
    }

    Main.log.noteToken(nextToken);
  }


  //reads a line of source code
  private void readNextLine() {
    if (sourceFile != null) {
      try {
        sourceLine = sourceFile.readLine();
        //come to end of file
        if (sourceLine == null) {
          sourceFile.close();  sourceFile = null;
          sourceLine = "";
        }

        //adds space to end of sourceline after read (to avoid null error
        //at end?)
        else {
          sourceLine += " ";
        }
        sourcePos = 0;
      } catch (IOException e) {
        Main.error("Scanner error: unspecified I/O error!");
      }
    }

    if (sourceFile != null) {
      Main.log.noteSourceLine(getFileLineNum(), sourceLine);
    }
  }


  private int getFileLineNum() {
    if (sourceFile != null) {
      return sourceFile.getLineNumber();
    }

    else {
      return 0;
    }
  }

  public String sourceLine() {
    return sourceLine;
  }

  public int sourcePos() {
    return sourcePos;
  }

  //utility for handling block comments
  private void blockComment(String delimiter) {
    int commentStartLine = getFileLineNum();
    //comment delimiter found in the same line, tries to read token
    // from new pos
    if (sourceLine.indexOf(delimiter) != -1) {
      sourcePos = sourceLine.indexOf(delimiter) + delimiter.length();
      readNextToken();
      return;
    }

    //comment delimiter not found in same line
    else {
      //read next line until the delimiter is found
      do {
        if (sourceFile == null) {
          Main.infniteCommentError(commentStartLine);
        }
        readNextLine();
      } while (sourceLine.indexOf(delimiter) == -1);
      sourcePos = sourceLine.indexOf(delimiter) + delimiter.length();
      readNextToken();
      return;
    }
  }

  // Character test utilities:

  private boolean isLetterAZ(char c) {
    return 'A'<=c && c<='Z' || 'a'<=c && c<='z';
  }


  private boolean isDigit(char c) {
    return '0'<=c && c<='9';
  }


  // Parser tests:

  public void test(TokenKind t) {
    if (curToken.kind != t)
    testError(t.toString());
  }

  public void testError(String message) {
    System.out.println();
    verboseBlock(this, true);
    Main.error(curLineNum(),
    "Expected a " + message +
    " but found a " + curToken.kind + "!");
  }

  public void skip(TokenKind t) {
    test(t);
    readNextToken();
  }
}
