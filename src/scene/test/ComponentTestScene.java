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
	private static final int PADDING = 20;
	
	private TextField textField;
	private Button button;
	private Label label;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
				
		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);

		Label textFieldLabel = new Label(container, fieldFont, Color.white, "Text Field");
		mainLayer.add(textFieldLabel, mainLayer.getPosition(Positionable.ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft, PADDING, PADDING);
		
		textField = new TextField(container, fieldFont, 300, 40);
		textField.addListener(new ButtonListener());
		mainLayer.add(textField, textFieldLabel.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft);
		
		Label buttonLabel = new Label(container, fieldFont, Color.white, "Button");
		mainLayer.add(buttonLabel, textField.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		
		button = new Button(container, new Label(container, fieldFont, Color.white, "my button"), 300, 40);
		button.addListener(new ButtonListener());
		mainLayer.add(button, buttonLabel.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft);
		
		label = new Label(container, fieldFont, Color.white, "Label");
		mainLayer.add(label, button.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub
	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			label.setText("TextField has \""+textField.getText()+"\"");
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
