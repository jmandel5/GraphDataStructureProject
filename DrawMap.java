import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawMap extends JPanel  {
	private static Graph mapToDraw;
	public static Graph minSpanningTree;
	private static double windowSizeX;
	public static String location;
	public static void main (Graph gr, Graph minTree, int w, String loc) {
		windowSizeX = w;
		mapToDraw = gr;
		location = loc;
		minSpanningTree = minTree;
		DrawMap d = new DrawMap();
		JFrame jf = new JFrame();
		jf.setTitle("Jack's Epic Map");
		jf.setSize((int) (1.2*windowSizeX), (int) (1.2* windowSizeX) );
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(d);
	}
	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 g.setColor(Color.ORANGE);
		 drawTree(mapToDraw,g);
		 if ( minSpanningTree != null) {
			 g.setColor(Color.BLUE);
			 drawTree(minSpanningTree, g);
		 }
		 if ( location != "") {
			 g.setColor(Color.BLACK);
			drawPath(mapToDraw,location,g);
		 }
	}
	 
	private static void drawTree(Graph toDraw, Graphics g) {
		Collection<Vertex> verticies = toDraw.hashmap.values();
		Iterator<Vertex> it1 = verticies.iterator();
		Vertex v;
		Vertex w;
		Collection<String> n;
		Iterator<String> it2;
		double x1, y1, x2, y2;
		//Draw map
		while ( it1.hasNext()) {
			 v = it1.next();
			 n = v.neighbors.values();
			 it2 = n.iterator();
			 while (it2.hasNext()) {
				 x1 = getNewX(toDraw,v);
				 y1 = getNewY(toDraw,v);
				 w = toDraw.hashmap.get(it2.next());
				 x2 = getNewX(toDraw,w);
				 y2 = getNewY(toDraw,w);
				 g.drawLine( (int) (windowSizeX * x1),(int) ((-(windowSizeX * y1))+windowSizeX),(int) (windowSizeX * x2),(int) ((-(windowSizeX * y2)) +windowSizeX));
			 }
		 }
		
	}
	public static double getNewY( Graph toDraw,  Vertex v) {
		 double y = Math.abs((v.latitude - toDraw.MIN_LAT)/(toDraw.MAX_LAT-toDraw.MIN_LAT));
		 return  y;
	 }
	 public static double getNewX ( Graph toDraw, Vertex v) {	
		 double y = Math.abs((v.longitude - toDraw.MIN_LONG)/(toDraw.MAX_LONG-toDraw.MIN_LONG));
		 return  y;
 
	 }
	 public static void drawPath( Graph toDraw, String startName, Graphics g) {
		 Vertex start = toDraw.hashmap.get(startName);
		 Vertex next = toDraw.hashmap.get(startName).parent;
		 double x1, y1, x2, y2;
		 if ( next != null) {
			 x1 = getNewX(toDraw,start);
			 y1 = getNewY(toDraw,start);
			 x2 = getNewX(toDraw,next);
			 y2 = getNewY(toDraw,next);
			 g.drawLine( (int) (windowSizeX * x1),(int) ((-(windowSizeX * y1))+windowSizeX),(int) (windowSizeX * x2),(int) ((-(windowSizeX * y2)) +windowSizeX));
			 drawPath( toDraw,next.name, g);
		 }
	 }
}
