
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;

public class Group3 {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {

        if (args.length < 2) {
            System.out.println(
                    "Please run with two command line arguments: input and output file names");
            System.exit(0);
        }

        String inputFileName = args[0];
        String outFileName = args[1];

        String[] data = readData(inputFileName);

        String[] toSort = data.clone();

        sort(toSort);

        toSort = data.clone();

        Thread.sleep(10);

        long start = System.currentTimeMillis();
        sort(toSort);
        long end = System.currentTimeMillis();

        System.out.println(end - start);

        writeOutResult(toSort, outFileName);

    }

    private static String[] readData(String inputFileName) throws FileNotFoundException {
        ArrayList<String> input = new ArrayList<>();
        Scanner in = new Scanner(new File(inputFileName));
        while (in.hasNext()) {
            input.add(in.next());
        }
        in.close();
        return input.toArray(new String[0]);
    }

    /**
     * Computes and stores the sum of prime factors for the given number 'n'.
     * 
     * @param n    The number for which to calculate the sum of prime factors.
     * @param temp A HashMap to store the sum of prime factors for each number.
     */
    private static void sumPrimeFactors(int n, HashMap<Integer, Integer> temp) {
        // Check if we already computed the sum of prime factors for this number
        if (temp.containsKey(n)) {
            return;
        }
        int sum = 0; // Variable to hold the sum of prime factors
        int originalN = n; // Store the original value of 'n'
        
        // Loop to find prime factors up to the square root of 'n'
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) { // If 'i' is a factor of 'n'
                sum += i; // Add 'i' to the sum
                while (n % i == 0) { // Remove all instances of 'i' from 'n'
                    n /= i;
                }
            }
        }
        
        // If 'n' is greater than 1, then it is a prime number and a factor of itself
        if (n > 1) {
            sum += n;
        }
        
        // Store the computed sum in the HashMap
        temp.put(originalN, sum);
    }

    /**
     * The sorting is done in ascending order of the sum of prime factors.
     * If two numbers have the same sum, they are sorted in descending order of their values.
     */
    private static void sort(String[] toSort) {
        HashMap<Integer, Integer> temp = new HashMap<>(); // HashMap to hold the sum of prime factors
        
        // Pre-calculate the sum of prime factors for each number in the array
        for (String s : toSort) {
            int num = Integer.parseInt(s);
            sumPrimeFactors(num, temp);
        }
        
        Arrays.sort(toSort, (a, b) -> {
            int numA = Integer.parseInt(a);
            int numB = Integer.parseInt(b);
            
            // Compare based on the sum of prime factors
            int compare = temp.get(numA) - temp.get(numB);
            
            // If the sums are equal, compare based on the actual number values
            if (compare == 0) {
                return numB - numA;
            }
            
            return compare;
        });
    }


    private static void writeOutResult(String[] sorted, String outputFilename) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(outputFilename);
        for (String s : sorted) {
            out.println(s);
        }
        out.close();
    }
}



