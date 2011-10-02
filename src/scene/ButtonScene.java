package scene;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.gui.*;

import component.Label;


import core.FontManager;
import core.GameDirector;

public class ButtonScene extends Scene {
	public static SceneID ID = SceneID.ButtonScene;
	TextField testField;
	MouseOverArea testButton;
	Label[] playerLabels;
	Label titleLabel;
	MouseOverArea[] playerSelection;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		Font h1 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);

		testField = new TextField(container, fieldFont, 40, 400, 300, 20);
		testButton = new MouseOverArea(container, new Image("resources/button.png"), 600, 400, new ButtonListener() );
		titleLabel = new Label( 50, 20, h1, "Button Scene");
		playerLabels = new Label[4];
		for (int i = 0; i < playerLabels.length; i++)
			playerLabels[i] = new Label(60 + 160*i, 100, fieldFont, "Player " + (i+1) );
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(new Color(10, 10, 200));
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
		g.setColor(new Color(255,255,255));
		testField.render(container, g);
		testButton.render(container, g);
		titleLabel.draw();
		for ( Label l : playerLabels )
			l.draw();
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub

	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			String str = testField.getText();
			for (int i = 0; i < str.length(); i++) {
				str = str.substring(1) + str.charAt(0);
			}
			playerLabels[0].setText(str);
		}
	}

}
