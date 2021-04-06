import java.io.*;
import java.util.ArrayList;
import java.util.*;
class bplustree 
{
	
	public static void main(String[] args) throws Exception {
		Map<String,String> inputt=new HashMap<String,String>();
		
		/*Create input and output files, create buffered readers and writers*/
		File inputFilename = new File(args[0]+".txt"); /*Enter input.txt with cmd*/
		//File inputFilename = new File("input.txt"); /*Enter input.txt with Idea*/
		BufferedReader infile = new BufferedReader(new FileReader(inputFilename));
		
		File outputFilename = new File("output_file.txt");
		if(!outputFilename.exists()){
			outputFilename.createNewFile();
			
		}
		OutputStreamWriter filewriter = new FileWriter(outputFilename.getAbsoluteFile());
		Writer printwriter = new PrintWriter(filewriter);
		
		
		int Node_degrees = Integer.parseInt(infile.readLine().split("Initialize\\(")[1].split("\\)")[0]);
		BPTree BPlustree = new BPTree(Node_degrees); /* Pass the degree to creat a b+ tree*/
		
		String Inputline;
		Inputline = infile.readLine();
		do {
			if(Inputline!=null) {
			if(Inputline.startsWith("Insert")){/*Insert*/
					String s = Inputline.split("Insert\\(")[1];
					String k = s.split(",")[0];
					System.out.println("k="+k);/*See the changing values of k while the loop executes*/
					Integer node_Key = Integer.parseInt(k);
					String v=s.split(",")[1].split("\\)")[0];
					System.out.println("v="+v);/*See the changing values of v*/
					float value =Float.parseFloat(v);
					inputt.put(k,v);
					BPlustree.inNode(node_Key,value);
			}
			else if(Inputline.startsWith("Delete"))
			{
					String str = Inputline.split("Delete\\(")[1];
					String y=str.split("\\)")[0];
					int x =Integer.parseInt(y);
					BPlustree=BPlustree.delete(inputt,x);
				}
			else if(Inputline.startsWith("Search")){/*Search*/
				if(Inputline.contains(",")){ /*Call the range function*/
					String S=Inputline.split("Search\\(")[1];
					String K=S.split(",")[0];
					int aa=Integer.parseInt(K);
					String X=S.split(",")[1].split("\\)")[0];
					float z=Float.parseFloat(X);
					String value_Range = BPlustree.node_search(aa,z).trim();
					if( value_Range.charAt(value_Range.length() - 1) == ','){
						value_Range = value_Range.substring(0, value_Range.length() - 1);
					}
					((PrintWriter) printwriter).println(value_Range);
				}
				else { /* Call the node search method*/
					String s = Inputline.split("Search\\(")[1];
					String y=s.split("\\)")[0];
					int x =Integer.parseInt(y);
					int zz = 0;

					for(int three_Z =0;three_Z<x;three_Z++)
					{
						zz=zz+1;
					}
					String node_Search = BPlustree.node_search(x).trim();
					if( node_Search.charAt(node_Search.length() - 1) == ','){
						node_Search = node_Search.substring(0, node_Search.length() - 1);
					}
					((PrintWriter) printwriter).println(node_Search);
				}
			}
		}
	Inputline = infile.readLine();	
	}while(Inputline!=null);
		filewriter.close();
		infile.close();
	}
}

    class BPTree {
	
	public static Bpnode root = null;
	public static int node_Degrees;
	
	public BPTree(int Node_Degrees){
		this.node_Degrees = Node_Degrees;
	}
	
	static BPTree delete (Map inputt,Integer key_Value){
		System.out.println(inputt);
		Iterator iterators=inputt.entrySet().iterator();
		BPTree bPlustree=new BPTree(node_Degrees);
		String aoi= new Integer(key_Value).toString();
		while(iterators.hasNext()){
			Map.Entry<String,String> mapElement=(Map.Entry)iterators.next();

			if(!mapElement.getKey().equals(aoi)){
				System.out.println(mapElement.getKey()+ " : "+  mapElement.getValue());
				bPlustree.inNode(Integer.parseInt(mapElement.getKey()),Float.parseFloat(mapElement.getValue()));
			}
		}
		return bPlustree;
	}
	
	
	protected static Bpnode inNode(Bpnode root, Bp_Datanode data_Node){
		/*Whether root is leaf or not*/
		if(!root.Node_Isleaf){ /* The root is not leaf*/
			root.Node_issplit = false;
			ArrayList<Integer> root_Number = new ArrayList<Integer>();
			root_Number = ((node_IntNode)root).key_Number; /*Arraylist of rootkeys*/
			int key = 0;
			while(key < root_Number.size()){
				/*Insert a brand new node*/
				if(root_Number.get(key) > data_Node.node_Key || key == root_Number.size()-1) {
					int pos = root_Number.get(key) > data_Node.node_Key ? key : key + 1;  /*Check the right positioning in the node*/
					
					Bpnode node = new Bpnode();
					node = inNode(((node_IntNode)root).int_Ptrs.get(pos), data_Node); /*Insert in the right place and check everything recursively with pointer*/
					
					if(node.Node_issplit){ /*After successful insertion, if the size of the data node is equal to degree, it will be split*/
						
						((node_IntNode)root).insert(((node_IntNode)node).key_Number.get(0), ((node_IntNode)node).int_Ptrs.get(0),
								((node_IntNode)node).int_Ptrs.get(1)); /*Insert the middle of the pointer*/
						if(((node_IntNode)root).key_Number.size() == node_Degrees){ /*Check of degree*/
							int node_SplitPos = ((int) Math.ceil(((double)node_Degrees)/2)) - 1;
							node_IntNode int_Node = new node_IntNode();
							node_IntNode child_Node = new node_IntNode();
							int_Node.insert(((node_IntNode)root).key_Number.get(node_SplitPos), root, child_Node); /*Add the corresponding key to the parent pointer at the specified split position*/
							((node_IntNode)root).key_Number.remove(node_SplitPos);/*Delete in internal node*/
							

							int i = node_SplitPos, j = node_SplitPos,lu=0;

							for(;i < node_Degrees-1;i++){/*Split the node from the current node here to add a new node*/
								child_Node.key_Number.add(((node_IntNode)root).key_Number.remove(j));
							}
							while(lu!=0)
							{
								System.out.print("checking the middle position");
								System.out.print("\n");
							}
							 j = node_SplitPos + 1;
							 i = node_SplitPos+1;
							for(;i <= node_Degrees;i++){ /*Update the pointer*/
								child_Node.int_Ptrs.add(((node_IntNode)root).int_Ptrs.remove(j));
							}
							int_Node.Node_issplit = true;						
							return int_Node;
						}
						return root;
					}
					else /*No split*/
						return root;
					
				}
				key++;
			}
		}
		else{ /*The root is a leaf*/
			((Node_leaf)root).inNode(data_Node); /*Insertion of a node*/

			if(((Node_leaf)root).data_Nodes.size() == node_Degrees){ /* if datanode is equal to size then split*/
				int position_In_Tree = node_Degrees/2; /*CENTER SPLIT*/
				Node_leaf leaf = new Node_leaf();
				for(int i = position_In_Tree, j = position_In_Tree; i < node_Degrees; i++){ /*Split the node*/
					leaf.inNode(((Node_leaf)root).data_Nodes.remove(j));
					int cond=1;
					while(cond!=1)
					{
						System.out.println("condition check");
					} 
					}
				
				/*Update the pointer for the resultant root*/
				leaf.next = ((Node_leaf)root).next;
				((Node_leaf)root).next = leaf;
				leaf.prev = ((Node_leaf)root);
				if(leaf.next!=null)
					leaf.next.prev = leaf;
				
				/*Update the split to the resultant root*/
				node_IntNode split = new node_IntNode();
				split.key_Number.add(leaf.data_Nodes.get(0).node_Key);
				split.Node_issplit = true;
				split.int_Ptrs.add((Node_leaf)root);
				split.int_Ptrs.add(leaf);
				int x_split=0;
				while(x_split>1)
				{
					if(leaf.next.prev ==leaf)
					{
						System.out.println("node split");
					}
					else
						{
						System.out.println("no node split");
						}
				}
				return split;
			}
			else
				return root;
		}
		return root;
	}
	
				/*Internal function for inserting new nodes*/
	protected static void inNode(int node_Key, float node_Value){
		Bp_Datanode data_Node = new Bp_Datanode(node_Key, node_Value);
		if(root == null){
			root = new Node_leaf();
			((Node_leaf)root).inNode(new Bp_Datanode(node_Key, node_Value));
		}
		else if(root!=null){
			root = inNode(root, data_Node);
		}
	}
	
	/*Search*/
	
	protected static String node_search(int node_Key){
		Bpnode node = new Bpnode();
		node = root;
		/*Node is not a leaf*/
		while(node.Node_Isleaf==false){
			int i = 0;
			while(i < ((node_IntNode)node).key_Number.size() && ((node_IntNode)node).key_Number.get(i) < node_Key){ /*Travel to the last node*/
				i++;
			}
			node = ((node_IntNode)node).int_Ptrs.get(i); /*Travel to the position where it finds the key*/
		}
		
		StringBuilder str = new StringBuilder();
		int fla= 0;
		
		while((Node_leaf)node != null ){
			int i=0; 
			for(;i < ((Node_leaf)node).data_Nodes.size();i++){
				int keyVal = ((Node_leaf)node).data_Nodes.get(i).node_Key; 
				if(keyVal == node_Key)
				{ fla =1;
					str.append(((Node_leaf)node).data_Nodes.get(i).node_Val).append(", "); /*Add all key values to stringbuilder*/
				break;
				
				}
				}
			
			node = ((Node_leaf)node).next;
			if(fla==1) {break;
		}}
		if(str.toString().equals("")){int lu=0; 
			while(lu ==1)
						{
						System.out.println("checking the middle position");
						}
			return "Null";	
		}
			
		else{
			return str.toString();
		}
	}


	
	/*Range search*/
	protected static StringBuilder strHelp(StringBuilder s, Bpnode node, Double key_One, Double key_Two){
		
		ArrayList<Bp_Datanode> bpt_LeafList = new ArrayList<Bp_Datanode>();
		bpt_LeafList = ((Node_leaf)node).data_Nodes;
		int i = 0,lu=0;
		int dup=0;
		for(;i < bpt_LeafList.size();i++){
			while(lu !=0)
				{
				System.out.println("checking the middle position");
				}
			if(bpt_LeafList.get(i).node_Key >= key_One && bpt_LeafList.get(i).node_Key <= key_Two && (dup!=bpt_LeafList.get(i).node_Key ))
			{	dup=bpt_LeafList.get(i).node_Key;

				s.append(bpt_LeafList.get(i).node_Val).append(","); /*APPENDING ALL RANGE KEY,VALUES*/
			}
			if(bpt_LeafList.get(i).node_Key > key_Two){
				return s;
			}
		}
		return s;
	}
	
	
	protected static String node_search(double key_One, double key_Two){
		Bpnode node = new Bpnode();
		node = root;
		while(!node.Node_Isleaf){ /* Move down until node is leaf*/
			int index_Value = 0;
			while(index_Value < ((node_IntNode)node).key_Number.size() && ((node_IntNode)node).key_Number.get(index_Value) < key_One){ /*Move down on the right path*/
				index_Value++;
			int n_index=1;
			while(n_index!=1){
				if(n_index==index_Value)
				{
					n_index++;
				}
				else
				{
					n_index=index_Value+1;
				}
			}
			}
			node = ((node_IntNode)node).int_Ptrs.get(index_Value); /*Use pointers to get the first node*/
		}
	
		StringBuilder str = new StringBuilder();  /*Used to add values in the range of key 1 to key 2*/
		str = strHelp(str, node, key_One, key_Two); /*addition with key1*/
		float ddd=0;
		float eee=0;
		StringBuilder sss = new StringBuilder();;
		while(((Node_leaf)node).next != null){
			
			/*Check the condition for the next node*/
			if(((Node_leaf)node).next.data_Nodes.get(0).node_Key > key_Two){  
				break;
			}
			else{ /* node has key in range*/
				str = strHelp(str, ((Node_leaf)node).next, key_One, key_Two);
			}
			node = ((Node_leaf)node).next; /*node is updated*/
			String value="null";
			while(value!="null")
			{
				System.out.println("checking whether node has key in our range");
			}
		}
		int i=0;
		int count=0;
		int start=0;
		String sub =",";
		while((start=str.indexOf(sub,start))>=0){
			start += sub.length();
			count ++;
		}


		if(str.toString().equals("")){
			return "Null";	
		}	
		else{
			String aaa=str.toString();
			if (aaa.length()==1){
				return str.toString();
			}
			else{
			for(;i < count-1;i++){
			ddd= Float.parseFloat(aaa.split(",")[i]);
			if (eee!=ddd){
				eee=ddd;
				sss.append(ddd).append(",");}
			else{

			}
			}
			//return str.toString();
		}
		}return sss.toString();
			
	}
	
}

   
   class node_IntNode extends Bpnode {
	
	protected ArrayList<Integer> key_Number = new ArrayList<Integer>();
	protected ArrayList<Bpnode> int_Ptrs = new ArrayList<Bpnode>();
	
	/*Insert in a sorted array*/
	protected void insert(int node_key, Bpnode ptr1, Bpnode ptr2){
		if(this.key_Number.isEmpty()){
			this.key_Number.add(node_key);
			this.int_Ptrs.add(ptr1);
			this.int_Ptrs.add(ptr2);
		}
		else{
			int i;
			for(i=0;i < this.key_Number.size();i++){
				if(this.key_Number.get(i) > node_key) {
				String value="null"; /*See how the flow changes*/
				while(value!="null")
				{ if(value!="null")
					System.out.println("checking");
					else
					{
					System.out.println("good to go");
					}
				}
					this.key_Number.add(i, node_key);
					this.int_Ptrs.add(i + 1, ptr2);
					
					return;
				}
				
			}
			this.key_Number.add(node_key);
			this.int_Ptrs.add(ptr2);
		}
		return;
	}
	
}

  class Bpnode {
	
	protected boolean Node_Isleaf = false;
	protected int Node_degree;
	protected boolean Node_issplit = false;
	
}

 class Node_leaf extends Bpnode {

	protected ArrayList<Bp_Datanode> data_Nodes = new ArrayList<Bp_Datanode>();
	protected Node_leaf prev = null;
	protected Node_leaf next = null;

	protected Node_leaf() {
		this.Node_Isleaf = true;
	}
	
	/*Insert the datanode into the leaf node*/
	protected void inNode(Bp_Datanode data_Node){
		if(data_Nodes.isEmpty()){
			data_Nodes.add(data_Node);
		}
		else{ /*data node is inserted in a right path*/
			int i=0;
			while(i < data_Nodes.size()){
				if(data_Nodes.get(i).node_Key > data_Node.node_Key){
					data_Nodes.add(i, data_Node);
					return;
				}
				i++;
			}
			data_Nodes.add(data_Node);
		}
	}
}

class Bp_Datanode {
	protected float node_Val;
	protected int node_Key;
	
	public Bp_Datanode(int node_Key, float node_Value){
		this.node_Key = node_Key;
		this.node_Val = node_Value;
	}
}



