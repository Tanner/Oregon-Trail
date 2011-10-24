package scene;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import component.Label;
import component.Panel;
import component.Positionable;
import component.sprite.Sprite;

import core.*;

/**
 * Scene displayed upon game startup - displays title of game.
 */

public class MainMenuScene extends Scene {
	public static final SceneID ID = SceneID.MAINMENU;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font h2 = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.H2);
		
		Label subtitleLabel = new Label(container, h2, Color.white, ConstantStore.get("MAIN_MENU", "PRESS_ENTER"));
		
		//mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -5);
		
		Sprite logoSprite = new Sprite(container, 480, new Image("resources/graphics/logo.png", false, Image.FILTER_NEAREST));
				
		mainLayer.add(logoSprite, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER, 0, -logoSprite.getHeight() / 4 - 20);
		mainLayer.add(subtitleLabel, logoSprite.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, 20);
		
		backgroundLayer.add(new Panel(container, new Image("resources/graphics/backgrounds/dark_dirt.png")));
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
