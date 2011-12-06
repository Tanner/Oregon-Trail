/**
 * 
 */
package component;

import java.util.Map;

import model.HuntingMap;
import model.huntingMap.TerrainObject;

import org.newdawn.slick.gui.GUIContext;


/**
 * builds the hunting grounds panel that will hold the terrain that the hunting minigame will occur upon
 */
public class HuntingGroundsComponent extends Component {

	/**the width of the hunt grounds panel*/
	private static int groundsWidth;
	/**the height of the hunt grounds panel*/
	private static int groundsHeight;
	/** */
	
	
	
	public HuntingGroundsComponent(GUIContext context, int width, int height, HuntingMap huntLayout) {
		super(context, width, height);
		this.groundsHeight = height;
		this.groundsWidth = width;
	
 		//Map<Integer, TerrainObject> tmpLayoutArray = huntLayout.getHuntingGroundsMap();

 			
		
		
		
	}
	public HuntingGroundsComponent(GUIContext context) {
		super(context, groundsHeight, groundsHeight);
		

	}

}
