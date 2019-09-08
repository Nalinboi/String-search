package code;

import java.lang.reflect.Array;

/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {

	char[] patAry; //(S)
	char[] txtAry; //(T)
	int[] jmpTbl; //(M)
	
	public KMP(String pattern, String text) {
		// TODO maybe fill this in.		      
        patAry = pattern.toCharArray();
        txtAry = text.toCharArray();  
        jmpTbl = new int[pattern.length()];
        //makeJumpTable(pattern, text);
	}


	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {
		search2(pattern, text);
		
		int patLen = pattern.length(); //(m)		
		int txtLen = text.length(); //(n)
		int curTxtChar = 0; //k = start of current match in T
		int curPatChar = 0; //i = position of current character in S
		long timer = System.currentTimeMillis();
		System.out.println();
		System.out.print("Start KMP Jump Table: " + timer + "\t");
		
		//**JUMP TABLE**//
		jmpTbl[0] = -1;
		jmpTbl[1] = 0;
		int j = 0; //position in prefix
		int pos = 2; //position in table
		
		while (pos < patLen) {
			if (patAry[pos - 1] == patAry[ j ]) { //substrings ...pos-1 and 0..j match
				jmpTbl[pos] = j+1;
				pos++;
				j++;
			}
			else if (j > 0) { // mismatch, restart the prefix
				j = jmpTbl[j];
			}
			else {// j = 0 // we have run out of candidate prefixes
				jmpTbl[pos] = 0;
				pos++;
			}
		}
		//**JUMP TABLE**//
		
		long timerKMP = System.currentTimeMillis();
		System.out.println();
		System.out.print("Start KMP: " + timerKMP + "\t");
		//**KMP**//
		while((curTxtChar+curPatChar) < txtLen) { //(while(k + i < n))
			if(patAry[curPatChar] == txtAry[curTxtChar+curPatChar]) { //match at i ( S[ i ] = T[ k + i ] ) 
				curPatChar = curPatChar + 1; 
				if(curPatChar == patLen) {
					long timerEnd = System.currentTimeMillis();
					System.out.println("KMP finish in ms: " + timerEnd);
					System.out.println("Total Time before jump table: " + (timerEnd-timer) + "ms");
					System.out.println("Total Time after jump table: " + (timerEnd-timerKMP) + "ms");
					return curTxtChar;
				} //found s
			}
			else if(jmpTbl[curPatChar] == -1) { //mismatch, no self overlap
				curTxtChar = curTxtChar + curPatChar + 1;
				curPatChar = 0;
			}
			else { //mismatch, with self overlap
				curTxtChar = curTxtChar + curPatChar - jmpTbl[curPatChar];
				curPatChar = jmpTbl[curPatChar];
			}
		}
		return -1;
		//**KMP**//
	}
	
	public int search2(String pattern, String text) {
		//given a string s = "pattern" 
		//and given a text t = "text"
		//look for an occurance of s as a substring of t
		//If found, return index of first character of S in T;
		//otherwise return -1 (or some other index outside of T)
			
		//m = pattern.length()
		//n = text.length()
		int patLen = pattern.length(); //(m)		
		int txtLen = text.length(); //(n)
		
		//**BRUTE FORCE**//						
		long timer = System.currentTimeMillis();
		System.out.println();
		System.out.print("Start Brute Force: " + timer + "\t");
		
		boolean found = false;
		
		for(int k = 0; k < txtLen-patLen; k++) {
			found = true;
			for(int i = 0; i < patLen; i++){ //maybe not -1
				if(patAry[i] != txtAry[k+i]) {
					found = false;
					break;
				}
			}
			if(found) { 
				long timerEnd = System.currentTimeMillis();
				System.out.println("Brute force finish: " + timerEnd);
				System.out.println("Total Brute Force Time: " + (timerEnd-timer) + "ms");
				return k; 
			}
		}
	
		return -1;		
		//**BRUTE FORCE**//
	}
}
