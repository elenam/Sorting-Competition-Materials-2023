import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;
// To run on a single core, compile and then run as:
// taskset -c 0 java GroupN
// To avoid file reading/writing connections to the server, run in /tmp 
// of your lab machine.

public class Group7 {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {

        if (args.length < 2) {
            System.out.println(
                    "Please run with two command line arguments: input and output file names");
            System.exit(0);
        }

        String inputFileName = args[0];
        String outFileName = args[1];

        // Uncomment to test comparator methods
        // SortingCompetitionComparator.runComparatorTests();

        Integer[] toSort = readData(inputFileName); // read data as strings
        int width = getWidth(inputFileName);
        int[] sorted;

        sort(toSort, width); // call the sorting method once for JVM warmup

        Thread.sleep(10); // to let other things finish before timing; adds stability of runs

        long start = System.currentTimeMillis();

        sorted = sort(toSort, width); // sort again

        long end = System.currentTimeMillis();

        System.out.println(end - start);

        writeOutResult(sorted, outFileName, width); // write out the results

    }

    private static Integer[] readData(String inputFileName) throws FileNotFoundException {
        ArrayList<Integer> input = new ArrayList<>(400000000);
        Scanner in = new Scanner(new File(inputFileName));

        while (in.hasNext()) {
            input.add(Integer.parseInt(in.next()));
        }

        in.close();

        return input.toArray(new Integer[0]);
    }

    static Hashtable<Integer, Integer> table = new Hashtable<>();

    private static int[] sort(Integer[] toSort, int width) {
        // if it is going to have a lot of repeats put data in a table to use again
        // later.
        if (toSort.length > (int) Math.pow(10, width)) {
            table = new Hashtable<>((int) Math.pow(10, width));
            table.put(2, 2);
            table.put(3, 3);
            table.put(4, 2);
            table.put(5, 5);
            table.put(6, 5);
            table.put(7, 7);
            table.put(8, 2);
            table.put(9, 3);
            table.put(10, 7);
            table.put(11, 11);
            table.put(12, 5);
            table.put(13, 13);
            table.put(14, 9);
            table.put(15, 8);
            table.put(16, 2);
            table.put(17, 17);
            table.put(18, 5);
            table.put(19, 19);
            table.put(20, 7);
            table.put(21, 10);
            table.put(22, 13);
            table.put(23, 23);
            table.put(24, 5);
            table.put(25, 5);
            table.put(26, 15);
            table.put(27, 3);
            table.put(28, 9);
            table.put(29, 29);
            table.put(30, 10);
            table.put(31, 31);
            table.put(32, 2);
            table.put(33, 14);
            table.put(34, 19);
            table.put(35, 12);
            table.put(36, 5);
            table.put(37, 37);
            table.put(38, 21);
            table.put(39, 16);
            table.put(40, 7);
            table.put(41, 41);
            table.put(42, 12);
            table.put(43, 43);
            table.put(44, 13);
            table.put(45, 8);
            table.put(46, 25);
            table.put(47, 47);
            table.put(48, 5);
            table.put(49, 7);
            table.put(50, 7);
            table.put(51, 20);
            table.put(52, 15);
            table.put(53, 53);
            table.put(54, 5);
            table.put(55, 16);
            table.put(56, 9);
            table.put(57, 22);
            table.put(58, 31);
            table.put(59, 59);
            table.put(60, 10);
            table.put(61, 61);
            table.put(62, 33);
            table.put(63, 10);
            table.put(64, 2);
            table.put(65, 18);
            table.put(66, 16);
            table.put(67, 67);
            table.put(68, 19);
            table.put(69, 26);
            table.put(70, 14);
            table.put(71, 71);
            table.put(72, 5);
            table.put(73, 73);
            table.put(74, 39);
            table.put(75, 8);
            table.put(76, 21);
            table.put(77, 18);
            table.put(78, 18);
            table.put(79, 79);
            table.put(80, 7);
            table.put(81, 3);
            table.put(82, 43);
            table.put(83, 83);
            table.put(84, 12);
            table.put(85, 22);
            table.put(86, 45);
            table.put(87, 32);
            table.put(88, 13);
            table.put(89, 89);
            table.put(90, 10);
            table.put(91, 20);
            table.put(92, 25);
            table.put(93, 34);
            table.put(94, 49);
            table.put(95, 24);
            table.put(96, 5);
            table.put(97, 97);
            table.put(98, 9);
            table.put(99, 14);
            return Arrays.stream(toSort)
                    .mapToLong(i -> ((long) getTabledSumPrimeFactors(i) << 32) - i)
                    .sorted().mapToInt(i -> -((int) i))
                    .toArray();
        } else {
            return Arrays.stream(toSort)
                    // for every value turn it into a long, with big end holding prime factors sum,
                    // and small end holding negative (so large is smaller than small)
                    .mapToLong(i -> ((long) getSumPrimeFactors(i) << 32) - i)
                    // sort the longs, map to int ignoring big end, and inverting small end
                    .sorted().mapToInt(i -> -((int) i))
                    .toArray();
        }
    }

    private static int getTabledSumPrimeFactors(int n) {
        return table.computeIfAbsent(n, num -> (getSumPrimeFactors(num)));
    }

    private static int getSumPrimeFactors(int n) {
        if (n < 2) {
            return 0;
        }
        int factorsSum = 0;

        if (n % 2 == 0) {
            factorsSum += 2;
            while (n % 2 == 0) {
                n /= 2;
            }
        }

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                factorsSum += i;
                while (n % i == 0) {
                    n /= i;
                }
            }
        }

        if (n > 1) {
            factorsSum += n;
        }

        return factorsSum;
    }

    private static void writeOutResult(int[] sorted, String outputFilename, int width) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(outputFilename);
        for (int s : sorted) {
            out.printf("%0" + Integer.toString(width) + "d\n", s);
        }
        out.close();
    }

    private static int getWidth(String inputFileName) {
        try (Scanner in = new Scanner(new File(inputFileName))) {
            return in.nextLine().length();
        } catch (FileNotFoundException e) {
            return 9;
        }

    }
}

