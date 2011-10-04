package model;

import java.util.ArrayList;
import java.util.Random;

import model.Party.Pace;
import model.Party.Rations;
import core.Logger;

/**
 * Holds all game data
 * 
 * @author George Johnston
 */
public class Game {
	private Player player;
	
	/**
	 * Know nothing, make nothing.
	 */
	public Game() {
		Random r = new Random();
		ArrayList<Person.Skill> person1Skill = new ArrayList<Person.Skill>();
		
		Person person1 = new Person("Alice");
		person1.setProfession(Person.Profession.values()[r.nextInt(Person.Profession.values().length)]);
		person1.setProfession(Person.Profession.values()[r.nextInt(Person.Profession.values().length)]);
		person1.setProfession(Person.Profession.values()[r.nextInt(Person.Profession.values().length)]);
		int skillPoints = 0;
		for (Person.Skill tempSkill = Person.Skill.values()[r.nextInt(Person.Skill.values().length)];
			tempSkill != Person.Skill.NONE && person1Skill.size() < 3 && (skillPoints + tempSkill.getCost()) < 120; 
			tempSkill = Person.Skill.values()[r.nextInt(Person.Skill.values().length)]) {
			if(!person1Skill.contains(tempSkill)){
				person1Skill.add(tempSkill);
				skillPoints += tempSkill.getCost();
			}
		}
		for(Person.Skill skill : person1Skill) {
			person1.addNewSkill(skill);
		}
		Logger.log("First set of skills", Logger.Level.INFO);
		person1Skill.clear();
		
		for (Person.Skill tempSkill = Person.Skill.values()[r.nextInt(Person.Skill.values().length)];
		tempSkill != Person.Skill.NONE && person1Skill.size() < 3 && (skillPoints + tempSkill.getCost()) < 120; 
		tempSkill = Person.Skill.values()[r.nextInt(Person.Skill.values().length)]) {
			if(!person1Skill.contains(tempSkill)){
				person1Skill.add(tempSkill);
				skillPoints += tempSkill.getCost();
			}
		}
		for(Person.Skill skill : person1Skill) {
			person1.addNewSkill(skill);
		}
		
		
		this.player = new Player(new Party(Pace.GRUELING, Rations.BAREBONES, person1/*, person2*/));
		if(player != null){
			Logger.log("Player was created successfully", Logger.Level.INFO);
		}
	}
	
	/**
	 * For use by GameDirector.  Returns player
	 * @return The player game currently knows.
	 */
	public Player getPlayer() {
		return this.player;
	}
}