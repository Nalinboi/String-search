package code;

public class huffNode implements Comparable<huffNode>{
	char character;
	int freq;
	boolean isLeaf = false; //if its a leaf node
	huffNode leftChild;
	huffNode rightChild;
	
	public huffNode(char character, int frequency, huffNode childLeft, huffNode childRight){
		//left child = 0, right child = 1
		this.character = character;
		this.freq = frequency;
		
		this.leftChild = childLeft;
		this.rightChild = childRight;		
	}
	
	public void setLeftChild(huffNode node) {
		this.leftChild = node;
	}
	public void setRightChild(huffNode node) {
		this.rightChild = node;
	}	


	@Override
	public int compareTo(huffNode o) {
		if(this.freq<o.freq) {
			return -1;
		}
		else if(this.freq > o.freq) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
