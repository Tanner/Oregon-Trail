package scene;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import component.Label;
import component.Panel;
import component.Positionable;

import core.*;

/**
 * Scene displayed upon game startup - displays title of game.
 */

public class MainMenuScene extends Scene {
	public static final SceneID ID = SceneID.MAINMENU;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font h1 = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.H1);
		Font h2 = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.H2);
		
		Label titleLabel = new Label(container, h1, Color.white, ConstantStore.get("MAIN_MENU", "TITLE"));
		Label subtitleLabel = new Label(container, h2, Color.white, ConstantStore.get("MAIN_MENU", "PRESS_ENTER"));
		
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -5);
		mainLayer.add(subtitleLabel, titleLabel.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, 5);
		
		backgroundLayer.add(new Panel(container, new Image("resources/dark_dirt.png")));
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			GameDirector.sharedSceneListener().requestScene(SceneID.PARTYCREATION, this);
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
