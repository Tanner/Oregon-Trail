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
	
	private Panel huntPanel;
	
	private Panel collisionPanel;
	
	
	
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
			
		huntPanel = new Panel(container, width, height);
		collisionPanel = new Panel(container, width, height);
		
		//add the 4 pictures required to make the background
		huntPanel.add(backgroundPanel,huntPanel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		huntPanel.add(backgroundPanel,huntPanel.getPosition(ReferencePoint.TOPCENTER), ReferencePoint.TOPLEFT);
		huntPanel.add(backgroundPanel,huntPanel.getPosition(ReferencePoint.CENTERLEFT), ReferencePoint.TOPLEFT);
		huntPanel.add(backgroundPanel,huntPanel.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.TOPLEFT);
		
		
 		add(huntPanel,getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
	}//constructor

	public void displayTerrain(GUIContext context, HuntingMap huntLayout){
		for (int huntMapX = 0; huntMapX < huntLayout.getHuntingGroundsMap().length; huntMapX ++){
			for (int huntMapY = 0; huntMapY < huntLayout.getHuntingGroundsMap()[0].length; huntMapY++){
				
				System.out.printf("x%d y%d %s | ", huntMapX, huntMapY, huntLayout.getHuntingGroundsMap()[huntMapY][huntMapX].getImageStoreName() );
				Sprite tempImage = new Sprite(context,ImageStore.get().IMAGES.get(huntLayout.getHuntingGroundsMap()[huntMapY][huntMapX].getImageStoreName()));
				Sprite tempShadImage = new Sprite(context,ImageStore.get().IMAGES.get(huntLayout.getHuntingGroundsMap()[huntMapY][huntMapX].getImageStoreShadName()));
				huntPanel.add(tempImage, huntPanel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, (int) (huntMapX * huntLayout.getTILE_WIDTH()), (int)(huntMapY * huntLayout.getTILE_HEIGHT()));
				collisionPanel.add(tempShadImage, collisionPanel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, (int) (huntMapX * huntLayout.getTILE_WIDTH()), (int)(huntMapY * huntLayout.getTILE_HEIGHT()));
				
			}//for huntmapy
			System.out.printf("\n");
		}//for huntmapx
		

	}
	
	
}//hunting grounds component
