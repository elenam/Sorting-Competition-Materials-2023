
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// To run on a single core, compile and then run as:
// taskset -c 0 java GroupN
// To avoid file reading/writing connections to the server, run in /tmp 
// of your lab machine.

public class Group2 {

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

		if (args.length < 2) {
			System.out.println(
					"Please run with two command line arguments: input and output file names");
			System.exit(0);
		}

		String inputFileName = args[0];
		String outFileName = args[1];
		
		// Uncomment to test comparator methods
		//SortingCompetitionComparator.runComparatorTests();

		String[] data = readData(inputFileName); // read data as strings
		
		String[] toSort = data.clone(); // clone the data

		sort(toSort); // call the sorting method once for JVM warmup
		
		toSort = data.clone(); // clone again

		Thread.sleep(10); // to let other things finish before timing; adds stability of runs

		long start = System.currentTimeMillis();

		sort(toSort); // sort again

		long end = System.currentTimeMillis();

		System.out.println(end - start);

		writeOutResult(toSort, outFileName); // write out the results

	}


	private static String[] readData(String inputFileName) throws FileNotFoundException {
		ArrayList<String> input = new ArrayList<>();
		Scanner in = new Scanner(new File(inputFileName));

		while (in.hasNext()) {
			input.add(in.next());
		}

		in.close();

		// the string array is passed just so that the correct type can be created
		return input.toArray(new String[0]);
	}

	// YOUR SORTING METHOD GOES HERE.
	// You may call other methods and use other classes.
	// Note: you may change the return type of the method.
	// You would need to provide your own function that prints your sorted array to
	// a file in the exact same format that my program outputs
	private static void sort(String[] toSort) {
		Arrays.sort(toSort, new SortingCompetitionComparator());
	}

	private static class SortingCompetitionComparator implements Comparator<String> {
    private static Map<Integer, Integer> primeFactorSumCache = new HashMap<>();

    @Override
    public int compare(String s1, String s2) {
        int num1 = Integer.parseInt(s1);
        int num2 = Integer.parseInt(s2);

		
        int sum1 = getSumPrimeFactorsCached(num1);
        int sum2 = getSumPrimeFactorsCached(num2);

        if (sum1 < sum2) {
            return -1;
        } else if (sum1 > sum2) {
            return 1;
        }

         // If the prime factor sums are equal, compare the numbers in reverse order
        return num2 - num1;
    }

    private static int getSumPrimeFactorsCached(int n) {
        if (primeFactorSumCache.containsKey(n)) {
            return primeFactorSumCache.get(n);
        }

        // Calculate the sum of the prime factors here and store the result in the cache.
		// This can help us avoid redundant calculations when dealing with repetitive prime-sums.
        int sum = calculatePrimeFactors(n);
        primeFactorSumCache.put(n, sum);

        return sum;
    }

    // implements modified sieve of eratosthenes algorithm 
    // to calculate the sum of the prime factors of a given number n.
    private static int calculatePrimeFactors(int n) {

		// No prime factors for non-positive integers and 1
        if (n <= 1) {
            return 0; 
        }
    
        int sum = 0;
        int sqrtN = (int) Math.sqrt(n);
    
		// initializes logical array as true,
		// which means that we assume all numbers
		// to be prime in the beginning.
        boolean[] isPrime = new boolean[sqrtN + 1];
        Arrays.fill(isPrime, true);
		
		// implements sieve of eratosthenes algorithm 
        for (int p = 2; p * p <= sqrtN; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i <= sqrtN; i += p) {
                    isPrime[i] = false;
                }
            }
        }
		
		//Calculate the sum of the prime factors
        for (int p = 2; p <= sqrtN; p++) {
            if (isPrime[p] && n % p == 0) {
                sum += p;
                while (n % p == 0) {
                    n /= p;
                }
            }
        }
		
		
        if (n > 1) {
            sum += n; // n is prime and we add it to the sum.
        }
    
        return sum;
    }
    
		// We have just implemented the method of checking prime in
		// the CalculatePrimeFactors method. So the following isPrime
		// method is not that necessary. So we commented all of them.

		/*
		private static boolean isPrime(int n) {

            if (n <= 1) return false;

			if (n <= 3) return true;
			
			if (n % 2 == 0 || n % 3 == 0) return false; // an even number is not prime
			
			
			for (int i = 5; i * i <= n; i += 6) {
				if (n % i == 0 || n % (i + 2) == 0) return false;
			}
			
			return true;
		}
		*/

		public static void runComparatorTests() {		
			// System.out.println("isPime(12) = " + isPrime(12)); // not prime
            // System.out.println("isPrime(1) = " + isPrime(1)); // not prime
			// System.out.println("isPime(2) = " + isPrime(2)); // prime
			// System.out.println("isPime(11) = " + isPrime(11)); // prime
			// System.out.println("isPime(999999937) = " + isPrime(999999937)); // prime
			// System.out.println("isPime(23785129) = " + isPrime(23785129)); // not prime
			// System.out.println("isPime(40464469) = " + isPrime(40464469)); // not prime
			
			
			System.out.println("getSumPrimeFactors(11) = " + getSumPrimeFactorsCached(11)); //should be 11
			System.out.println("getSumPrimeFactors(20) = " + getSumPrimeFactorsCached(20)); //should be 7
			System.out.println("getSumPrimeFactors(100) = " + getSumPrimeFactorsCached(100)); //should be 7
			System.out.println("getSumPrimeFactors(999999937) = " + getSumPrimeFactorsCached(999999937)); //should be 999999937
			System.out.println("getSumPrimeFactors(23785129) = " + getSumPrimeFactorsCached(23785129)); //should be 4877
			System.out.println("getSumPrimeFactors(40464469) = " + getSumPrimeFactorsCached(40464469)); //should be 13174
			
		}
		

	}
	
	private static void writeOutResult(String[] sorted, String outputFilename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(outputFilename);
		for (String s : sorted) {
			out.println(s);
		}
		out.close();
	}
	
	
}
