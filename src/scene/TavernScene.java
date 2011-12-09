package scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Party;
import model.Person;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Label;
import component.Panel;
import component.Positionable;
import component.Positionable.ReferencePoint;
import component.modal.ChoiceModal;
import component.modal.MessageModal;
import component.modal.Modal;
import component.sprite.Sprite;
import core.ConstantStore;
import core.FontStore;
import core.GameDirector;
import core.ImageStore;

/**
 * A scene to recruit new party members
 */
public class TavernScene extends Scene {
	public static final SceneID ID = SceneID.TAVERN;
	
	private final int PADDING = 20;
	private final int BUTTON_HEIGHT = 30;
	private final int BUTTON_WIDTH = 200;
	
	private final int MAX_PARTY_SIZE = 4;
	
	private Person[] person;
	private Button[] personButton;
	private Party party;
	private Modal modal, partyFullModal;
	
	private int chosenOne;
	
	private List<String> maleNames = new ArrayList<String>();
	private List<String> femaleNames = new ArrayList<String>();
	private Label tempLabel;
	private Button leaveButton;
	
	public TavernScene(Party party) {
		person = new Person[MAX_PARTY_SIZE];
		personButton = new Button[MAX_PARTY_SIZE];
		
		//Create some names for new party members
		maleNames.add("Alfred");
		maleNames.add("Bob");
		femaleNames.add("Carlotta");
		maleNames.add("David");
		femaleNames.add("Elizabeth");
		femaleNames.add("Francine");
		maleNames.add("Geoff");
		maleNames.add("Henry");
		femaleNames.add("Irene");

		
		for (int i = 0; i < person.length; i++) {
			String name = randomPersonName();
			person[i] = new Person(name);
			person[i].makeRandom();
			if(name.equals("Carlotta") || name.equals("Elizabeth") || name.equals("Francine") || name.equals("Irene")) {
				person[i].setIsMale(false);
			}
		}
		
		this.party = party;
	}
	

	/**
	 * Produces a random female or male name.
	 * @return A random first name
	 */
	private String randomPersonName() {
		Random random = new Random();
		String name;
		if(random.nextBoolean()) {
			name = maleNames.get(random.nextInt(maleNames.size()));
			maleNames.remove(name);
		} else {
			name = femaleNames.get(random.nextInt(femaleNames.size()));
			femaleNames.remove(name);
		}
		return name;
	}


	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		partyFullModal = new MessageModal(container, this, "Your party is full, so you can't recruit any more members!");
		
		for (int i = 0; i < person.length; i++) {
			personButton[i] = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, 300, fieldFont, Color.white, person[i].getName()));
			mainLayer.add(personButton[i], mainLayer.getPosition(ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.BOTTOMCENTER, (220)*(i - 2) + 110, -100);
			Image icon = person[i].isMale() ? ImageStore.get().getImage("HILLBILLY_LEFT") : ImageStore.get().getImage("MAIDEN_LEFT");
			mainLayer.add(new Sprite(container, icon.getWidth()*2, icon)
			, personButton[i].getPosition(ReferencePoint.TOPCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -10);
			personButton[i].addListener(new ButtonListener(i));
		}
		
		tempLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "LEAVE"));
		leaveButton = new Button(container, (container.getWidth() - PADDING * 4) / 4, BUTTON_HEIGHT, tempLabel);
		leaveButton.addListener(new ButtonListener(-1));
		mainLayer.add(leaveButton, mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, PADDING, -PADDING);

		backgroundLayer.add(new Panel(container, ImageStore.get().getImage("SALOON_BACKGROUND")));
		
	}
	
	/**
	 * Generate a detailed message describing a person to popup in a modal.
	 * @param person The person to generate a String from
	 * @return A String representation of a person's details
	 */
	public String generateDetails(Person person) {
		StringBuilder sb = new StringBuilder();
		String skills = person.getSkillsAsString();
		String profession = person.getProfession().name();
		profession = profession.charAt(0) + profession.substring(1).toLowerCase();
		sb.append("Name: " + person.getName() + "\n");
		sb.append("Profession: " + profession + "\n");
		sb.append("Skills: " + skills);
		return sb.toString();
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}

	private class ButtonListener implements ComponentListener {
		private int ordinal;
		
		public ButtonListener(int ordinal) {
			this.ordinal = ordinal;
		}
		
		public void componentActivated(AbstractComponent source) {
			if(source.equals(leaveButton)) {
				GameDirector.sharedSceneListener().sceneDidEnd(TavernScene.this);
			} else {
				if (party.getPartyMembers().size() == MAX_PARTY_SIZE)
					showModal(partyFullModal);
				else {
					chosenOne = ordinal;
					modal = new ChoiceModal(container, TavernScene.this, generateDetails(person[ordinal]), 2);
					modal.setButtonText(modal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
					modal.setButtonText(modal.getCancelButtonIndex() + 1, ConstantStore.get("GENERAL", "CONFIRM"));
					showModal(modal);
				}
			}
		}
	}
	
	@Override
	public void dismissModal(Modal modal, int button) {
		super.dismissModal(modal, button);
		if (modal == partyFullModal)
			return;
		if (button != modal.getCancelButtonIndex()) {
			party.addPartyMember(person[chosenOne]);
			personButton[chosenOne].setDisabled(true);
		}
	}
}
