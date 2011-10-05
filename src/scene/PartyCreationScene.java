package scene;

import java.util.ArrayList;

import model.Party;
import model.Party.Pace;
import model.Party.Rations;
import model.Person;
import model.Person.Skill;
import model.Player;

import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
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

/**
 * The Party Creation Scene
 * @author Tanner Smith
 */
public class PartyCreationScene extends Scene {
	public static final SceneID ID = SceneID.PartyCreation;
	
	private static final int PADDING = 20;
	private static final int NUM_PEOPLE = 4;
	private static final int NUM_SKILLS = 3;
	private static final int newPersonButtonHeight = 100;
	private static final int regularButtonHeight = 30;
	
	private Player player;
	
	private Button newPersonButtons[] = new Button[NUM_PEOPLE];
	private Button personDeleteButtons[] = new Button[NUM_PEOPLE];
	private TextField personNameTextFields[] = new TextField[NUM_PEOPLE];
	private Button personChangeProfessionButtons[] = new Button[NUM_PEOPLE];
	private Label personProfessionLabels[] = new Label[NUM_PEOPLE];
	private Label personMoneyLabels[] = new Label[NUM_PEOPLE];
	private Button personChangeSkillButtons[] = new Button[NUM_PEOPLE];
	private Label personSkillLabels[][] = new Label[NUM_PEOPLE][NUM_SKILLS];
	
	private Button confirmButton;
	private SegmentedControl rationsSegmentedControl, professionSegmentedControl, skillSegmentedControl, paceSegmentedControl;
	
	private ArrayList<Person> people = new ArrayList<Person>();
		
	private Modal professionModal;
	private Modal skillModal;
	
	private Rations rations;
	private Pace pace;
	
	private int currentPersonModifying;
	
	public PartyCreationScene(Player player) {
		this.player = player;
	}
	
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
			
			personChangeProfessionButtons[i] = new Button(container, new Label(container, fieldFont, Color.white, LIT_MAP.get("OT_PROFESSION")), buttonWidth, regularButtonHeight);
			personChangeProfessionButtons[i].setTopLeftRoundedCorner(true);
			personChangeProfessionButtons[i].setTopRightRoundedCorner(true);
			personChangeProfessionButtons[i].addListener(new ButtonListener());
			personChangeProfessionButtons[i].setVisible(false);
			mainLayer.add(personChangeProfessionButtons[i], personNameTextFields[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
			personProfessionLabels[i] = new Label(container, fieldFont, Color.white, "No Profession", buttonWidth);
			personProfessionLabels[i].setHeight(regularButtonHeight);
			personProfessionLabels[i].setBackgroundColor(Color.darkGray);
			personProfessionLabels[i].setVisible(false);
			personProfessionLabels[i].setAlignment(Alignment.Center);
			mainLayer.add(personProfessionLabels[i], personChangeProfessionButtons[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, 0);
			
			personMoneyLabels[i] = new Label(container, fieldFont, Color.white, "$0", buttonWidth);
			personMoneyLabels[i].setAlignment(Alignment.Center);
			personMoneyLabels[i].setVisible(false);
			mainLayer.add(personMoneyLabels[i], personProfessionLabels[i].getPosition(ReferencePoint.BottomLeft), ReferencePoint.TopLeft, 0, PADDING);
			
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
				
				personSkillLabels[i][j] = new Label(container, fieldFont, Color.white, "", buttonWidth);
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
	
	/**
	 * Enable the next "new person" button
	 */
	private void enableNextPersonField() {
		for (int i = 0; i < NUM_PEOPLE; i++) {
			if (i == people.size()) {
				newPersonButtons[i].setDisabled(false);
			} else {
				newPersonButtons[i].setDisabled(true);
			}
		}
	}
	
	/**
	 * Hide all the fields for a specific column
	 * @param col Column we want to hide everything
	 */
	private void hidePersonColumn(int col) {
		personDeleteButtons[col].setVisible(false);
		personNameTextFields[col].setVisible(false);
		personChangeProfessionButtons[col].setVisible(false);
		personProfessionLabels[col].setVisible(false);
		personMoneyLabels[col].setVisible(false);
		personChangeSkillButtons[col].setVisible(false);
		for (int j = 0; j < personSkillLabels[col].length; j++) {
			personSkillLabels[col][j].setVisible(false);
		}
		
		if (col > 0) {
			personDeleteButtons[col - 1].setVisible(true);
		}
		
		if (col < NUM_PEOPLE - 1) {
			personNameTextFields[col + 1].setVisible(false);
		}
	}
	
	/**
	 * Clear the person's data for a column.
	 * @param col Column of data we want to remove
	 */
	private void clearPersonData(int col) {
		people.remove(col);
		
		personNameTextFields[col].clear();
		
		rationsSegmentedControl.clear();
		professionSegmentedControl.clear();
		//TODO: Clear other things when deleted
	}
	
	@Override
	public void resignModal(Modal modal, int[] segmentedControlResults) {
		super.resignModal(modal, segmentedControlResults);
		if(modal == professionModal) {
			personMoneyLabels[currentPersonModifying].setText("$" + Person.Profession.values()[segmentedControlResults[0]].getMoney());
			people.get(currentPersonModifying).setProfession(Person.Profession.values()[segmentedControlResults[0]]);
			for(int j = 0; j < 3; j++) {
				personSkillLabels[currentPersonModifying][j].setText("");
			}
			personProfessionLabels[currentPersonModifying].setText(people.get(currentPersonModifying).getProfession().getName());
		}
		else if (modal == skillModal) {
			people.get(currentPersonModifying).clearSkills();
			for(int j = 0; j < 3; j++) {
				if(segmentedControlResults.length >= j + 1) {
					people.get(currentPersonModifying).addSkill(Skill.values()[segmentedControlResults[j]]);
					personSkillLabels[currentPersonModifying][j].setText(people.get(currentPersonModifying).getSkills().get(j).getName());
				}
				else {
					personSkillLabels[currentPersonModifying][j].setText("");
				}
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
					if(people.size() < i + 1) {
						people.add(i, new Person(personNameTextFields[i].getText()));
	
						// Moves the delete button to the latest person created
						for (int j = 0; j < people.size() - 1; j++) {
							personDeleteButtons[j].setVisible(false);
						}
						personDeleteButtons[people.size() - 1].setVisible(true);
						
						personChangeProfessionButtons[i].setVisible(true);
						personProfessionLabels[i].setVisible(true);
					}
					else {
						people.get(i).setName(personNameTextFields[i].getText());
					}
				}
				
				if (source == personChangeProfessionButtons[i]) {
					professionModal = new Modal(container, PartyCreationScene.this, LIT_MAP.get("PC_PROMPT"), professionSegmentedControl, LIT_MAP.get("OT_CONFIRM"), LIT_MAP.get("OT_CANCEL"));
					currentPersonModifying = i;
					
					showModal(professionModal);
					personMoneyLabels[i].setVisible(true);
					
					personChangeSkillButtons[i].setVisible(true);
					for (int j = 0; j < personSkillLabels[i].length; j++) {
						personSkillLabels[i][j].setVisible(true);
					}
				}
				
				if (source == personChangeSkillButtons[i]) {
					skillModal = new Modal(container, PartyCreationScene.this, LIT_MAP.get("PC_PROMPT"), skillSegmentedControl, LIT_MAP.get("OT_CONFIRM"), LIT_MAP.get("OT_CANCEL"));
					currentPersonModifying = i;
					
					showModal(skillModal);
				}
				
				if (source == personDeleteButtons[i]) {
					//Delete the last created person
					clearPersonData(i);
					hidePersonColumn(i);
				}
			}
			
			enableNextPersonField();
			
			if (source == confirmButton) {
				//TODO: Show dialog box if persons don't have a profession
				for (Person person : people) {
					if (person.getProfession() == null) {
						Logger.log("Not all party members have professions selected", Logger.Level.INFO);
						return;
					}
				}
				pace = Pace.values()[paceSegmentedControl.getState()[0]];
				rations = Rations.values()[rationsSegmentedControl.getState()[0]];
				player.setParty(new Party(pace, rations, people));
				
				Logger.log("Confirm button pushed", Logger.Level.DEBUG);
			}
		}
	}
}
