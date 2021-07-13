package proj1545;

import org.la4j.Vector;
import org.la4j.Matrix;

import java.util.Random;
/*
This class represents a multi-layer perceptron (MLP) although currently it can only represent a 2 layer MLP
Uses the la4j linear algebra package to perform matrix and vector operations, which are used to simplify the
process. Model consists of two weight matrices, one for the hidden layer and one for the output layer.
 */

public class MLP {
    protected int input;
    protected int hidden;
    protected int output;
    public static final double BETA = 0.5; // sigmoid function term
    public static final double ALPHA = 0.9; // momentum term
    // weight matrices
    protected Matrix hiddenWeights;
    protected Matrix outWeights;
    // accumulators for delta values for epoch learning
    protected Matrix hiddenDelta;
    protected Matrix outDelta;
    // to save the last delta values
    protected Matrix oDeltaLast;
    protected Matrix hDeltaLast;

    protected Vector lastIn;
    protected Vector lastOut;
    protected Vector lastHid;
    protected Random r = new Random(System.currentTimeMillis());
    double eta;
    boolean modeSequential;

    public MLP(){

    }

    /*
    parameterized constructor:
        newInput - number of nodes in the input to the MLP
        newHidden - number of nodes in the hidden layer
        newOutput - number of nodes in the output layer
        eta - learning rate term

     creates a new MLP with the specified terms, all weights are initialized to a random number -0.05 < x < 0.05
     */
    public MLP(int newInput, int newHidden, int newOutput, double eta) {
        ++newInput; ++newHidden; // account for bias term
        // randomize and scale the initial weights
        hiddenWeights = Matrix.random(newHidden, newInput, r);
        hiddenWeights = hiddenWeights.subtract(0.5);
        hiddenWeights = hiddenWeights.divide(5);
        outWeights = Matrix.random(newOutput, newHidden, r);
        outWeights = outWeights.subtract(0.5);
        outWeights = outWeights.divide(5);
        // delta accumulator matrices for epoch training mode
        initDeltas(newInput, newHidden, newOutput);
        this.eta = eta;
        modeSequential = true; // default to sequential training mode
        hidden = newHidden;
        input = newInput;
        output = newOutput;
        // matrices to save the last delta term for momentum sake
        oDeltaLast = Matrix.zero(newOutput, newHidden);
        hDeltaLast = Matrix.zero(newHidden, newInput);
    }

    // initializes delta matrices to zero, uses input hidden and output for size
    private void initDeltas(int input, int hidden, int output) {
        hiddenDelta = Matrix.zero(hidden, input);
        outDelta = Matrix.zero(output, hidden);
    }

    // puts the MLP in sequential training mode
    public void setModeSequential() {
        modeSequential = true;
    }

    // puts the MLP in epoch training mode
    public void setModeEpoch() {
        modeSequential = false;
    }

    // setter function for a new eta value
    public void updateEta(double newEta) {
        eta = newEta;
    }

    // sigmoid function for determining activation
    protected static double sig(double input) {
        return 1d / (1 + Math.exp(-1 * BETA * input));
    }

    // function to apply the sigmoid function to a vector
    protected Vector sigVec(Vector input) {
        int length = input.length();
        Vector buffer = Vector.zero(length);
        // builds a new vector using sigmoid of each member of the original vector
        for(int i=0; i<length; ++i) {
            buffer.set(i, sig(input.get(i)));
        }
        return buffer;
    }

    // run given input through the machine
    public double[] run(Double[] inputs) {
        double[] temp = new double[inputs.length + 1];
        // convert Object Double vector to a primative double vector, also shift right 1 and introduce bias term
        for(int i=0; i<inputs.length; ++i) {
            temp[i+1] = inputs[i];
        }
        temp[0] = 1;
        // do the actual math, save the values for training later
        lastIn = Vector.fromArray(temp);
        lastHid = sigVec(hiddenWeights.multiply(lastIn));
        lastOut = sigVec(outWeights.multiply(lastHid));
        return toArray(lastOut); // return result as double array
    }

    // function to convert an la4j vector to an array
    protected static double[] toArray(Vector toConvert) {
        double[] buffer = new double[toConvert.length()];
        for (int i=0; i< toConvert.length(); ++i)  {
            buffer[i] = toConvert.get(i);
        }
        return buffer;
    }

    // training function: given the correct answer to the training example in actuals, computes error values
    // and uses loss function to compute delta values for changing weights
    public void train(double[] actuals) {
        Vector actVec = Vector.fromArray(actuals);
        int outputSize = actVec.length();
        int hiddenSize = hiddenWeights.rows();
        int inputSize = hiddenWeights.columns();
        Vector outErr = Vector.zero(outputSize);
        Vector hidErr = Vector.zero(hiddenSize);
        double ok;
        double hj;
        // create matrices to update weights with
        Matrix oDelta = Matrix.zero(outputSize, hiddenSize);
        Matrix hDelta = Matrix.zero(hiddenSize, inputSize);
        // determine the deltas for the output layer
        for(int k = 0; k<outputSize; ++k) {
            ok = lastOut.get(k);
            outErr.set(k, ok * (1-ok) * (actVec.get(k) - ok));
            // update weight delta matrix
            for(int j = 0; j<hiddenSize; ++j) {
                oDelta.set(k, j, eta * outErr.get(k) * lastHid.get(j));
            }
        }
        // determine deltas for the hidden layer
        for(int j = 0; j<hiddenSize; ++j) {
            hj = lastHid.get(j);
            hidErr.set(j, hj * (1-hj) * outWeights.getColumn(j).innerProduct(outErr));
            for(int i=0; i<inputSize; ++i) {
                hDelta.set(j, i, eta * hidErr.get(j) * lastIn.get(i));
            }
        }
        // If training in sequential mode, update weights now
        if(modeSequential) {
            hiddenWeights = hiddenWeights.add(hDelta).add(hDeltaLast);
            outWeights = outWeights.add(oDelta).add(oDeltaLast);
        } else {
            // if not in sequential mode, add deltas to persistent delta matrices
            hiddenDelta.add(hDelta);
            outDelta.add(oDelta);
        }
        // save the deltas for next time
        oDeltaLast = oDelta.multiply(ALPHA);
        hDeltaLast = hDelta.multiply(ALPHA);
    }

    // function to be run at the end of a training epoch to update weights and reset deltas
    // does nothing if in sequential mode
    public void endEpoch() {
        if(!modeSequential) {
            hiddenWeights.subtract(hiddenDelta);
            outWeights.subtract(outDelta);
            initDeltas(input, hidden, output);
        }
    }
}
