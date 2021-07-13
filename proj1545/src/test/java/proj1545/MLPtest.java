package proj1545;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MLPtest {
    @Test
    public void runSigmoidTest() {
        assertTrue((MLP.sig(0.5) - 0.61) < 0.1);
    }

    @Test
    public void canInstantiateMLP() {
        MLP test = new MLP();
        Assert.assertNotNull(test);
    }

    @Test
    public void canInstantiateMLPParamterized() {
        MLP test = getMlp();
        Assert.assertNotNull(test);
    }

    @Test
    public void canRunAVector() {
        MLP test = getMlp();
        Double[] array = {1d, 2d, 3d};
        Assert.assertNotNull(test.run(array));
    }

    @NotNull
    private MLP getMlp() {
        return new MLP(3, 4, 5, 0.1);
    }

    @Test
    public void runRejectsWrongSizeVector() {
        MLP test = getMlp();
        Double[] array = {1d, 2d, 3d, 4d};
        Assertions.assertThrows(IllegalArgumentException.class , () -> {
            test.run(array);
        });
    }

    @Test
    public void canTrainMLP() {
        MLP test = getMlp();
        Double[] array = {1d, 2d, 3d};
        double[] output = test.run(array);
        test.train(output);
        Assert.assertTrue(true);
    }
}
