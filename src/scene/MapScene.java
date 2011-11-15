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
					//if (!devMode) {
						playerMap.add(locationButtons[location.getID()], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) location.getPlayerMapX(), (int) location.getPlayerMapY());
					//}
					locationButtons[location.getID()].addListener(new ButtonListener(location));
				}//if location is visible, paint it.				
			}
		}

		if (devMode){
			Button[] tmpButtonAra = new Button[30];
			for (int i = 0; i < tmpButtonAra.length; i++){
				tmpButtonAra[i] = new Button(container, 10, 10);
				tmpButtonAra[i].setTooltipEnabled(true);
				tmpButtonAra[i].setTooltipMessage(""+ i);
			}
			int i = 0;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 910, (int) 475);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 1010, (int) 490);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 940, (int) 560);
			i++;
			//utah/washington territory border => y = (335-275)/(420-218) = (.3) x + 210; 218<x<420 and 275<y<335
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 218, (int) 275);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 300, (int) 300);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 415, (int) 330);
			i++;
			//nebraska/dakota/washington border y = (390 - 290)/(900 - 430) = (.213) x + 200 : 420<x<900 ; 290 < y < 390
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 430, (int) 290);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 460, (int) 300);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 900, (int) 390);
			i++;
			//kansas/nebraska border y =
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 940, (int) 510);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 670, (int) 470);
			i++;
			//colorado/nebraska border y =
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 680, (int) 430);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 430, (int) 375);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 405, (int) 365);
			i++;
			
			//longitudinal borders
			//colorado/kansas
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 645, (int) 560);
			i++;
			//colorado/utah
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 375, (int) 520);
			i++;
			
			//oregon/washington
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 255, (int) 115);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 175, (int) 255);
			i++;
			//washington/dakota
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 470, (int) 265);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 325, (int) 155);
			i++;
			playerMap.add(tmpButtonAra[i], playerMap.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) 180, (int) 340);
			i++;
		}
		
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