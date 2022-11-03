package algorithm;

import problem.Grid;
import problem.Path;
import problem.Scenario;

public abstract class Algorithm {
	protected Grid grid;
	
	public Algorithm(Grid grid) {
		this.grid = grid;
	}
	
	public abstract Path getPath(Scenario scenario);
}
