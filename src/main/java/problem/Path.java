package problem;

import java.util.List;

import problem.Grid.Coord;

/**
 * also wraps up some details of the algorithm run
 */
public class Path {
	private final List<Coord> path;
	private final double cost;
	private long expansionCount;
	
	public Path(List<Coord> path, double cost) {
		this.path = path;
		this.cost = cost;
		this.expansionCount = 0;
	}
	
	public List<Coord> getPath() {
		return path;
	}
	
	public double getCost() {
		return cost;
	}
	
	public void setExpansionCount(long expansionCount) {
		this.expansionCount = expansionCount;
	}
	
	public long getExpansionCount() {
		return expansionCount;
	}
}
