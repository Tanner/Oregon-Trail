package scene;

import model.Party;
import model.Person;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import component.Panel;
import component.Label;
import component.Positionable;

import core.*;

/**
 * The scene which represents a town.
 */
public class TownScene extends Scene {
	public static final SceneID ID = SceneID.TOWN;
	
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
		
		Font h1 = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.H1);
		Font h2 = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.H2);
		
		Label titleLabel = new Label(container, h1, Color.white, "Town");
		Label subtitleLabel = new Label(container, h2, Color.white, "Press Enter to Go to Store");
		
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -5);
		mainLayer.add(subtitleLabel, titleLabel.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, 5);
		
		backgroundLayer.add(new Panel(container, new Color(0x003e84)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			GameDirector.sharedSceneListener().requestScene(SceneID.STORE, this);
		}
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}
