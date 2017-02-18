/* WORD LADDER Main.java
 * EE422C Project 3 submission by February 21st
 * Grayson Barrett
 * gmb974
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Spring 2017
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static Set<String> visited;
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("Program Started");
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		ArrayList<String> input = parse(kb);
		//System.out.println(input);
		//NEED TO ADD CHECK FOR /QUIT
		ArrayList<String> ladder = getWordLadderDFS(input.get(0), input.get(1));
		//public static ArrayList<String> getWordLadderBFS(String start, String end)
		printLadder(ladder);
		
		// TODO methods to read in words, output ladder
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		visited = new HashSet<String>(); 
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		String[] temp;
		ArrayList<String> retVal = new ArrayList<String>();
		temp = keyboard.nextLine().split(" ");
		retVal.add(temp[0]);
		if(temp.length == 2)     //could be one word if the /quit command is given
			retVal.add(temp[1]);
		return retVal;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		// TODO some code
		ArrayList<String> ladder = new ArrayList<String>();
		ladder.add(start);
		/*ladder.add("I");
		ladder.add("haven't");
		ladder.add("actually");
		ladder.add("written");
		ladder.add("dfs");
		ladder.add("yet");
		*/
		Set<String> dict = makeDictionary();
		// TODO more code
		ladder.add(end);
		
		return ladder; // replace this line later with real return
	}
	
	public static boolean getWordLadderDFS(ArrayList<String> ladder, String word, String end){
		if(word.equals(end))
			return true;
		
		
		return false;
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		return null; // replace this line later with real return
	}
    
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	public static void printLadder(ArrayList<String> ladder) {
		for(int k=0; k<ladder.size(); k++){
			System.out.println(ladder.get(k));
		}
	}
	// TODO
	// Other private static methods here
}
