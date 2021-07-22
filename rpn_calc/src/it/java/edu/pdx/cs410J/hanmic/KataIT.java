package edu.pdx.cs410J.hanmic;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

class KataIT extends InvokeMainTestCase {

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
  void invokingMainWithValidArgumentsCalculatesAnswer() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Kata.class, "25", "5", "/" );
    assertThat(result.getTextWrittenToStandardOut(), containsString("5"));

  }

  @Test
  void providingOnlyNumbersPrintsError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Kata.class, "5", "5", "5");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Arguments, need to provide operators"));
  }

  @Test
  void providingOnlyOperatorsPrintsError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Kata.class, "/", "+", "-");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Arguments, need to provide numbers."));
  }


}
