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
	static ArrayList<String> visited;
	static int count;
	static ArrayList<String> queue;
	static ArrayList<Integer> parents;
	static int index;
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("Program Started");
		initialize();
		boolean done = false;
		
		while (!done){
		
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
			ArrayList<String> input = parse(kb);
			if(input.size() == 2){
				//System.out.println(input);
				//NEED TO ADD CHECK FOR /QUIT
				//ArrayList<String> ladder = getWordLadderDFS(input.get(0), input.get(1));
				ArrayList<String> ladder = getWordLadderBFS(input.get(0), input.get(1));
				printLadder(ladder);
				visited.clear();
				queue.clear();
			}
			
			if(input.get(0).equals("/quit"))
				done = true;
		}
		
		// TODO methods to read in words, output ladder
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		visited = new ArrayList<String>(); 
		queue = new ArrayList<String>();
		parents = new ArrayList<Integer>();
		index = 0;
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		
		ArrayList<String> retVal = new ArrayList<String>();
		int index = 0;
		String input = keyboard.nextLine();
		while(!Character.isLetter(Character.valueOf(input.charAt(index)))){
			index++;
		}
		
		if(index != 0){
			if(input.substring(index-1, index+4).equals("/quit")){
				retVal.add("/quit");
				return retVal;
			}
		}
		retVal.add(input.substring(index, index+5).toUpperCase());
	
		index += 5;
		while(!Character.isLetter(Character.valueOf(input.charAt(index))) && 
			index < input.length()){
			index++;
		}
		
		if(index < input.length()){
			retVal.add(input.substring(index, index+5).toUpperCase());
		}
		
		return retVal;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		// TODO some code
		ArrayList<String> ladder = new ArrayList<String>();
		Set<String> dict = makeDictionary();
		ladder.add(start);
		visited.add(start);
		ladder.add(end);
		count = 0;
		getWordLadderDFS(dict, ladder, start, end);
		
		return ladder; // replace this line later with real return
	}
	
	public static boolean getWordLadderDFS(Set<String> dict, ArrayList<String> ladder, String word, String end){
		if(word.equals(end))
			return true;
		for(int k=0; k<5; k++){
			for(int i = 0; i<26; i++){
				String newWord = word.substring(0, k) + ((char) (65 + i)) + word.substring(k+1,5);
				if(dict.contains(newWord) && !visited.contains(newWord)){
					visited.add(newWord);
					if(getWordLadderDFS(dict, ladder, newWord, end)) {
                        if (!newWord.equals(end)) {
                            ladder.add(newWord);
                            count++;
                        }
                        return true;
                    }
				}
			}
		}
		return false;
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
    	ArrayList<String> ladder = new ArrayList<String>();
    	count = 0;
    	ladder.add(start);
    	visited.add(start);
    	if(start.equals(end)){
    		ladder.add(end);
			return ladder;
    	}
		Set<String> dict = makeDictionary();
		index = 0;
		parents.clear();
		
		parents.add(-1);
		exploreFrontier(start, ladder, dict, end);
		index++;
		while(queue.size() > 0){
			exploreFrontier(queue.get(0), ladder, dict, end);
			if(queue.size() > 0)
				queue.remove(0);
			index++;
		}
		// TODO more code
		if(ladder.size() == 1)
			ladder.add(end);
		
		return ladder; // replace this line later with real return
	}
    
    public static void exploreFrontier(String word, ArrayList<String> ladder, Set<String> dict, String end){
    	for(int k=0; k<5; k++){
			for(int i = 0; i<26; i++){
				String newWord = word.substring(0, k) + ((char) (65 + i)) + word.substring(k+1,5);
				if(dict.contains(newWord) && !visited.contains(newWord)){
					visited.add(newWord);
					queue.add(newWord);
					parents.add(index);
					if(newWord.equals(end)){
						//ladder.add(newWord);
						ladder.add(visited.get(visited.size()-1)); //adds the end word
						while(parents.get(index) != -1){
							ladder.add(visited.get(index));
							index = parents.get(index);
							count++;
						}
						queue.clear();
					}
				}
			}
		}
    }
    
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt")); //changed file
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
		
		ladder.add(ladder.get(0));
		ladder.remove(0);
		
		if(ladder.size() > 2){
			System.out.println("a " + count + "-rung ladder exists between " 
					+ ladder.get(ladder.size()-1).toLowerCase() + " and " 
					+ ladder.get(0).toLowerCase() + ".");
		}
		else{
			System.out.println("no word ladder can be found between " 
					+ ladder.get(ladder.size()-1).toLowerCase() + " and " 
					+ ladder.get(0).toLowerCase() + ".");
		}
		
		//System.out.println(ladder.get(0).toLowerCase());
		for(int k= ladder.size()-1; k >= 0; k--){
			System.out.println(ladder.get(k).toLowerCase());
		}
	}
}
