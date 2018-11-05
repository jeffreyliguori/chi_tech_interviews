/*
The question was "Given a file with a bunch of words, return the n most common words in that file.

I did not do any file parsing for this example; I made a list of string words and came up with
the n most common strings in that list.

I did this two ways; one with a PriorityQueue (basically a heap) and one with a Node class and
a map, which has a smaller runtime.

*/

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

class TopNWordsInFile {

    class Node {
	HashSet<String> vals;
	int count;
	Node next;

	public Node(String s, int count) {
	    vals = new HashSet<>();
	    vals.add(s);
	    this.count = count;
	}
    }

    // Using my own node class and the countable property.
    String[] FastGetTopNWordsInFile(List<String> fileList, int n) {
	HashMap<String, Node> counts = new HashMap<>();
	Node most = null;
	Node least = null;
	for (String word : fileList) {
	    if (!counts.containsKey(word)) {
		if (most == null) {
		    most = new Node(word, 1);
		    least = most;
		} else if (least.count == 1) {
		    least.vals.add(word);
		} else {
		    Node curr = new Node(word, 1);
		    curr.next = least;
		    least = curr;
		}
		counts.put(word, least);
	    } else {
		Node curr = counts.get(word);
		curr.vals.remove(word);
		if (curr.next == null) {
		    curr.next = new Node(word, curr.count + 1);
		    curr = curr.next;
		    most = curr;
		} else if (curr.next.count == curr.count + 1) {
		    curr.next.vals.add(word);
		    curr = curr.next;
		} else {
		    curr.next = new Node(word, curr.count + 1);
		    curr = curr.next;
		}
		counts.put(word, curr);
	    }
	}
	String[] out = new String[n];
	int i = 0;
	Node curr = least;
	while (curr != null) {
	    for (String s : curr.vals) {
		out[i] = s;
		i = (i + 1) % n;
	    }
	    curr = curr.next;
	}
	return out;
    }
    
    // Using a PriorityQueue and calculating sizes first.
    List<String> GetTopNWordsInFile(List<String> fileList, int n) {
	List<String> out = new ArrayList<String>();
	HashMap<String, Integer> counts = new HashMap<>();
	for (String word : fileList) {
	    if (!counts.containsKey(word)) counts.put(word, 0);
	    counts.put(word, counts.get(word) + 1);
	}
	PriorityQueue<String> pq = new PriorityQueue<>(
	    new Comparator<String>() {
		public int compare(String s1, String s2) {
		    return counts.get(s1) - counts.get(s2);
		}
	    });
	for (Map.Entry<String, Integer> entry : counts.entrySet()) {
	    pq.offer(entry.getKey());
	    if (pq.size() > n) pq.poll();
	}
	while (pq.size() > 0) out.add(pq.poll());
	return out;
    }

    static List<String> fakeInput(Map<String, Integer> counts,
				  int others) {
	List<String> out = new ArrayList<String>();
	for (Map.Entry<String, Integer> entry : counts.entrySet()) {
	    for (int i = 0; i < entry.getValue(); i++) {
		out.add(entry.getKey());
	    }
	}
	for (int i = 0; i < others; i++) {
	    out.add("word" + i);
	}
	Collections.shuffle(out);
	return out;
    }    

    static double test(int size, int n, int numtrials, boolean fast) {
	TopNWordsInFile test = new TopNWordsInFile();
	Map<String, Integer> map = new HashMap<>();
	List<String> input = fakeInput(map, size);
	double runtime = 0;
	for (int i = 0; i < numtrials; i++) {
	    long start = System.nanoTime();
	    if (fast) {
		test.FastGetTopNWordsInFile(input, n);
	    } else {
		test.GetTopNWordsInFile(input, n);
	    }
	    runtime += (System.nanoTime() - start) / 1000000.0;
	}
	return runtime / numtrials;
    }

    public static void main(String[] args) {
	Map<String, Integer> map = new HashMap<String, Integer>();
	map.put("dog", 100);
	map.put("cat", 101);
	map.put("bird", 102);
	map.put("duck", 103);
	map.put("bat", 104);
	map.put("ant", 105);
	map.put("frog", 106);
	List<String> input = fakeInput(map, 1000000);

	// Simple test to see I can do things right :)
	TopNWordsInFile test = new TopNWordsInFile();
	for (String s : test.GetTopNWordsInFile(input, 5)) {
	    System.out.println(s);
	}
	System.out.println("---");
	for (String s : test.FastGetTopNWordsInFile(input, 5)) {
	    System.out.println(s);
	}

	System.out.println(
            "Avg slow w 100000: " + test(100000, 100, 30, false));
	System.out.println(
            "Avg slow w 200000: " + test(200000, 100, 30, false));
	System.out.println(
            "Avg slow w 300000: " + test(300000, 100, 30, false));

    	System.out.println(
            "Avg fast w 100000: " + test(100000, 100, 30, true));
	System.out.println(
            "Avg fast w 200000: " + test(200000, 100, 30, true));
	System.out.println(
            "Avg fast w 300000: " + test(300000, 100, 30, true));


	System.out.println("--- with large n's ---");
	
	System.out.println(
            "Avg slow w 50k: " + test(500000, 50000, 10, false));
	System.out.println(
            "Avg slow w 100k: " + test(500000, 100000, 10, false));
	System.out.println(
            "Avg slow w 200k: " + test(500000, 200000, 10, false));
	System.out.println(
	    "Avg slow w 400k: " + test(500000, 400000, 10, false));

    	System.out.println(
            "Avg fast w 50k: " + test(500000, 50000, 10, true));
	System.out.println(
            "Avg fast w 100k: " + test(500000, 100000, 10, true));
	System.out.println(
            "Avg fast w 200k: " + test(500000, 200000, 10, true));
	System.out.println(
            "Avg fast w 400k: " + test(500000, 400000, 10, true));
    }
}
