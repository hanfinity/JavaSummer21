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
    String toReturn = "";
    if(valueOfInput % 3 == 0){
      toReturn = "Foo";
    }
    if(valueOfInput % 5 == 0)
    {
      toReturn += "Bar";
    }
    if(valueOfInput % 7 == 0)
    {
      toReturn += "Qix";
    }
    for (char num:
         input.toCharArray()) {
      if(num == '3') toReturn += "Foo";
    }
    return toReturn;
  }
}