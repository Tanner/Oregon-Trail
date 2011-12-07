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
import component.sprite.Sprite;

import core.ConstantStore;
import core.ImageStore;


/**
 * builds the hunting grounds panel that will hold the terrain that the hunting minigame will occur upon
 */
public class HuntingGroundsComponent extends Component {

	/** the background to the panel */
	private Image background;
	
	
	
	
	public HuntingGroundsComponent(GUIContext context, int width, int height, HuntingMap huntLayout) {
		super(context, width, height);
		//pick correct background for this component
		switch (huntLayout.getBckGround()){
		case GRASS : 	this.background = ImageStore.get().getImage("HUNT_GRASS");
						break;
		case SNOW : 	this.background = ImageStore.get().getImage("HUNT_SNOW");
						break;
		case MOUNTAIN : this.background = ImageStore.get().getImage("HUNT_MOUNTAIN");
						break;
		case DESERT : 	this.background = ImageStore.get().getImage("HUNT_DESERT");
						break;
		default :		this.background = ImageStore.get().getImage("HUNT_GRASS");
						break;
	}

	//build the terrain layout
		//TerrainObject[][] tmpLayoutArray = huntLayout.getHuntingGroundsMap();
		
		//make a sprite to hold the background image
		Sprite backgroundPanel = new Sprite(container, this.background) ;
			
		Panel huntPanel = new Panel(container, width, height);
		
		//add the 4 pictures required to make the background
		huntPanel.add(backgroundPanel,huntPanel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		huntPanel.add(backgroundPanel,huntPanel.getPosition(ReferencePoint.TOPCENTER), ReferencePoint.TOPLEFT);
		huntPanel.add(backgroundPanel,huntPanel.getPosition(ReferencePoint.CENTERLEFT), ReferencePoint.TOPLEFT);
		huntPanel.add(backgroundPanel,huntPanel.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.TOPLEFT);
		
 		
 		add(huntPanel,getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
	}


}//hunting grounds component
