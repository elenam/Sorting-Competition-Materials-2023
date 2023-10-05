import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

/*
 * Generates a specified number of uniformly distributed 
 * integers of a given length, writing them into file. 
 * Keeps leading zeros. 
 *
 * The length is between 3 and 9.
 */

public class DataGenerator {
	private static int seed = 1001; // change the seed to get different data
	private static Random r = new Random(seed);

	public static void main(String[] args) throws FileNotFoundException {

		if (args.length < 3) {
			System.out.println(
					"Please run with two command line arguments: output file name, the length of the numbers, and the number of items");
			System.exit(0);
		}
		String outFileName = args[0];
		int len = Integer.parseInt(args[1]);
		int n = Integer.parseInt(args[2]);
		
		if (len < 3 || len > 9) {
			System.out.println("Length must be between 3 and 9 (inclusive), given " + len);
			System.exit(0);
		}


		String[] data = new String[n];
		
		for(int i = 0; i < n; ++i) {
			StringBuffer num = new StringBuffer();
			
			// generating digits of the numbers
			for(int j = 0; j < len; ++j) {
				char d = (char) ('0' + r.nextInt(10));
				num.append(d);
			}
			
			data[i] = num.toString();
		}



		PrintWriter out = new PrintWriter(outFileName);
		for (String s : data) {
			out.println(s);
		}
		out.close();
	}
	


}