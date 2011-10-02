package scene.layout;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;

import scene.Scene;

public class GridLayout {
	private static final int PADDING = 20;
	
	private Row[] rows;
	private int colCount;
	private int componentsAdded;
	private int colWidth;
	
	public GridLayout(GameContainer container, int rowCount, int colCount) {
		this.colCount = colCount;

		this.colWidth = container.getWidth() - (PADDING*colCount) / colCount;
		int colHeight = container.getHeight() - (PADDING*rowCount) / rowCount;
				
		this.rows = new Row[rowCount];
		int y = PADDING;
		for (Row r : this.rows) {
			r = new Row(y, colHeight, colCount);
			y += colHeight+PADDING;
		}
		
		componentsAdded = 0;
	}
	
	public int getColWidth() {
		return colWidth;
	}
	
	public void setComponentLocation(AbstractComponent component) {
		Cell cell = getCellForIndex(componentsAdded);
		component.setLocation((int)cell.location.x, (int)cell.location.y);
		
		componentsAdded++;
	}
	
	public Cell getCellForIndex(int index) {
		return rows[index/colCount].cells[index%colCount];
	}
	
	private class Row {
		private int height;
		private Cell[] cells;
		
		public Row(int y, int height, int colCount) {
			this.height = height;
			this.cells = new Cell[colCount];
			int x = PADDING;
			for (Cell c : this.cells) {
				c = new Cell(x, y, colWidth, height);
				x += colWidth+PADDING;
			}
		}
	}
	
	private class Cell {
		private	 Vector2f location;
		private Vector2f size;
		
		public Cell(int x, int y, int width, int height) {
			this.location = new Vector2f(x, y);
			this.size = new Vector2f(width, height);
		}
		
		public Vector2f getLocation() {
			return location;
		}
	}
}
