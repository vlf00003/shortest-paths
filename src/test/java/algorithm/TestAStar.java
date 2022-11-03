package algorithm;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import problem.Grid;
import problem.Path;
import problem.Scenario;

public class TestAStar {

	@Test
	public void test() {
		Grid g = Grid.loadFromFile("./examples/dao/arena.map");
		List<Scenario> s = Scenario.loadFromFile("./examples/dao/arena.map.scen");
		Algorithm d = new AStar(g);
		
		int i = 0;
		for (Scenario ss : s) {
			Path p = d.getPath(ss);
			assertEquals("Failed scenario " + i++, ss.getKnownBestCost(), p.getCost(), 0.00001);
		}
	}

}
