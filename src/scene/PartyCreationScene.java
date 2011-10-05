package scene;

import model.Party;
import model.Person;

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
import component.Label.Alignment;
import component.Modal;
import component.Positionable;
import component.Positionable.ReferencePoint;
import component.SegmentedControl;
import component.TextField;
import core.FontManager;
import core.GameDirector;
import core.Logger;

public class PartyCreationScene extends Scene {
	public static final SceneID ID = SceneID.PartyCreation;
	
	private static final int PADDING = 20;
	private static final int NUM_PEOPLE = 4;
	private static final int newPersonButtonHeight = 100;
	private static final int regularButtonHeight = 30;
	
	private Button newPersonButtons[] = new Button[NUM_PEOPLE];
	private TextField personNameTextFields[] = new TextField[NUM_PEOPLE];
	private Button personProfessionButtons[] = new Button[NUM_PEOPLE];
	private Label personMoneyLabels[] = new Label[NUM_PEOPLE];
	private Button personSkillOneButtons[] = new Button[NUM_PEOPLE];
	private Button personSkillTwoButtons[] = new Button[NUM_PEOPLE];
	private Button personSkillThreeButtons[] = new Button[NUM_PEOPLE];
	
	private Button confirmButton;
	private SegmentedControl rationsSegmentedControl;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		int buttonWidth = (container.getWidth() - PADDING * (NUM_PEOPLE + 1)) / NUM_PEOPLE;

		for (int i = 0; i < newPersonButtons.length; i++) {
			Positionable newPersonButton = (i == 0) ? mainLayer : newPersonButtons[i - 1];
			ReferencePoint newPersonButtonReferencePoint = (i == 0) ? ReferencePoint.TopLeft :ReferencePoint.TopRight;
			int newPersonButtonPaddingY = (i == 0) ? PADDING : 0;
			
			newPersonButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "New Player"), buttonWidth, newPersonButtonHeight);
			newPersonButtons[i].addListener(new ButtonListener());
			mainLayer.add(newPersonButtons[i], newPersonButton.getPosition(newPersonButtonReferencePoint), Positionable.ReferencePoint.TopLeft, PADDING, newPersonButtonPaddingY);
			
			personNameTextFields[i] = new TextField(container, fieldFont, buttonWidth, regularButtonHeight);
			personNameTextFields[i].addListener(new ButtonListener());
			personNameTextFields[i].setVisible(false);
			mainLayer.add(personNameTextFields[i], newPersonButtons[i].getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
			
			personProfessionButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "Profession"), buttonWidth, regularButtonHeight);
			personProfessionButtons[i].addListener(new ButtonListener());
			personProfessionButtons[i].setVisible(false);
			mainLayer.add(personProfessionButtons[i], personNameTextFields[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personMoneyLabels[i] = new Label(container, fieldFont, Color.white, "$0", buttonWidth);
			personMoneyLabels[i].setAlignment(Alignment.Center);
			personMoneyLabels[i].setVisible(false);
			mainLayer.add(personMoneyLabels[i], personProfessionButtons[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personSkillOneButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "Skill 1"), buttonWidth, regularButtonHeight);
			personSkillOneButtons[i].addListener(new ButtonListener());
			personSkillOneButtons[i].setVisible(false);
			mainLayer.add(personSkillOneButtons[i], personMoneyLabels[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personSkillTwoButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "Skill 2"), buttonWidth, regularButtonHeight);
			personSkillTwoButtons[i].addListener(new ButtonListener());
			personSkillTwoButtons[i].setVisible(false);
			mainLayer.add(personSkillTwoButtons[i], personSkillOneButtons[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personSkillThreeButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "Skill 3"), buttonWidth, regularButtonHeight);
			personSkillThreeButtons[i].addListener(new ButtonListener());
			personSkillThreeButtons[i].setVisible(false);
			mainLayer.add(personSkillThreeButtons[i], personSkillTwoButtons[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
		}
		
		String[] rationLabels = new String[3];
		for (int i = 0; i < Party.Rations.values().length; i++) {
			rationLabels[i] = Party.Rations.values()[i].toString();
		}
		
		rationsSegmentedControl = new SegmentedControl(container, fieldFont, Color.white, 400, regularButtonHeight, 1, rationLabels.length, 1, rationLabels);
		mainLayer.add(rationsSegmentedControl, mainLayer.getPosition(ReferencePoint.BottomLeft), ReferencePoint.BottomLeft, PADDING, -PADDING);
		
		confirmButton = new Button(container, new Label(container, fieldFont, Color.white, "Confirm"), buttonWidth, regularButtonHeight);
		confirmButton.addListener(new ButtonListener());
		mainLayer.add(confirmButton, mainLayer.getPosition(ReferencePoint.BottomRight), ReferencePoint.BottomRight, -PADDING, -PADDING);
		
		backgroundLayer.add(new Background(container, new Color(0xa00008)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {
			UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
			Person.Skill[] arr = Person.Skill.values();
			String[] strs = new String[arr.length - 1];
			for (int i = 0; i < strs.length; i++) {
				strs[i] = arr[i].getName();
			}	
			SegmentedControl sc = new SegmentedControl(container, fieldFont, Color.white, 400, 200, 5, 3, 3, strs);
			
			for (int i = 0; i < NUM_PEOPLE; i++) {
				if (source == newPersonButtons[i]) {
					personNameTextFields[i].setVisible(true);
					return;
				}
				
				if (source == personNameTextFields[i]) {
					showModal(new Modal(container, PartyCreationScene.this, "You want to select a profession, eh?", "Confirm"));
					personProfessionButtons[i].setVisible(true);
					return;
				}
				
				if (source == personProfessionButtons[i]) {
					personMoneyLabels[i].setVisible(true);
					personSkillOneButtons[i].setVisible(true);
					personSkillTwoButtons[i].setVisible(true);
					personSkillThreeButtons[i].setVisible(true);
					return;
				}
				
				if (source == personSkillOneButtons[i] || source == personSkillTwoButtons[i] || source == personSkillThreeButtons[i]) {
					showModal(new Modal(container, PartyCreationScene.this, "You want to select a skill, eh?", sc, "Confirm", "Cancel"));
				}
			}
			
			if (source == confirmButton) {
				Logger.log("Confirm button pushed", Logger.Level.DEBUG);
			}
		}
	}
}
