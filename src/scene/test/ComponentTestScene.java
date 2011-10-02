package scene.test;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.*;

import scene.Scene;
import scene.SceneID;
import scene.layout.GridLayout;

import component.*;

import core.*;

public class ComponentTestScene extends Scene {
	
	public static SceneID ID = SceneID.ComponentTestScene;
	private TextField testField;
	private Button myButton;
	private Label[] playerLabels;
	private Label titleLabel;
	private Spinner playerSpinner, playerSpinner2;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		mainLayer.setLayout(new GridLayout(container, 2, 4));
		
		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		Font h1 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);
		
		playerSpinner = new Spinner(container, 400, 400, fieldFont, Color.white, new Image("resources/up.png"), new Image("resources/down.png"), false, "Player 1", "Player 2", "Player 3", "Player 4");
		mainLayer.add(playerSpinner);
		
		playerSpinner2 = new Spinner(container, 400, 200, fieldFont, Color.white, new Image("resources/up.png"), new Image("resources/down.png"), true, "1", "20", "30", "100");
		mainLayer.add(playerSpinner2);
		
		testField = new TextField(container, fieldFont, new Vector2f(40, 400), 300, 20);
		mainLayer.add(testField);
		
		titleLabel = new Label(container, new Vector2f(50, 20), h1, Color.white,"Button Scene");
		mainLayer.add(titleLabel);
		
		myButton = new Button(container, new Label(container, fieldFont, Color.white, "my button"), new Vector2f(600, 400));
		myButton.addListener(new ButtonListener());
		mainLayer.add(myButton);
		
		playerLabels = new Label[4];
		for (int i = 0; i < playerLabels.length; i++) {
			playerLabels[i] = new Label(container, new Vector2f(60 + 160*i, 100), fieldFont, Color.white, "Player " + (i+1) );
			mainLayer.add(playerLabels[i]);
		}
		
		for ( Label l : playerLabels )
			mainLayer.add(l);
		
		mainLayer.add(playerSpinner);
		mainLayer.add(playerSpinner2);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub

	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			playerLabels[playerSpinner.getState()].setText(testField.getText() + playerSpinner2.getState());
		}
	}
}
