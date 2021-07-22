package edu.pdx.cs410J.hanmic;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * A class for getting started with a code kata
 *
 * Use IntelliJ's "Refactor | Rename..." command to change the name of this
 * class (and its tests).
 */
public class Kata {
                                                                                    

  public static void main(String[] args) {
    if(args.length == 0) {
      System.err.println("Missing command line arguments");
      System.exit(1);
    }

    for (String arg : args) {
      if(symbols.contains(arg)){
        addSymbol(arg);
      }else {
        addNumber(arg);
      }
    }
      // else error
  }

}