import java.util.Comparator;

public class CompScar implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		// check and see if first key has a lower frequency than second key, will sort by ascending order (low -> high)
		if (getVal(o1) < getVal(o2)) {
			return -1;
		// vice versa here
		} else if (getVal(o1) > getVal(o2)) {
			return 1;
		} 
		
		// if their vals are equal in frequency, compare the keys and see if first string is first alphabetically or not
		return o1.compareTo(o2);
	}
	
	// since we know letters in this case keys cant contain numbers, values can be selected for
	// this method will skip over the key and retrieve the val which will sort by that
	public int getVal(String o) {
		String num = "";
		for (int i = 0; i < o.length(); i++) {
			char c = o.charAt(i);
			if (Character.isDigit(c)) {
				num += Character.toString(c);
			}
		}
		
		return Integer.parseInt(num);
	}
	
}
