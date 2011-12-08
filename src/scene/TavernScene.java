package scene;

import java.util.ArrayList;
import java.util.List;
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
	
	List<String> names = new ArrayList<String>();	
	
	public TavernScene(Party party) {
		person = new Person[MAX_PARTY_SIZE];
		personButton = new Button[MAX_PARTY_SIZE];
		
		names.add("Alice");
		names.add("Bob");
		names.add("Carlotta");
		names.add("David");
		names.add("Eli");
		names.add("Frank");
		names.add("Geoff");
		names.add("Henry");

		
		for (int i = 0; i < person.length; i++) {
			person[i] = new Person(randomPersonName());
			person[i].makeRandom();
		}
		this.party = party;
	}
	

	private String randomPersonName() {
		Random random = new Random();
		String name = names.get(random.nextInt(names.size()));
		names.remove(name);
		return name;
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
