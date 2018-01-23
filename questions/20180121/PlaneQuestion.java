/* To run (larger inputs gave me heap space problems):
javac PlaneQuestion.java && java PlaneQuestion 40000000 80000000 160000000 320000000 640000000

output:
Should be 3, is: 3
Should be 4, is: 4
Count sort: Answer: 28534294, time: 625
Quick sort: Answer: 28534294, time: 803
Count sort: Answer: 57056897, time: 1133
Quick sort: Answer: 57056897, time: 1479
Count sort: Answer: 114140223, time: 2285
Quick sort: Answer: 114140223, time: 3004
Count sort: Answer: 228247892, time: 4522
Quick sort: Answer: 228247892, time: 5912
*/


import java.lang.Math;
import java.util.Arrays;
import java.util.Random;

public class PlaneQuestion {
  // Assume times are [0, 2400) where # corresponds to military time (note: not all #s in that range
  // are valid, i.e. 2299, but we're making this work for all #s in range for simplicity). Will
  // throw index out of bounds if any time not in that range. Note midnight is 0, not 2400.
  public static void inPlaceCountSortTimes(int[] times) {
    int[] timeCounts = new int[2360];
    for (int i : times) {
      timeCounts[i]++;
    }
    int ind = 0;
    for (int i = 0; i < timeCounts.length; i++) {
      while (timeCounts[i] > 0) {
        times[ind] = i;
        ind++;
        timeCounts[i]--;
      }
    }
  }

  // TODO: no input validation. Should check arrivals < departures, all valid times in military time
  // format, same length arrays.
  // Only use count sort if you have guarantees that arrivals/departures are in specified range. The
  // normal sort should work for arbitrary times (i.e. unix timestamps over many days).
  public static int maxNumGates(int[] arrivals, int[] departures, boolean countSort) {
    if (countSort) {
      // Runtime is O(n) because of count sort & fixed time intervals. Should use normal sorting if
      // we don't have fixed time interval guarantee, hence O(n log n).
      inPlaceCountSortTimes(arrivals);
      inPlaceCountSortTimes(departures);
    } else {
      Arrays.sort(arrivals);
      Arrays.sort(departures);
    }
    int maxNumGates = 0;
    int departureIndex = 0;
    for (int i = 0; i < arrivals.length; i++) {
      while (departures[departureIndex] < arrivals[i]) departureIndex++;
      maxNumGates = Math.max(maxNumGates, i - departureIndex + 1);
    }
    return maxNumGates;
  }


  // Code to actually run our method and test it.
  public static void main(String[] args) {
    int[] threeArrivals = new int[]{100, 200, 300, 400, 500, 600, 700};
    int[] threeDepartures = new int[]{300, 400, 500, 600, 700, 800, 900};
    System.out.println("Should be 3, is: " + maxNumGates(threeArrivals, threeDepartures, true));

    int[] fourArrivals = new int[]{100, 130, 200, 230, 400, 430, 600, 700};
    int[] fourDepartures = new int[]{415, 145, 415, 415, 430, 700, 730, 800};
    System.out.println("Should be 4, is: " + maxNumGates(fourArrivals, fourDepartures, true));

    Random rand = new Random();
    for (String s : args) {
      int numToTest = Integer.parseInt(s);

      // Generate random arrival/departure times.
      int[] arrivals = new int[numToTest];
      int[] departures = new int[numToTest];
      for (int i = 0; i < numToTest; i++) {
        int arrivalHour = rand.nextInt(24);
        int departureHour = arrivalHour + rand.nextInt(24 - arrivalHour);
        int arrivalMinute = rand.nextInt(60);
        int departureMinute = rand.nextInt(60);
        if (arrivalHour == departureHour) {
          departureMinute = arrivalMinute + rand.nextInt(60 - arrivalMinute);
        }
        arrivals[i] = arrivalHour * 100 + arrivalMinute;
        departures[i] = departureHour * 100 + departureMinute;
      }

      // Run and time count sort vs. normal sorting.
      long startTime = System.currentTimeMillis();
      int answer = maxNumGates(arrivals, departures, true);
      System.out.println(
          "Count sort: Answer: " + answer + ", time: " + (System.currentTimeMillis() - startTime));

      startTime = System.currentTimeMillis();
      answer = maxNumGates(arrivals, departures, true);
      System.out.println(
          "Quick sort: Answer: " + answer + ", time: " + (System.currentTimeMillis() - startTime));
    }
  }
}
