/*
NOTE: There is a better solution using a mathematical formula that I haven't implemented.
 */

import java.lang.Math;
import java.math.BigInteger;

public class GridQuestion {

    // Dumb version, does not protect against overflow, exponential time/space complexity.
    static int recursive(int m, int n) {
	if (m < 1 || n < 1) return -1;
	if (m == 1 || n == 1) return 1;
	return recursive(m - 1, n) + recursive(m, n - 1);	
    }

    // Memoized above, O(m*n) space/time complexity, still overflow problems.
    static int memoized(int m, int n) {
	if (m < 1 || n < 1) return -1;
	int[][] arr = new int[m][n];
	for (int i = 0; i < arr.length; i++) {
	    arr[i][n-1] = 1;
	}
	for (int i = 0; i < arr[0].length; i++) {
	    arr[m-1][i] = 1;
	}
	for (int i = m - 2; i >= 0; i--) {
	    for (int j = n - 2; j >= 0; j--) {
		arr[i][j] = arr[i+1][j] + arr[i][j+1];
	    }
	}
	return arr[0][0];
    }

    // Memoized, avoids overflow problems.
    static BigInteger memoizedBigInt(int m, int n) {
	if (m < 1 || n < 1) return new BigInteger("-1");
	BigInteger[][] arr = new BigInteger[m][n];
	for (int i = 0; i < arr.length; i++) {
	    arr[i][n-1] = BigInteger.ONE;
	}
	for (int i = 0; i < arr[0].length; i++) {
	    arr[m-1][i] = BigInteger.ONE;
	}
	for (int i = m - 2; i >= 0; i--) {
	    for (int j = n - 2; j >= 0; j--) {
		// NOTE: BigIntegers are immutable, so these add calls construct a new one.
		arr[i][j] = arr[i+1][j].add(arr[i][j+1]);
	    }
	}
	return arr[0][0];
    }

    public static void main(String[] args) {
	long time = System.currentTimeMillis();
	System.out.println("RECURSIVE: Should be -1, is: " + recursive(3, 2));
	System.out.println("RECURSIVE: Should be 1, is: " + recursive(1, 5));
	System.out.println("RECURSIVE: Should be 1, is: " + recursive(5, 1));
	System.out.println("RECURSIVE: Should be 3, is: " + recursive(3, 2));
	System.out.println("RECURSIVE: Should be 818809200, is: " + recursive(15, 20));
	System.out.println("RECURSIVE TOTAL RUNTIME: " + (System.currentTimeMillis() - time));

	time = System.currentTimeMillis();
	System.out.println("MEMOIZED: Should be -1, is: " + memoized(3, 2));
	System.out.println("MEMOIZED: Should be 1, is: " + memoized(1, 5));
	System.out.println("MEMOIZED: Should be 1, is: " + memoized(5, 1));
	System.out.println("MEMOIZED: Should be 3, is: " + memoized(3, 2));
	System.out.println("MEMOIZED: Should be 818809200, is: " + memoized(15, 20));
	System.out.println("MEMOIZED TOTAL RUNTIME: " + (System.currentTimeMillis() - time));

	time = System.currentTimeMillis();
	System.out.println("NO OVERFLOW: Should be -1, is: " + memoizedBigInt(3, 2));
	System.out.println("NO OVERFLOW: Should be 1, is: " + memoizedBigInt(1, 5));
	System.out.println("NO OVERFLOW: Should be 1, is: " + memoizedBigInt(5, 1));
	System.out.println("NO OVERFLOW: Should be 3, is: " + memoizedBigInt(3, 2));
	System.out.println("NO OVERFLOW: Should be 818809200, is: " + memoizedBigInt(15, 20));
	System.out.println("NO OVERFLOW TOTAL RUNTIME: " + (System.currentTimeMillis() - time));
	
	// Lets test overflow now.
	System.out.println("MEMOIZED: Should overflow, is: " + memoized(100, 100));
	System.out.println("MEMOIZED: Should be big, is: " + memoizedBigInt(100, 100));	
    }
}
