import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.reflect.Array;

public class WordSearchSolver {

 public static String[] searchRows;
 public static  ArrayList<String> Words = new ArrayList<String>();

// Color codes for letters in the word search.
	public static final String GREEN = "\033[032m";
	public static final String RESET = "\033[0m";

	public static void main(String[] args) {

		// Create a scanner for the terminal.
		Scanner scnr = new Scanner(System.in);
		int numLines = 0;

		System.out.println(
				"Welcome to Word Search Solver. Paste your word Search below. Press enter twice when finished (link to some word searches: https://wordmint.com/public_puzzles/11097).");

		ArrayList<String> searchRowsList = new ArrayList<String>();

		// Parse the pasted word search into an array of strings.
		while (true) {
			String nextLine = scnr.nextLine();
			if (nextLine.isBlank() == false) {
				searchRowsList.add(nextLine);
				numLines++;
			} else
				break;
		}
    
    searchRows = new String[numLines];
		for (int i = 0; i < numLines; i++) {
			searchRows[i] = searchRowsList.get(i);
		}

		

		System.out.println(
				"Enter the words you need to find seperate by spaces or newlines. Type \"end!\" as your last word. For increased accuracy, remove spaces between mutli-word tokens.");

		// Get all the words pasted into the terminal.
		String nextWord;

		while (true) {
			nextWord = scnr.next();
			if (!nextWord.equals("end!"))
				Words.add(nextWord);
			else
				break;
		}

    // Create a timer.
		double time1 = System.currentTimeMillis();

    // Call the outermost function for solving the wordsearch, passing the words, rows of letters, and number of lines as parameters.
		ArrayList<Integer[]> columnsAndLines = wordMatch(numLines);
		
    // These values are the indeces of letters that belong to a word.
    Integer[] colNums = columnsAndLines.get(0);
		Integer[] lineNums = columnsAndLines.get(1);

		System.out.println();
		System.out.println("Solved wordsearch:");

    // Print out the solved word search. If the index of a character matches up with one of the values in the arrays of colNums and lineNums, color it green, signaling that it belongs to a word.
		for (int i = 0; i < numLines; i++) {
			System.out.printf("%-4d", i + 1);
			for (int j = 0; j < searchRows[i].length(); j++) {
				boolean trueChar = false;
				for (int k = 0; k < colNums.length; k++) {
					if (lineNums[k] != null) {
						if (i == lineNums[k]) {
							if (j == colNums[k]) {
								trueChar = true;
								System.out.print(GREEN + searchRows[i].charAt(j) + RESET);
								break;
							}

						}
					}
				}
				if (trueChar == false) {
					System.out.print(searchRows[i].charAt(j));
				}

			}
			System.out.println();

		}

    // Time taken to solve the word search.
		double timeElapsed = System.currentTimeMillis() - time1;

		System.out.println("milliseconds elapsed : " + timeElapsed);
	}

  // Outermost function for searching for each word in the word bank.
	public static ArrayList<Integer[]> wordMatch(int numLines) {
		
    ArrayList<Integer> colNums = new ArrayList<Integer>();
		ArrayList<Integer> lineNums = new ArrayList<Integer>();
		
    // Iterate through the words in the word bank.
    for (int v = 0; v < Words.size(); v++) {
			String word = Words.get(v);
			
      // Here, we iterate through every character in the word search.
      for (int i = 0; i < numLines; i++) {
        for (int j = 0; j < searchRows[i].length(); j += 2) {

          // Call the function to search for a word around the current character that we are on.
          ArrayList<Integer[]> useNums = charSearch(i, j, word, numLines);
					
          // Remember any indeces of characters that were determined to be a part of the word.
          Integer[] addColumns = useNums.get(0);
					Integer[] addRows = useNums.get(1);
					
          // Add the indeces to the overall memory of indeces that belong to words.
          for (int k = 0; k < addRows.length; k++) {
						lineNums.add(addRows[k]);
						colNums.add(addColumns[k]);
					}
				}
			}
		}

    // Here just composing and returning the final list of indeces that belong to a word.
		Integer[] colNumsFinal = colNums.toArray(new Integer[0]);
		Integer[] lineNumsFinal = lineNums.toArray(new Integer[0]);
		ArrayList<Integer[]> colNumsLineNums = new ArrayList<Integer[]>();
		colNumsLineNums.add(colNumsFinal);
		colNumsLineNums.add(lineNumsFinal);
		return colNumsLineNums;
	}

    // TODO: This function could be simplified by having a mutable process for searching in any direction, rather than hardcoding a search for all eight directions.
	public static ArrayList<Integer[]> charSearch(int lineNum, int charNum, String word,
			int numLines) {
	
        // Store the original line index.	
        int origLine = lineNum;

        // Create an empty string with which we will build a word to compare to the word we are searching for.
		String wordCheck = "";

		Integer[] linesOfWord = new Integer[word.length()];
		Integer[] charsOfWord = new Integer[word.length()];
		int j = 0;

		Integer[] linesOfWordNull = new Integer[0];
		Integer[] charsOfWordNull = new Integer[0];
		ArrayList<Integer[]> colNumsLineNumsNull = new ArrayList<Integer[]>();
		colNumsLineNumsNull.add(charsOfWordNull);
		colNumsLineNumsNull.add(linesOfWordNull);
		
        // if the first character does not match, return null
		if (!Character.toString(searchRows[lineNum].charAt(charNum))
				.equalsIgnoreCase(Character.toString(word.charAt(j)))) {
			return colNumsLineNumsNull;
		}

		// Search to the right of the character.
		for (int i = charNum; i < searchRows[0].length(); i += 2) {

            // If exceeded the length of a searched-for word, break.
			if (j >= word.length()) {
				break;
			}

            // Break if we get a character that does not match with the next character of a word we are looking for.
			if (!Character.toString(searchRows[lineNum].charAt(i))
					.equalsIgnoreCase(Character.toString(word.charAt(j)))) {
				break;
			}

            // Add the next character to the word we are building and store the indeces of those characters
			wordCheck = wordCheck + searchRows[lineNum].charAt(i);
			linesOfWord[j] = lineNum;
			charsOfWord[j] = i;
			if (wordCheck.equalsIgnoreCase(word)) {
				ArrayList<Integer[]> colNumsLineNums = new ArrayList<Integer[]>();
				colNumsLineNums.add(charsOfWord);
				colNumsLineNums.add(linesOfWord);
				return colNumsLineNums;
			}
			j++;
		}
		Integer[] linesOfWord2 = new Integer[word.length()];
		Integer[] charsOfWord2 = new Integer[word.length()];
		j = 0;
		wordCheck = "";
		// Search to the left of the character
		for (int i = charNum; i >= 0; i -= 2) {

			if (j >= word.length()) {
				break;
			}

			if (!Character.toString(searchRows[lineNum].charAt(i))
					.equalsIgnoreCase(Character.toString(word.charAt(j)))) {
				break;
			}

			wordCheck = wordCheck + searchRows[lineNum].charAt(i);
			linesOfWord2[j] = lineNum;
			charsOfWord2[j] = i;
			if (wordCheck.equalsIgnoreCase(word)) {
				ArrayList<Integer[]> colNumsLineNums = new ArrayList<Integer[]>();
				colNumsLineNums.add(charsOfWord2);
				colNumsLineNums.add(linesOfWord2);
				return colNumsLineNums;
			}

			j++;
		}
		Integer[] linesOfWord3 = new Integer[word.length()];
		Integer[] charsOfWord3 = new Integer[word.length()];
		j = 0;
		wordCheck = "";
		// search down
		for (int i = lineNum; i < numLines; i++) {

			if (j >= word.length()) {
				break;
			}

			if (!Character.toString(searchRows[i].charAt(charNum))
					.equalsIgnoreCase(Character.toString(word.charAt(j)))) {
				break;
			}
			wordCheck = wordCheck + searchRows[i].charAt(charNum);
			linesOfWord3[j] = i;
			charsOfWord3[j] = charNum;
			if (wordCheck.equalsIgnoreCase(word)) {
				ArrayList<Integer[]> colNumsLineNums = new ArrayList<Integer[]>();
				colNumsLineNums.add(charsOfWord3);
				colNumsLineNums.add(linesOfWord3);
				return colNumsLineNums;
			}

			j++;
		}
		Integer[] linesOfWord4 = new Integer[word.length()];
		Integer[] charsOfWord4 = new Integer[word.length()];
		j = 0;
		wordCheck = "";
		// search up
		for (int i = lineNum; i >= 0; i--) {

			if (j >= word.length()) {
				break;
			}

			if (!Character.toString(searchRows[i].charAt(charNum))
					.equalsIgnoreCase(Character.toString(word.charAt(j)))) {
				break;
			}

			wordCheck = wordCheck + searchRows[i].charAt(charNum);
			linesOfWord4[j] = i;
			charsOfWord4[j] = charNum;
			if (wordCheck.equalsIgnoreCase(word)) {
				ArrayList<Integer[]> colNumsLineNums = new ArrayList<Integer[]>();
				colNumsLineNums.add(charsOfWord4);
				colNumsLineNums.add(linesOfWord4);
				return colNumsLineNums;
			}

			j++;
		}
		Integer[] linesOfWord5 = new Integer[word.length()];
		Integer[] charsOfWord5 = new Integer[word.length()];
		j = 0;
		wordCheck = "";
		// search down and to the right
		for (int i = charNum; i < searchRows[0].length(); i += 2) {
			if (lineNum >= numLines || j >= word.length()) {
				break;
			}

			if (!Character.toString(searchRows[lineNum].charAt(i))
					.equalsIgnoreCase(Character.toString(word.charAt(j)))) {
				break;
			}

			wordCheck = wordCheck + searchRows[lineNum].charAt(i);
			linesOfWord5[j] = lineNum;
			charsOfWord5[j] = i;
			if (wordCheck.equalsIgnoreCase(word)) {
				ArrayList<Integer[]> colNumsLineNums = new ArrayList<Integer[]>();
				colNumsLineNums.add(charsOfWord5);
				colNumsLineNums.add(linesOfWord5);
				return colNumsLineNums;
			}
			j++;
			lineNum++;
		}
		lineNum = origLine;
		Integer[] linesOfWord6 = new Integer[word.length()];
		Integer[] charsOfWord6 = new Integer[word.length()];
		j = 0;
		wordCheck = "";
		// search down and to the left
		for (int i = charNum; i >= 0; i -= 2) {

			if (j >= word.length() || lineNum >= numLines) {
				break;
			}

			if (!Character.toString(searchRows[lineNum].charAt(i))
					.equalsIgnoreCase(Character.toString(word.charAt(j)))) {
				break;
			}

			wordCheck = wordCheck + searchRows[lineNum].charAt(i);
			linesOfWord6[j] = lineNum;
			charsOfWord6[j] = i;
			if (wordCheck.equalsIgnoreCase(word)) {
				ArrayList<Integer[]> colNumsLineNums = new ArrayList<Integer[]>();
				colNumsLineNums.add(charsOfWord6);
				colNumsLineNums.add(linesOfWord6);
				return colNumsLineNums;
			}
			j++;
			lineNum++;
		}
		lineNum = origLine;
		Integer[] linesOfWord7 = new Integer[word.length()];
		Integer[] charsOfWord7 = new Integer[word.length()];
		j = 0;
		wordCheck = "";
		// search up and to the left
		for (int i = charNum; i >= 0; i -= 2) {

			if (j >= word.length() || lineNum < 0) {
				break;
			}

			if (!Character.toString(searchRows[lineNum].charAt(i))
					.equalsIgnoreCase(Character.toString(word.charAt(j)))) {
				break;
			}

			wordCheck = wordCheck + searchRows[lineNum].charAt(i);
			linesOfWord7[j] = lineNum;
			charsOfWord7[j] = i;
			if (wordCheck.equalsIgnoreCase(word)) {
				ArrayList<Integer[]> colNumsLineNums = new ArrayList<Integer[]>();
				colNumsLineNums.add(charsOfWord7);
				colNumsLineNums.add(linesOfWord7);
				return colNumsLineNums;
			}
			j++;
			lineNum--;
		}
		lineNum = origLine;
		Integer[] linesOfWord8 = new Integer[word.length()];
		Integer[] charsOfWord8 = new Integer[word.length()];
		j = 0;
		wordCheck = "";
//search up and to the right
		for (int i = charNum; i < searchRows[0].length(); i += 2) {

			if (j >= word.length() || lineNum < 0) {
				break;
			}

			if (!Character.toString(searchRows[lineNum].charAt(i))
					.equalsIgnoreCase(Character.toString(word.charAt(j)))) {
				break;
			}

			wordCheck = wordCheck + searchRows[lineNum].charAt(i);
			linesOfWord8[j] = lineNum;
			charsOfWord8[j] = i;
			if (wordCheck.equalsIgnoreCase(word)) {
				ArrayList<Integer[]> colNumsLineNums = new ArrayList<Integer[]>();
				colNumsLineNums.add(charsOfWord8);
				colNumsLineNums.add(linesOfWord8);
				return colNumsLineNums;
			}
			j++;
			lineNum--;
		}
		lineNum = origLine;

		j = 0;
		wordCheck = "";

		return colNumsLineNumsNull;
	}
}
