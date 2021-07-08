package edu.pdx.cs410J.hanmic;

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
    String input = args[0];
    try {
      int number = Integer.parseInt(input);
    } catch (NumberFormatException exc) {
      System.err.println("Arguments must be a number");
      System.exit(1) ;
    }


  }


  String compute(String input){
    Integer valueOfInput = Integer.parseInt(input);
    if(valueOfInput % 3 == 0){
      return "Foo";
    }
    return null;
  }
}