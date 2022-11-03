package algorithm;

import problem.Grid.Coord;

/**
 * Euclidean by default
 */
public class Heuristic {
	public double cost(Coord coord1, Coord coord2) {
		return coord1.distanceFrom(coord2);
	}
}
