public class Edge implements Comparable<Edge> {
	public String roadID;
	public final String IntID1, IntID2; // an edge from v to w
	public double weight;
	public Edge(String id, String v, String w) { 
		roadID = id;
		this.IntID1 = v;
		this.IntID2 = w;
	}
	public int compareTo(Edge o) {
		if ( this.weight < o.weight) {
			return -1;
		}
		else if ( this.weight > o.weight) {
			return 1;
		}
		return 0;
	}
}