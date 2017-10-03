package main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeFile {
  private String codeFileName;
  private PrintWriter code;
  private int numLabels = 0;
  boolean verbose = false;

  CodeFile(String fName) {
    codeFileName = fName;
    try {
      code = new PrintWriter(new FileOutputStream(fName), true);
    } catch (FileNotFoundException e) {
      Main.error("Cannot create code file " + fName + "!");
    }
    code.println("# Code file created by Pascal2016 compiler " +
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
  }

  void finish() {
    code.close();
  }

  public String identify() {
    return "Code file named " + codeFileName;
  }


  public String getLabel(String origName) {
    return origName + "_" + (++numLabels);
  }

  public String getLocalLabel() {
    return String.format(".L%04d", ++numLabels);
  }


  public void genInstr(String lab, String instr, String arg, String comment) {
    if (lab.length() > 0)
    code.println(lab + ":");
    if ((instr+arg+comment).length() > 0) {
      code.printf("        %-7s %-23s ", instr, arg);
      if (comment.length() > 0) {
        code.print("# " + comment);
      }
      code.println();
    }
  }

  public void verboseGenInstr(String lab, String instr,
                              String arg, String comment) {
    if (verbose) genInstr(lab, instr, arg, comment);
  }

}
