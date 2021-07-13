package proj1545;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;


public class IT_MLP {
    public static final int TRAINING_SIZE = 60000;
    public static final int TESTING_SIZE = 10000;
    public static final int DATA_SIZE = 784;
    @Test
    public void canInstantiate() {
        prog1 project = new prog1(0.1, 25, TRAINING_SIZE);
        assertNotNull(project);
    }

    @Test
    public void canRunTraining() throws IOException {
        prog1 project = getProg1();
        project.train();
        assertNotNull(project);
    }

    @Test
    public void canRunNonSequential() throws IOException {
        prog1 project = getProg1();
        project.machine.setModeEpoch();
        project.train();
        assertNotNull(project);
    }

    @NotNull
    private prog1 getProg1() throws IOException {
        prog1 project = new prog1(0.1, 25, TRAINING_SIZE);
        project.loadTraining();
        project.loadTesting();
        return project;
    }
}
