import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;

public class Group1 {

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
		int arrayLength = toSort.length;
		int i = 0; //indexing variable
		Pair [] digitPairs = new Pair[arrayLength];
		while (i < arrayLength) {
			Pair newPair = new Pair(getSumPrimeFactors(Integer.parseInt(toSort[i])), toSort[i]);
			digitPairs[i] = newPair;
			i++;
		}
		Arrays.sort(digitPairs);
			
		Pair tmpPair = new Pair(i, null);

		int m = 0; //indexing value for iterating through the array
		int h = 0; //index value for iterating through "partition"
		while (m < arrayLength - 1) {
			while (digitPairs[m] == digitPairs[m+1]) {
				h++;
				m++;
			}
			for (int t = h; t >= 0; t--) {
				if (Integer.parseInt(digitPairs[m-t].getSecond()) < Integer.parseInt(digitPairs[m-t+1].getSecond()) && digitPairs[m-t].getFirst() == digitPairs[m-t+1].getFirst()) {
					tmpPair = digitPairs[m];
					digitPairs[m] = digitPairs[m+1];
					digitPairs[m+1] = tmpPair;
				}
			}
			m++;
		}

		// take our pairs and pull the second, the string we want
		int k = 0; //indexing variable

		
		while (k < arrayLength) {
			Pair givenPair = digitPairs[k];
			toSort[k] = givenPair.getSecond();
			k++;
		}

		//System.out.println(Arrays.toString(toSort));

	}
	
	private static void writeOutResult(String[] sorted, String outputFilename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(outputFilename);
		for (String s : sorted) {
			out.println(s);
		}
		out.close();
	}
	
	//moved out of the comparator so we can use in out sorting method
	public static int getSumPrimeFactors(int n) {
		if (n == 1 || n == 0) return 0; // special cases: don't have prime factors 
		
		int sum = 0;
		// checking all numbers until n, including n itself
		// since n can itself be prime
		for (int i = 2; i <= n; ++i) {
			// if i divides n and is prime, add it to the sum
			if (n % i == 0 && isPrime(i)) {
				sum += i;
			}
		}
		
		return sum;
	}
		
	public static boolean isPrime(int n) {
		if (n == 2) return true;
		
		if (n % 2 == 0) return false; // an even number is not prime
		
		// going up to the largest candidate, checking only odd numbers
		for (int i = 3; i < Math.sqrt(n) + 1; i += 2) {
			if (n % i == 0) return false;
		}
		
		return true;
	}

	// static void bucketSort(Pair digitPairs[], int arrayLength, int n) {

	// 	//int n = 10; //0-9 buckets
	// 	@SuppressWarnings("unchecked")
	// 	Vector<Integer> [] buckets = new Vector[n];
	// 	//creating our buckets
	// 	for (int i = 0; i < n; i++) {
	// 		buckets[i] = new Vector<Integer>();
	// 	}

	// 	//put elements into the buckets based on their sum of prime factors
	// 	for (int i = 0; i < arrayLength; i++) {
	// 		int index = digitPairs[i].getFirst();
	// 		buckets[index].add(digitPairs[i].getSecond());
	// 	}

	// 	//sort the buckets
	// 	for (int i = 0; i < n; i++) {
	// 		Collections.sort((buckets[i]), Collections.reverseOrder());
	// 	}

	// 	//combine the buckets
	// 	int k = 0; //index value
	// 	for (int i = 0; i < n; i++) {
	// 		for (int m = 0; m < buckets[i].size(); m++) {
	// 			toSort[k++] = buckets[i].get(m).toString();
	// 		}
	// 	}
	// }

	

}
