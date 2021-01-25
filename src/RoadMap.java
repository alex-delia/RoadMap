import java.io.*;
import java.util.*;

public class RoadMap {
	//instance variables
	int scale;
	int start;
	int end;
	int width;
	int length;
	int initial_budget;
	int toll;
	int gain;
	Stack<Node> stack = new Stack<Node>();
	Graph graph;
	
	public RoadMap(String inputFile) throws MapException {
		//trys to make the roadmap from the input file
		try {
			String currentLine;
			String nextLine;
			String nextNext;
			
			//outs the file into buffered reader
			File file = new File(inputFile);
			BufferedReader input = new BufferedReader(new FileReader(file));
			
			//uses the first 8 lines to set instance variables
			scale = Integer.parseInt(input.readLine());
			start = Integer.parseInt(input.readLine());
			end = Integer.parseInt(input.readLine());
			width = Integer.parseInt(input.readLine());
			length = Integer.parseInt(input.readLine());
			initial_budget = Integer.parseInt(input.readLine());
			toll = Integer.parseInt(input.readLine());
			gain = Integer.parseInt(input.readLine());
			
			//sets the current line to line 9
			currentLine = input.readLine();
			//sets the next line to line 10
			nextLine = input.readLine();
			//sets the next next line to line 11
			nextNext = input.readLine();
			
			//makes a new graph of size length*width
			graph = new Graph(width*length);
			
			//sets the node counter to negative 1 as our first node is 0
			int nodeCounter = -1;
			char next;
			char prev;
			Node currNode;
			Node prevNode;
			Node nextNode;
			int edgeType;
			
			//checks that the current line is not null
			while(currentLine != null) {
				//for all the characters in the line
				for(int i=0; i<currentLine.length(); i++) {
					char curr = currentLine.charAt(i);
					//if the current character is a node
					if(curr == '+') {
						//increment the node counter
						nodeCounter++;
						//get the node at that position
						currNode = graph.getNode(nodeCounter);
						try {
							//if the character below is not X and the character 2 below is a +
							if(nextLine.charAt(i) != 'X' && nextNext.charAt(i) == '+') {
								//create an edge vertically between these two node
								edgeType = edgeType(nextLine.charAt(i));
								nextNode = graph.getNode(nodeCounter+width);
								graph.insertEdge(currNode, nextNode, edgeType);
							}
						}catch(Exception e) {
							//if an exception is caught, move on to the next character in the current line
							continue;
						}
					//otherwise if the current character is not '+'
					}else {
						
						//try to get the previous character
						try {
							prev = currentLine.charAt(i-1);
						}catch(IndexOutOfBoundsException e) {
							prev = ' ';
						}
						
						//try to get the next character
						try {
							next = currentLine.charAt(i+1);
						}catch(IndexOutOfBoundsException e) {
							next = ' ';
						}
						
						//gets the edge type of the current character
						edgeType = edgeType(curr);
						
						//if the current edge type is -2, representing a house, continue to the next character
						if(edgeType == -2)
							continue;
						
						//if the previous node is a +, get that node, otherwise continue to next character
						if(prev == '+') {
							prevNode = graph.getNode(nodeCounter);
						}else {
							continue;
						}
						
						//if the next node is a + get that node, otherwise continue to next character
						if(next == '+') {
							nextNode = graph.getNode(nodeCounter+1);
						}else {
							continue;
						}
						
						//insert an edge between these two nodes of the current edge type
						graph.insertEdge(prevNode, nextNode, edgeType);
					}	
				}
				//set the current line to next line
				currentLine = nextLine;
				//set the next line to next next line
				nextLine = nextNext;
				//set the next next line to the next line in the file
				nextNext = input.readLine();
			}
			//close the input file
			input.close();
		}catch(IOException e) {
			//if an IO error is caught with the file, throws a map exception
			throw new MapException();
		}
	}
	
	//gets the edge type of the current character
	private int edgeType(char c) {
		//if the character is T our road type is 1
		if(c == 'T')
			return 1;
		//if the character is F our road type is 0
		else if(c == 'F')
			return 0;
		//if the character is C our road type is -1
		else if(c == 'C')
			return -1;
		//if the character is none of these the road type is -2
		else
			return -2;
	}
	
	//returns the graph
	public Graph getGraph() {
		return graph;
	}
	
	//gets the starting node
	public int getStartingNode() {
		return start;
	}
	
	//gets the destination node
	public int getDestinationNode() {
		return end;
	}
	
	//gets the initial money
	public int getInitialMoney() {
		return initial_budget;
	}
	
	//finds a path from start node to finish node
	public Iterator findPath(int start, int destination, int initialMoney) {
		//gets the start node, sets its mark to true, and push it into the stack
		Node startNode = getGraph().getNode(start);
		startNode.setMark(true);
		stack.push(startNode);
		
		//if this node matches the destination, return the stack iterator
		if(start == destination) {
			return stack.iterator();
		}
		
		//get the incident edges of the current node
		Iterator<Edge> incidentEdges = graph.incidentEdges(startNode);
		
		//while there is another incident edge
		while(incidentEdges.hasNext()) {
			//get this edge
			Edge nextEdge = incidentEdges.next();
			
			//get the node on the other side of this edge
			Node nextNode = nextEdge.secondEndpoint();
			if(nextNode.equals(startNode)) {
				nextNode = nextEdge.firstEndpoint();
			}
			
			//get the cost of the current road
			int cost = roadValue(nextEdge.getType());
			
			//if the node isn't already marked and they have more money or equal money to cost
			if(!nextNode.getMark() && (initialMoney >= cost)) {
				//subtract the cost from money
				initialMoney -= cost;
				
				//recursively check the path at this node
				Iterator<Edge> path = findPath(nextNode.getName(), destination, initialMoney);
				
				//if this path is not null, return it
				if(path != null)
					return path;
				//otherwise add the money back
				else 
					initialMoney+=cost;
				}
			}
		
		//set the node's mark to false, pop it from the stack, and return null
		startNode.setMark(false);
		stack.pop();
		return null;
	}
	
	//returns the money value of the road
	private int roadValue(int roadType) {
		//if the roadtype is 0 it is free
		if(roadType == 0) {
			return 0;
		//if the road type is -1 it you gain money from it, therefore returning a negative gain will become
		//positive when subtracting
		}else if(roadType == -1) {
			return -gain;
		//if the road type is 1, it is a toll and this value is returned
		}else {
			return toll;
		}
	}

	
}
