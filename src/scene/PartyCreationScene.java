package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Background;
import component.Button;
import component.Label;
import component.Modal;
import component.Positionable;
import component.TextField;
import core.FontManager;
import core.GameDirector;

public class PartyCreationScene extends Scene {
	public static final SceneID ID = SceneID.PartyCreation;
	private static final int PADDING = 20;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		int buttonCount = 4;
		int buttonWidth = (container.getWidth() - PADDING * (buttonCount + 1)) / buttonCount;
		int buttonheight = 150;

		// Background
		backgroundLayer.add(new Background(container, new Color(0xa00008)));
		
		// New Player Buttons
		Button firstPersonButton = new Button(container,
				new Label(container, fieldFont, Color.white, "New Player"),
				buttonWidth,
				150);
		mainLayer.add(firstPersonButton, mainLayer.getPosition(Positionable.ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft, PADDING, PADDING);
		
		Button secondPersonButton = new Button(container,
				new Label(container, fieldFont, Color.white, "New Player"),
				buttonWidth,
				150);
		mainLayer.add(secondPersonButton, firstPersonButton.getPosition(Positionable.ReferencePoint.TopRight), Positionable.ReferencePoint.TopLeft, PADDING, 0);
		
		Button thirdPersonButton = new Button(container,
				new Label(container, fieldFont, Color.white, "New Player"),
				buttonWidth,
				150);
		mainLayer.add(thirdPersonButton, secondPersonButton.getPosition(Positionable.ReferencePoint.TopRight), Positionable.ReferencePoint.TopLeft, PADDING, 0);
		
		Button fourthPersonButton = new Button(container,
				new Label(container, fieldFont, Color.white, "New Player"),
				buttonWidth,
				150);
		mainLayer.add(fourthPersonButton, thirdPersonButton.getPosition(Positionable.ReferencePoint.TopRight), Positionable.ReferencePoint.TopLeft, PADDING, 0);
		
		firstPersonButton.addListener(new NewPlayerButtonListener());
		
		// Name Text Fields
		TextField firstPersonNameTextField = new TextField(container, fieldFont, buttonWidth, 40);
		mainLayer.add(firstPersonNameTextField, firstPersonButton.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		
		TextField secondPersonNameTextField = new TextField(container, fieldFont, buttonWidth, 40);
		mainLayer.add(secondPersonNameTextField, secondPersonButton.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		
		TextField thirdPersonNameTextField = new TextField(container, fieldFont, buttonWidth, 40);
		mainLayer.add(thirdPersonNameTextField, thirdPersonButton.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		
		TextField fourthPersonNameTextField = new TextField(container, fieldFont, buttonWidth, 40);
		mainLayer.add(fourthPersonNameTextField, fourthPersonButton.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class NewPlayerButtonListener implements ComponentListener {

		@Override
		public void componentActivated(AbstractComponent source) {
			showModal(new Modal(container, PartyCreationScene.this, "What's your profession?", "Confirm", "Cancel"));
		}
	}
}
