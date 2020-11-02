import java.io.*;
import java.util.*;
//Jack Mandell
//jmandel5@u.rochester.edu

public class Graph {
	public double MAX_LAT;
	public double MAX_LONG;
	public double MIN_LAT;
	public double MIN_LONG ;
	private int vertexCount, edgeCount = 0;
	public boolean directed; // false for undirected graphs, true for directed
	public HashMap <String,Vertex> hashmap;
 	public PriorityQueue<Vertex> queue;
 	public PriorityQueue<Edge> edgesPQ;
 	public ArrayList<Vertex> myVerticies;
 	public ArrayList<Edge> myEdges;
 	public int[] parent;
	
 	public Graph( boolean isDirected, ArrayList<Vertex> verticies, ArrayList<Edge> edges) {
 		myVerticies = verticies;
 		myEdges = edges;
 		MAX_LAT = verticies.get(0).latitude;
 		MIN_LAT = verticies.get(0).latitude;
 		MAX_LONG = verticies.get(0).longitude;
 		MIN_LONG =  verticies.get(0).longitude;
 		directed = isDirected;
 		vertexCount = verticies.size();
 		hashmap = new HashMap<String,Vertex>();
 		edgesPQ = new PriorityQueue<Edge>();
 		Vertex v;
 		for ( int i = 0 ; i < vertexCount ; i++) {
 			v = verticies.get(i);
 			v.index = i;
 			hashmap.put(v.name,v);
 			if ( v.latitude < MIN_LAT ) {
 				MIN_LAT = v.latitude;
 			}
 			else if ( v.latitude > MAX_LAT) {
 				MAX_LAT = v.latitude;
 			}
 			if ( v.longitude < MIN_LONG ) {
 				MIN_LONG = v.longitude;
 			}
 			else if ( v.longitude > MAX_LONG ) {
 				MAX_LONG = v.longitude;
 			}
 			
 		}
 		Vertex v1, v2;
 		Edge e;
 		double d;
 		for ( int i = 0 ; i < edges.size(); i++) {
 			e = edges.get(i);
 			v1 = hashmap.get(e.IntID1);
 			v2 = hashmap.get(e.IntID2);
 			v1.neighbors.put(v2.name,v2.name);
 			v2.neighbors.put(v1.name,v1.name);
 			hashmap.replace(e.IntID1, v1);
 			hashmap.replace(e.IntID2, v2);
 			d = computeDistance(v1,v2);
 			e.weight = d; 
 			edgesPQ.add(e);
 			v1.distances.put(v2.name, d);
 			v2.distances.put(v1.name, d);
 			edgeCount++;
 		}
  	}
	public Graph() {
		// TODO Auto-generated constructor stub
	}
	private double computeDistance ( Vertex v1, Vertex v2) {
 		double R, lat1, lon1, lat2, lon2;
 		R = 3958.8;
 		lat1 = v1.latitude;
		lon1 = v1.longitude;
		lat2 = v2.latitude;
		lon2 = v2.longitude;
		Double latDistance = toRad(lat2-lat1);
		Double lonDistance = toRad(lon2-lon1);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
		Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
		Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double distance = R * c;
		return distance;
 	}
 	public boolean isDirected() { 
 		return directed;
 	}
 	public int verticies() { 
 		return vertexCount;
 	}
 	public int edges() { 
 		return edgeCount;
 	}
 	public void insert(Edge e) { 
 		Vertex v1, v2;
		v1 = hashmap.get(e.IntID1);
		v2 = hashmap.get(e.IntID2);
		v1.neighbors.put(v2.name,v2.name);
		v2.neighbors.put(v1.name,v1.name);
		hashmap.replace(e.IntID1, v1);
		hashmap.replace(e.IntID2, v2);
 	}
 	
 	public boolean connected(String node1, String node2) { 
 		return hashmap.get(node1).isConnected(hashmap.get(node2));
 	}
 	public void dijkstra(String sourceName) {
 		queue = new PriorityQueue<Vertex>(vertexCount);
 		Vertex src;
 		src = hashmap.get(sourceName);
 		src.dist = 0;
 		queue.add(src);
 		while ( ! queue.isEmpty() ) {
 			Vertex u = queue.remove();
 			u.known = true;
 			check_Neighbors(u);
 		}
 	}
 	private void check_Neighbors(Vertex u ) {
 		double edgeDistance = -1;
 		double newDistance = -1;  
 		//Collects all names of neighbors of u
 		Collection<String> n = u.neighbors.values();
 		Iterator<String> it2 = n.iterator();
 		Vertex w;
 		while ( it2.hasNext()) {
 			w = hashmap.get(it2.next());
 			//If node has not been processed
 			if ( w.known == false ) {
 				edgeDistance = w.distances.get(u.name);
 				newDistance = u.dist + edgeDistance;
 				if ( newDistance < w.dist ) {
 					w.dist = newDistance;
 					w.parent = u;
 					if ( queue.contains(w) == false) {
 						queue.add(w);
 					}
 				}
 			}
 		}
		
	}
 	public double printPath( String locationName) {
 		Vertex v = hashmap.get(locationName);
 		if ( v.parent != null) {
 			System.out.println(v.name);
 			System.out.println(" to");
 			return v.distances.get(v.parent.name) + printPath(v.parent.name);
		}
 		else {
 			System.out.println(v.name);
 			return 0;
 		}
 	}
 	public Double toRad(Double value) {
 		return value * Math.PI / 180;
 	}
 	public ArrayList<Edge> kruskal() {
 		ArrayList<Edge> spanningTreeEdges = new ArrayList<>();
 		int v1set, v2set;
 		Edge e;
 		parent = new int[vertexCount];
 		for ( int j = 0 ; j < vertexCount ; j++) {
 			parent[j] = j;
 		}
 		int i = 0;
 		while ( ! edgesPQ.isEmpty() && i <vertexCount  ) {
 			e = edgesPQ.poll();
 			v1set = find(hashmap.get(e.IntID1).index);
 			v2set = find(hashmap.get(e.IntID2).index);
 			if ( v1set != v2set ) {
 				spanningTreeEdges.add(e);
 				union(v1set,v2set);	
 				i++;
 			}
 		}	
 		return spanningTreeEdges;
 		
 	}
 	public int find(  int vertexIndex) {
		if(parent[vertexIndex]!=vertexIndex)
            return find(parent[vertexIndex]);
        return vertexIndex;

 	}
 	public void union( int v1Index, int v2Index) {
 		parent[v1Index] = v2Index;
 	}
 	public void printRoads() {
 		System.out.println("Roads Traveled: ");
 		for ( int i = 0; i < edgeCount ; i++) {
 			System.out.println(myEdges.get(i).roadID);
 		}
 	}
}
 	