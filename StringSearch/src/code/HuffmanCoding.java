package code;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {
	/**
	 * This would be a good place to compute and store the tree.
	 */
	
	Map<Character, Integer> charFreq = new TreeMap<Character, Integer>();
	Queue<huffNode> mergeTheseNodes = new PriorityQueue<huffNode>();
	
	Map<Character, String> letterCode = new TreeMap<Character, String>();
	
	public HuffmanCoding(String text) {		
		char txtChar[] = text.toCharArray(); //an array of the txt
		for(int i = 0; i < text.length(); i++) { //creating the map of all characters and there frequency 		
			System.out.println("creating map: " + i + "    out of " + text.length());
			if(charFreq.containsKey(txtChar[i])) {
				charFreq.put(txtChar[i], charFreq.get(txtChar[i]) + 1); //if we already have this character in the map then increment the freq
			}
			else {
				charFreq.put(txtChar[i], 1); //if we do not already have this character in this map we make a new one
			}			
		}
		
		for(Character c : charFreq.keySet()) { //going through the map
			int freq = charFreq.get(c); 
			huffNode h = new huffNode(c, freq, null, null); //we create a node from all of these values in the map 
			
			System.out.println("Character = " + c + "\t Freq = " + freq);
			
			mergeTheseNodes.add(h); //we add this to the priority queue (being compared in the huffnode class)
			h.isLeaf = true;
		}

		while(mergeTheseNodes.size()>1) { //while we can still poll two nodes to merge them
			huffNode leftNode = mergeTheseNodes.poll(); //remove the top two nodes
			huffNode rightNode = mergeTheseNodes.poll();
			//System.out.println("whoop");	
			System.out.println("OOYA --- left = " + leftNode.character + "\t right: " + rightNode.character);
			
			//we use character min value as a sort of null value as this value does not matter
			huffNode parentNode = new huffNode(Character.MIN_VALUE, (leftNode.freq)+(rightNode.freq), leftNode, rightNode); //create a new tree with the two nodes as children, freq = sum of the childrens freqs
			parentNode.isLeaf = false; //i thought this wasnt needed
			
			mergeTheseNodes.offer(parentNode);	//put this "parent" of the merged two characters back in the queue.		
		}
		
		compressText(mergeTheseNodes.poll(), "");		
		
		for(Character h : letterCode.keySet()) {
			char letter = h;
			String code = letterCode.get(h);
			
			//int frequency = h.freq;
			System.out.print("u beta do ur job \t\t");
			System.out.println(letter + ": " + code);
		}
		
		String finished = encode(text);
		System.out.println(finished);
	}

	private void compressText(huffNode thisNode, String s) {
		//if(thisNode == null) { return; }		
		if(thisNode.isLeaf) {
			letterCode.put(thisNode.character, s); //THIS S MIGHT NOT BE RIGHT	
			System.out.println("LEAF REACHED " + thisNode.character);
		}
		else {			
			System.out.println("\t " + thisNode.leftChild.character + "  " + thisNode.rightChild.character);
			compressText(thisNode.leftChild, s+"0");
			compressText(thisNode.rightChild, s+"1");
		}		
	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		// TODO fill this in.
		//use string builder
		//count freq 
		//build tree
		
		char toFix[] = text.toCharArray();
		StringBuilder translated = new StringBuilder(); //we use a string builder to build a string from characters
		int i = 0;
		for(char c : toFix) {
			translated.append(letterCode.get(c));
			System.out.println("character: " + i + "   out of: " + toFix.length);
			i++;
		}		
		return translated.toString();
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		// TODO fill this in.
		//use string builder
		
		//go through string in coded
		//if the character is there in the binary coded map (values of letterCode)
			//append the character to return
		
		
		char toFixBack[] = encoded.toCharArray();
		StringBuilder currentCode = new StringBuilder(); //we use a string builder to build a string from characters
		StringBuilder returnedText = new StringBuilder(); //we use a string builder for the output text decoded
		int i = 0;
		for(char c : toFixBack) { //for each of the binary letters
			System.out.println("Looking for code: " + i + "   out of: " + toFixBack.length);
			currentCode.append(c);//first, append the string code
			if(letterCode.containsValue(currentCode.toString())) { //if the code is found in the map 
				//System.out.println(currentCode);				
				for(char cc : letterCode.keySet()) { //look through the key set
					String code = letterCode.get(cc);
					if(code.equals(currentCode.toString())) { //find the key (character) of the value (string in binary) 
						returnedText.append(cc);	//append this to what we return ie what we have decoded													
					}
				}
				currentCode = new StringBuilder(); //reset the new code we are looking for 
			}
			i++;
		}	
		//System.out.println(returnedText.toString());		
		return returnedText.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		return "";
	}
}
