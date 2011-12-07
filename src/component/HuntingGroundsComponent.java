/**
 * 
 */
package component;

//import java.util.Map;

import model.HuntingMap;
import model.huntingMap.TerrainObject;

import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

import component.Positionable.ReferencePoint;

import core.ConstantStore;
import core.ImageStore;


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
		
		Image background;
		
		switch (huntLayout.getBckGround()){
			case GRASS : 	background = ImageStore.get().getImage("HUNT_GRASS");
							break;
			case SNOW : 	background = ImageStore.get().getImage("HUNT_SNOW");
							break;
			case MOUNTAIN : background = ImageStore.get().getImage("HUNT_MOUNTAIN");
							break;
			case DESERT : 	background = ImageStore.get().getImage("HUNT_DESERT");
							break;
			default :		background = ImageStore.get().getImage("HUNT_GRASS");
							break;
		}

		
		
 		Panel huntPanel = new Panel(context, width, height);
 		
 		
 		
 		
 		add(huntPanel,getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
 			
		
		
		
	}
	public HuntingGroundsComponent(GUIContext context) {
		super(context, groundsHeight, groundsHeight);
		

	}

}
