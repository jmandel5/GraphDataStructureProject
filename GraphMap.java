import java.awt.Graphics;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

public class GraphMap extends JFrame {
	public static void main ( String[] args ) throws IOException {
		File f = new File(args[0]);
		Scanner in = new Scanner(f);
		
		//Find all intersections
		Double Lat;
		Double Long;
		String IntersectionID;
		ArrayList<Vertex> intersections= new ArrayList<>();
		ArrayList<Vertex> intersections2 = new ArrayList<>();
		String token;
		int count = 0;
		while ( (token = in.next()).equals("i")) {
			IntersectionID = in.next();
			Lat = in.nextDouble();
			Long = in.nextDouble();
			intersections.add( new Vertex (IntersectionID, Lat, Long));
			intersections2.add( new Vertex (IntersectionID, Lat, Long));
			count ++;
		}
		//Find all roads
		ArrayList<Edge> roads = new ArrayList<>();
		String RoadID = in.next() ;
		String ID1 = in.next();
		String ID2 = in.next();
		roads.add( new Edge(RoadID, ID1, ID2));

		int count2 = 1;
		while (in.hasNext() ) {
			token = in.next();
			RoadID = in.next() ;
			ID1 = in.next();
			ID2 = in.next();
			roads.add( new Edge(RoadID, ID1, ID2));
			count2++;
		}
		//Construct the Graph
		Graph newMap = new Graph( false, intersections, roads);

		int s;
		String int1 = "", int2 = "";
		boolean toShow = false;
		boolean giveMeridian = false;
		boolean giveDirections = false;
		for ( int i = 1 ; i < args.length ; i++) {
			if ( args[i].equals("-show")) {
				toShow = true;
			}
			else if (args[i].equals("-directions") ) {
				i++;
				int1 = args[i];
				i++;
				int2 = args[i];
				giveDirections = true;
			}
			else if ( args[i].equals("-meridianmap")) {
				giveMeridian = true;
			}
		}
		double d;
		int window = 600;
		if ( toShow == true && giveMeridian == true && giveDirections == false) {
			 ArrayList<Edge> newEdges = newMap.kruskal();
			 Graph treeSpan = new Graph(false,intersections2, newEdges);
			 DrawMap.main(newMap,treeSpan,window,"");
			 treeSpan.printRoads();
		}
		else if ( toShow == true && giveMeridian == false && giveDirections == true) {
			 newMap.dijkstra(int2);
			 d = newMap.printPath(int1);
			 System.out.println("The distance from " + int1 + " to " + int2 + " is " + d + " miles");
			 DrawMap.main(newMap,null,window,int1);
		}
		else if ( toShow == true && giveMeridian == false && giveDirections == false) {
			DrawMap.main(newMap,null,window,"");
		}
		else if ( toShow == false && giveMeridian == true && giveDirections == false) {
			ArrayList<Edge> newEdges = newMap.kruskal();
			Graph treeSpan = new Graph(false,intersections2, newEdges);
			treeSpan.printRoads();
		}
		else if ( toShow == false && giveMeridian == false && giveDirections == true) {
			newMap.dijkstra(int2);
			d = newMap.printPath(int1);			
			System.out.println("The distance from " + int1 + " to " + int2 + " is " + d + " miles");
		}
 	}
}
