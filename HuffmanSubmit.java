import java.util.*;
import java.lang.Object.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HuffmanSubmit implements Huffman {

	public class Node implements Comparable<Node>{
		Character c;
		Node left;
		Node right;
		int freq;

		Node(char c, int freq, Node left, Node right) {
			this.c = c;
			this.freq  = freq;
			this.left  = left;
			this.right = right;
		}
		public Node(Node left, Node right) {
			this.c=null;
			this.freq= left.freq+right.freq;
			this.left=left;
			this.right=right;
		}
		public Node left() {
			return left;
		}
		public Node right() {
			return right;
		}
		Node(Map.Entry<Character,Integer> entry) {
			this.c = entry.getKey();
			this.freq = entry.getValue();
			this.left = null;
			this.right = null;
		}

		private boolean isLeaf() {
			return (left == null) && (right == null);		
		}

		public String toString() {			//for the frequency file
			return this.c + ": " + this.freq;
		}

		@Override
		public int compareTo(Node o) {  // to compare the nodes in priority queue
			return this.freq-o.freq;
		}
	}

	// Feel free to add more methods and variables as required.  
	public void encode(String inputFile, String outputFile, String freqFile){
		BinaryIn in= new BinaryIn(inputFile);
		BinaryOut out= new BinaryOut(outputFile);
		BinaryOut freq= new BinaryOut(freqFile);

		HashMap<Character, Integer> hm = new HashMap<>();  // to store each character and its frequency
		List<Character> allChar = new LinkedList<>(); //to store the complete file in form of characters
		//loop to calculate the frequency of each character
		while(!in.isEmpty()){
			char c= in.readChar();
			allChar.add(c);
			int count;
			count= hm.containsKey(c)? hm.get(c):0;
			hm.put(c, count+1);
		}
		//generating the frequency file
		for(Map.Entry<Character, Integer> entry: hm.entrySet()) {
			String str= Integer.toBinaryString(entry.getKey());
			str = String.format("%8s", str).replace(' ', '0');
			System.out.println(str);
			System.out.println(entry.getValue());
			freq.write(str+ ":"+ entry.getValue()+"\n");
		}
		freq.flush();

		//building the Huffman tree
		PriorityQueue<Node> tree= new PriorityQueue();
		for(Map.Entry<Character,Integer> entry: hm.entrySet()) {
			tree.add(new Node(entry));			
		}
		while(tree.size()>1) {
			Node left= tree.remove();
			Node right= tree.remove();
			Node root= new Node(left,right);
			tree.add(root);
		}
		Node HTree= tree.remove();
		HashMap<Character, String> HCode= new HashMap<>();
		
		//Traversing the tree and giving each character a code according to its weight
		String codeBegin="";
		codedTree(HTree, HCode, codeBegin);

		//writing the encoded file
		ListIterator<Character> it= allChar.listIterator();
		while(it.hasNext()) {
			char temp= it.next();
			String code= HCode.get(temp);
			for(int i=0; i<code.length(); i++) {
				if(code.charAt(i)=='0') {
					out.write(false);
				}else if(code.charAt(i)=='1') {
					out.write(true);
				}			
			}
		}
		out.flush();
	}

	public void codedTree(Node root, HashMap<Character, String> HCode, String code ) {
		if(root.isLeaf()) {
			HCode.put(root.c, code);
			return;
		}
		else {
			codedTree(root.left, HCode, code+'0');
			codedTree(root.right, HCode, code+'1');
		}
	}

	public void decode(String inputFile, String outputFile, String freqFile){

		BinaryIn in= new BinaryIn(inputFile);
		BinaryOut out= new BinaryOut(outputFile);

		HashMap<Character, Integer>  hm = new HashMap<>(); //this map contains the character and their frequencies.

		try (BufferedReader br = new BufferedReader(new FileReader(freqFile))) { 
			String line;
			while ((line = br.readLine()) != null) {
				String array[] = line.split(":");
				String rep  = array[0];
				String frequency = array[1];
				char c = (char)Integer.parseInt(rep, 2);
				int freq =Integer.parseInt(frequency);
				hm.put(c, freq);
			}
		}

		catch (Exception e)
		{
			System.err.println(e); // handle exception
		}

		//building the tree again
		PriorityQueue<Node> tree= new PriorityQueue();
		for(Map.Entry<Character,Integer> entry: hm.entrySet()) {
			tree.add(new Node(entry));			
		}
		while(tree.size()>1) {
			Node left= tree.remove();
			Node right= tree.remove();
			Node root= new Node(left,right);
			tree.add(root);
		}
		Node HTree= tree.remove();

		// Read decoding starts now
		List <Boolean> b_list = new LinkedList<>();
		while (!in.isEmpty()) {
			boolean b = in.readBoolean();
			b_list.add(b);
		}
		Node x = HTree;
		for (int i = 0; i < b_list.size()-1; i++) {
			if (!x.isLeaf()) {
				if (b_list.get(i))
					x =  x.right;
				else
					x =  x.left;
			}
			else {
				out.write((char)x.c);
				x = HTree;
				i--;
			}
		}
		out.flush();
	}

	public static void main(String[] args) {
		Huffman  huffman = new HuffmanSubmit();
		huffman.encode(" ur . jpg " , " ur . enc " , " freq . txt ");
		huffman.decode(" ur . enc " , " ur_dec . jpg " , " freq . txt ");
	}
}