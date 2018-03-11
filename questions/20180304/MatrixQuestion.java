import java.util.Arrays;
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixQuestion {

    enum Comp { LESS, EQUAL, MORE }

    static class Point {
	int row;
	int col;

	Point(int row, int col) {
	    this.row = row;
	    this.col = col;
	}
    }

    static class CompareCounter {
	Comp compare(int x, int y) {
	    count++;
	    if (x < y) return Comp.LESS;
	    if (x > y) return Comp.MORE;
	    return Comp.EQUAL;
	}

	int getCountAndReset() {
	    int c = count;
	    count = 0;
	    return c;
	}

	int count = 0;
    }
    
    // min 0.
    private static int[][] makeRandomSample(int n, int m, int min) {
	int[][] answer = new int[n][m];
	for (int i = 0; i < n; i++) {
	    for (int j = 0; j < n; j++) {
		if (i > 0) {
		    min = answer[i-1][j];
		}
		if (j > 0) {
		    min = Math.max(min, answer[i][j-1]);
		}
		answer[i][j] =
		    ThreadLocalRandom.current().nextInt(min + 1, min + 100);
	    }
	}
	return answer;
    }

    public static boolean dumbSolve(int[][] array, int x, CompareCounter c) {
	for (int[] i : array) {
	    for (int j : i) {
		if (c.compare(j, x) == Comp.EQUAL) return true;
	    }
	}
	return false;
    }

    public static boolean linearSolve(int[][] array, int x, CompareCounter c) {
	int startRow = 0;
	int startCol = array[0].length - 1;
	while (startCol >= 0 && startRow < array.length) {
	    Comp comp = c.compare(array[startRow][startCol], x);
	    if (comp == Comp.EQUAL) {
		return true;
	    } else if (comp == Comp.LESS) {
		startRow++;
	    } else {
		startCol--;
	    }
	}
	return false;
    }

    // Proof of concept: this only works on square 
    public static boolean logSolve(int[][] array, int x, CompareCounter c) {
	return logSolve(
	    array, x, c, 0, 0, array.length - 1, array[0].length - 1);
    }
    
    public static boolean logSolve(int[][] array, int x, CompareCounter c,
				   int startRow, int startCol, int endRow,
				   int endCol) {
	//System.out.println("Points: (" + startRow + ", " + startCol + "), ("
	//		   + endRow + ", " + endCol + ")");
	// Find the inflection point on the diagonal.
	Point p = diagBinSearch(
   	    array, x, c, startRow, startCol, endRow, endCol);
	//System.out.println("Point: " + p.row + " , " + p.col);
	if (c.compare(array[p.row][p.col], x) == Comp.EQUAL) {
	    return true;
	}
	if (startRow == endRow) {
	    return binSearch(array[startRow], x, c, startCol, endCol);
	}
	if (startCol == endCol) {
	    return colBinSearch(array, x, c, startRow, endRow, startCol);
	}
	return logSolve(array, x, c, startRow, p.col, p.row, endCol) ||
	       logSolve(array, x, c, p.row, startCol, endRow, p.col);
    }

    public static boolean colBinSearch(int[][] array, int x, CompareCounter c,
				       int startRow, int endRow, int col) {
	if (startRow == endRow) {
	    return c.compare(array[startRow][col], x) == Comp.EQUAL;
	}
	int row = (startRow + endRow) / 2;
	Comp comp = c.compare(array[row][col], x);
	if (comp == Comp.EQUAL) {
	    return true;
	}
	if (endRow == startRow + 1) {
	    return c.compare(array[row + 1][col], x) == Comp.EQUAL;
	}
	if (comp == Comp.LESS) {
	    return colBinSearch(array, x, c, row + 1, endRow, col);
	}
	return colBinSearch(array, x, c, startRow, row - 1, col);
    }

    public static boolean binSearch(int[] array, int x, CompareCounter c,
		                    int start, int end) {
	//System.out.println(start + ", " + end);
	if (start == end) {
	    return c.compare(array[start], x) == Comp.EQUAL;
	}
	int row = (start + end) / 2;
	Comp comp = c.compare(array[row], x);
	if (comp == Comp.EQUAL) {
	    return true;
	}
	if (end == start + 1) {
	    return c.compare(array[row + 1], x) == Comp.EQUAL;
	}
	if (comp == Comp.LESS) {
	    return binSearch(array, x, c, row + 1, end);
	}
	return binSearch(array, x, c, start, row - 1);
    }

    public static Point diagBinSearch(int[][] array, int x, CompareCounter c,
		      		      int startRow, int startCol, int endRow,
				      int endCol) {
	int rowsLeft = endRow - startRow + 1;
	int colsLeft = endCol - startCol + 1;
	int diagStartRow = startRow;
	int diagStartCol = startCol;
	int diagEndRow = endRow;
	int diagEndCol = endCol;
	if (rowsLeft > colsLeft) {
	    diagStartRow += (rowsLeft - colsLeft) / 2;
	    diagEndRow = diagStartRow + colsLeft - 1;
	} else {
	    diagStartCol += (colsLeft - rowsLeft) / 2;
	    diagEndCol = diagStartCol + rowsLeft - 1;
	}
	return diagBinSearchF(
	    array, x, c, diagStartRow, diagStartCol, diagEndRow, diagEndCol);
    }

    public static Point diagBinSearchF(int[][] array, int x, CompareCounter c,
   				       int startRow, int startCol, int endRow,
  				       int endCol) {
	if (startRow == endRow) return new Point(startRow, startCol);
	int row = (endRow + startRow) / 2;
	int col = (endCol + startCol) / 2;
	Comp comp = c.compare(array[row][col], x);
	if (comp == Comp.EQUAL) {
	    return new Point(row, col);
	}
	if (endRow == startRow + 1) {
	    if (c.compare(array[endRow][endCol], x) != Comp.MORE)
		return new Point(endRow, endCol); 
	    return new Point(row, col);
	}
	if (comp == Comp.LESS) {
	    return diagBinSearchF(
		array, x, c, row, col, endRow, endCol);
	}
	return diagBinSearchF(
	    array, x, c, startRow, startCol, row - 1, col - 1);
    }

    public static void main(String[] args) {
	int[][] rand55 = makeRandomSample(5, 5, 0);
	System.out.println(Arrays.deepToString(rand55));
	int[][] rand = makeRandomSample(1000, 1000, Integer.MIN_VALUE);
	CompareCounter counter = new CompareCounter();

	System.out.println(" ==== Dumb solution O(mn) ====");
	System.out.println(
	    "Original: " + dumbSolve(rand, rand[999][999], counter));
	System.out.println("count: " + counter.getCountAndReset());
	rand = makeRandomSample(2000, 2000, Integer.MIN_VALUE);
	System.out.println(
	    "2x: " + dumbSolve(rand, rand[1999][1999], counter));
	System.out.println("count: " + counter.getCountAndReset());
	System.out.println(
	    "Not present: " + dumbSolve(rand, rand[1999][1999] + 1, counter));
	System.out.println("count: " + counter.getCountAndReset());

	System.out.println(" ==== Linear solution O(m + n) ====");
	System.out.println(
	    "Original: " + linearSolve(rand, rand[999][0], counter));
	System.out.println("count: " + counter.getCountAndReset());
	System.out.println(
	    "2x: " + linearSolve(rand, rand[1999][1999], counter));
	System.out.println("count: " + counter.getCountAndReset());
	System.out.println("Not present: " +
			   linearSolve(rand, rand[1999][1999] + 1, counter));
	System.out.println("count: " + counter.getCountAndReset());

    	System.out.println(" ==== Log solution O(log(mn) ====");
	System.out.println(
	    "Original: " + logSolve(rand, rand[999][0], counter));
	System.out.println("count: " + counter.getCountAndReset());
	System.out.println(
	    "2x: " + logSolve(rand, rand[1999][1999], counter));
	System.out.println("count: " + counter.getCountAndReset());
	System.out.println("Not present: " +
			   logSolve(rand, rand[1999][1999] + 1, counter));
	System.out.println("count: " + counter.getCountAndReset());
	// Test it can find everything.
	for (int[] i : rand) {
	    for (int j : i) {
		if (!logSolve(rand, j, counter)) {
		    System.out.println("Log fail " + j);
		}
	    }
	}
    }
}
