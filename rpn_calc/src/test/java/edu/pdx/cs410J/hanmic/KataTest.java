package edu.pdx.cs410J.hanmic;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KataTest
{

  @Test
  void canInstantiateKataClass() {
    new Kata();
  }

  @Test
  void addNumberAddsTheNumber() {
    Kata.addNumber("5");
    assertTrue(Kata.numbers.contains(5F));
  }

  @Test
  void addNumberChokesOnNonNumber() {
    assertThrows(NumberFormatException.class, () ->Kata.addNumber("f"));
  }

  @Test
  void addSymbolFailsWithEmptyStack() {

  }

}
