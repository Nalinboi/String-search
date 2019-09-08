package code;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * A new instance of LempelZiv is created for every run.
 */
public class LempelZiv {
	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */	
	public String compress(String input) {
		// TODO fill this in.		
	
		StringBuilder compressed = new StringBuilder();
		
		int cursor = 0;
		int windowSize = 100; // some suitable size
		int lab = 0; //look ahead buffer
		
		ArrayList<TupleNode> textCompressed = new ArrayList<>(); //we will compress the text into this array of tuple nodes

		while(cursor < input.length()) {
			System.out.println();
			lab = (cursor + lab >= input.length()) ? input.length() : cursor + lab;
			int sbStart = (cursor - windowSize < 0) ? 0 : cursor - windowSize; 
			int matchLength = 1;
			int matchLocation = 0;
			String sb = (cursor == 0) ? "" : input.substring(sbStart, cursor);

			String next = input.substring(cursor, cursor + matchLength);
			System.out.println("Cursor = " + next + "   at: " + cursor + "\t\t - ");
			char nextC = input.charAt(cursor);
			
			int offset = 0;
			//System.out.println(cursor);
			if (sb.contains(next)) {				
				for(int i = cursor-1; i>=sbStart; i--) { //find the position of the very last occurance of this 
					//System.out.println("Looking for: " + nextC + "     next char is: " + input.charAt(i));
					if(input.charAt(i) == nextC) {
						matchLocation = i;
						offset = cursor-matchLocation;
						System.out.println("\t found the first at index " + i + "   offset = " + offset);						
						break;
					}
				}
				int length = 1; //the length of the match we have found so far is 1, now we look further on
				//System.out.println("match location: " + matchLocation + "   cursor: " + cursor);
				for(int i = matchLocation+1; i<cursor; i++) {
					if(cursor+length>=input.length()) {
						System.out.println("We done");
						break;
					}
					char peek = input.charAt(cursor+length);
					System.out.println("\t\t does this next match? " + input.charAt(i) + " - " + peek);
					//input char at i is the search buffer, peek is the look ahead
					if(input.charAt(i) == peek) { //if the next c at the search buffer is equal to the next c at the look ahead buffer
						length++;
					}
					else {
						break; //if the pattern does not match anymore then we break out
					}
				}
				if(cursor+length<input.length()) {
					cursor = cursor+length;
				}
				System.out.println("\t\t\t cursor:" + cursor);
				textCompressed.add(new TupleNode(offset, length, input.charAt(cursor)));
				//System.out.println("offset: "+ offset + "    len: "+ length + "   char: "+ input.charAt(cursor));
			} else {
				char character = input.charAt(cursor);
				textCompressed.add(new TupleNode(0, 0, character)); //add an empty tuple as this was the first occurance of the word
			}
			cursor++;
		}
		//return tuplesToByteArray(textCompressed);

		//compressed.append("[");
		for(TupleNode tn : textCompressed) {
			compressed.append(tn.toString());
			System.out.println(tn.toString());
		}
		//compressed.append("]");
		
		System.out.println(compressed.toString());
			
		return compressed.toString();
	}
//		loop{
//		match = stringMatch( text[cursor.. cursor+length],
//				text[(cursor<windowSize)?0:cursor-windowSize .. cursor-1])
//		if(match succeeded then) {
//			prevMatch = match
//			length = length + 1
//		}
//		else {
//			output( [a value for prevMatch, length, text[cursor+length ]])
//			cursor = cursor + length + 1
//			break
//		}	
	
		
	


	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */
	public String decompress(String compressed) {
		// TODO fill this in.
		
		char compressedChar[] = compressed.toCharArray(); //an array of the compressed file which is seperated by [ ] |
		
		ArrayList<TupleNode> formatted = new ArrayList<TupleNode>(); //makng a list of tuple nodes we recieve from the compressed file				
		StringBuilder offsetHold = new StringBuilder(); //we convert to an int later
		StringBuilder lenHold = new StringBuilder(); //we convert to an int later
		//char charHold;
		int state = 0; //1 = we are looking at the offset, 2 we are looking at the length, 3 we are looking at the char
		for(char c : compressedChar) {
			//System.out.println("State: " + state + " symbol: " + c);
			if(c != '[' && c != ']' && c != '|') {
				if(state == 1) {
					offsetHold.append(c);
				}
				else if(state == 2) {					
					lenHold.append(c);
				}
				else if(state == 3) {					
					int off = Integer.parseInt(offsetHold.toString());
					int len = Integer.parseInt(lenHold.toString());
					
					TupleNode toAdd = new TupleNode(off, len, c);	
					formatted.add(toAdd);
					
					//System.out.println(toAdd.toString());
					
					offsetHold = new StringBuilder(); //reset string builder
					lenHold = new StringBuilder(); //reset string builder					
					state = -1; //restart the states but before we see the ending brackert ]
				}
			}
			else {
				state++;
			}
		}
		
		StringBuilder sb = new StringBuilder();		

		ArrayList<Character> output = new ArrayList<Character>();		
		int cursor = 0;
		int formatNumber = 0;
		for(TupleNode tn : formatted) {
			if(tn.length == 0) {
				System.out.println(tn.character + "    cursor: " + cursor);
				output.add(cursor++, tn.character);
				//System.out.println(tn.character);
			} //the length is only 0 if it is the first occurance of the word, so just print the letter
			else { //if ([offset, length, ch ])
				for (int j = 0; j< tn.length; j++) {
					//System.out.println(output.get(cursor-tn.offset));
					System.out.println(output.get(cursor-tn.offset) + "    cursor: " + (cursor));
					output.add(output.get(cursor-tn.offset));
					cursor++;
					//output[cursor++] = output[cursor-tn.offset ];
				}
				//System.out.println(tn.character + "    cursor: " + cursor);
				//System.out.println(formatNumber + "    final tuple numberz: " + formatted.size());
				if(formatNumber<formatted.size()-1) { //for some reason mine adds the last character iff the final character(s) are a patter.
					output.add(cursor++, tn.character);
				}
				//output[cursor++] = tn.character;
			}
			formatNumber++;
		}	
		for(char c : output) {
			sb.append(c);
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You can use this to print out any relevant
	 * information from your compression.
	 */
	public String getInformation() {
		return "";
	}
}
