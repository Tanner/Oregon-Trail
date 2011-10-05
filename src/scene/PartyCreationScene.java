package scene;

import java.util.ArrayList;

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
import static core.ConstantStore.LIT_MAP;

public class PartyCreationScene extends Scene {
	public static final SceneID ID = SceneID.PartyCreation;
	
	private static final int PADDING = 20;
	private static final int NUM_PEOPLE = 4;
	private static final int NUM_SKILLS = 3;
	private static final int newPersonButtonHeight = 100;
	private static final int regularButtonHeight = 30;
	
	private Button newPersonButtons[] = new Button[NUM_PEOPLE];
	private Button personDeleteButtons[] = new Button[NUM_PEOPLE];
	private TextField personNameTextFields[] = new TextField[NUM_PEOPLE];
	private Button personProfessionButtons[] = new Button[NUM_PEOPLE];
	private Label personMoneyLabels[] = new Label[NUM_PEOPLE];
	private Button personChangeSkillButtons[] = new Button[NUM_PEOPLE];
	private Label personSkillLabels[][] = new Label[NUM_PEOPLE][NUM_SKILLS];
	
	private Button confirmButton;
	private SegmentedControl rationsSegmentedControl, professionSegmentedControl, skillSegmentedControl, paceSegmentedControl;
	
	private ArrayList<Person> people = new ArrayList<Person>();
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		int buttonWidth = (container.getWidth() - PADDING * (NUM_PEOPLE + 1)) / NUM_PEOPLE;

		int numOfProfessions = Person.Profession.values().length;
		String[] professionLabels = new String[numOfProfessions];
		for (int i = 0; i < numOfProfessions; i++) {
			professionLabels[i] = Person.Profession.values()[i].toString();
		}
		professionSegmentedControl = new SegmentedControl(container, 400, 200, 5, 5, 5, 1, professionLabels);
		
		int numOfSkills = Person.Skill.values().length - 1;
		String[] skillLabels = new String[numOfSkills];
		for (int i = 0; i < numOfSkills; i++) {
			skillLabels[i] = Person.Skill.values()[i].getName();
		}	
		skillSegmentedControl = new SegmentedControl(container, 400, 200, 5, 3, 5, 3, skillLabels);
		
		for (int i = 0; i < newPersonButtons.length; i++) {
			Positionable newPersonButton = (i == 0) ? mainLayer : newPersonButtons[i - 1];
			ReferencePoint newPersonButtonReferencePoint = (i == 0) ? ReferencePoint.TopLeft :ReferencePoint.TopRight;
			int newPersonButtonPaddingY = (i == 0) ? PADDING : 0;
			
			newPersonButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, LIT_MAP.get("OT_NEW_PLAYER")), buttonWidth, newPersonButtonHeight);
			newPersonButtons[i].setRoundedCorners(true);
			newPersonButtons[i].addListener(new ButtonListener());
			mainLayer.add(newPersonButtons[i], newPersonButton.getPosition(newPersonButtonReferencePoint), Positionable.ReferencePoint.TopLeft, PADDING, newPersonButtonPaddingY);
			
			personNameTextFields[i] = new TextField(container, fieldFont, buttonWidth, regularButtonHeight);
			personNameTextFields[i].setPlaceholderText("Name");
			personNameTextFields[i].addListener(new ButtonListener());
			personNameTextFields[i].setVisible(false);
			mainLayer.add(personNameTextFields[i], newPersonButtons[i].getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
			
			personProfessionButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, LIT_MAP.get("OT_PROFESSION")), buttonWidth, regularButtonHeight);
			personProfessionButtons[i].setRoundedCorners(true);
			personProfessionButtons[i].addListener(new ButtonListener());
			personProfessionButtons[i].setVisible(false);
			mainLayer.add(personProfessionButtons[i], personNameTextFields[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personMoneyLabels[i] = new Label(container, fieldFont, Color.white, "$0", buttonWidth);
			personMoneyLabels[i].setAlignment(Alignment.Center);
			personMoneyLabels[i].setVisible(false);
			mainLayer.add(personMoneyLabels[i], personProfessionButtons[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personChangeSkillButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, LIT_MAP.get("OT_CHANGE_SKILL")), buttonWidth, regularButtonHeight);
			personChangeSkillButtons[i].setTopLeftRoundedCorner(true);
			personChangeSkillButtons[i].setTopRightRoundedCorner(true);
			personChangeSkillButtons[i].addListener(new ButtonListener());
			personChangeSkillButtons[i].setVisible(false);
			mainLayer.add(personChangeSkillButtons[i], personMoneyLabels[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			Positionable skillLabelReferenceObject = personChangeSkillButtons[i];
			for (int j = 0; j < personSkillLabels[i].length; j++) {
				if (j != 0) {
					skillLabelReferenceObject = personSkillLabels[i][j - 1];
				}
				
				personSkillLabels[i][j] = new Label(container, fieldFont, Color.white, LIT_MAP.get("OT_SKILL") +j, buttonWidth);
				personSkillLabels[i][j].setHeight(regularButtonHeight);
				personSkillLabels[i][j].setBackgroundColor(Color.darkGray);
				personSkillLabels[i][j].setVisible(false);
				personSkillLabels[i][j].setAlignment(Alignment.Center);
				mainLayer.add(personSkillLabels[i][j], skillLabelReferenceObject.getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, 0);
			}
		}
		
		for (int i = 0; i < newPersonButtons.length; i++) {
			personDeleteButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, "X"), 20, regularButtonHeight);
			personDeleteButtons[i].setVisible(false);
			personDeleteButtons[i].setRoundedCorners(true);
			personDeleteButtons[i].addListener(new ButtonListener());
			mainLayer.add(personDeleteButtons[i], newPersonButtons[i].getPosition(ReferencePoint.TopRight), ReferencePoint.CenterCenter, 0, 0);
		}
		
		// Ration Selection
		Label rationsLabel = new Label(container, fieldFont, Color.white, LIT_MAP.get("OT_RATIONS"));
		rationsLabel.setHeight(regularButtonHeight);
		mainLayer.add(rationsLabel, mainLayer.getPosition(ReferencePoint.BottomLeft), ReferencePoint.BottomLeft, PADDING, -PADDING);
		
		int numOfRations = Party.Rations.values().length;
		String[] rationLabels = new String[numOfRations];
		for (int i = 0; i < numOfRations; i++) {
			rationLabels[i] = Party.Rations.values()[i].toString();
		}
		rationsSegmentedControl = new SegmentedControl(container, buttonWidth * 2 + PADDING, regularButtonHeight, 1, rationLabels.length, 0, 1, rationLabels);
		mainLayer.add(rationsSegmentedControl, rationsLabel.getPosition(ReferencePoint.TopRight), ReferencePoint.TopLeft, PADDING, 0);
		
		// Pace Selection
		Label paceLabel = new Label(container, fieldFont, Color.white, LIT_MAP.get("OT_PACE"));
		paceLabel.setHeight(regularButtonHeight);
		mainLayer.add(paceLabel, rationsLabel.getPosition(ReferencePoint.TopLeft), ReferencePoint.BottomLeft, 0, -PADDING);
		
		int numOfPaces = Party.Pace.values().length;
		String[] paceLabels = new String[numOfRations];
		for (int i = 0; i < numOfPaces; i++) {
			paceLabels[i] = Party.Pace.values()[i].toString();
		}
		paceSegmentedControl = new SegmentedControl(container, buttonWidth * 2 + PADDING, regularButtonHeight, 1, paceLabels.length, 0, 1, paceLabels);
		mainLayer.add(paceSegmentedControl, rationsSegmentedControl.getPosition(ReferencePoint.TopLeft), ReferencePoint.BottomLeft, 0, -PADDING);
		
		//Confirm Button
		confirmButton = new Button(container, new Label(container, fieldFont, Color.white,  LIT_MAP.get("OT_CONFIRM")), buttonWidth, regularButtonHeight);
		confirmButton.setRoundedCorners(true);
		confirmButton.addListener(new ButtonListener());
		mainLayer.add(confirmButton, mainLayer.getPosition(ReferencePoint.BottomRight), ReferencePoint.BottomRight, -PADDING, -PADDING);
		
		backgroundLayer.add(new Background(container, new Color(0xa00008)));
		
		enableNextPersonField();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private void enableNextPersonField() {
		for (int i = 0; i < NUM_PEOPLE; i++) {
			if (i == people.size()) {
				newPersonButtons[i].setDisabled(false);
			} else {
				newPersonButtons[i].setDisabled(true);
			}
		}
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {			
			for (int i = 0; i < NUM_PEOPLE; i++) {
				if (source == newPersonButtons[i]) {
					personNameTextFields[i].setVisible(true);
				}
				
				if (source == personNameTextFields[i]) {
					people.add(i, new Person(personNameTextFields[i].getText()));

					// Moves the delete button to the latest person created
					for (int j = 0; j < people.size() - 1; j++) {
						personDeleteButtons[j].setVisible(false);
					}
					personDeleteButtons[people.size() - 1].setVisible(true);
					
					personProfessionButtons[i].setVisible(true);
				}
				
				if (source == personProfessionButtons[i]) {
					showModal(new Modal(container, PartyCreationScene.this, LIT_MAP.get("PC_PROMPT"), professionSegmentedControl, LIT_MAP.get("OT_CONFIRM"), LIT_MAP.get("OT_CANCEL")));
					personMoneyLabels[i].setVisible(true);
					
					personChangeSkillButtons[i].setVisible(true);
					for (int j = 0; j < personSkillLabels[i].length; j++) {
						personSkillLabels[i][j].setVisible(true);
					}
				}
				
				if (source == personChangeSkillButtons[i]) {
					showModal(new Modal(container, PartyCreationScene.this, LIT_MAP.get("PC_PROMPT"), skillSegmentedControl, LIT_MAP.get("OT_CONFIRM"), LIT_MAP.get("OT_CANCEL")));
				}
			}
			
			enableNextPersonField();
			
			if (source == confirmButton) {
				Logger.log("Confirm button pushed", Logger.Level.DEBUG);
			}
		}
	}
}
