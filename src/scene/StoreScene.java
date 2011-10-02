package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.*;

import core.*;


public class StoreScene extends Scene {
	public static final SceneID ID = SceneID.StoreScene;
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {		
		super.init(container, game);
	}

//	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
//		g.setColor(new Color(0xa55e00));
//		g.fillRect(0, 0, container.getWidth(), container.getHeight());
//		
//		UnicodeFont h1Font = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);
//		UnicodeFont h2Font = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);
//		
//		String title = "STORE Scene";
//		h1Font.drawString((container.getWidth() - h1Font.getWidth(title)) / 2,
//				container.getHeight()/2 - h1Font.getAscent(),
//				title,
//				Color.white);
//		
//		String instruction = "Press Escape to Leave";
//		h2Font.drawString((container.getWidth() - h2Font.getWidth(instruction)) / 2,
//				container.getHeight()/2 + h1Font.getAscent(),
//				instruction,
//				Color.white);
//	}

	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}
	
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			GameDirector.sharedSceneDelegate().sceneDidEnd(this);
		}
	}
}
