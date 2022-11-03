package problem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JFrame;
import javax.swing.JPanel;

import problem.Grid.Coord;

public class GridsCanvas extends JFrame {
	private static final long serialVersionUID = 1936850876410049156L;
	private Grid grid;
	private Path path;
	
	public class GridPanel extends JPanel {
		private static final long serialVersionUID = -3795610879745042741L;

		int width, height;
	
		int rows;
	
		int cols;
	
		GridPanel(int w, int h) {
			setSize(width = w, height = h);
			rows = grid.getHeight();
			cols = grid.getWidth();
		}
	
		public void paint(Graphics gfx) {
			width = getSize().width;
			height = getSize().height;

			int rowHeight = height / (rows);
			int colWidth = width / (cols);
			
			// draw cells first
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					switch (grid.getCell(new Coord(j, i)).getCellType()) {
					case PassableTerrain:
						gfx.setColor(Color.LIGHT_GRAY);
						break;
					case Swamp:
						gfx.setColor(Color.CYAN);
						break;
					case Water:
						gfx.setColor(Color.BLUE);
						break;
					case Trees:
						gfx.setColor(Color.GREEN);
						break;
					case OutOfBounds:
						gfx.setColor(Color.BLACK);
						break;
					}
					
					gfx.fillRect(j * colWidth, i * rowHeight, colWidth, rowHeight);
				}
			}
			
			// then path if it exists
			if (path != null) {
				gfx.setColor(Color.BLACK);

				Stroke s = ((Graphics2D)gfx).getStroke();
				((Graphics2D)gfx).setStroke(new BasicStroke(Math.max(1, (rowHeight + colWidth) / 10)));
				
				for (int i = 0; i < path.getPath().size() - 1; i++) {
					Coord c1 = path.getPath().get(i);
					Coord c2 = path.getPath().get(i + 1);
					gfx.drawLine(c1.getX() * colWidth + (colWidth/2), 
							c1.getY() * rowHeight + (rowHeight/2), 
							c2.getX() * colWidth + (colWidth/2), 
							c2.getY() * rowHeight + (rowHeight/2));
				}
				
				((Graphics2D)gfx).setStroke(s);
			}
			
			gfx.setColor(Color.BLACK);
			gfx.setFont(gfx.getFont().deriveFont(8.0f));
			
			// draw the rows
			for (int i = 0; i < rows; i++) {
				gfx.drawLine(0, i * rowHeight, width, i * rowHeight);
				gfx.drawString(Integer.toString(i), cols * colWidth, (i+1) * rowHeight);
			}
			gfx.drawLine(0, rows * rowHeight - 1, width, rows * rowHeight - 1);
				
			// draw the columns
			for (int i = 0; i < cols; i++) {
				gfx.drawLine(i * colWidth, 0, i * colWidth, height);
				gfx.drawString(Integer.toString(i), i * colWidth, (rows + 1) * rowHeight);
			}
			gfx.drawLine(cols * colWidth - 1, 0, cols * colWidth - 1, height);
			
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(width, height);
		}
	}


	public GridsCanvas(Grid g, Path p) {
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.grid = g;
		this.path = p;
		
		GridPanel xyz = new GridPanel(400, 400);
		add(xyz);
		pack();
		
		setVisible(true);
	}
	
	public void setPath(Path path) {
		this.path = path;
		repaint();
	}
	
	public void update() {
		repaint();
	}
}
