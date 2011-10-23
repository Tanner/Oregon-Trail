package scene;

import java.util.List;

import model.Party;
import model.RandomEncounterTable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.HUD;
import component.Positionable.ReferencePoint;
import component.sprite.ParallaxSprite;
import core.GameDirector;
import core.Logger;

public class TrailScene extends Scene {
	public static final SceneID ID = SceneID.TRAIL;
	private static final int STEP_WAIT_TIME = 1000;
	
	private ParallaxSprite ground;
	private ParallaxSprite trees;
	
	private int distance = 0;
	
	private int timeElapsed;
	
	private Party party;
	private RandomEncounterTable randomEncounterTable;
	
	private HUD hud;
	
	public TrailScene(Party party, RandomEncounterTable randomEncounterTable) {
		this.party = party;
		this.randomEncounterTable = randomEncounterTable;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);		
		ground = new ParallaxSprite(container, container.getWidth(), new Image("resources/graphics/ground/trail.png", false, Image.FILTER_NEAREST), 300);
		trees = new ParallaxSprite(container, container.getWidth(), new Image("resources/graphics/test/trees.png"), 900);
		
		backgroundLayer.add(ground, backgroundLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT);
		backgroundLayer.add(trees, ground.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT);

		hud = new HUD(container, party, new HUDListener());
		showHUD(hud);
	}
		
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		ground.move(delta);
		trees.move(delta);
		
		timeElapsed += delta;
		if (timeElapsed % STEP_WAIT_TIME < timeElapsed) {
			timeElapsed = 0;

			List<String> notifications = party.walk();
			
			if (party.getPartyMembers().isEmpty()) {
				GameDirector.sharedSceneListener().requestScene(SceneID.GAMEOVER, this);
			}
			Logger.log("Current distance travelled = " + party.getLocation(), Logger.Level.INFO);
			GameDirector.sharedSceneListener().requestScene(randomEncounterTable.getRandomEncounter(), this);

			hud.updatePartyInformation();
			hud.addNotifications(notifications);
		}
		
		if (timeElapsed % (STEP_WAIT_TIME / 2) < timeElapsed) {
			hud.updateNotifications();
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class HUDListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			GameDirector.sharedSceneListener().requestScene(SceneID.PARTYINVENTORY, TrailScene.this);
		}
	}
}
