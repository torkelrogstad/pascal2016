package main;

import parser.*;
import scanner.Scanner;
import static scanner.TokenKind.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Collections;

import java.io.*;

public class Main {
  private static final DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
  private static final Date date = new Date();
  public static final String version = dateformat.format(date);
  public static int declLevel = 0;

  public static parser.Library library;

  public static LogFile log = new LogFile();

  private static String sourceFileName, baseFileName;
  private static boolean testChecker = false, verboseLog = false,
  testParser = false, testScanner = false, verbose = false,
  viewBlocks = false, verboseCompile = false;
  private static String OS;


  public static void main(String arg[]) {
    OS = System.getProperty("os.name");
    System.out.println("This is the Ifi Pascal2016 compiler (" +
    version + ") running on " + OS);

    int exitStatus = 0;

    /*
    try-catch-finally block exits if arguments are wrong, but logs what
    went wrong
     */
    try {
      readArgs(arg);
      log.init(baseFileName + ".log");

      Scanner s = new Scanner(sourceFileName);
      if (testScanner) doTestScanner(s);
      // Del 2:
      else if (testParser)
          doTestParser(s);
      // Del 3:
      else if (testChecker)
          doTestChecker(s);
      // Del 4:
      else
          doRunRealCompiler(s);
    } catch (PascalError e) {
      System.out.println();
      System.err.println(e.getMessage());
      exitStatus = 1;
    } finally {
      log.finish();
    }

    System.exit(exitStatus);
  }


  public static boolean useUnderscore() {
    // Should global names start with an '_'? Not with Linux/Unix.
    return ! OS.matches(".*n.*x.*");
  }

  /*
  Reads arguments from the command line by looping through the arguments and
  checking for args. Therefore, a program cannot be run with multiple arguments
  written together. Also picks up incorrect arguments
   */
  private static void readArgs(String args[]) {
    for (int i = 0;  i < args.length;  i++) {
      String a = args[i];

      if (a.equals("-logB")) {
        log.doLogBinding = true;
      }
      else if (a.equals("-logD")) {
        log.doLogDeclarations = true;
      }
      else if (a.equals("-logP")) {
        log.doLogParser = true;
      }
      else if (a.equals("-logS")) {
        log.doLogScanner = true;
      }
      else if (a.equals("-logT")) {
        log.doLogTypeChecks = true;
      }
      else if (a.equals("-logY")) {
        log.doLogPrettyPrint = true;
      }
      else if (a.equals("-testchecker")) {
        testChecker = log.doLogDeclarations = log.doLogBinding = log.doLogTypeChecks = true;
      }
      else if (a.equals("-testparser")) {
        testParser = log.doLogParser = log.doLogPrettyPrint = true;
      }
      else if (a.equals("-testscanner")) {
        testScanner = log.doLogScanner = true;
      }
      else if (a.equals("-verbose")) {
        verbose = log.verbose = true;
      }
      else if (a.equals("-verboseLog")) {
        verboseLog = log.verboseLog = true;
      }
      else if (a.equals("-viewBlocks")) {
        viewBlocks = true;
      }
      else if (a.equals("-verboseCompile")) {
        verboseCompile = true;
      }
      else if (a.startsWith("-")) {
        warning("Warning: Unknown option " + a + " ignored.");

      }
      //incorrect syntax of arguments, throws error
      else if (sourceFileName != null) {
        usage();

      }
      else {
        sourceFileName = a;
      }
    }

    //incorrect syntax of arguments, throws error
    if (sourceFileName == null) {
      usage();
    }

    baseFileName = sourceFileName;
    if (baseFileName.length()>4 && baseFileName.endsWith(".pas")) {
      baseFileName = baseFileName.substring(0,baseFileName.length()-4);
    }
  }


  private static void doTestScanner(Scanner s) {
    while (s.nextToken.kind != eofToken)
    s.readNextToken();
  }


  /* Del 2: */
  private static void doTestParser(Scanner s) {
    Program prog = Program.parse(s);
    if (s.curToken.kind != eofToken) {
      error("Scanner error: Garbage after the program!");
    }

    prog.prettyPrint();
  }

  /* Del 3: */
  private static void doTestChecker(Scanner s) {
    Program prog = Program.parse(s);
    if (s.curToken.kind != eofToken) {
      error("Scanner error: Garbage after the program!");
    }

    if (log.doLogPrettyPrint) {
      prog.prettyPrint();
    }

    library = new Library(-1);
    prog.check(library, library);
  }

  /* Del 4: */
  private static void doRunRealCompiler(Scanner s) {
    System.out.print("Parsing... ");
    Program prog = Program.parse(s);
    if (s.curToken.kind != eofToken)
    error("Scanner error: Garbage after the program!");

    if (log.doLogPrettyPrint)
    prog.prettyPrint();

    System.out.print("checking... ");
    library = new Library(-1);
    prog.check(library, library);

    if (viewBlocks) {
      prog.viewBlocks();
    }

    System.out.print("generating code... ");
    CodeFile code = new CodeFile(baseFileName+".s");
    code.verbose = verboseCompile;

    library.genCode(code);
    prog.genCode(code);
    code.finish();
    System.out.println("OK");

    assembleCode();
  }


  private static void assembleCode() {
    String pName = baseFileName;
    String sName = baseFileName + ".s";

    String cmd[] = new String[8];
    cmd[0] = "gcc";  cmd[1] = "-m32";
    cmd[2] = "-o";   cmd[3] = pName;
    cmd[4] = sName;
    cmd[5] = "-L.";  cmd[6] = "-L/hom/inf2100";  cmd[7] = "-lpas2016";

    System.out.print("Running");
    for (String s: cmd) {
      if (s.contains(" "))
      System.out.print(" '" + s + "'");
      else
      System.out.print(" " + s);
    }
    System.out.println();

    try {
      String line;
      Process p = Runtime.getRuntime().exec(cmd);

      // Print any output from the assembly process:
      BufferedReader out = new BufferedReader
      (new InputStreamReader(p.getInputStream()));
      BufferedReader err = new BufferedReader
      (new InputStreamReader(p.getErrorStream()));

      while ((line = out.readLine()) != null) {
        System.out.println(line);
      }
      while ((line = err.readLine()) != null) {
        System.out.println(line);
      }
      out.close();  err.close();
      p.waitFor();
    } catch (Exception err) {
      error("Assembly errors detected.");
    }
  }

  //development logging
  public static void verbose(String message) {
    if (verbose) System.out.println(message);
  }

  public static void verboseBlock(Scanner s) {
    verbose("lineNum: " + s.curLineNum() + "; " + s.sourceLine().trim());
    verbose("curToken: " + s.curToken.identify() + ", nextToken: " + s.nextToken.identify());
  }

  public static void verboseBlock(Scanner s, boolean fin) {
    if (fin) {
      verbose = true;
      verboseBlock(s);
    }
  }

  public static void verboseLog(String message) {
    if (verbose) log.noteLine(message);
  }

  //Taken from Stack Overflow
  //stackoverflow.com/questions/2250031/null-check-in-an-enhanced-for-loop
  @SuppressWarnings("unchecked")
  public static <T> List<T> safe( List<T> other ) {
    return other == null ? Collections.EMPTY_LIST : other;
  }

  // Error message utilities:
  public static void illegalCharError(int lineNum, char c) {
    error("Scanner error on line " + lineNum + ": Illegal character : '" + c + "'!");
  }

  public static void infniteCommentError(int lineNum) {
    error("Scanner error on line " + lineNum + ": No end for comment starting on line " + lineNum + "!");
  }

  public static void illegalCharLiteralError(int lineNum) {
    error("Scanner error on line " + lineNum + ": Illegal char literal!");
  }

  public static void error(String message) {
    log.noteError(message);
    throw new PascalError(message);
  }

  public static void error(int lineNum, String message) {
    error("Error in " +
    (lineNum<0 ? "last line" : "line "+lineNum) +
    ": " + message);
  }

  private static void usage() {
    error("Usage: java -jar pascal2016.jar " +
    "[-log{D|B|P|S|T|Y}] [-test{checker|parser|scanner}] [-verbose] file");
  }

  public static void panic(String where) {
    error("PANIC! Programming error in " + where);
  }

  public static void warning(String message) {
    log.noteError(message);
    System.err.println(message);
  }
}
