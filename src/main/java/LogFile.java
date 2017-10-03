package main;

import parser.*;
import scanner.Token;

import java.io.*;

public class LogFile {
  boolean doLogBinding = false, doLogParser = false, doLogPrettyPrint = false,
  verbose = false, doLogDeclarations = false, doLogScanner = false,
  doLogTypeChecks = false, verboseLog = false;

  private String logFileName = null;
  private int nLogLines = 0;
  private int parseLevel = 0;
  private int binderLevel = 0;
  private String prettyLine = "";
  private int prettyIndentation = 0;

  void init(String fName) {
    logFileName = fName;
  }

  public void finish() {
    if (prettyLine.length() > 0) {
      prettyPrintLn();
    }
  }


  public String identify() {
    String t = "Log file";
    if (logFileName != null) {
      t += " named " + logFileName;
    }
    return t;
  }


  private void writeLogLine(String data) {
    if (logFileName == null) return;

    try {
      PrintWriter log = null;
      if (nLogLines == 0) {
        log = new PrintWriter(logFileName);
      }
      else {
        FileOutputStream s = new FileOutputStream(logFileName, true);
        log = new PrintWriter(s);
      }

      log.println(data);  ++nLogLines;
      log.close();

    } catch (FileNotFoundException e) {
      String lName = logFileName;
      logFileName = null;  // To avoid infinite recursion
      // Main.error -> noteError ->
      //   writeLogLine -> ...
      Main.error("Cannot open log file " + lName + "!");
    }
  }


  /**
  * Make a note in the log file that an error has occured.
  * (If the log file is not in use, request is ignored.)
  *
  * @param message  The error message
  */
  public void noteError(String message) {
    if (nLogLines > 0) {
      writeLogLine(message);
    }
  }


  /**
  * Make a note in the log file that a source line has been read.
  * This note is only made if the user has requested appropriate logging.
  *
  * @param lineNum  The line number
  * @param line     The actual line
  */
  public void noteSourceLine(int lineNum, String line) {
    if (doLogParser || doLogScanner) {
      writeLogLine(String.format("%4d: %s",lineNum,line));
    }
  }


  /*
  * Make a note in the log file that a token has been read.
  * This note will only be made if the user has requested it.
  */
  public void noteToken(Token tok) {
    if (doLogScanner) {
      writeLogLine("Scanner: " + tok.identify());
    }
  }


  //op: operand
  public void noteTypeCheck(types.Type t1, String op,
  types.Type t2, PascalSyntax where) {
    if (doLogTypeChecks) {
      writeLogLine("Type check " + op + " on line " + where.lineNum +
      ": " + t1.identify() + " vs " + t2.identify());
    }
  }


  public void noteBinding(String id, PascalSyntax where, PascalDecl decl) {
    if (doLogBinding) {
      writeLogLine("Binding on line " + where.lineNum + ": " + id +
      " was declared as " + decl.identify());
    }
  }

  public void noteDeclaration(String id, PascalSyntax where, PascalDecl decl) {
    if (doLogDeclarations) {
      String identify = decl.identify();
      String s = "";

      if (decl.type == null) {
        s = "null";
      }

      else {
        s = decl.type.identify();
      }

      String pos = "";

      if (decl.lineNum < 0) {
        pos = "in the library";
      }

      else {
        pos = "on line " + decl.lineNum;
      }

      writeLogLine("Declaration " + pos + ": " + id + " was declared as " + identify.substring(0, identify.indexOf('>') + 1) + " with type " + s);
    }
  }

  public void noteLine(String message) {
    writeLogLine(message);
  }

  public void noteVerboseLine(String message) {
    if (verboseLog) writeLogLine(message);
  }


  /**
  * Make a note that the parser has started parsing a non-terminal.
  * This note is only recorded if the user has requested it.
  *
  * @param name The name of the non-terminal.
  */
  public void enterParser(String name) {
    if (doLogParser) {
      noteParserInfo(name);  ++parseLevel;
    }
  }

  /**
  * Make a note that the parser has finished parsing a non-terminal.
  * This note is only recorded if the user has requested it.
  *
  * @param name The name of the non-terminal.
  */
  public void leaveParser(String name) {
    if (doLogParser) {
      --parseLevel;  noteParserInfo("/"+name);
    }
  }

  private void noteParserInfo(String name) {
    String logLine = "Parser:   ";
    for (int i = 1;  i <= parseLevel;  ++i) {
      logLine += "  ";
    }
    writeLogLine(logLine + "<" + name + ">");
  }

  public void enterVerboseBinder(String message) {
    if (verbose || verboseLog) {
      noteBinderInfo(message); ++binderLevel;
    }
  }

  public void leaveVerboseBinder(String message) {
    if (verbose || verboseLog) {
      --binderLevel; noteBinderInfo("/" + message);
    }
  }

  private void noteBinderInfo(String message) {
    String logLine = "Binder:   ";
    for (int i = 1; i <= binderLevel; ++i) {
      logLine+= "  ";
    }
    writeLogLine(logLine + "<" + message + ">");
  }


  public void prettyPrint(String s) {
    if (prettyLine.equals("")) {
      for (int i = 1;  i <= prettyIndentation;  i++)
      prettyLine += "  ";
    }
    prettyLine += s;
  }

  public void prettyPrintLn(String s) {
    prettyPrint(s);  prettyPrintLn();
  }

  public void prettyPrintLn() {
    writeLogLine(prettyLine);
    prettyLine = "";
  }

  public void prettyIndent() {
    prettyIndentation++;
  }

  public void prettyOutdent() {
    prettyIndentation--;
  }
}
