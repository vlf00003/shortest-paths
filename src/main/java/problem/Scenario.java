package problem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import problem.Grid.Coord;

public class Scenario {
	private int bucket;
	private Coord origin;
	private Coord target;
	private double knownBestCost;
	
	public Scenario(int bucket, Coord origin, Coord target, double knownBestCost) {
		this.bucket = bucket;
		this.origin = origin;
		this.target = target;
		this.knownBestCost = knownBestCost;
	}
	
	public int getBucket() {
		return bucket;
	}
	
	public Coord getOrigin() {
		return origin;
	}
	
	public Coord getTarget() {
		return target;
	}
	
	public double getKnownBestCost() {
		return knownBestCost;
	}
	
	public static List<Scenario> loadFromFile(String filename) {
		try {
			List<String> lines = Files.readAllLines(new File(filename).toPath());
			List<Scenario> rval = new ArrayList<>(lines.size() - 1);
			
			// format: https://www.movingai.com/benchmarks/formats.html
			// The optimal path length is assuming sqrt(2) diagonal costs.
			// The optimal path length assumes agents cannot cut corners through walls
			// If the map height/width do not match the file, it should be scaled to that size *** NB this is not done yet
			// (0, 0) is in the upper left corner of the maps
			// Technically a single scenario file can have problems from many different maps, but currently every scenario only contains problems from a single map
			for (int i = 1; i < lines.size(); i++) { // skip header line
				String[] cols = lines.get(i).split("\\s+");
				
				rval.add(new Scenario(
						Integer.parseInt(cols[0]),
						new Coord(Integer.parseInt(cols[4]),Integer.parseInt(cols[5])),
						new Coord(Integer.parseInt(cols[6]),Integer.parseInt(cols[7])),
						Double.parseDouble(cols[8])));
			}
			
			return rval;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
}
