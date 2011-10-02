package scene;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.gui.*;

import core.FontManager;
import core.GameDirector;

public class ButtonScene extends Scene {
	public static SceneID ID = SceneID.ButtonScene;
	TextField testField;
	MouseOverArea testButton;
	String str = "hello";
	UnicodeFont fieldFont;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		testField = new TextField(container, fieldFont, 40, 40, 300, 20);
		testButton = new MouseOverArea(container, new Image("resources/button.png"), 100, 100, new ButtonListener() );
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(new Color(10, 10, 200));
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
		g.setColor(new Color(255,255,255));
		testField.render(container, g);
		testButton.render(container, g);
		fieldFont.drawString(300, 300, str);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub

	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			str = testField.getText();
			for (int i = 0; i < str.length(); i++) {
				str = str.substring(1) + str.charAt(0);
			}
		}
	}

}
