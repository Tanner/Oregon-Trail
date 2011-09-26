package scene;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import core.*;


public class MainMenuScene extends Scene {

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		UnicodeFont h1Font = GameDirector.sharedDirector().getFontManager().getFont(FontManager.FontID.H1);
		UnicodeFont h2Font = GameDirector.sharedDirector().getFontManager().getFont(FontManager.FontID.H2);

		String title = "Oregon Trail";
		h1Font.drawString((container.getWidth() - h1Font.getWidth(title)) / 2,
				container.getHeight()/2 - h1Font.getAscent(),
				title,
				Color.white);
		
		String instruction = "Press Enter to Start";
		h2Font.drawString((container.getWidth() - h2Font.getWidth(instruction)) / 2,
				container.getHeight()/2 + h1Font.getAscent(),
				instruction,
				Color.white);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			GameDirector.sharedDirector().requestScene(SceneID.TownScene);
		}
	}
}
