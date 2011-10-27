package scene;

import model.Party;
import model.Person;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Panel;
import component.Label;
import component.Positionable;
import component.Positionable.ReferencePoint;

import core.*;

/**
 * The scene which represents a town.
 */
public class TownScene extends Scene {
	public static final SceneID ID = SceneID.TOWN;
	
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 60;
	
	private Button trailButton;
	
	/**
	 * builds town scene
	 * @param party where/who the action is
	 */
	public TownScene(Party party) {
		for (Person p : party.getPartyMembers()) {
			Logger.log(p.getName() + ", the " + p.getProfession() + ", entered the town.", Logger.Level.INFO);
		}
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
						
		Font h1 = FontStore.get(FontStore.FontID.H1);
		Font h2 = FontStore.get(FontStore.FontID.H2);
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		
		Label titleLabel = new Label(container, h1, Color.white, "Town");
		Label subtitleLabel = new Label(container, h2, Color.white, "Press Enter to Go to Store");
		
		trailButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, "Go To Trail"));
		trailButton.addListener(new ButtonListener());
		
		mainLayer.add(trailButton, mainLayer.getPosition(ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, -100);
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -5);
		mainLayer.add(subtitleLabel, titleLabel.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, 5);
		
		backgroundLayer.add(new Panel(container, new Color(0x003e84)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)  {
		super.enter(container, game);
		//SoundStore.get().playMusic("Smooth");
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			GameDirector.sharedSceneListener().requestScene(SceneID.STORE, this, false);
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	/**
	 * Temporary listener to allow movement to the trail
	 */
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			System.out.println("asda");
			GameDirector.sharedSceneListener().requestScene(SceneID.TRAIL, TownScene.this, true);
		}
	}
}
