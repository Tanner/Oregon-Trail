package scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Party;
import model.Person;
import model.Profession;
import model.Skill;

import org.newdawn.slick.Animation;
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
import component.Positionable;
import component.SegmentedControl;
import component.Positionable.ReferencePoint;
import component.modal.ChoiceModal;
import component.modal.Modal;
import component.sprite.Sprite;
import core.ConstantStore;
import core.FontStore;
import core.GameDirector;
import core.ImageStore;

public class TavernScene extends Scene {
	
	public static final SceneID ID = SceneID.RIVER;
	
	private final int MAX_PARTY_SIZE = 4;
	
	private Person[] person;
	private Button[] personButton;
	private Party party;
	private ChoiceModal modal;
	
	private Person chosenOne;
	
	private List<String> maleNames = new ArrayList<String>();
	private List<String> femaleNames = new ArrayList<String>();
	private Label tempLabel;
	private Button leaveButton;
	
	public TavernScene(Party party) {
		person = new Person[MAX_PARTY_SIZE];
		personButton = new Button[MAX_PARTY_SIZE];
		
		maleNames.add("Alfred");
		maleNames.add("Bob");
		femaleNames.add("Carlotta");
		maleNames.add("David");
		femaleNames.add("Elizabeth");
		femaleNames.add("Francine");
		maleNames.add("Geoff");
		maleNames.add("Henry");

		
		for (int i = 0; i < person.length; i++) {
			String name = randomPersonName();
			person[i] = new Person(name);
			person[i].makeRandom();
			if(name.equals("Carlotta") || name.equals("Elizabeth") || name.equals("Francine")) {
				person[i].setIsMale(false);
			}
		}
		this.party = party;
	}
	

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

		for (int i = 0; i < person.length; i++) {
			personButton[i] = new Button(container, 200, 40, new Label(container, 300, fieldFont, Color.white, person[i].getName()));
			mainLayer.add(personButton[i], mainLayer.getPosition(ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER, (220)*(i - 2) + 110, -20);
			Image icon = person[i].isMale() ? ImageStore.get().getImage("HILLBILLY_RIGHT") : ImageStore.get().getImage("MAIDEN_RIGHT");
			mainLayer.add(new Sprite(container, 48, icon)
			, personButton[i].getPosition(ReferencePoint.TOPCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -10);
			personButton[i].addListener(new ButtonListener(i));
		}
		
		tempLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "LEAVE"));
		leaveButton = new Button(container, (container.getWidth()) / 4, 30, tempLabel);
		leaveButton.addListener(new ButtonListener(-1));
		mainLayer.add(leaveButton, mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMLEFT, 10, -20);
		
	}
	
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
				chosenOne = person[ordinal];
				modal = new ChoiceModal(container, TavernScene.this, generateDetails(person[ordinal]), 2);
				modal.setButtonText(modal.getCancelButtonIndex(), ConstantStore.get("GENERAL", "CANCEL"));
				modal.setButtonText(modal.getCancelButtonIndex() + 1, ConstantStore.get("GENERAL", "CONFIRM"));
				showModal(modal);
			}
		}
	}
	
	@Override
	public void dismissModal(Modal modal, int button) {
		super.dismissModal(modal, button);
		if (button != modal.getCancelButtonIndex()) {
			party.addPartyMember(chosenOne);
		}
	}
}
