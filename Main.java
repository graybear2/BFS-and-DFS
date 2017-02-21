/* WORD LADDER Main.java
 * EE422C Project 3 submission by February 21st
 * Grayson Barrett
 * gmb974
 * 16215
 * Shariq Memon
 * skm2662
 * 16215
 * Slip days used: <0>
 * Git URL: git@github.com:graybear2/assignment3.git
 * Spring 2017
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static ArrayList<String> visited;
	static ArrayList<String> queue;
	static ArrayList<Integer> parents;
	static int index;
	
	public static void main(String[] args) throws Exception {
		
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
		
		System.out.println("Program Started");
		initialize();
		
		engine(kb);
	}
	
	
	/**
	 * @param kb scanner object for reading
	 * continuously accepts word ladder input and produces word ladders
	 * until the user gives the /quit command
	 */
	public static void engine(Scanner kb){
		boolean done = false;
		
		while (!done){
		
			ArrayList<String> input = parse(kb);
			if(input.size() == 2){
				ArrayList<String> ladder2 = getWordLadderBFS(input.get(0), input.get(1));
				printLadder(ladder2);
				ArrayList<String> ladder = getWordLadderDFS(input.get(0), input.get(1));
				//ArrayList<String> ladder2 = getWordLadderDFS("hello", "cells");
				printLadder(ladder);
			}
			
			if(input.get(0).equals("/quit"))
				done = true;
		}
		
	}
	
	/**
	 * initializes all static variables
	 */
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
		String command1 = keyboard.next();
		if(command1.equals("/quit")){
			retVal.add(command1);
			return retVal;
		}
		else{
			String command2 = keyboard.next();
			retVal.add(command1);
			retVal.add(command2);
			return retVal;
		}
		
	}
	
	/**
	 * @param start: start word of the ladder
	 * @param end: end word of the ladder
	 * @return arrayList containing a ladder produced by a depth first search
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		// TODO some code
		visited.clear();
		queue.clear();
		ArrayList<String> ladder = new ArrayList<String>();
		Set<String> dict = makeDictionary();
		ladder.add(start.toUpperCase());
		visited.add(start.toUpperCase());
		ladder.add(end.toUpperCase());
		getWordLadderDFS(dict, ladder, start.toUpperCase(), end.toUpperCase());
		
		ladder.add(ladder.get(0));
		ladder.remove(0);
		
		return ladder; // replace this line later with real return
	}
	
	/**
	 * @param dict: the dictionary
	 * @param ladder: the ladder being developed
	 * @param word: the current node the DFS is on
	 * @param end: the final word to be reached
	 * @return the success of the search from that node
	 */
	public static boolean getWordLadderDFS(Set<String> dict, ArrayList<String> ladder, String word, String end){
		visited.add(word);
		if(word.equals(end))
			return true;
		ArrayList<String> neighbors = new ArrayList<String>();
		
		for(int k=0; k<word.length(); k++){
			for(int i = 0; i<26; i++){
				String newWord = word.substring(0, k) + ((char) (65 + i)) + word.substring(k+1,word.length());
				if(dict.contains(newWord) && !visited.contains(newWord)){
					neighbors.add(newWord);
					visited.add(newWord);
				}
			}
		}
		
		//try using the "best" neighbor to continue DFS
		for(int k = 0; k<neighbors.size(); k++){
			String bestRoute = findBestRoute(neighbors, end);
			
			if(getWordLadderDFS(dict, ladder, bestRoute, end)) {
                if (!bestRoute.equals(end)) {
                    ladder.add(bestRoute);
                }
                return true;
            }
			
			else{
				neighbors.remove(bestRoute);
			}
		}
		
		return false;
	}
	
	/**
	 * @param neighbors: arrayList of nodes to continue DFS
	 * @param end: the final word to be reached
	 * @return the neighbor most similar to the end word
	 */
	public static String findBestRoute(ArrayList<String> neighbors, String end){
		String bestRoute = "";
		int maxDiff = end.length();
		
		for(int k = 0; k<neighbors.size(); k++){
			int countDiff = 0;
			for(int i=0; i<end.length(); i++){
				if(neighbors.get(k).toUpperCase().charAt(i) != end.toUpperCase().charAt(i)){
					countDiff++;
				}
			}
			
			if(countDiff <= maxDiff){
				maxDiff = countDiff;
				bestRoute = neighbors.get(k);
			}
		}
		return bestRoute;
	}
	
	/**
	 * @param start: start word of the ladder
	 * @param end: end word of the ladder
	 * @return arrayList containing a ladder produced by a breadth first search
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	visited.clear();
		queue.clear();
		parents.clear();
    	ArrayList<String> ladder = new ArrayList<String>();
    	
    	ladder.add(start.toUpperCase());
    	visited.add(start.toUpperCase());
    	
    	if(start.toUpperCase().equals(end.toUpperCase())){
    		ladder.add(end);
			return ladder;
    	}
		Set<String> dict = makeDictionary();
		index = 0;
		
		parents.add(-1);
		exploreFrontier(start.toUpperCase(), ladder, dict, end.toUpperCase());
		index++;
		while(queue.size() > 0){
			exploreFrontier(queue.get(0).toUpperCase(), ladder, dict, end.toUpperCase());
			if(queue.size() > 0)
				queue.remove(0);
			index++;
		}
		if(ladder.size() == 1)
			ladder.add(end);
		
		ladder.add(ladder.get(0));
		ladder.remove(0);
		
		return ladder;
	}
    
    /**
	 * @param dict: the dictionary
	 * @param ladder: the ladder being developed
	 * @param word: the current node the DFS is on
	 * @param end: the final word to be reached
	 * Will fill ladder with the rungs on the shortest path from start to finish
	 */
    public static void exploreFrontier(String word, ArrayList<String> ladder, Set<String> dict, String end){
    	for(int k=0; k<word.length(); k++){
			for(int i = 0; i<26; i++){
				String newWord = word.substring(0, k) + ((char) (65 + i)) + word.substring(k+1,word.length());
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
	
	/**
	 * @param ladder (in reverse order)
	 * prints a ladder in the correct order
	 */
	public static void printLadder(ArrayList<String> ladder) {
		if(ladder.size() > 2){
			System.out.print("a " + Integer.valueOf(ladder.size()-2) + "-rung ladder exists between " 
					+ ladder.get(ladder.size()-1).toLowerCase() + " and " 
					+ ladder.get(0).toLowerCase() + ".\n");
		}
		else{
			System.out.println("no word ladder can be found between " 
					+ ladder.get(ladder.size()-1).toLowerCase() + " and " 
					+ ladder.get(0).toLowerCase() + ".");
		}
		
		for(int k= ladder.size()-1; k >= 0; k--){
			System.out.println(ladder.get(k).toLowerCase());
		}
	}
}
