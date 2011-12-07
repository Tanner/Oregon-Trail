package scene;

import java.util.ArrayList;
import java.util.Random;

import model.Party;
import model.Person;
import model.Profession;
import model.Skill;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
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
import core.FontStore;
import core.GameDirector;

public class TavernScene extends Scene {
	
	public static final SceneID ID = SceneID.RIVER;
	
	private final int MAX_PARTY_SIZE = 4;
	
	private Person[] person;
	private Button[] personButton;
	private Party party;
	private ChoiceModal modal;
	
	
	public TavernScene(Party party) {
		person = new Person[MAX_PARTY_SIZE];
		personButton = new Button[MAX_PARTY_SIZE];
		for (int i = 0; i < person.length; i++) {
			person[i] = makeRandomPerson();
		}
		this.party = party;
	}
	

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		for (int i = 0; i < person.length; i++) {
			personButton[i] = new Button(container, 200, 40, new Label(container, 300, fieldFont, Color.white, person[i].getName()));
			mainLayer.add(personButton[i], mainLayer.getPosition(ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT, (220)*i + 20, -20);
			personButton[i].addListener(new ButtonListener(i));
		}
		//int maxNewMembers = MAX_PARTY_SIZE - party.getPartyMembers().size();
		//System.out.println(maxNewMembers);
		//String[] names = {"Billy Joe Bob", "John Henry Jingle", "Washington George", "New Partymember", "Place Holder Jr."};
		//hireBoard = new SegmentedControl(container, 300, 400, 5, 1, 10, false, maxNewMembers, names);
		//hireButton = new Button(container, 280, 40, new Label(container, 300, fieldFont, Color.white, "Hire!"));
		//mainLayer.add(hireBoard, mainLayer.getPosition(ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
		//mainLayer.add(hireButton, hireBoard.getPosition(ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER);
	}
	
	public String generateDetails(Person person) {
		StringBuilder sb = new StringBuilder();
		String skills = person.getSkills().toString();
		String profession = person.getProfession().name();
		profession = profession.charAt(0) + profession.substring(1).toLowerCase();
		skills = skills.substring(1, skills.length()-1);
		sb.append("Name: " + person.getName() + "\n");
		sb.append("Profession: " + profession + "\n");
		sb.append("Skills: " + skills);
		return sb.toString();
	}
	
	public Person makeRandomPerson() {
		Person person = new Person("Bob");
		Random random = new Random();
		ArrayList<Skill> personSkill = new ArrayList<Skill>();
		person.setProfession(Profession.values()[random.nextInt(Profession.values().length)]);

		int skillPoints = 0;
		
		// Randomly assign some skills
		Skill tempSkill = Skill.values()[random.nextInt(Skill.values().length)];
		while (tempSkill != Skill.NONE && personSkill.size() < 3 && (skillPoints + tempSkill.getCost()) < 120) {
			if (!personSkill.contains(tempSkill)) {
				personSkill.add(tempSkill);
				skillPoints += tempSkill.getCost();
			}
			
			tempSkill = Skill.values()[random.nextInt(Skill.values().length)];
		}
		
		for (Skill skill : personSkill) {
			person.addSkill(skill);
		}
		
		person.getInventory().addRandomItems();
		return person;
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
			modal = new ChoiceModal(container, TavernScene.this, generateDetails(person[ordinal]), 2);
			showModal(modal);
		}
	}
	
}
