package edu.pdx.cs410J.hanmic;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class KataTest
{




  @Test
  void canInstantiateKataClass() {
    new Kata();
  }

  @Test
  void canReadFromCommandLine() {

  }

  @Test
  void computeIfGivenMultipleOf3ReturnsStringContainingFoo(){
    Kata test = new Kata();
    String testString = "3";
    String returnVal =  test.compute(testString);
    assertThat(returnVal, containsString("Foo"));
  }

  @Test
  void computeIfGivenMultipleOf5ReturnsStringContainingBar(){
    Kata test = new Kata();
    String testString = "5";
    String returnVal =  test.compute(testString);
    assertThat(returnVal, containsString("Bar"));
  }

  @Test
  void computeIfGivenMultipleOf7ReturnsStringContainingQix(){
    Kata test = new Kata();
    String testString = "7";
    String returnVal =  test.compute(testString);
    assertThat(returnVal, containsString("Qix"));
  }
}
