import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// To run on a single core, compile and then run as:
// taskset -c 0 java GroupN
// To avoid file reading/writing connections to the server, run in /tmp 
// of your lab machine.

public class Group4 {

    public static void main(String[] args) throws InterruptedException {
        String fileName = args[0];
        String outFileName = args[1];
        String[] originalData = readData(fileName);
        String[] toSort;

        // JVM warmup
        toSort = originalData.clone();
        sort(toSort);

        // Pause for 10ms
        Thread.sleep(10);

        // Timing the sorting process
        toSort = originalData.clone();
        long startTime = System.currentTimeMillis();
        String[] sorted = sort(toSort);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        writeOutResult(sorted, outFileName);  // write out the results
    }


	public static String[] readData(String file) {
		List<String> dataList = new ArrayList<>();
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			String line;
			while ((line = input.readLine()) != null) {
				dataList.add(line.trim());
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(99);
		}
		// Convert the list to an array and return
		return dataList.toArray(new String[0]);
	}

    public static void writeOutResult(String[] sorted, String file) {
        try {
            PrintWriter out = new PrintWriter(file);
            // Calculate the maximum number of digits
            int maxDigits = Arrays.stream(sorted)
                                  .mapToInt(String::length)
                                  .max()
                                  .orElse(0);
            for (int i = 0; i < sorted.length; i++) {
                out.println(String.format("%0" + maxDigits + "d", Integer.parseInt(sorted[i])));
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(98);
        }
    }    


    // Your sorting algorithm goes here.
    // You may call other methods and use other classes.
    // You may ALSO add methods to the Data Class (or any other class) BUT...
    // DO NOT REMOVE OR CHANGE THIS METHOD'S SIGNATURE OR ITS TYPE

    public static String[] sort(String[] data) {

        // Find the maximum number in the data
        int maxNumberInData = Arrays.stream(data).mapToInt(Integer::parseInt).max().getAsInt();

        // Calculate the square root of the maximum number, which will be used to precompute primes
        int limit = (int) Math.sqrt(maxNumberInData);

        // Precompute prime numbers up to the calculated limit
        List<Integer> primes = precomputePrimes(limit);

        // Initialize a cache to store the sum of prime factors for numbers to speed up computations
        HashMap<Integer, Integer> cache = new HashMap<>();
        
        // Step 1: Initialize the map (buckets) for bucketing numbers based on their sum of prime factors
        HashMap<Integer, List<String>> buckets = new HashMap<>();

        // Step 2: Populate the buckets
        for (String numberStr : data) {
            int number = Integer.parseInt(numberStr);

            // Compute the sum of prime factors for the current number
            int sumPrimeFactors = getSumPrimeFactors(number, primes, cache);

            // Add the number to the appropriate bucket in the 'buckets' map
            buckets.computeIfAbsent(sumPrimeFactors, k -> new ArrayList<>()).add(numberStr);
        }

        // Step 3: Sort each bucket in descending order
        for (List<String> bucket : buckets.values()) {
            Collections.sort(bucket, Comparator.reverseOrder());
        }

        // Step 4: Merge sorted buckets into a single list
        // S4-1. get the keys (sum of prime factors) and sort them in ascending order
        Integer[] keysSorted = buckets.keySet().toArray(new Integer[0]);
        Arrays.sort(keysSorted);

        // S4-2. Initialize the array to store the final sorted data
        String[] sortedData = new String[data.length];
        int index = 0;

        // S4-3. Populate the sortedData array by merging the sorted buckets
        for (Integer key : keysSorted) {
            for (String numberStr : buckets.get(key)) {
                sortedData[index++] = numberStr;
            }
        }

        // Return the final sorted data
        return sortedData;
    }


    // Method: Sieve of Eratosthenes algorithm to precompute prime numbers up to the given limit
    
    public static List<Integer> precomputePrimes(int limit) {

        // Create a boolean array to identify prime numbers up to the given limit
        boolean[] isPrime = new boolean[limit + 1];

        // Initially set all values to true, indicating that all numbers are prime
        Arrays.fill(isPrime, true);
        isPrime[0] = false;  // 0 is not prime
        isPrime[1] = false;  // 1 is not prime
        
        // Implement the Sieve of Eratosthenes algorithm to identify prime numbers
        for (int i = 2; i <= Math.sqrt(limit); i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= limit; j += i) {
                    isPrime[j] = false;  // Mark multiples of i as non-prime
                }
            }
        }
        
        // Collect all prime numbers into a list
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }

        // Return the list of prime numbers
        return primes;
    }    


    // Method: Computes the sum of unique prime factors of a given number. 
    /* It uses a cache to store previously computed results to optimize the process for repeated numbers. 
     * If the sum for a particular number is already in the cache, it's returned directly. 
     * Otherwise, the function calculates the sum by iterating through precomputed prime numbers 
     * and checking which of them are factors of the given number.
    */
    
    public static int getSumPrimeFactors(int number, List<Integer> primes, HashMap<Integer, Integer> cache) {
        
        // If the sum of prime factors for the given number is already computed and cached, return it
        if (cache.containsKey(number)) {
            return cache.get(number);
        }
        
        int sum = 0;  // Initialize the sum of prime factors
        int originalNumber = number;  // Store the original number to cache the result later

        // Iterate through the precomputed prime numbers to find the prime factors of the given number
        for (int prime : primes) {

            // If the current prime number is greater than the given number, break out of the loop
            if (prime > number) {
                break;
            }

            // If the given number is divisible by the current prime number, add it to the sum
            if (number % prime == 0) {
                sum += prime;

                // Divide the given number by the current prime number as long as it remains divisible
                while (number % prime == 0) {
                    number /= prime;
                }
            }
        }
        
        // If after the above process, the given number is greater than 1, it is a prime number and added to the sum
        if (number > 1) {
            sum += number;
        }
        
        // Cache the computed sum for the given number
        cache.put(originalNumber, sum);

        // Return the computed sum
        return sum;
    }
    

    // The `Pair` class is a simple data structure that pairs an integer with its sum of prime factors. 
    // It's used in the bucket sort approach, where numbers are grouped based on their sum of prime factors.
    
    static class Pair {
        int number;  // The original number
        int sum;  // The sum of its prime factors

        // Constructor to initialize the Pair object
        Pair(int number, int sum) {
            this.number = number;
            this.sum = sum;
        }
    }
}
