package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.*;

import component.Background;
import component.Label;
import component.Positionable;

import core.*;


public class StoreScene extends Scene {
	public static final SceneID ID = SceneID.StoreScene;
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {		
		super.init(container, game);
		
		UnicodeFont h1 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);
		UnicodeFont h2 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);
		
		Label titleLabel = new Label(container, h1, Color.white, "Store");
		Label subtitleLabel = new Label(container, h2, Color.white, "Press Escape to Leave");
		
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.BottomCenter);
		mainLayer.add(subtitleLabel, titleLabel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.TopCenter, 0, 20);
		
		backgroundLayer.add(new Background(container, new Color(0xa55e00)));
	}

	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}
	
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			GameDirector.sharedSceneDelegate().sceneDidEnd(this);
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
