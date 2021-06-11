import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PopularWords {

	public static void main(String[] args) {
		// Declare objects used 
		
		// hashmap used for storing words and its occurrence
		HashMap<String, Integer> words;
		// retrieving parameters file
		File parInp = new File("data/parameters.txt");
		// Initializing scanner used to read parameters
		Scanner parFile = null;
		
		//store parameters in arraylist (since it can vary between 2 and 3 entries due to last parameter being optional)
		ArrayList<String> parameters;
		
		try {
			parFile = new Scanner(parInp);
			parameters = new ArrayList<String>();
			words = new HashMap<String, Integer>();
			
			// read through each line of the parameters file
			while (parFile.hasNextLine()) {
				// retrieve the line
				String temp = parFile.nextLine();
				
				// replace any spaces with blanks to prevent errors
 				temp = temp.replaceAll(" ", "");
 				
 				// if user leaves empty lines between parameters, skipping here will prevent errors from inputting empty entries
				if (temp.isEmpty()) {
					continue;
				}
				
				// add to array list
				parameters.add(temp);
			}
			
			
			// first index of arraylist should be the file name
			String file = parameters.get(0);
			
			// parse file and store all the information into the words map inputed using method
			boolean foo = parseFile(file, words);
			
			// if method returned false, that means file was not found.
			if (!foo) {
				// exit jvm as file isn't found
				System.exit(0);
			}
			
			// 2nd index of arraylist should be the order the user would like to organize the text file
			String order  = parameters.get(1).toUpperCase();
			
			// make sure its equal to one of the 3 options
			if (!(order.equals("SCARCITY") || order.equals("FREQUENCY") || order.equals("NAME"))) {
					System.out.println("Invalid sorting entry (please enter 'Scarcity', 'Fequency', or 'Name'), try again.\n");
					System.exit(0);
			}
			
			// capitalize first letter and lower case the rest just for aesthetics
			order = order.substring(0,1).toUpperCase() + order.substring(1).toLowerCase();
			
			// Initialize num first since arraylist since it might not have a 3rd entry
			int num = 0;
			
			// if parameters had a 3rd entry
			if (parameters.size() >= 3) {
				// try catch to catch errors if non-digits used
				try {
					num = Integer.parseInt(parameters.get(2));
				} catch (Exception e) {
						System.out.println("Entry must be a number, try again\n");
						System.exit(0);
				}
				// make sure its not a negative or 0
				if (num <= 0) {
						System.out.println("Number must be positive, try again.\n");
						System.exit(0);
				}
			} 
			
			// use information gathered to create an object that collects, sorts, and prints out the results based on the three parameters
			Result res = new Result(words, order, num);
			
			// print out results
			System.out.println("Results from '" + file + "', ordering by " + order + ", showing " + res.num + " results:");
			res.print();
			
		} catch (Exception FileNotFoundException) {
			// if parameters file isnt found for some reason
			System.out.print("File not found, make sure parameters.txt is present in /data folder.");
			System.exit(0);
		}
		
		// close scanner
		parFile.close();
		
	}
	
	// parse file inputed and put unique words into hashmap in 2nd parameter
	public static boolean parseFile(String name, HashMap<String, Integer> words) {
		Scanner fileInput = null;
		File reading = new File ("data/" + name);
		
		// setup scanner for reading
		try {
			fileInput = new Scanner(reading);
		}
		catch (FileNotFoundException e) {
			System.out.println("File doesn't exist in /data, try again.");
			
			// return false here as file isn't found
			return false;
		}		
		
		// read each line of file
		while (fileInput.hasNextLine()) {
			// read line from file
			String line = fileInput.nextLine();
			
			String temp = "";
			
			// go through each char of the line
			for (int i = 0; i < line.length(); i++) {
				// convert char index to string
				String c = Character.toString(line.charAt(i));
				
				// see if char is a letter or a word connector
				if (Character.isLetter(c.charAt(0)) || "_-'".contains(c)) {
					// if its the last letter of the line, increment the string as this is the last iteration before for loop ends
					if (i == line.length()-1) {
						// accumulate last char
						temp += c;
						
						// validate/modify string to fit rules
						temp = validate(temp);
						
						// increment into hashmap if it follows the rules
						if (!temp.equals("")) {
							increment(words, temp);
							temp = "";
						}
						continue;
					}
					
					// if there are two word connectors in a row, increment current word and reset the temp string for next word
					if ("_-'".contains(c) && "_-'".contains(Character.toString(line.charAt(i+1)))) {
						temp = validate(temp);
						
						if (!temp.equals("")) {
							increment(words, temp);
							temp = "";
						}
						continue;
					}
					
					// accumulate to temp string if neither of the if statements above apply to the char
					temp += c;
				
				// a delimiter is here so previous chars must accumulate (if there was any)
				} else {
					// analyze and modify string given rules from doc
					temp = validate(temp);
					
					// if validate returned a blank string, it was invalid so just ignore and move on
					if (!temp.equals("")) {
						// if it wasnt blank, increment/accumulate into hash map
						increment(words, temp);
						temp = "";
					}
				}
			}
		}
		// close reading scanner
		fileInput.close();
		
		// if it successfully reached this part, it parsed file successfully
		return true;
	}
	
	public static String validate(String temp) {
		// make everything lower case
		temp = temp.toLowerCase();
		
		// check if it contains at least one alphabetical char using regex
		if (!temp.matches(".*[a-zA-Z]+.*")) {
			// return a blank string as this string was invalid and has no letters
			return "";
		}
		
		// putting in a while loop in-case there's multiple word connectors in the beginning
		while (true) {
			// check to see if a word connector is in the beginning
			if ("_-'".contains(Character.toString(temp.charAt(0)))) {
				// change the first index to cut off the connector
				temp = temp.substring(1);
				//re-check and see if there is another word connector in the beginning 
				continue;
			}
			// reaches here once there are no word connectors in the beginning 
			break;
		}
		
		while (true) {
			// check to see if a word connector is at the end
			if ("_-'".contains(Character.toString(temp.charAt(temp.length()-1)))) {
				// remove last index containing connector
				temp = temp.substring(0, temp.length()-1);
				continue;
			}
			break;
		}
		
		// return the validated/modified string
		return temp;
	}
	
	// increments or accumulates 'temp' string into 'map'
	public static void increment(HashMap<String, Integer> map, String temp) {
		// check first if key already exists
		if (map.containsKey(temp)) {
			// retrieve val from key and add 1
			int val = map.get(temp) + 1;
			
			// add the new accumulated val into the map
			map.replace(temp, val);
		} else {
			// if key doesnt exist yet, add it with val of 1
			map.put(temp, 1);
		}
	}
	

}
