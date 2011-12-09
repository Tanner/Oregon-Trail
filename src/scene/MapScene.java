package scene;

import model.WorldMap;
import model.worldMap.LocationNode;
import model.worldMap.TrailEdge;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

//import component.Positionable.ReferencePoint;

import component.sprite.AnimatingSprite;
import component.MapComponent;
import component.Button;
import component.Label;
import component.Panel;
import component.Positionable;
//import component.sprite.Sprite;

import core.*;


/**
 * Shows a map to the user.
 */
public class MapScene extends Scene {
	public static final SceneID ID = SceneID.MAP;
	
	private final int PADDING = 20;
	private final int REGULAR_BUTTON_HEIGHT = 30;
	
	private Button returnToCamp;

	private WorldMap worldMap;
	private LocationNode currNode;
	private TrailEdge currTrail;
	private AnimatingSprite currLocPtr;
	private AnimatingSprite currLocParty;
	private Button[] locationButtons;
	
	public MapScene(WorldMap worldMap) {
		this.worldMap = worldMap;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		boolean devMode = false;
		boolean devTrail = false;

		MapComponent playerMap = new MapComponent(container, worldMap, devTrail);
		
		this.locationButtons = new Button[worldMap.getNumLocations()];

		
		//add to mapComponent
		for (int i = 0; i <= worldMap.MAX_RANK; i++){
			for(LocationNode location : worldMap.getMapNodes().get(i)) {
				if ((location.isVisible()) || devMode){
					locationButtons[location.getID()] = new Button(container, 8 + (int)(location.getConditionPercentage() * 5) , 8 + (int)(location.getConditionPercentage() * 5), getLocColor(location));					
					locationButtons[location.getID()].setTooltipEnabled(true);
					locationButtons[location.getID()].setTooltipMessage(location.getName());
					//if (!devMode) {
					playerMap.add(locationButtons[location.getID()], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) location.getPlayerMapX(), (int) location.getPlayerMapY());
					//}
					locationButtons[location.getID()].addListener(new ButtonListener(location));
				}//if location is visible, paint it.				
			}
		}//for each rank of world map
		
		
		
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);

		//city definition moved to mapComponent
		
		this.currNode = worldMap.getCurrLocationNode();
		this.currTrail = worldMap.getCurrTrail();
		
		//Animating the current pointer

		currLocPtr = new AnimatingSprite(container, 48, addPointer(), AnimatingSprite.Direction.LEFT);

		currLocParty = new AnimatingSprite(container, 24, addParty(), AnimatingSprite.Direction.LEFT);
		
		//mainLayer.add(currLocPtr, mainLayer.getPosition(ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER, -50, 120);

		//Sprite ptrSprite = new Sprite(container, 48, ImageStore.get().getImage("MAP_POINTER1"));	
		
		//Button curLocMarker = new Button(container,10 , 10, Color.white);
		int curLocX;
		int curLocY;
		if (currTrail.getConditionPercentage() < 1.0){//if current condition of trail is less than full, use current location on trail to calculate where to put pointer
			curLocX = (int)currTrail.getCurrTrailLocationX();
			curLocY = (int)currTrail.getCurrTrailLocationY();
			//System.out.println("x : " + curLocX + " y : " + curLocY);
			
		} else {//we're in a city, modify the location of the pointer to reflect this
			curLocX = (int)currNode.getPlayerMapX();
			curLocY = (int)currNode.getPlayerMapY();
		}
		
		playerMap.add(currLocPtr, playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, curLocX-18, curLocY-48 );
		playerMap.add(currLocParty, playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, curLocX-6, curLocY-75 );
		//playerMap.add(curLocMarker, playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, curLocX, curLocY );
		
		//the following paints nodes at the corners of the various territories, to help determine equatiosn that would more accurately reflect the 
		//approriate terrority for a particular town, based on its generated location on the map, than the current (11/18/11) mechanism coded in worldmap.java
/*		
		int[][] coords = {
				//missouri border
				{ 910, 475}, { 940, 510}, { 1010, 490}, { 940, 560},
				
				//utah/washington territory border => y = (335-275)/(420-218) = (.3) x + 210; 218<x<420 and 275<y<335				
				{ 218, 275}, { 300, 300}, { 415, 330},
				
				//nebraska/dakota/washington border y = (390 - 290)/(900 - 430) = (.213) x + 200 : 420<x<900 ; 290 < y < 390			
				{ 430, 290}, { 460, 300}, { 900, 390},
				
				//kansas/nebraska border y =
				{670, 470},
				
				//colorado/nebraska border y =
				{680, 430}, {430, 375}, {405, 365},
				
				//longitudinal borders
				//colorado/kansas
				{645, 560},
				
				//colorado/utah
				{375, 520},
				
				//oregon/washington : northern border : y = (115 - 80) /  (255 - 110)  : .2414 x + 54
				{255, 115}, {110, 80},
									//eastern border : y = (115 - 255)/ (255 - 175) :  -1.75 x + 561
				{175, 255},
				
				//washington/dakota
				{470, 265}, {325, 155}, {180, 340}
				
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
		*/
		//add map component
		
		mainLayer.add(playerMap, mainLayer.getPosition(Positionable.ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT,0,0);
		
		//add return to camp button
		returnToCamp = new Button(container, (container.getWidth() - PADDING * 4) / 4, REGULAR_BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("MAP_SCENE", "RETURN_CAMP")));
		returnToCamp.addListener(new ButtonListener());
				
		mainLayer.add(returnToCamp, mainLayer.getPosition(Positionable.ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT, 20, -20);
		
		//set the background image - the map picture;		
		backgroundLayer.add(new Panel(container, ImageStore.get().getImage("TRAIL_MAP")));
		
	}
	
	/**
	 * will return a color based on the quality of this location
	 * 
	 * @param node the location in question
	 * @return the color based on this location's quality
	 */
	private Color getLocColor(LocationNode node){
		if (node.getHasInTrail() == false){
			return Color.white;
		} else {
			if (node.getConditionPercentage() < .10){
				return new Color(0,0,0);
			} else if (node.getConditionPercentage() < .30){
				return new Color(255,255,0);
			} else if (node.getConditionPercentage() < .50){
				return new Color(255,200,0);
			} else if (node.getConditionPercentage() < .70){
				return new Color(255,150,0);
			} else if (node.getConditionPercentage() < .90){
				return new Color(255,100,0);
			} else {
				return new Color(255,0,0);
			}
		}
	}
	
	public Button[] getLocationButtons(){
		return this.locationButtons;
	}
			
	/**
	 * builds the animation object for the party representation over the pointer
	 * @return the animation array for the party
	 */

	public Animation addParty()		{
		Animation curParty = new Animation();
		curParty.addFrame(ImageStore.get().getImage("MAP_PARTY1"), 200);
		curParty.addFrame(ImageStore.get().getImage("MAP_PARTY2"), 200);
		curParty.addFrame(ImageStore.get().getImage("MAP_PARTY3"), 200);
		curParty.addFrame(ImageStore.get().getImage("MAP_PARTY4"), 200);
		return curParty;
	}
	
	/**
	 * builds the animation array for the pointer to show the party location
	 * @return the arrow pointing to current party location
	 */
	
	public Animation addPointer(){
		Animation curPtr = new Animation();
		curPtr.addFrame(ImageStore.get().getImage("MAP_POINTER6"), 100);
		curPtr.addFrame(ImageStore.get().getImage("MAP_POINTER5"), 100);
		curPtr.addFrame(ImageStore.get().getImage("MAP_POINTER4"), 100);
		curPtr.addFrame(ImageStore.get().getImage("MAP_POINTER3"), 100);
		curPtr.addFrame(ImageStore.get().getImage("MAP_POINTER2"), 100);
		curPtr.addFrame(ImageStore.get().getImage("MAP_POINTER1"), 100);
		return curPtr;
	}
	
		
	/* (non-Javadoc)
	 * @see scene.Scene#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		this.currLocPtr.update(delta);
		this.currLocParty.update(delta);
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
				// print out location-specific information to console
				System.out.println("location : " + this.node.debugToString());
			}//componentActivated
		}//if source check
	}//button listener private class
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (mainLayer.isVisible() && mainLayer.isAcceptingInput()) {
			
		}//if main layer is visible
	}//mouse moved


}