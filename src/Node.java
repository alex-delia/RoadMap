
public class Node {
	int name;
	boolean mark;
	
	//constructor for class to make a new node
	public Node(int name){
		this.name = name;
		mark = false;
	}
	
	//Marks the node with the specified value, either true or false
	public void setMark(boolean mark){
		 this.mark = mark;
	}
	 
	//Returns the value with which the node has been marked.
	public boolean getMark() {
		 return mark;
	}
	 
	//Returns the name of the vertex.
	public int getName() {
		 return name;
	}	 
}
