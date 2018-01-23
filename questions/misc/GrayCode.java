import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

public class GrayCode {
    // Calculates a valid gray code series of ints for bit size n.
    // e.g. grayCode(2) would return [0, 1, 3, 2].
    public static List<Integer> grayCode(int n) {
	if (n <= 0)
	    return new LinkedList<Integer>(Arrays.asList(new Integer[]{0}));
	List<Integer> out = new ArrayList<Integer>();
	for (List<Boolean> l : populateGrayCodes(n)) {
	    int x = 0;
	    for (boolean b : l) {
		x = x * 2 + (b ? 1 : 0);
	    }
	    out.add(x);
	}
	return out;
    }

    // Recursive helper method for grayCode(n)
    private static List<List<Boolean>> populateGrayCodes(int n) {
	if (n == 1) {
	    List<List<Boolean>> out = new LinkedList<List<Boolean>>();
	    out.add(new LinkedList<Boolean>(
		Arrays.asList(new Boolean[]{false})));
	    out.add(new LinkedList<Boolean>(
	        Arrays.asList(new Boolean[]{true})));
	    return out;
	}
	List<List<Boolean>> firstHalf = populateGrayCodes(n-1);
	List<List<Boolean>> secondHalf = new LinkedList<List<Boolean>>();
	for (List<Boolean> l : firstHalf) {
	    secondHalf.add(new LinkedList<Boolean>(l));
	}
	Collections.reverse(secondHalf);
	firstHalf.forEach(l -> l.add(0, false));
	secondHalf.forEach(l -> l.add(0, true));
	firstHalf.addAll(secondHalf);
	return firstHalf;
    }

    // Returns the num'th gray code if you calculate gray codes the standard
    // way with digits number of digits. Zero indexed.
    // e.g. grayNumfor(1, 2) would return 1 (the second gray code number when
    // there are two digits).
    public static int grayNumFor(int num, int digits) {
	if (digits <= 0) return 0;
	if (num < Math.pow(2, digits - 1)) {
	    return grayNumFor(num, digits - 1);
	}
	return (int)Math.pow(2, digits - 1) +
	    grayNumFor((int)(Math.pow(2, digits) - 1 - num), digits - 1);
    }

    // Print some test values.
   public static void main(String[] args) {
      	int[] gray3ans = new int[]{0, 1, 3, 2, 6, 7, 5, 4};
	List<Integer> gray3 = grayCode(3);
	System.out.println("Size should be 8, is " + gray3.size());
	for (int i = 0; i < gray3ans.length; i++) {
	    System.out.println(
		"Ans: " + gray3ans[i] + ", grayCode: "  + gray3.get(i)
		+ ", numFor: " + grayNumFor(i, 3));
	}
	
	System.out.println("======= Testing on more nums =======");
	List<Integer> gray10 = grayCode(10);
	boolean allRight = true;
	for (int i = 0; i < gray10.size(); i++) {
	    if (gray10.get(i) != grayNumFor(i, 10)) {
		allRight = false;
		System.out.println("Should be " + gray10.get(i)
				   + ", is " + grayNumFor(i, 10));
	    }
	}
	if (allRight) {
	    System.out.println("You got them all right!");
	}
    }
}
