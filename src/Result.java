import java.util.HashMap;
import java.util.Map.Entry;

public class Result {
	// declare variables being used
	HashMap<String, Integer> words;
	int num;
	String order;
	String[] keyVal;
	
	public Result(HashMap<String, Integer> words, String order, int num) {
		this.words = words;
		this.order = order;
		this.num = num;
		
		// num would be 0 if user did not add a 3rd parameter, so show all results
		// to prevent out of bounds exception, if user entered larger number than size of map, make num equal to max size
		if (num == 0 || num > words.size()) {
			this.num = words.size();
		}
		
		// sort it using info from constructor parameters
		this.sort();
	}
	
	public void sort() {
		// set size of array the same size of hashmao
		// this will be array used for mergesort
		keyVal = new String[words.size()];

		int i = 0;
		// iterate through each key/value pair
		for (Entry<String, Integer> temp : words.entrySet()) {
			// each entry of array will be the key followed by a space followed by the value
			keyVal[i] = temp.getKey() + " ";
			keyVal[i] += Integer.toString(temp.getValue());
			i++;
		}
		
		// Mergesort using order chosen and their respective comparator 
		if (order.equals("Scarcity")) {
			CompScar comp = new CompScar();
			MergeSort.mergeSort(keyVal, comp);
		} else if (order.equals("Frequency")) {
			CompFreq comp = new CompFreq();
			MergeSort.mergeSort(keyVal, comp);
		} else if (order.equals("Name")) {
			CompName comp = new CompName();
			MergeSort.mergeSort(keyVal, comp);
		}
	}
	
	// print out the sorted array made in the constructor
	public void print() {
		System.out.println();
		// print according to how many results the user wanted 
		for (int i = 0; i < num; i++) {
			System.out.println(keyVal[i]);
		}
		System.out.println();
	}
}
