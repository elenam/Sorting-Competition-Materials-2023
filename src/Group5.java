import java.util.*;
import java.io.*;

// To run on a single core, compile and then run as:
// taskset -c 0 java GroupN
// To avoid file reading/writing connections to the server, run in /tmp 
// of your lab machine.

public class Group5 {

	private static HashMap<Integer, Integer> foundSums = new HashMap<>();

	//For testing purposes, add a timing element
	//private static SnippetTimer st = new SnippetTimer();
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

		if (args.length < 2) {
			System.out.println(
					"Please run with two command line arguments: input and output file names");
			System.exit(0);
		}

		String inputFileName = args[0];
		String outFileName = args[1];

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
		foundSums.clear();
		if (toSort.length > 2000) {
			introSort(toSort);
		}
		else {
			Arrays.sort(toSort, Group5::compare);	
		}
	}
		//Move local method variables to static variables to hopefully reduce gc.
		private static int[] foundFactors = new int[10]; 
		private static int factorIndex;
		private static int sum = 0;
		private static int n1;

		
		public static int compare(String s1, String s2) {
		
			if (getSumPrimeFactors(Integer.parseInt(s1)) < getSumPrimeFactors(Integer.parseInt(s2))) {
				return -1;
			} else if (getSumPrimeFactors(Integer.parseInt(s1)) > getSumPrimeFactors(Integer.parseInt(s2))) {
				return 1;
			}
			// got here, so the two sums are equal. Compare the numbers 
			// in the opposite order:
			return Integer.parseInt(s2) - Integer.parseInt(s1);		
		}

		private static int getSumPrimeFactors(int n) {
			factorIndex = 0; //reset the foundFactorsIndex
			if (foundSums.containsKey(n)) {
				return foundSums.get(n);
			}

			sum = 0;
			//Keep track of the remainder after we keep dividing out prime factors of n
			n1 = n;

			if (n < 2) return 0; // special cases: don't have prime factors 

			//If n is prime, just return n
			if (isPrime(n)) {
				foundSums.put(n, n);
				return n;
			}
	
			//Check if even
			if ((n & 1) == 0) {
				foundFactors[factorIndex++] = 2;
				n1 = n1 >> 1;
				sum += 2;
			}

			if (n % 3 == 0) {
				foundFactors[factorIndex++] = 3;
				n1 /= 3;
				sum += 3;
			}


			for (int i=5; i*i <= n; i+=6){
				//Check 5 mod 6
				if (foundSums.containsKey(n1)) {
					foundSums.put(n, sum + foundSums.get(n1));
				}
				if (n % i == 0 && !arrayContains(i) && isPrime(i) ) {
					foundFactors[factorIndex++] = i;
					sum += i;
					n1 /= i;
				}
				if (n % (i + 2) == 0 && !arrayContains(i + 2) && isPrime(i + 2)) {
					foundFactors[factorIndex++] = i + 2;
					sum += i + 2;
					n1 /= i + 2;
				}
			}

			if (n1 != 1 && !isPrime(n1)) {
				for (int i = 0; i < factorIndex; i++) {
					while (n1 % foundFactors[i] == 0) {
						n1 /= foundFactors[i];
					}
				}
				if (!arrayContains(n1)) {
					sum += n1;
				}
			}
			foundSums.put(n, sum);
			return sum;
		}

		private static boolean arrayContains(int x) {
			for (int i = 0; i < factorIndex; i++) {
				if (foundFactors[i] == x) {
					return true;
				}
			}
			return false;
		}

		//Returns the given index, or the index of the next largest element if not present.
		private static int binarySearch(short[] arr, int n) {
			int s = 0;
			int e = arr.length;
			int m = e >> 1;
			while (m != e) {
				if (arr[m] < n) {
					s = m;
					m = (s + e + 1) >> 1;
				}
				else {
					e = m;
					m = (s + e) >> 1;
				}
			}
			return m;
		}

		private static boolean isPrime(int n) {
			if (n < 2719) {
				return shortPrimes[binarySearch(shortPrimes, n)] == n;
			}
			for (short s: shortPrimes) {
				if (s * s > n) {
					return true;
				}
				if (n % s == 0) {
					return false;
				}
			}
			
			// going up to the largest candidate, checking only numbers equivalent to 1 and 5 mod 6.
			for (int i = 2723; i*i <= n; i += 6) {
				if (n % i == 0) return false;
				if (n % (i + 2) == 0) return false;
			}
			return true;
		}
	private static void writeOutResult(String[] sorted, String outputFilename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(outputFilename);
		for (String s : sorted) {
			out.println(s);
		}
		out.close();
	}

	private static void swap(String[] arr, int l, int h) {
		String tmp = arr[l];
		arr[l] = arr[h];
		arr[h] = tmp;
	}

	private static int iLog2(int n) {
		if (n == 0) {
			return 0;
		}
		return 31 - Integer.numberOfLeadingZeros(n);
	}

	private static void introSort(String[] arr) {
		int maxDepth = (int)(Math.log(arr.length) / Math.log(2)) * 2;
		int l = 0, h = arr.length - 1;
		Stack<int[]> s = new Stack<>();
		s.push(new int[]{l, h});
		while (!s.isEmpty()) {
			int[] bounds = s.pop();
			maxDepth--;
			if (bounds[0] > bounds[1]) {
				//Done with this set.
				continue;
			}

			if (bounds[1] - bounds[0] < 24) {
				//Found small set for insertion sort
				continue;
			}
			if (maxDepth == 0) {
				break;
			}
			
			int[] piv = partition(arr, bounds[0], bounds[1]);
			s.push(new int[]{bounds[0], piv[0] - 1});
			s.push(new int[]{piv[0] + 1, piv[1] - 1});
			s.push(new int[]{piv[1], bounds[1]});
		}
		s.clear();
	
		//Quicksort degenerated into heapsort
		if (maxDepth == 0) {
			heapSort(arr, arr.length);
		}
		
		else {
			//Done, quicksort didn't degenerate
			insertionSort(arr);
		}
	}


	public static void heapSort(String[] arr, int count) {
        heapify(arr, count);
        
        int end = count - 1;
        while (end > 0) {
            String tmp = arr[end];
            arr[end] = arr[0];
            arr[0] = tmp;

            end--;

            siftDown(arr, 0, end);
        }
    }

    public static void heapify(String[] arr, int count) {
        int start = parent(count - 1);

        while (start >= 0) {
            siftDown(arr, start, count - 1);
            start--;
        }
    }

    public static void siftDown(String[] arr, int root, int end) {
        while (leftChild(root) <= end) {
            int child = leftChild(root);
            if (child + 1 <= end && compare(arr[child], arr[child + 1]) < 0) {
                child++;
            }

            if (compare(arr[root], arr[child]) < 0) {
                //swap root and child
                String tmp = arr[root];
                arr[root] = arr[child];
                arr[child] = tmp;
                root = child;
            }
            else {
                break;
            }
        }
    }

    public static int parent(int node) {
        return (node - 1) >> 1;
    }

    public static int leftChild(int node) {
        return (node << 1) | 1;
    }

    public static int rightChild(int node) {
        return (node << 1) + 2;
    }

	public static int[] partition(String[] arr, int low, int high) {
		if (compare(arr[low], arr[high]) > 0) {
			swap(arr, high, low);
		}
			  
		// p is the left pivot, and q 
		// is the right pivot.
		int j = low + 1;
		int g = high - 1, k = low + 1;
		String p = arr[low], q = arr[high];
			  
		while (k <= g) 
		{
			  
			// If elements are less than the left pivot
			if (compare(arr[k], p) < 0)
			{
				swap(arr, j, k);
				j++;
			}
			  
			// If elements are greater than or equal
			// to the right pivot
			else if (compare(arr[k], q) >= 0) 
			{
				while (compare(arr[g], q) > 0 && k < g)
					g--;
					  
				swap(arr, g, k);
				g--;
				  
				if (compare(arr[k], p) < 0)
				{
					swap(arr, j, k);
					j++;
				}
			}
			k++;
		}
		j--;
		g++;
		  
		// Bring pivots to their appropriate positions.
		swap(arr, j, low);
		swap(arr, g, high);
		return new int[] { j, g };
	}
		
	public static void insertionSort(String[] arr) {
			int i = 1;
			while (i < arr.length) {
				String x = arr[i];
				int j = i - 1;
				while (j >= 0 && compare(arr[j], x) > 0) {
					arr[j + 1] = arr[j];
					j--;
				}
				arr[j + 1] = x;
				i++;
			}			
		}
	
	//Store all the primes that fit into shorts until we run out of allowed static memory
	public static short[] shortPrimes = new short[] {
			2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127,  131,  137,  139,  
			149,  151,  157,  163,  167,  173,  179,  181,  191,  193,  197,  199,  211,  223,  227,  229,  233,  239,  241,  251,  257, 263, 269, 271, 277,  
			281,  283,  293,  307,  311,  313,  317,  331,  337,  347,  349,  353,  359,  367,  373,  379,  383,  389,  397,  401, 409, 419, 421, 431, 433, 
			439,  443,  449,  457,  461,  463,  467,  479,  487,  491,  499,  503,  509,  521,  523,  541,  547,  557,  563,  569, 571, 577, 587, 593, 599, 
			601, 607, 613, 617, 619, 631, 641, 643,  647,  653,  659,  661,  673,  677,  683,  691,  701,  709, 719,  727,  733,  739,  743,  751,  757,  
			761,  769,  773,  787,  797,  809,  811,  821,  823,  827,  829,  839,  853,  857,  859,  863,  877,
			881,  883,  887,  907,  911,  919,  929,  937,  941,  947,  953,  967,  971,  977,  983,  991,  997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 
			1049, 1051, 1061, 1063, 1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201, 1213, 1217, 
			1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 
			1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499, 1511, 1523, 1531, 1543, 1549, 1553, 1559, 1567, 
			1571, 1579, 1583, 1597, 1601, 1607, 1609, 1613, 1619, 1621, 1627, 1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699, 1709, 1721, 1723, 1733, 1741, 
			1747, 1753, 1759, 1777, 1783, 1787, 1789, 1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873, 1877, 1879, 1889, 1901, 1907, 1913, 1931, 1933, 
			1949, 1951, 1973, 1979, 1987, 1993, 1997, 1999, 2003, 2011, 2017, 2027, 2029, 2039, 2053, 2063, 2069, 2081, 2083, 2087, 2089, 2099, 2111, 2113, 
			2129, 2131, 2137, 2141, 2143, 2153, 2161, 2179, 2203, 2207, 2213, 2221, 2237, 2239, 2243, 2251, 2267, 2269, 2273, 2281, 2287, 2293, 2297, 2309, 
			2311, 2333, 2339, 2341, 2347, 2351, 2357, 2371, 2377, 2381, 2383, 2389, 2393, 2399, 2411, 2417, 2423, 2437, 2441, 2447, 2459, 2467, 2473, 2477,
			2503, 2521, 2531, 2539,	2543, 2549,	2551, 2557,	2579, 2591, 2593, 2609, 2617, 2621,	2633, 2647,	2657, 2659,	2663, 2671,	2677, 2683, 2687, 2689,
			2693, 2699,	2707, 2711,	2713, 2719
		};
}