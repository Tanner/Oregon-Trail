package scene;

import model.WorldMap;
import model.worldMap.LocationNode;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.MapComponent;
import component.Button;
import component.Label;
import component.Panel;
import component.Positionable;

import core.*;


/**
 * Shows a map to the user.
 */
public class MapScene extends Scene {
	public static final SceneID ID = SceneID.MAP;

	private Button returnToCamp;

	private WorldMap worldMap;

	public MapScene(WorldMap worldMap) {
		this.worldMap = worldMap;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		boolean devMode = false;
		boolean devTrail = false;

		MapComponent playerMap = new MapComponent(container, worldMap, devTrail);

		Button[] locationButtons = new Button[worldMap.getNumLocations()];

		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		//add to mapComponent
		for (int i = 0; i <= worldMap.MAX_RANK; i++){
			for(LocationNode location : worldMap.getMapNodes().get(i)) {
				if ((location.isVisible()) || devMode){
					locationButtons[location.getID()] = new Button(container, 10, 10);					
					locationButtons[location.getID()].setTooltipEnabled(true);
					locationButtons[location.getID()].setTooltipMessage(location.getName());
					if (!devMode) {
						playerMap.add(locationButtons[location.getID()], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) location.getPlayerMapX(), (int) location.getPlayerMapY());
					}
					locationButtons[location.getID()].addListener(new ButtonListener(location));
				}//if location is visible, paint it.				
			}
		}

		//the following paints nodes at the corners of the various territories, to help determine equatiosn that would more accurately reflect the 
		//approriate terrority for a particular town, based on its generated location on the map, than the current (11/18/11) mechanism coded in worldmap.java
		
		int[][] coords = {
				//missouri border
				{ 910, 475},
				{ 940, 510},
				{ 1010, 490},
				{ 940, 560},
				
				//utah/washington territory border => y = (335-275)/(420-218) = (.3) x + 210; 218<x<420 and 275<y<335				
				{ 218, 275},
				{ 300, 300},
				{ 415, 330},
				
				//nebraska/dakota/washington border y = (390 - 290)/(900 - 430) = (.213) x + 200 : 420<x<900 ; 290 < y < 390			
				{ 430, 290},
				{ 460, 300},
				{ 900, 390},
				
				//kansas/nebraska border y =
				{670, 470},
				
				//colorado/nebraska border y =
				{680, 430},
				{430, 375},
				{405, 365},
				
				//longitudinal borders
				//colorado/kansas
				{645, 560},
				
				//colorado/utah
				{375, 520},
				
				//oregon/washington
				{255, 115},
				{175, 255},
				
				//washington/dakota
				{470, 265},
				{325, 155},
				{180, 340}
				
		};

		if (devMode){
			Button[] tmpButtonAra = new Button[coords.length];
			for (int i = 0; i < tmpButtonAra.length; i++){
				tmpButtonAra[i] = new Button(container, 10, 10);
				tmpButtonAra[i].setTooltipEnabled(true);
				playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, coords[i][0], coords[i][1]);
				tmpButtonAra[i].setTooltipMessage(""+ i + ":|x = " + coords[i][0] + "|y = " + coords[i][1]);
			}
		}
		//end territory corner painting
		
		//add map component
		
		mainLayer.add(playerMap, mainLayer.getPosition(Positionable.ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT,0,0);
		
		//add return to camp button
		returnToCamp = new Button(container, 240, 60, new Label(container, fieldFont, Color.white, ConstantStore.get("MAP_SCENE", "RETURN_CAMP")));
		returnToCamp.addListener(new ButtonListener());
				
		mainLayer.add(returnToCamp, mainLayer.getPosition(Positionable.ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT, 20, -20);
		
		//set the background image - the map picture;		
		backgroundLayer.add(new Panel(container, ImageStore.get().getImage("TRAIL_MAP")));
		
	}
	/* (non-Javadoc)
	 * @see scene.Scene#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}

	/* (non-Javadoc)
	 * @see scene.Scene#getID()
	 */
	@Override
	public int getID() {
		return ID.ordinal();
	}
	

	private class ButtonListener implements ComponentListener {
		private LocationNode node;
		public ButtonListener(LocationNode node){
			this.node = node;
		}
		public ButtonListener(){
			this(null);
		}
		@Override
		public void componentActivated(AbstractComponent source) {
			if (source == returnToCamp) {
				SoundStore.get().setMusicVolume(.25f);
				GameDirector.sharedSceneListener().sceneDidEnd(MapScene.this);
			}//if source==return2camp
			else {
				System.out.println("location : " + this.node.debugToString());
			}
		}//componentActivated		
	}//button listener private class
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (mainLayer.isVisible() && mainLayer.isAcceptingInput()) {
			
		}//if main layer is visible
	}//mouse moved


}