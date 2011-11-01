package scene;

import model.Person;
import model.WorldMap;
import model.worldMap.LocationNode;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Label;
import component.Panel;
import component.Positionable;
import component.sprite.AnimatingSprite;
import component.sprite.Sprite;


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

		Button[] locationButtons = new Button[worldMap.getNumLocations()];

		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		 
		for (int i = 0; i <= worldMap.MAX_RANK; i++){
			for(LocationNode location : worldMap.getMapNodes().get(i)) {
				locationButtons[location.getID()] = new Button(container, 10, 10, new Label(container, fieldFont ,Color.white, "" + location.getID()));
				mainLayer.add(locationButtons[location.getID()], mainLayer.getPosition(Positionable.ReferencePoint.TOPLEFT),Positionable.ReferencePoint.TOPLEFT, (int) location.getPlayerMapX(), (int) location.getPlayerMapY());
				System.out.println("location : " + location.getID() + " x : " + (int) location.getPlayerMapX() + " Y : " + (int) location.getPlayerMapY() );
			}
		}
		
		returnToCamp = new Button(container, 240, 60, new Label(container, fieldFont, Color.white, ConstantStore.get("MAP_SCENE", "RETURN_CAMP")));
		returnToCamp.addListener(new ButtonListener());
				
		mainLayer.add(returnToCamp, mainLayer.getPosition(Positionable.ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT, 20, -20);
		
		Image mapImg = new Image("resources/graphics/backgrounds/playerMap.png", false, Image.FILTER_NEAREST);
		
		backgroundLayer.add(new Panel(container, mapImg));
		
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
		@Override
		public void componentActivated(AbstractComponent source) {
			SoundStore.get().playSound("Click");
			if (source == returnToCamp) {
				SoundStore.get().setMusicVolume(.25f);
				GameDirector.sharedSceneListener().sceneDidEnd(MapScene.this);
			}//if source==return2camp
		}//componentActivated		
	}//button listener private class
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (mainLayer.isVisible() && mainLayer.isAcceptingInput()) {
			
		}//if main layer is visible
	}//mouse moved


}