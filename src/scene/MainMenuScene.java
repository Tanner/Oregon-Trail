package scene;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import component.Label;
import component.Panel;
import component.Positionable;

import core.*;


public class MainMenuScene extends Scene {
	public static final SceneID ID = SceneID.MainMenu;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		UnicodeFont h1 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);
		UnicodeFont h2 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);
		
		Label titleLabel = new Label(container, h1, Color.white, ConstantStore.get("MAIN_MENU", "TITLE"));
		Label subtitleLabel = new Label(container, h2, Color.white, ConstantStore.get("MAIN_MENU", "PRESS_ENTER"));
		
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.BottomCenter, 0, -5);
		mainLayer.add(subtitleLabel, titleLabel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.TopCenter, 0, 5);
		
		backgroundLayer.add(new Panel(container, new Image("resources/dark_dirt.png")));
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			GameDirector.sharedSceneDelegate().requestScene(SceneID.PartyCreation, this);
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
