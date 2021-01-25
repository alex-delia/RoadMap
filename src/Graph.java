import java.util.*;

public class Graph {
	Node[] graphNodes;
	Edge adjMatrix[][];
	
	//constructor to make a new graph
	public Graph(int n) {
		//creates a matrix of size nxn
		adjMatrix = new Edge[n][n];
		
		//creates an array to store nodes
		graphNodes = new Node[n];
		
		//fills each array slot with a node of the same name
		for(int i=0; i<n; i++) {
			graphNodes[i] = new Node(i);
		}
		
	}
	
	//returns the node with the inputted name if it exists
	public Node getNode(int name) throws GraphException{
		//if the name is less than the length of the array
		if(name < graphNodes.length) {
			//if the node at name is not equal to null, return that node
			if(graphNodes[name] != null)
				return graphNodes[name];
		}
		
		//if the node doesn't exist, throw a graph exception
		throw new GraphException();
			
	}
	
	//inserts edges into our matrix
	public void insertEdge(Node u, Node v, int edgeType) throws GraphException {
		//checks if the 2 nodes exist
		getNode(u.getName());
		getNode(v.getName());
		
		
		//checks to see that an edge doesn't already exist at this spot
		boolean noEdge = false;
		try {
			getEdge(u,v);
		}catch(GraphException e) {
			noEdge = true;
		}
		
		//if there isnt't already an edge, insert the edge into the matrix between the two nodes
		if(noEdge) {
			Edge edge = new Edge(u,v,edgeType);
			adjMatrix[u.getName()][v.getName()] = edge;
			adjMatrix[v.getName()] [u.getName()] = edge;
		}else {
			//otherwise throw a new GraphException
			throw new GraphException();
		}
	}
	
	//returns an iterator containing all the incident edges of the nodes
	public Iterator incidentEdges(Node u) throws GraphException{
		//make a list of edges
		ArrayList<Edge> list = new ArrayList<Edge>();
	
		//if the name of the node is less than the length of our node array
		if(u.getName() < graphNodes.length) {
			//for each node in the array
			for(int i=0; i<graphNodes.length; i++) {
				//if there is an edge between the two nodes, add that edge to the list
				if(adjMatrix[u.getName()][i] != null) {
					list.add(adjMatrix[u.getName()][i]);
				}
			}
			
			//make the list into an iterator and return it
			Iterator<Edge> iterator = list.iterator();
			return iterator;
		}
		//if the node is invalid, throw a graphException
		throw new GraphException();
		
	}
	
	//returns the edge between the 2 nodes
	public Edge getEdge(Node u, Node v) throws GraphException{
		//if both nodes are valid
		if(u.getName() < graphNodes.length && v.getName() < graphNodes.length) {
			//if there is an edge between these nodes, return the edge
			if(adjMatrix[u.getName()][v.getName()] != null)
				return adjMatrix[u.getName()][v.getName()];
		}
		
		//if there is no edge throw a graph exception
		throw new GraphException();
	}
	
	//checks if the nodes are adjacent
	public boolean areAdjacent(Node u, Node v) throws GraphException{
		//checks if the 2 nodes are valid
		getNode(u.getName());
		getNode(v.getName());
		
		//if there is an edge between the two nodes return true, otherwise return false
		try{
			getEdge(u,v);
			return true;
		}catch(GraphException e) {
			return false;
		}
		
		
	}
}
