package algorithm;

import java.util.ArrayList;
import java.util.List;

import problem.Grid.Coord;

class Label implements Comparable<Label> {
	private final double cost;
	private final double heuristicCost;
	private final Label previous;
	private final Coord coord;

	public Label(double cost, double heuristicCost, Coord coord, Label prev) {
		this.cost = cost;
		this.heuristicCost = heuristicCost;
		this.coord = coord;
		this.previous = prev;
	}
	
	public boolean dominates(Label that) {
		return this.compareTo(that) < 0;
	}
	
	@Override
	public int compareTo(Label that) {
		return Double.compare(this.cost + this.heuristicCost, that.cost + that.heuristicCost);
	}
	
	@Override
	public boolean equals(Object that) {
		return this.compareTo((Label)that) == 0;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(this.cost + this.heuristicCost);
	}
	
	public Coord getCoord() {
		return coord;
	}
	public Label getPrevious() {
		return previous;
	}
	public double getCost() {
		return cost;
	}
	public double getHeuristicCost() {
		return heuristicCost;
	}
	
	public List<Coord> toRoute() {
		List<Coord> l = new ArrayList<>();
		Label lab = this;
		while (lab != null) {
			l.add(lab.getCoord());
			lab = lab.getPrevious();
		}
		return l;
	}
	
	public StringBuffer routeToString() {
		if (this.previous == null) { // start of route
			return new StringBuffer(coord.getX() + "," + coord.getY());
		} else {
			return this.getPrevious().routeToString().append(" ").append(this.coord.getX()).append(",").append(this.coord.getY());
		}
	}
	
	@Override
	public String toString() {
		return "L[n=" + coord.getX() + "," + coord.getY() + "; prevN=" + (previous!=null?previous.getCoord().getX()+","+previous.getCoord().getX():"null") + "; cost=" + cost + ", heuristic=" + heuristicCost + "]";
	}
}