package proj1545;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Consumer;
/*
main class for my program 1 solver
main initiates the two experiments specified in the homework assignment and stores the results to .csv files
prog1 class operates as an extension of the Consumer class to work with the Mnist reader
uses javagl's mnist reader to read from the mnist binary files
<link>https://github.com/javagl/MnistReader/blob/master/src/main/java/de/javagl/mnist/reader/MnistCompressedReader.java</link>
Test routines for this program are found in Maintest
 */

public class prog1 implements Consumer<MnistEntry> {

    public static final int TRAINING_SIZE = 60000;
    public static final int TESTING_SIZE = 10000;
    public static final int DATA_SIZE = 784;
    MLP machine;
    private Path path;
    private double[] accuracy;
    private double[] accuracyTest;
    private int correctTest;
    private int totalTest;
    private int correct;
    private int total;
    private int[] labels;
    private int[] labelsTest;
    private java.util.Vector<Double[]> data;
    private java.util.Vector<Double[]> testing;
    private boolean phaseIsTraining;
    private int[][] confusion;
    double eta;
    private int trainingSize;
    private double accuracyValidation = 0;

    public static void main(String[] args) throws IOException{
        prog1 program;
        System.out.println("MNIST Recognition Algorithm - Michael Han");
        System.out.println("Experiment 1: Vary hidden units");
        // run the model with each of the three required hidden layer sizes
        int[] hiddens = {20, 50, 100};
        for (int i:
             hiddens) {
            System.out.println("# hidden layers = " + i + " running...");
            program = new prog1(0.1, i, TRAINING_SIZE);
            program.load();
            program.train();
            program.finalValidation();
            program.writeToFile("hidden" + i);
        }
        System.out.println("Experiment 2: Vary size of training set");
        int[] trainingSize = {15000, 30000};
        for (int i:
             trainingSize) {
            System.out.println("training set size = " + i + " running...");
            program = new prog1(0.1, 100, i);
            program.load();
            program.train();
            program.finalValidation();
            program.writeToFile("datasetsize" + i);
        }

    }

    // writes stored confusion matrix and training data to a .csv file, filename is "data" concatenated with given
    // string name
    public void writeToFile(String name) throws IOException{
        File csvOutputFile = new File(path + "/data" + name + ".csv");
        try(PrintWriter pw = new PrintWriter(csvOutputFile)) {
            for(int i=0; i<70; i++) {
                pw.print(accuracy[i]+",");
            }
            pw.println();
            for(int i=0; i<70; i++) {
                pw.print(accuracyTest[i]+",");
            }
            pw.println();
            for(int i=0; i<10; i++) {
                for(int j=0; j<10; j++) {
                    pw.print(confusion[i][j] + ",");
                }
                pw.println();
            }
            pw.println(accuracyValidation);
        }
    }

    // initialze MLP with the given etta (rate of learning);
    public prog1(double rate, int hidden, int trainingSize) {
        // store the given training size, this is only used in experiment 2
        this.trainingSize = trainingSize;
        machine = new MLP(DATA_SIZE, hidden, 10, rate);
        accuracy = new double[70];
        accuracyTest = new double[70];
        labels = new int[TRAINING_SIZE];
        labelsTest = new int[TESTING_SIZE];
        data = new java.util.Vector<>(TRAINING_SIZE);
        testing = new java.util.Vector<>(TESTING_SIZE);
        phaseIsTraining = true;
        path = FileSystems.getDefault().getPath("data/training_data");
        eta = rate;
        // initialize confusion matrix to 0
        confusion = new int[10][10];
        for(int i=0; i<10; ++i) {
            for(int j=0; j<10; ++j) {
                confusion[i][j] = 0;
            }
        }
    }

    // load all function
    public void load() throws IOException {
        loadTesting();
        loadTraining();
    }

    // load in training data
    public void loadTraining() throws IOException{
        phaseIsTraining = true;
        MnistDecompressedReader reader = new MnistDecompressedReader();
        reader.readDecompressedTraining(path, this);
    }

    // load in testing data
    public void loadTesting() throws IOException {
        phaseIsTraining = false;
        MnistDecompressedReader reader = new MnistDecompressedReader();
        reader.readDecompressedTesting(path, this);
    }

    // display function for the confusion matrix
    public void showConfusion() {
        System.out.println("     |  0  |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |");
        for(int i=0; i<10; i++) {
            System.out.print("  " + i + "  |");
            for(int j=0; j<10; j++) {
                System.out.format("%05d|", confusion[i][j]);
            }
            System.out.println();
        }
    }

    // run a test with set parameters for final confirmaiton
    public void finalValidation() {
        correctTest = totalTest = 0;
        confusion = new int[10][10];
        for(int i=0; i<10000; ++i) {
            test(i);
        }
        accuracyValidation = 100.0 * correctTest / totalTest;
        System.out.println(" | Testing Accuracy: " + accuracyValidation);
    }

    // main training routine: steps through epochs until either 70 have passed or the accuracy changes by less than .01%
    // displays accuracy at the end of each epoch
    public synchronized void train() {
        int epoch = 0;
        boolean cont = true;
        while(epoch < 70 && cont) {
            correct = total = correctTest = totalTest = 0; //initialize the # of correct guesses
            // run each training example through the MLP
            for(int i = 0; i< trainingSize; ++i) {
                sample(i);
            }
            System.out.print("Epoch #" + epoch);
            accuracy[epoch] = (100.0 * correct) / total; // compute accuracy
            System.out.print(" | Training Accuracy: " + accuracy[epoch]);
            if(epoch > 0) {
                // compute accuracy difference between epochs to decide whether to continue
                cont = Math.abs(accuracy[epoch] - accuracy[epoch - 1]) > 0.01;
            }
            // finally, run through the test data
            for(int i=0; i<TESTING_SIZE; ++i) {
                test(i);
            }
            System.out.println(" | Testing Accuracy: " + 100.0 * correctTest / totalTest);
            accuracyTest[epoch] = (100.0) * correctTest / totalTest;
            machine.endEpoch();
            ++epoch;
        }

    }

    // run the model on test data
    protected int test(int i) {
        double[] guess = machine.run(testing.get(i));
        int result = -1;
        double max = -1;
        // check which location in the guess matrix has the highest value, that's the MLP's result
        for(int j=0; j<10; ++j) {
            if(guess[j] > max) {
                max = guess[j];
                result = j;
            }
        }
        ++totalTest;
        if(result == labelsTest[i]) {
            ++correctTest;
        }
        ++confusion[labelsTest[i]][result];
        return result;
    }

    // run a single sample of the test, uses training data item 'i'
    // runs training if MLP was wrong, does nothing if correct answer is given
    protected synchronized void sample(int i) {
        // run the model for each perceptron
        double[] guess;
        int result = -1;
        double max = -1;
        // run MLP on given data, store in guess matrix
        guess = machine.run(data.get(i));
        ++total;
        // check which location in the guess matrix has the highest value, that's the MLP's result
        for(int j=0; j<10; ++j) {
            if(guess[j] > max) {
                max = guess[j];
                result = j;
            }
        }
        // finally, check if the result is the correct one
        if(result == labels[i]) {
            ++correct; // log we got one right
        } else {
            // if we were wrong, run the training method
            double[] actuals = new double[10];
            // build the t vector, correct answer gets 0.9, all others get 0.1
            Arrays.fill(actuals, 0.1);
            actuals[labels[i]] = 0.9;
            machine.train(actuals);
        }
    }


    // accept function: mnist entry reader uses this to load in data
    // takes the given MnistEntry curr and converts it from a byte array to an array of scaled doubles
    // entries are stored in the data or testing vectors
    @Override
    public void accept(MnistEntry curr) {
        int index = curr.getIndex();
        int tempInt;
        Double[] tempData = new Double[784];
        byte[] temp = curr.getImageData();
        for(int i=0; i<(784); ++i) {
            tempInt = Byte.toUnsignedInt(temp[i]);
            tempData[i] = ((double) tempInt) / 255.0;
        }
        // use phaseIsTraining boolean to determine where to store this entry
        if(phaseIsTraining) {
            data.add(index, tempData);
            labels[curr.getIndex()] = curr.getLabel();
        }
        else {
            testing.add(index, tempData);
            labelsTest[curr.getIndex()] = curr.getLabel();
        }

    }
}
