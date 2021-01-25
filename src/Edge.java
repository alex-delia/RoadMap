
public class Edge {
	Node u;
	Node v;
	int type;
	
	//constructor for the class to make a new edge
	public Edge(Node u, Node v, int type){
		this.u = u;
		this.v = v;
		this.type = type;
	}
	
	//returns the first endpoint of the edge
	public Node firstEndpoint() {
		return u;
	}
	
	//returns the second endpoint of the edge
	public Node secondEndpoint() {
		return v;
	}
	
	//returns the type of the edge
	int getType() {
		return type;
	}
	
	
}
