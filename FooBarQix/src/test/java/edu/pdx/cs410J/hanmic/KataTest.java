package edu.pdx.cs410J.hanmic;

import org.junit.jupiter.api.Test;


import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
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
    String testString = "6";
    String returnVal =  test.compute(testString);
    assertThat(returnVal, containsString("Foo"));
  }

  @Test
  void computeIfGivenMultipleOf5ReturnsStringContainingBar(){
    Kata test = new Kata();
    String testString = "10";
    String returnVal =  test.compute(testString);
    assertThat(returnVal, containsString("Bar"));
  }

  @Test
  void computeIfGivenMultipleOf7ReturnsStringContainingQix(){
    Kata test = new Kata();
    String testString = "14";
    String returnVal =  test.compute(testString);
    assertThat(returnVal, containsString("Qix"));
  }

  @Test
  void computeIfGivenNumeral3ReturnsFooFoo(){
    Kata test = new Kata();
    String testString = "3";
    String returnVal = test.compute(testString);
    assertThat(returnVal, equalTo("FooFoo"));

  }

  @Test
  void computeIfGiven33ReturnsFooFooFoo(){
    Kata test = new Kata();
    String testString = "33";
    String returnVal = test.compute(testString);
    assertThat(returnVal, equalTo("FooFooFoo"));
  }
}
