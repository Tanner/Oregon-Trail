/**
 * 
 */
package component;

import org.newdawn.slick.gui.GUIContext;

/**
 * builds the hunting grounds panel that will hold the terrain that the hunting minigame will occur upon
 */
public class HuntingGroundsComponent extends Component {

	/**the width of the hunt grounds panel*/
	private static final int GRNDS_WIDTH = 4096;
	/**the height of the hunt grounds panel*/
	private static final int GRNDS_HEIGHT = 4096;

	
	
	public HuntingGroundsComponent(GUIContext context, int width, int height) {
		super(context, width, height);
		
	}
	public HuntingGroundsComponent(GUIContext context) {
		super(context, GRNDS_WIDTH, GRNDS_HEIGHT);
		

	}

}
