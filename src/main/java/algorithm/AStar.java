package algorithm;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math3.util.Precision;

import problem.Grid;
import problem.Grid.Coord;
import problem.NoPath;
import problem.Path;
import problem.Scenario;

public class AStar extends Algorithm {

	static Logger logger = Logger.getLogger("");
	private Heuristic heuristic;
	private double weight;
	
	public static void main(String[] args) {
		Grid g = Grid.loadFromFile("./examples/dao/arena2.map");
		List<Scenario> s = Scenario.loadFromFile("./examples/dao/arena2.map.scen");
		
		logger.setLevel(Level.OFF);
		
		Algorithm d = new AStar(g);
		
		final int REPEATS = 10;
		int passes = 0;
		int count = 0;
		long startTime = System.nanoTime();
		for (Scenario ss : s) {
			for (int rpt = 0; rpt < REPEATS; rpt++) {
				Path p = d.getPath(ss);
				boolean good = Precision.compareTo(p.getCost(), ss.getKnownBestCost(), 0.00001) == 0;
	//			System.out.println(count++ + ": " + p.getCost() + "(exp:" + ss.getKnownBestCost() + ") " + good + " [" + p.getExpansionCount() + "] " + p.getPath());
				if (good) {
					passes++;
				}
			}
		}
		
		long endTime = System.nanoTime();
		System.out.println("Shortest path found for " + passes + "/" + (REPEATS * s.size()));
		System.out.println("Total time: " + (endTime - startTime) / 1000000000.0 + "s");
		System.out.println("Avg time per path: " + (endTime - startTime) / ((REPEATS * s.size()) * 1000000.0) + "ms");
		
	}
	
	public AStar(Grid grid) {
		this(grid, 1.0);
	}
	
	public AStar(Grid grid, double weight) {
		super(grid);
		this.weight = weight;
		this.heuristic = new Heuristic();
	}
	
	@Override
	public Path getPath(Scenario scenario) {
		Map<Coord,Label> labels = new TreeMap<Coord,Label>();//new HashMap<Coord,Label>();
		PriorityQueue<Label> heap = new PriorityQueue<>();
		boolean[][] visited = new boolean[grid.getWidth()][grid.getHeight()];
		
		// journey is from ... to ...
		Coord origin = scenario.getOrigin();
		Coord target = scenario.getTarget();
		
		logger.info("From: " + origin + " to: " + target);
		
		// create label for origin and add to heap
		Label l0 = new Label(0, 0, origin, null);
		heap.add(l0);
		labels.put(origin, l0);
		visited[origin.getX()][origin.getY()] = true;
		
		Label l = null;
		long lastExpansionCount = 0;
		
		mainLoop:
		while (!heap.isEmpty()) {
			l = heap.remove();
			lastExpansionCount++;
			
			//System.out.println("Mainloop:" + l);
			visited[l.getCoord().getX()][l.getCoord().getY()] = true;
			
			// if this label is for the destination, stop
			//logger.fine("Comparing " + l.getCoord() + " and " + target);
			if (l.getCoord().equals(target)) {
				break mainLoop;
			}
			
			// for each outgoing edge...
			// the 8 cells around the present label's location
			// except those already visited
			int[][] offsets = {{-1,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1}};
			List<Coord> neighbours = new ArrayList<>();
			for (int[] offset : offsets) {
				int newX = l.getCoord().getX() + offset[0];
				int newY = l.getCoord().getY() + offset[1];
				if (newX >= 0 && newX < grid.getWidth() && newY >= 0 && newY < grid.getHeight()) {
					Coord c = new Coord(newX, newY);
					if (grid.getCell(c).isPassable()) {
						// we're not allowed to cut a diagonal through a 
						// corner of an impassable square, so check those too
						// vertical/horizontal movements okay, and diagonals okay if corner checks out
						if ((offset[0] == 0) ||
							(offset[1] == 0) ||
								(grid.getCell(l.getCoord().getX() + offset[0], l.getCoord().getY()).isPassable() &&
								 grid.getCell(l.getCoord().getX(), l.getCoord().getY() + offset[1]).isPassable())) {
							neighbours.add(c);
						}
					}
				}	
			}
			
			neighbourLoop:
			for (Coord opposite : neighbours) {
				if (visited[opposite.getX()][opposite.getY()]) {
					continue neighbourLoop;
				}
				
				// cost to reach node
				double cost = l.getCost();
				if (opposite.getX() == l.getCoord().getX() || opposite.getY() == l.getCoord().getY()) {
					cost += 1;
				} else { // diagonal
					cost += Math.sqrt(2.0);
				}
				
				double heuristicCost = weight * heuristic.cost(opposite, target);
				
				Label newLabel = new Label(cost, heuristicCost, opposite, l);
				
				//logger.fine("Opposite:" + opposite + ", total cost " + cost);
				
				// does this improve on the current label for that node?
				// if so, update label and add to queue
				if (!labels.containsKey(opposite) || newLabel.dominates(labels.get(opposite))) {
					//logger.fine("Route to " + opposite + " best yet, adding...");
					labels.put(opposite, newLabel);
					heap.add(newLabel);
				} else {
					//logger.fine("A better route to " + opposite + " exists.");
				}
			}
		}
		
		logger.info("Done...");
		
		Path p;
		
		if (l == null || !l.getCoord().equals(target)) {
			logger.info("no route!");
			
			p = new NoPath();
		} else {
			logger.info("[" + l.routeToString() + "]");
			
			p = new Path(l.toRoute(), l.getCost());
		}
		
		logger.info(lastExpansionCount + " labels visited.");
		
		logger.info("cost: " + l.getCost());
		
		p.setExpansionCount(lastExpansionCount);
		
		return p;
	}


}
