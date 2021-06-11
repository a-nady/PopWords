import java.util.Comparator;

public class CompName implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		// compare alphabetically letter by letter and see which is first
		// it will never reach to the point of touching the vals as the keys are unique so a boolean statement will be made before then (due to the space in between key and val)
		if (o1.compareTo(o2) < 0) {
			return -1;
		// vice versa here
		} else if (o1.compareTo(o2) > 0) {
			return 1;
		}
		
		// They're equal at this point, which would return 0 (in theory, not possible since keys are unique)
		return o1.compareTo(o2);
		
	}

}