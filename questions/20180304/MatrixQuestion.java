import java.util.Arrays;
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixQuestion {

    // min 0.
    private static int[][] makeRandomSample(int n, int m, int max) {
	int[][] answer = new int[n][m];
	for (int i = 0; i < n; i++) {
	    for (int j = 0; j < n; j++) {
		int min = 0;
		if (i > 0) {
		    min = answer[i-1][j];
		}
		if (j > 0) {
		    min = Math.max(min, answer[i][j-1]);
		}
		answer[i][j] = ThreadLocalRandom.current().nextInt(min, max);
	    }
	}
	return answer;
    }

    public static void main(String[] args) {
	int[][] rand55 = makeRandomSample(5, 5, 100);
	System.out.println(Arrays.deepToString(rand55));

	
    }
}
