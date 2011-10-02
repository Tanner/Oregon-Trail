package scene;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;

import component.*;

import core.*;

public class ButtonScene extends Scene {
	
	public static SceneID ID = SceneID.ButtonScene;
	private TextField testField;
	private MouseOverArea testButton;
	private Label[] playerLabels;
	private Label titleLabel;
	private Spinner playerSpinner;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		Font h1 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);
		playerSpinner = new Spinner(container, 400, 400, fieldFont, Color.white,
				new Image("resources/up.png"), new Image("resources/down.png"), "Player 1",
				"Player 2", "Player 3", "Player 4");
		testField = new TextField(container, fieldFont, 40, 400, 300, 20);
		testButton = new MouseOverArea(container, new Image("resources/button.png"), 600, 400, new ButtonListener() );
		titleLabel = new Label(container, 50, 20, h1, Color.white,"Button Scene");
		playerLabels = new Label[4];
		for (int i = 0; i < playerLabels.length; i++)
			playerLabels[i] = new Label(container, 60 + 160*i, 100, fieldFont, Color.white, "Player " + (i+1) );
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(new Color(10, 10, 200));
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
		g.setColor(new Color(255,255,255));
		testField.render(container, g);
		testButton.render(container, g);
		titleLabel.render(container, g);
		playerSpinner.render(container, g);
		for ( Label l : playerLabels )
			l.render(container, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub

	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			playerLabels[playerSpinner.getState()].setText(testField.getText());
		}
	}


}
