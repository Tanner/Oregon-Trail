package component;

import org.newdawn.slick.gui.GUIContext;

public class GridComponent extends Component {

	public GridComponent(GUIContext context, Component[][] components, int xSpacing, int ySpacing) {
		super(context, getWidthOfComponents(components, xSpacing), getHeightOfComponents(components, ySpacing));
		
		this.addAsGrid(components, this.getPosition(ReferencePoint.TOPLEFT), xSpacing, ySpacing);
	}

	private static final int getWidthOfComponents(Component[][] components, int padding) {
		int width = 0;
		
		for (int i = 0; i < components[0].length; i++) {
			width += components[0][i].getWidth();
			if (i > 0) {
				width += padding;
			}
		}
				
		return width;
	}
	
	private static final int getHeightOfComponents(Component[][] components, int padding) {
		int height = 0;
		
		for (int i = 0; i < components.length; i++) {
			height += components[i][0].getHeight();
			if (i > 0) {
				height += padding;
			}
		}
		
		return height;
	}
}
