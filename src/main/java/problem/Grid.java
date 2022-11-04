package problem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {
	private Cell[][] cells;
	
	public Grid(int width, int height) {
		this.cells = new Cell[width][height];
	}
	
	public void setCell(Coord c, Cell cell) {
		this.cells[c.x][c.y] = cell;
	}
	
	public Cell getCell(Coord c) {
		return cells[c.x][c.y];
	}
	
	public Cell getCell(int x, int y) {
		return cells[x][y];
	}
	
	public int getWidth() {
		return cells.length;
	}
	
	public int getHeight() {
		return cells[0].length;
	}
	
	public static Grid loadFromFile(String filename) {
		try {
			List<String> lines = Files.readAllLines(new File(filename).toPath());

			// format: https://www.movingai.com/benchmarks/formats.html
			// All maps begin with the lines:
			// type octile
			// height y
			// width x
			// map
			// where y and x are the respective height and width of the map.
			// The map data is store as an ASCII grid. The upper-left corner of the map is (0,0). The following characters are possible:
			//
			// . - passable terrain
			// G - passable terrain
			// @ - out of bounds
			// O - out of bounds
			// T - trees (unpassable)
			// S - swamp (passable from regular terrain)
			// W - water (traversable, but not passable from terrain)
			
			int height = Integer.parseInt(lines.get(1).substring(7));
			int width = Integer.parseInt(lines.get(2).substring(6));
			
			Grid g = new Grid(width, height);
			
			// note, we're reading lines per row
			// so line i column j: x coord is j, y coord is i
			for (int i = 4; i < lines.size(); i++) { // skip header lines
				for (int j = 0; j < lines.get(i).length(); j++) {
					g.setCell(new Coord(j, i - 4), CHARS_TO_CELL.get(Character.valueOf(lines.get(i).charAt(j))));
				}
			}
			
			return g;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
	public enum CellType { PassableTerrain, OutOfBounds, Trees, Swamp, Water }
	
	public static class Coord implements Comparable<Coord> {
		private final int x;
		private final int y;
		
		public Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public double distanceFrom(Coord c) {
			double distanceX = this.x - c.x;
			double distanceY = this.y - c.y;
			return Math.sqrt(Math.pow(distanceX, 2.0) + Math.pow(distanceY, 2.0));
		}
		
		@Override
		public boolean equals(Object that) {
			return this.toString().equals(that.toString());
		}
		
		@Override
		public int hashCode() {
			return toString().hashCode();
		}
		
		@Override
		public int compareTo(Coord that) {
			int cmp = Integer.compare(this.x, that.x);
			if (cmp == 0) {
				cmp = Integer.compare(this.y, that.y);
			}
			return cmp;
		}
		
		@Override
		public String toString() {
			return ("[" + x + "," + y + "]");
		}
	}
	
	public static class Cell {
		private CellType cellType;
		private boolean passable;
		
		public Cell(CellType cellType, boolean passable) {
			this.cellType = cellType;
			this.passable = passable;
		}
		
		public CellType getCellType() {
			return cellType;
		}
		
		public boolean isPassable() {
			return passable;
		}
	}
	
	private static final Map<Character, Cell> CHARS_TO_CELL = new HashMap<>();
	static {
		CHARS_TO_CELL.put('.', new Cell(CellType.PassableTerrain, true));
		CHARS_TO_CELL.put('G', new Cell(CellType.PassableTerrain, true));
		CHARS_TO_CELL.put('@', new Cell(CellType.OutOfBounds, false));
		CHARS_TO_CELL.put('O', new Cell(CellType.OutOfBounds, false));
		CHARS_TO_CELL.put('T', new Cell(CellType.Trees, false));
		CHARS_TO_CELL.put('S', new Cell(CellType.Swamp, true));
		CHARS_TO_CELL.put('W', new Cell(CellType.Water, false));
	}
}
