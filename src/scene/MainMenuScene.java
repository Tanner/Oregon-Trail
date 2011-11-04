package scene;


import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
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
	
	private Button newGameButton;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		SoundStore.get().setVolume(.5f);
		SoundStore.get().loopMusic("GBU");
		
		//Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		newGameButton = new Button(container, 240, 60, new Label(container, fieldFont, Color.white, ConstantStore.get("MAIN_MENU", "NEW_GAME")));
		newGameButton.addListener(new ButtonListener());
				
		Sprite logoSprite = new Sprite(container, 480, ImageStore.get().getImage("LOGO"));
				
		mainLayer.add(logoSprite, mainLayer.getPosition(Positionable.ReferencePoint.TOPCENTER), Positionable.ReferencePoint.TOPCENTER, 0, 75);
		mainLayer.add(newGameButton, mainLayer.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -75);
		
		backgroundLayer.add(new Panel(container, ImageStore.get().getImage("MAP_BACKGROUND")));
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {
			if (source == newGameButton) {
				SoundStore.get().setMusicVolume(.25f);
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYCREATION, MainMenuScene.this, true);
			}
		}
		
	}
}
