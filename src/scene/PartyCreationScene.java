package scene;

import java.util.*;

import model.Party;
import model.Party.Pace;
import model.Party.Rations;
import model.Person;
import model.Skill;
import model.Player;
import model.Time;
import model.Profession;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Panel;
import component.Button;
import component.Label;
import component.Label.Alignment;
import component.Positionable;
import component.Positionable.ReferencePoint;
import component.modal.MessageModal;
import component.modal.Modal;
import component.modal.ComponentModal;
import component.SegmentedControl;
import component.TextField;
import core.ConstantStore;
import core.FontStore;
import core.GameDirector;
import core.ImageStore;
import core.Logger;

/**
 * Scene to allow user to create a party for the game.
 */
public class PartyCreationScene extends Scene {
	public static final SceneID ID = SceneID.PARTYCREATION;
	
	private static final int PADDING = 20;
	private static final int INNER_PADDING = 10;
	
	private static final int NUM_PEOPLE = 4;
	private static final int NUM_SKILLS = 3;
	private static final int NEW_PERSON_BUTTON_HEIGHT = 100;
	private static final int REGULAR_BUTTON_HEIGHT = 30;
	
	private Player player;
	
	// Array of people
	private List<Person> people = new ArrayList<Person>();
	
	// Component Listener
	private ComponentListener componentListener;
	
	// Add/delete buttons
	private Button newPersonButtons[] = new Button[NUM_PEOPLE];
	private Button personDeleteButtons[] = new Button[NUM_PEOPLE];
	
	// Name textfield
	private TextField personNameTextFields[] = new TextField[NUM_PEOPLE];
	
	// Gender segmented control
	private SegmentedControl personGenderControl[] = new SegmentedControl[NUM_PEOPLE];
	
	// Profession buttons and labels
	private Panel personProfessionPanel[] = new Panel[NUM_PEOPLE];
	private Button personChangeProfessionButtons[] = new Button[NUM_PEOPLE];
	private Label personProfessionLabels[] = new Label[NUM_PEOPLE];
	
	// Money label
	private Label personMoneyLabels[] = new Label[NUM_PEOPLE];
	
	// Skill buttons and labels
	private Panel personSkillPanel[] = new Panel[NUM_PEOPLE];
	private Button personChangeSkillButtons[] = new Button[NUM_PEOPLE];
	private Label personSkillLabels[][] = new Label[NUM_PEOPLE][NUM_SKILLS];
	
	// Segmented Controls
	private SegmentedControl rationsSegmentedControl, professionSegmentedControl, skillSegmentedControl, paceSegmentedControl;
	
	// Confirm Button
	private Button confirmButton;
	
	// Modals
	private ComponentModal<SegmentedControl> professionModal;
	private ComponentModal<SegmentedControl> skillModal;
	
	// Current person modying index
	private int currentPersonModifying;
	
	public PartyCreationScene(Player player) {
		this.player = player;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		int buttonWidth = (container.getWidth() - PADDING * (NUM_PEOPLE + 1)) / NUM_PEOPLE;

		componentListener = new SceneComponentListener();
		
		// Create the profession segmented control
		int numOfProfessions = Profession.values().length;
		String[] professionLabels = new String[numOfProfessions];
		String[] professionTooltips = new String[numOfProfessions];
		for (int i = 0; i < numOfProfessions; i++) {
			professionLabels[i] = Profession.values()[i].toString();
			
			int money = Profession.values()[i].getMoney();
			Skill startingSkill = Profession.values()[i].getStartingSkill();
			
			String moneyString = ConstantStore.get("PARTY_CREATION_SCENE", "PROFESSIONS_TOOLTIP_MONEY") + ConstantStore.get("GENERAL", "MONEY_SYMBOL") + String.format("%,d", money);
			String skillString = ConstantStore.get("PARTY_CREATION_SCENE", "PROFESSIONS_TOOLTIP_SKILL") + startingSkill;
			
			professionTooltips[i] = moneyString + "\n" + skillString;
		}
		professionSegmentedControl = new SegmentedControl(container, 800, 200, 5, 5, 5, true, 1, professionLabels);
		professionSegmentedControl.setTooltips(professionTooltips);
		
		// Create the skill segmented control
		int numOfSkills = Skill.values().length - 1;
		String[] skillLabels = new String[numOfSkills];
		for (int i = 0; i < numOfSkills; i++) {
			skillLabels[i] = Skill.values()[i].getName();
		}
		skillSegmentedControl = new SegmentedControl(container, 800, 200, 5, 3, 5, true, 3, skillLabels);
		
		// Create all the "add person" column
		for (int i = 0; i < newPersonButtons.length; i++) {
			Positionable newPersonButton = (i == 0) ? mainLayer : newPersonButtons[i - 1];
			ReferencePoint newPersonButtonReferencePoint = (i == 0) ? ReferencePoint.TOPLEFT :ReferencePoint.TOPRIGHT;
			int newPersonButtonPaddingY = (i == 0) ? PADDING : 0;
			
			newPersonButtons[i] = new Button(container, buttonWidth, NEW_PERSON_BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "NEW_PERSON")));
			newPersonButtons[i].addListener(componentListener);
			mainLayer.add(newPersonButtons[i], newPersonButton.getPosition(newPersonButtonReferencePoint), Positionable.ReferencePoint.TOPLEFT, PADDING, newPersonButtonPaddingY);
			
			personNameTextFields[i] = new TextField(container, buttonWidth, REGULAR_BUTTON_HEIGHT, fieldFont);
			personNameTextFields[i].setPlaceholderText(ConstantStore.get("PARTY_CREATION_SCENE", "NAME_PLACEHOLDER"));
			personNameTextFields[i].addListener(componentListener);
			personNameTextFields[i].setVisible(false);
			mainLayer.add(personNameTextFields[i], newPersonButtons[i].getPosition(Positionable.ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.TOPLEFT, 0, INNER_PADDING);
			
			personGenderControl[i] = new SegmentedControl(container, buttonWidth, REGULAR_BUTTON_HEIGHT, 1, 2, 0, false, 1, "Male", "Female");
			personGenderControl[i].setVisible(false);
			personGenderControl[i].addListener(componentListener);
			mainLayer.add(personGenderControl[i], personNameTextFields[i].getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, INNER_PADDING);
			
			// Profession
			personProfessionPanel[i] = new Panel(container, buttonWidth, REGULAR_BUTTON_HEIGHT * 2);
			personProfessionPanel[i].setVisible(false);
			personProfessionPanel[i].setBorderColor(ConstantStore.COLORS.get("INTERACTIVE_BORDER_DARK"));
			personProfessionPanel[i].setBorderWidth(2);
			
			personChangeProfessionButtons[i] = new Button(container, buttonWidth, REGULAR_BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "CHANGE_PROFESSION")));
			personChangeProfessionButtons[i].setAcceptingInput(false);
			personChangeProfessionButtons[i].setBottomBorderWidth(0);
			personChangeProfessionButtons[i].addListener(componentListener);
			personProfessionPanel[i].add(personChangeProfessionButtons[i], personProfessionPanel[i].getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
			
			personProfessionLabels[i] = new Label(container, buttonWidth, REGULAR_BUTTON_HEIGHT, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "NO_PROFESSION_LABEL"));
			personProfessionLabels[i].setBackgroundColor(Color.darkGray);
			personProfessionLabels[i].setAlignment(Alignment.CENTER);
			personProfessionPanel[i].add(personProfessionLabels[i], personChangeProfessionButtons[i].getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, 0);
			
			mainLayer.add(personProfessionPanel[i], personGenderControl[i].getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, INNER_PADDING);
			
			// Money
			personMoneyLabels[i] = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("GENERAL", "MONEY_SYMBOL") + "0");
			personMoneyLabels[i].setAlignment(Alignment.CENTER);
			personMoneyLabels[i].setVisible(false);
			mainLayer.add(personMoneyLabels[i], personProfessionLabels[i].getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, INNER_PADDING);
			
			// Skills
			personSkillPanel[i] = new Panel(container, buttonWidth, REGULAR_BUTTON_HEIGHT * 4);
			personSkillPanel[i].setVisible(false);
			personSkillPanel[i].setBorderColor(ConstantStore.COLORS.get("INTERACTIVE_BORDER_DARK"));
			personSkillPanel[i].setBorderWidth(2);
			
			personChangeSkillButtons[i] = new Button(container, buttonWidth, REGULAR_BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "CHANGE_SKILL")));
			personChangeSkillButtons[i].setAcceptingInput(false);
			personChangeSkillButtons[i].setBottomBorderWidth(0);
			personChangeSkillButtons[i].addListener(componentListener);
			personSkillPanel[i].add(personChangeSkillButtons[i], personSkillPanel[i].getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
			
			for (int j = 0; j < personSkillLabels[i].length; j++) {
				personSkillLabels[i][j] = new Label(container, buttonWidth, REGULAR_BUTTON_HEIGHT, fieldFont, Color.white, "");
				personSkillLabels[i][j].setBackgroundColor(Color.darkGray);
				personSkillLabels[i][j].setAlignment(Alignment.CENTER);
				personSkillLabels[i][j].setText(ConstantStore.get("PARTY_CREATION_SCENE", "EMPTY_SKILL_LABEL"));
			}
			
			personSkillPanel[i].addAsColumn(Arrays.asList(personSkillLabels[i]).iterator(), personChangeSkillButtons[i].getPosition(ReferencePoint.BOTTOMLEFT), 0, 0, 0);
			mainLayer.add(personSkillPanel[i], personMoneyLabels[i].getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.TOPLEFT, 0, INNER_PADDING);
		}
		
		// Create delete buttons for all the player columns
		for (int i = 0; i < newPersonButtons.length; i++) {
			personDeleteButtons[i] = new Button(container, REGULAR_BUTTON_HEIGHT, REGULAR_BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "DELETE_PERSON_LABEL")));
			personDeleteButtons[i].setVisible(false);
			personDeleteButtons[i].setButtonColor(Color.red);
			personDeleteButtons[i].addListener(componentListener);
			mainLayer.add(personDeleteButtons[i], newPersonButtons[i].getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.CENTERCENTER, 0, 0);
		}
		
		// Ration Selection
		int labelWidth = 100;
		Label rationsLabel = new Label(container, labelWidth, REGULAR_BUTTON_HEIGHT, fieldFont, Color.white, ConstantStore.get("GENERAL", "RATIONS_LABEL"));
		rationsLabel.setAlignment(Alignment.LEFT);
		mainLayer.add(rationsLabel, mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, PADDING, -PADDING);
		
		int numOfRations = Party.Rations.values().length;
		String[] rationLabels = new String[numOfRations];
		for (int i = 0; i < numOfRations; i++) {
			rationLabels[i] = Party.Rations.values()[i].toString();
		}
		rationsSegmentedControl = new SegmentedControl(container, buttonWidth * 2 + PADDING, REGULAR_BUTTON_HEIGHT, 1, rationLabels.length, 0, true, 1, rationLabels);
		mainLayer.add(rationsSegmentedControl, rationsLabel.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, PADDING, 0);
		
		// Pace Selection
		Label paceLabel = new Label(container, labelWidth, REGULAR_BUTTON_HEIGHT, fieldFont, Color.white, ConstantStore.get("GENERAL", "PACE_LABEL"));
		paceLabel.setAlignment(Alignment.LEFT);
		mainLayer.add(paceLabel, rationsLabel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, -PADDING);
		
		int numOfPaces = Party.Pace.values().length;
		String[] paceLabels = new String[numOfPaces];
		for (int i = 0; i < numOfPaces; i++) {
			paceLabels[i] = Party.Pace.values()[i].toString();
		}
		paceSegmentedControl = new SegmentedControl(container, buttonWidth * 2 + PADDING, REGULAR_BUTTON_HEIGHT, 1, paceLabels.length, 0, true, 1, paceLabels);
		mainLayer.add(paceSegmentedControl, rationsSegmentedControl.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.BOTTOMLEFT, 0, -PADDING);
		
		//Confirm Button
		confirmButton = new Button(container, buttonWidth, REGULAR_BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("PARTY_CREATION_SCENE", "PARTY_CONFIRM")));
		confirmButton.addListener(componentListener);
		mainLayer.add(confirmButton, mainLayer.getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, -PADDING, -PADDING);
		
		backgroundLayer.add(new Panel(container, ImageStore.get().getImage("DIRT_BACKGROUND")));
		
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
	 * Enable the next "new person" button.
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
		personGenderControl[col].setVisible(false);
		personProfessionPanel[col].setVisible(false);
		personMoneyLabels[col].setVisible(false);
		personSkillPanel[col].setVisible(false);
		
		personChangeProfessionButtons[col].setAcceptingInput(false);
		personChangeSkillButtons[col].setAcceptingInput(false);
		
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
		
		personGenderControl[col].setSelection(new int[]{});
		personNameTextFields[col].clear();
		personProfessionLabels[col].setText("No Profession");
		for(int j = 0; j < 3; j++) {
			personSkillLabels[col][j].setText(""); 
		}
	}
	
	/**
	 * Process the values from the modal dialog for profession
	 * @param segmentedControlResults The results of the data entry
	 */
	private void setProfession(int[] segmentedControlResults){
		personMoneyLabels[currentPersonModifying].setText(ConstantStore.get("GENERAL", "MONEY_SYMBOL") + Profession.values()[segmentedControlResults[0]].getMoney());
		people.get(currentPersonModifying).setProfession(Profession.values()[segmentedControlResults[0]]);
		
		personProfessionLabels[currentPersonModifying].setText(people.get(currentPersonModifying).getProfession().getName());
		
		personMoneyLabels[currentPersonModifying].setVisible(true);
		personSkillPanel[currentPersonModifying].setVisible(true);
		personChangeSkillButtons[currentPersonModifying].setAcceptingInput(true);
		
		for (int j = 0; j < personSkillLabels[currentPersonModifying].length; j++) {
			personSkillLabels[currentPersonModifying][j].setText(ConstantStore.get("PARTY_CREATION_SCENE", "EMPTY_SKILL_LABEL"));
			personSkillLabels[currentPersonModifying][j].setVisible(true);
		} 
		
		if (people.get(currentPersonModifying).getProfession().getStartingSkill() != Skill.NONE) {
			personSkillLabels[currentPersonModifying][0].setText(people.get(currentPersonModifying).getProfession().getStartingSkill().getName());
		}
	}
	
	/**
	 * Process the values from the modal dialog for Skill selection
	 * @param segmentedControlResults The results of the data entry
	 */
	private void setSkills(int[] segmentedControlResults){
		people.get(currentPersonModifying).clearSkills();
		
		for (int j = 0; j < 3; j++) {
			if (segmentedControlResults.length >= j + 1) {
				people.get(currentPersonModifying).addSkill(Skill.values()[segmentedControlResults[j]]);
			}
		}
		
		for (int j = 0; j < 3; j++) {
			if (people.get(currentPersonModifying).getSkills().size() >= j + 1) {
				personSkillLabels[currentPersonModifying][j].setText(people.get(currentPersonModifying).getSkills().get(j).getName());
			} else {
				personSkillLabels[currentPersonModifying][j].setText(ConstantStore.get("PARTY_CREATION_SCENE", "EMPTY_SKILL_LABEL"));
			}
		}
	}
	
	@Override
	public void dismissModal(Modal modal, int button) {
		super.dismissModal(modal, button);
		
		if (button != modal.getCancelButtonIndex()) {
			if (modal == professionModal) {
				int[] segmentedControlResults = professionModal.getComponent().getSelection();
				setProfession(segmentedControlResults);
			} else if (modal == skillModal) {
				int[] segmentedControlResults = skillModal.getComponent().getSelection();
				setSkills(segmentedControlResults);
			}
		}
	}
	
	private class SceneComponentListener implements ComponentListener {
		/**
		 * New person button triggered event; set text field access accordingly.
		 * @param i The index in the textfield array we are checking
		 */		
		private void newPersonButtonActivated(int i){
			personNameTextFields[i].setVisible(true);
			personNameTextFields[i].setFocus(true);
		}
	
		/**
		 * Person name text field triggered event, setup person's data accordingly.
		 * @param i The index in the textfield array we are checking
		 * @return exit code - 0 for natural exit, 1 for forced exit
		 */		
		private int personNameTextFieldActivated(int i) {
			if (people.size() < i + 1) {
				// If the party is not full...
				if (!personNameTextFields[i].isEmpty()) {
					// If the textfield is not empty...		
					for (Person person : people) {
						if (person.getName().equalsIgnoreCase(personNameTextFields[i].getText())) {
							// If the name already exists, tell the user
							showModal(new MessageModal(container,
									PartyCreationScene.this,
									ConstantStore.get("PARTY_CREATION_SCENE", "ERR_DUP_NAME")));
							personNameTextFields[i].clear();
							return 1;
						}
					}
					
					people.add(i, new Person(personNameTextFields[i].getText()));
					
					// Moves the delete button to the latest person created
					for (int j = 0; j < people.size() - 1; j++) {
						personDeleteButtons[j].setVisible(false);
					}
					personDeleteButtons[people.size() - 1].setVisible(true);
					
					personGenderControl[i].setVisible(true);
				}
			} else {
				// Party is full
				if (personNameTextFields[i].isEmpty()) {
					personNameTextFields[i].setText(people.get(i).getName());
				}
				
				people.get(i).setName(personNameTextFields[i].getText());
			}
			
			return 0;
		}
		
		/**
		 * Gender control changed value; handle new gender for player.
		 * @param i The index of the button pushed
		 */
		private void personGenderControlActivated(int i) {
			int[] genderControlSelection = personGenderControl[i].getSelection();
			
			if (genderControlSelection.length != 0) {
				if (genderControlSelection[0] == 0) {
					people.get(i).setIsMale(true);
				} else {
					people.get(i).setIsMale(false);
				}
				
				personProfessionPanel[i].setVisible(true);
				personChangeProfessionButtons[i].setAcceptingInput(true);
			}
		}
		
		/**
		 * Change profession button pressed; handle new profession entry by player.
		 * @param i The index of the button pushed
		 */	
		private void personChangeProfButtonActivated(int i) {
			professionSegmentedControl.clear();
			professionModal = new ComponentModal<SegmentedControl>(container,
					PartyCreationScene.this,
					ConstantStore.get("PARTY_CREATION_SCENE", "PROFESSION_MODAL"),
					2,
					professionSegmentedControl);
			professionModal.setButtonText(professionModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
			professionModal.setButtonText(professionModal.getCancelButtonIndex() + 1, ConstantStore.get("GENERAL", "CONFIRM"));

			int[] currentProfession = new int[1];
			if (people.get(i).getProfession() != null) {
				currentProfession[0] = people.get(i).getProfession().ordinal();
			}
			
			professionSegmentedControl.setSelection(currentProfession);
			currentPersonModifying = i;
			showModal(professionModal);
		}
		
		/**
		 * Change skill button pressed; handle data and code appropriately.
		 * @param i the index of the change skill button pressed
		 */
		private void personChangeSkillButtonActivated(int i) {
			skillSegmentedControl.clear();
			String skillModalMessage;
			
			if (people.get(i).getProfession().getStartingSkill() != Skill.NONE) {
				skillModalMessage = String.format(ConstantStore.get("PARTY_CREATION_SCENE", "SKILL_MODAL_MESSAGE"), people.get(i).getName(), people.get(i).getProfession().getStartingSkill().getName());
			}  else {
				skillModalMessage = String.format(ConstantStore.get("PARTY_CREATION_SCENE", "SKILL_MODAL_MESSAGE_NO_SKILL"), people.get(i).getName());
			}
			
			skillModal = new ComponentModal<SegmentedControl>(container,
					PartyCreationScene.this,
					skillModalMessage,
					2,
					skillSegmentedControl);
			skillModal.setButtonText(skillModal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
			skillModal.setButtonText(skillModal.getCancelButtonIndex() + 1, ConstantStore.get("GENERAL", "CONFIRM"));

			if (people.get(i).getProfession().getStartingSkill() != Skill.NONE) {
				int[] permanent = new int[1];
				permanent[0] = people.get(i).getProfession().getStartingSkill().ordinal();
				skillSegmentedControl.setPermanent(permanent);
			} else {
				skillSegmentedControl.setPermanent(new int[0]);
			}
			
			List<Skill> currentSkills = people.get(i).getSkills();
			
			currentSkills.remove(Skill.NONE);
			int[] currentSkillIndices = new int[currentSkills.size()];
			for (int j = 0; j < currentSkillIndices.length; j++) {
				currentSkillIndices[j] = currentSkills.get(j).ordinal();
			}
			
			System.out.println(Arrays.toString(currentSkillIndices));
			skillSegmentedControl.setSelection(currentSkillIndices);
			currentPersonModifying = i;
			showModal(skillModal);
		}

		/**
		 * Person delete button pressed; address person data accordingly.
		 * @param i Index in array of buttons for button pressed
		 */
		public void personDeleteButtonActivated(int i){
			//Delete the last created person
			Logger.log("Deleting pending Person at index " + i, Logger.Level.INFO);
			clearPersonData(i);
			hidePersonColumn(i);
		}
		
		/**
		 * Confirm button pressed; check status of party and set up next scene if valid.
		 */
		public void confirmButtonActivated(){
			if (people.size() == 0) {
				Modal errorModal = new MessageModal(container,
						PartyCreationScene.this,
						ConstantStore.get("PARTY_CREATION_SCENE", "ERR_NO_MEMBERS"));
				showModal(errorModal);
				return;
			}
			
			for (Person person : people) {//iterate through party memebers
				if (person.getProfession() == null) {
					Modal errorModal = new MessageModal(container,
							PartyCreationScene.this,
							ConstantStore.get("PARTY_CREATION_SCENE", "ERR_INCOMPLETE_PROFESSIONS"));
					showModal(errorModal);
					Logger.log("Not all party members have professions selected", Logger.Level.INFO);
					return;
				}
			}
			
			//set initial game values
			Pace pace = Pace.values()[paceSegmentedControl.getSelection()[0]];
			Rations rations = Rations.values()[rationsSegmentedControl.getSelection()[0]];
		
			player.setParty(new Party(pace, rations, people.get(0), people, new Time()));
			
			Logger.log("Confirm button pushed", Logger.Level.INFO);
			GameDirector.sharedSceneListener().requestScene(SceneID.TOWN, PartyCreationScene.this, true);
		}
		
		@Override
		public void componentActivated(AbstractComponent source) {
			int retCode = 0;
			int i = 0;
			//for loop needed to be modified to a while to handle the retcode returned from personNameTextField
			//originally method code resided here and had a return in it which broke this loop.  now
			//that it has been broken out from here, the retCode needs to be able to break loop to maintain
			//old functionality
			while ((i < NUM_PEOPLE) && (retCode == 0)){
			//for (int i = 0; i < NUM_PEOPLE; i++) {
				if (source == newPersonButtons[i]) {
					newPersonButtonActivated(i);
				} else if (source == personGenderControl[i]) {
					personGenderControlActivated(i);
				} else if (source == personNameTextFields[i]) {
					retCode = personNameTextFieldActivated(i);
				} else if (source == personChangeProfessionButtons[i]) {
					personChangeProfButtonActivated(i);
				} else if (source == personChangeSkillButtons[i]) {
					personChangeSkillButtonActivated(i);
				} else if (source == personDeleteButtons[i]) {
					personDeleteButtonActivated(i);
				}
				i++;
			}
			
			if (retCode == 0){
				// PersonNameTextField had instance of forced return - bypass this code if return was forced
				enableNextPersonField();
			}
			
			if (source == confirmButton) {
				confirmButtonActivated();
			}
		}
	}
}
