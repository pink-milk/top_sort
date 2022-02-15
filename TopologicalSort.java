class TopologicalSort {
	//uses DFS to create a topological sort
	//accepts a graph stored as an adjacency matrix
	//returns null for invalid inputs for a topological sort
	public static Node tsortDFS(boolean[][] graph, int startNode) {
		//error checking on inputs, requires graph, valid starting node, and square matrix
		if(startNode < 0 || startNode > graph.length-1) {
			return null;
		}
		
		for(int i = 0; i < graph.length; i++) {
			if(graph[i].length != graph.length) {
				return null;
			}
		}
		

		//indicators for whether or not a node has been visited
		//0 = not started, -1 = started not finished, 1 = finished
		int[] visited = new int[graph.length];
		
		//the topological ordering
		Node sortedOrder = new Node(); //dummy head
		
		//initialize DFS
		boolean dfsDone = false;
		
		while(!dfsDone) {
			//visit the start node

			if(!visit(graph, startNode, visited, sortedOrder)) {
				//graph has no topological sorting

				return null;
			}

			
			//make sure all the nodes have been visited
			dfsDone = true;
			for(int i = 0; i < graph.length; i++) {
				if(visited[i] == 0) {
					startNode = i;
					dfsDone = false;
					break;
				}
			}
		}
		
		//return the topological sorting
		return sortedOrder.next; //skip dummy head
	}
	
	private static boolean visit(boolean[][] graph, int currentNode, int[] visited, Node sortedOrder) {
		//mark current node as started
		visited[currentNode] = -1;
		
		//visit each neighbor who hasn't been visited before
		for(int i = 0; i < graph.length; i++) {
			//if i is a neighbor of the current node...
			if(graph[currentNode][i]) {
				//check if there's a cycle
				if(visited[i] == -1) {
					return false;
				}
				
				//try to visit neighbor if not visited, but stop if neighbor encounters a cycle
				if(visited[i] != 1 && !visit(graph, i, visited, sortedOrder)) {
					return false;
				}
			}
		}
		
		//node is finished, mark finished and prepend to topological sorting
		visited[currentNode] = 1;
		sortedOrder.next = new Node(currentNode, sortedOrder.next);
		
		return true;
	}
	
	//uses removal method to create a topological sort
	//accepts a graph stored as an adjacency list
	//returns null for invalid inputs for a topological sort
	public static Node tsortRemoval(Node[] graph) {
		//error checking on inputs, requires graph and nodes to only point within the graph
		for(int i = 0; i < graph.length; i++) {
			Node curr = graph[i];
			while(curr != null) {
				if(curr.value < 0 || curr.value > graph.length-1) {
					return null;
				}
				curr = curr.next;
			}
		}
		
		
		//a count of the number of incoming edges for each node
		int[] numIncoming = new int[graph.length];
		
		for(int i = 0; i < graph.length; i++) {
			Node curr = graph[i];
			while(curr != null) {

				// System.out.println("numincocming: "+numIncoming[curr.value]);

				numIncoming[curr.value]++;
				curr = curr.next;
			}
		}
		// System.out.println(numIncoming[2]);
		// System.out.println("here");


		//the set of nodes with no incoming edges
		Node setNoIncoming = new Node(); //dummy head
		
		for(int i = 0; i < numIncoming.length; i++) {
			if(numIncoming[i] == 0) {
				// System.out.println(numIncoming[i]);
				setNoIncoming.next = new Node(i, setNoIncoming.next);
			}
		}
		
		

		//the topological ordering
		int numSorted = 0;
		Node sortedOrder = new Node(); //dummy head
		Node sortedOrderTail = sortedOrder; //need a tail for appending
		
		
		//while there are nodes with no incoming edges
		while(setNoIncoming.next != null) {
			//get one node and remove it from the set
			int nodeId = setNoIncoming.next.value;
			setNoIncoming.next = setNoIncoming.next.next;
			
			//add it to the topological sorting
			sortedOrderTail.next = new Node(nodeId);
			sortedOrderTail = sortedOrderTail.next;
			numSorted++;
			
			//for each node it's connected to, remove one incoming edge
			Node curr = graph[nodeId];
			while(curr != null) {
				if(--numIncoming[curr.value] == 0) {
					//if there are no more incoming edges,
					//add this to the set of nodes with no incoming edges
					setNoIncoming.next = new Node(curr.value, setNoIncoming.next);
				}
				
				curr = curr.next;
			}
		}
		// System.out.println("here!!!");
		// System.out.println(sortedOrder.toString());

		//there must have been a cycle somewhere
		if(numSorted != graph.length) {
			return null;
		}
		
		

		return sortedOrder.next; //skip dummy head
	}
}