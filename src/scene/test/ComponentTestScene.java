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

/**
 * Scene where all components are tested.
 * 
 * @author Tanner Smith
 */
public class ComponentTestScene extends Scene {
	public static final SceneID ID = SceneID.ComponentTest;
	
	private TextField textField;
	private Button button;
	private Label label;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		mainLayer.setLayout(new GridLayout(container, 2, 4));
		
		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		Font h1 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);

		Label textFieldLabel = new Label(container, new Vector2f(50, 20), fieldFont, Color.white,"Text Field");
		mainLayer.add(textFieldLabel);
		
		textField = new TextField(container, fieldFont, new Vector2f(40, 400), 300, 20);
		mainLayer.add(textField);
		
		Label buttonLabel = new Label(container, new Vector2f(50, 20), fieldFont, Color.white, "Button");
		mainLayer.add(buttonLabel);
		
		button = new Button(container, new Label(container, fieldFont, Color.white, "my button"), new Vector2f(600, 400));
		button.addListener(new ButtonListener());
		mainLayer.add(button);
		
		label = new Label(container, fieldFont, Color.white, "Label");
		mainLayer.add(label);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub

	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			label.setText("B"+(int)(Math.random() * 100));
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
