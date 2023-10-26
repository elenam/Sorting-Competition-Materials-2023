
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// To run on a single core, compile and then run as:
// taskset -c 0 java GroupN
// To avoid file reading/writing connections to the server, run in /tmp 
// of your lab machine.

public class Group0 {
	
	private static class Data implements Comparable<Data>{
		private int value;
		private int sumPrimes;
		private String valStr;
		
		public Data(String str) {
			valStr = str;
			value = Integer.parseInt(str);
			if (value == 0 || value == 1) sumPrimes = 0;
			else sumPrimes = getSumPrimeFactors(value);
			
		}
		
		private static int getSumPrimeFactors(int n) {
			if (n == 1 || n == 0) return 0; // special cases: don't have prime factors 
			
			int sum = 0;
			// checking all numbers until n, including n itself
			// since n can itself be prime
			for (int i = 2; i <= n; ++i) {
				// if i divides n and is prime, add it to the sum
				if (n % i == 0 && isPrime(i)) {
					sum += i;
					while (n % i == 0) {
						n = n/i;
					}
				}
			}
			
			return sum;
		}
		
		private static boolean isPrime(int n) {
			if (n == 2) return true;
			
			if (n % 2 == 0) return false; // an even number is not prime
			
			// going up to the largest candidate, checking only odd numbers
			for (int i = 3; i < Math.sqrt(n) + 1; i += 2) {
				if (n % i == 0) return false;
			}
			
			return true;
		}
		
		public static void runTests() {		
			System.out.println("isPime(12) = " + isPrime(12)); // not prime
			System.out.println("isPime(2) = " + isPrime(2)); // prime
			System.out.println("isPime(11) = " + isPrime(11)); // prime
			System.out.println("isPime(999999937) = " + isPrime(999999937)); // prime
			System.out.println("isPime(23785129) = " + isPrime(23785129)); // not prime
			System.out.println("isPime(40464469) = " + isPrime(40464469)); // not prime
			
			
			System.out.println("getSumPrimeFactors(11) = " + getSumPrimeFactors(11)); //should be 11
			System.out.println("getSumPrimeFactors(20) = " + getSumPrimeFactors(20)); //should be 7
			System.out.println("getSumPrimeFactors(100) = " + getSumPrimeFactors(100)); //should be 7
			System.out.println("getSumPrimeFactors(999999937) = " + getSumPrimeFactors(999999937)); //should be 999999937
			System.out.println("getSumPrimeFactors(23785129) = " + getSumPrimeFactors(23785129)); //should be 4877
			System.out.println("getSumPrimeFactors(40464469) = " + getSumPrimeFactors(40464469)); //should be 13174
			
		}

		@Override
		public int compareTo(Data other) {
			if (this.sumPrimes - other.sumPrimes != 0) {
				return this.sumPrimes - other.sumPrimes;
			}
			
			return other.value - this.value;
		}
		
		public String toString() {
			return this.valStr;
		}
		
	}

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

		if (args.length < 2) {
			System.out.println(
					"Please run with two command line arguments: input and output file names");
			System.exit(0);
		}

		String inputFileName = args[0];
		String outFileName = args[1];
		
		// Uncomment to test comparator methods
		//Data.runTests();

		String[] data = readData(inputFileName); // read data as strings
		
		String[] toSort = data.clone(); // clone the data

		Data[] sortedThrowAway = sort(toSort); // call the sorting method once for JVM warmup
		
		toSort = data.clone(); // clone again

		Thread.sleep(10); // to let other things finish before timing; adds stability of runs

		long start = System.currentTimeMillis();

		Data[] sorted = sort(toSort); // sort again

		long end = System.currentTimeMillis();

		System.out.println(end - start);

		writeOutResult(sorted, outFileName); // write out the results

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
	private static Data[] sort(String[] toSort) {
		Data [] data = new Data[toSort.length];
		for (int i = 0; i < data.length; ++i) {
			data[i] = new Data(toSort[i]);
		}
		Arrays.sort(data);
		return data;
		
	}		
	
	private static void writeOutResult(Data[] sorted, String outputFilename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(outputFilename);
		for (Data s : sorted) {
			out.println(s);
		}
		out.close();
	}
		
}
