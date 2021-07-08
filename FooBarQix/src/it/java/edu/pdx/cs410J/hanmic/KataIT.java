package edu.pdx.cs410J.hanmic;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

class KataIT extends InvokeMainTestCase {
  private MainMethodResult invokeMain(String... args) {
    return invokeMain(Kata.class, args);
  }

  @Test
  void invokingMainWithNoArgumentsHasExitCodeOf1() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Kata.class);
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void invokingMainWithNoArgumentsPrintsMissingArgumentsToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Kata.class);
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  @Test
  void invokingMainWithArgumentsThatArentNumeric() {
    InvokeMainTestCase.MainMethodResult result = invokeMain("test");
    assertThat(result.getTextWrittenToStandardError(), containsString("Arguments must be a number"));
  }
}
