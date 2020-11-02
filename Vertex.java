import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

public class Vertex implements Comparable<Vertex>  {
	public String name;
	public int index;
	public double latitude;
	public double longitude;
	public HashMap<String,String> neighbors;
	public HashMap<String,Double> distances;
	public double dist = INFINITY;
	public boolean known;
	public static final double INFINITY = Double.MAX_VALUE;
	public Vertex parent;
	//only for union find
	
	public Vertex (String name ,double la, double lo) {
		neighbors = new HashMap<String,String>();
		distances = new HashMap<String,Double>();
		this.name = name;
		latitude = la;
		longitude = lo;
		known = false;
	}
	public boolean isConnected (Vertex other) {
		if ( neighbors.containsKey(other.name) == true ) {
			return true;
		}
		return false;
	}
	@Override
	public int compareTo(Vertex o) {
		if ( this.dist < o.dist) {
			return -1;
		}
		else if (this.dist > o.dist )
			return 1;
		return 0;
	}
	
}
