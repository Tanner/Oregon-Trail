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
import component.Component;
import component.Label;
import component.Label.Alignment;
import component.Modal;
import component.Positionable;
import component.Positionable.ReferencePoint;
import component.TextField;
import core.FontManager;
import core.GameDirector;
import core.Logger;

public class PartyCreationScene extends Scene {
	public static final SceneID ID = SceneID.PartyCreation;
	private static final int PADDING = 20;
	
	private Button newPersonButtons[] = new Button[4];
	private TextField personNameTextFields[] = new TextField[4];
	private Button personProfessionButtons[] = new Button[4];
	private Label personMoneyLabels[] = new Label[4];
	private Button personSkillOneButtons[] = new Button[4];
	private Button personSkillTwoButtons[] = new Button[4];
	private Button personSkillThreeButtons[] = new Button[4];
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		int buttonCount = 4;
		int buttonWidth = (container.getWidth() - PADDING * (buttonCount + 1)) / buttonCount;
		int buttonheight = 150;

		for (int i = 0; i < newPersonButtons.length; i++) {
			Positionable newPersonButton = (i == 0) ? mainLayer : newPersonButtons[i - 1];
			ReferencePoint newPersonButtonReferencePoint = (i == 0) ? ReferencePoint.TopLeft :ReferencePoint.TopRight;
			int newPersonButtonPaddingY = (i == 0) ? PADDING : 0;
			
			newPersonButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "New Player"), buttonWidth, 150);
			newPersonButtons[i].addListener(new PersonListener());
			mainLayer.add(newPersonButtons[i], newPersonButton.getPosition(newPersonButtonReferencePoint), Positionable.ReferencePoint.TopLeft, PADDING, newPersonButtonPaddingY);
			
			personNameTextFields[i] = new TextField(container, fieldFont, buttonWidth, 40);
			mainLayer.add(personNameTextFields[i], newPersonButtons[i].getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
			
			personProfessionButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "Profession"), buttonWidth, 40);
			personProfessionButtons[i].addListener(new PersonListener());
			mainLayer.add(personProfessionButtons[i], personNameTextFields[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personMoneyLabels[i] = new Label(container, fieldFont, Color.white, "$0", buttonWidth);
			personMoneyLabels[i].setAlignment(Alignment.Center);
			mainLayer.add(personMoneyLabels[i], personProfessionButtons[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personSkillOneButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "Skill 1"), buttonWidth, 40);
			personSkillOneButtons[i].addListener(new PersonListener());
			mainLayer.add(personSkillOneButtons[i], personMoneyLabels[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personSkillTwoButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "Skill 2"), buttonWidth, 40);
			personSkillTwoButtons[i].addListener(new PersonListener());
			mainLayer.add(personSkillTwoButtons[i], personSkillOneButtons[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personSkillThreeButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "Skill 3"), buttonWidth, 40);
			personSkillThreeButtons[i].addListener(new PersonListener());
			mainLayer.add(personSkillThreeButtons[i], personSkillTwoButtons[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
		}
		
		backgroundLayer.add(new Background(container, new Color(0xa00008)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class PersonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {
			showModal(new Modal(container, PartyCreationScene.this, "Button Pushed!", "YAY"));
			Logger.log(""+source, Logger.Level.INFO);
		}
	}
}
