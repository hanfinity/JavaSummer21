package proj1545;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;



public class Maintest {
    public static final int TRAINING_SIZE = 60000;
    public static final int TESTING_SIZE = 10000;
    public static final int DATA_SIZE = 784;
    @Test
    public void canLoadData() {
        prog1 test = new prog1(0.1, 50, TRAINING_SIZE);
        assertDoesNotThrow(() ->{test.loadTesting();});
        assertDoesNotThrow(() ->{test.loadTraining();});
    }
}
