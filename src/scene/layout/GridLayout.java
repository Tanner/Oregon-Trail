package scene.layout;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import component.Component;
import component.Positionable.ReferencePoint;


public class GridLayout extends Layout {
	private static final int PADDING = 20;
	
	private Row[] rows;
	private int colCount;
	private int componentsAdded;
	private int colWidth;
	
	public GridLayout(GameContainer container, int rowCount, int colCount) {
		this.colCount = colCount;
		
		this.colWidth = ((container.getWidth() - PADDING*2) - (PADDING*(colCount-1))) / colCount;
		int colHeight = ((container.getHeight() - PADDING*2) - (PADDING*(rowCount-1))) / rowCount;
		
		this.rows = new Row[rowCount];
		int y = PADDING;
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(y, colHeight, colCount);
			y += colHeight+PADDING;
		}
		
		componentsAdded = 0;
	}
	
	public int getColWidth() {
		return colWidth;
	}
	
	public void setComponentLocation(Component component) {
		Cell cell = getCellForIndex(componentsAdded);
		Vector2f cellCenter = cell.getCenter();
		component.setWidth(cell.width);
		component.setHeight(cell.height);
		component.setPosition(cellCenter, ReferencePoint.CenterCenter);
		
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
			for (int i = 0; i < cells.length ;i++) {
				cells[i] = new Cell(x, y, colWidth, height);
				x += colWidth+PADDING;
			}
		}
	}
	
	private class Cell {
		private	 Vector2f location;
		private int width;
		private int height;
		
		public Cell(int x, int y, int width, int height) {
			this.location = new Vector2f(x, y);
			this.width = width;
			this.height = height;
		}
		
		public Vector2f getCenter() {
			return new Vector2f(location.x + width/2, location.y + height/2); 
		}
	}
}
