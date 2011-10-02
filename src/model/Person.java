package model;

import java.util.ArrayList;

public class Person {
	
	private int skillPoints = 120;
	private ArrayList<Skill> skills = new ArrayList<Skill>();
	private String name;
	
	Person(Profession profession, String name, Skill ... skills){
		
		this.name = name;
		
		for (Skill skill: skills){
			if(!this.skills.contains(skill)){
				this.skills.add(skill);
				this.skillPoints -= skill.getCost();
			}
		}
		
		if(!this.skills.contains(profession.getStartingSkill())){
			this.skills.add(profession.getStartingSkill());
			this.skillPoints -= profession.getStartingSkill().getCost();
		}
	}
	
	public int getSkillPoints(){
		return skillPoints;
	}
	
	public ArrayList<Skill> getSkills(){
		return skills;
	}
	
	public String getName(){
		return name;
	}
	
	public enum Skill {
		MEDICAL (50),
		RIVERWORK (50),
		SHARPSHOOTING (50),
		BLACKSMITHING (40),
		CARPENTRY (40),
		FARMING (40),
		TRACKING (30),
		BOTANY (20),
		COMMERCE (20),
		COOKING (20),
		MUSICAL (10),
		SEWING (10),
		SPANISH (10),
		NONE (0);
		
		private final int cost;
		Skill(int cost){
			this.cost = cost;
		}
		
		public int getCost(){
			return cost;
		}
	}
	
	public enum Profession {
		BANKER (1, Skill.COMMERCE),
		DOCTOR (1.2f, Skill.MEDICAL),
		MERCHANT (1.4f, Skill.COMMERCE),
		PHARMACIST (1.6f, Skill.MEDICAL),
		WAINWRIGHT (1.8f, Skill.CARPENTRY),
		GUNSMITH (2, Skill.SHARPSHOOTING),
		BLACKSMITH (2.2f, Skill.BLACKSMITHING),
		MASON (2.4f, Skill.CARPENTRY),
		WHEELWRIGHT (2.6f, Skill.CARPENTRY),
		CARPENTER (2.8f, Skill.CARPENTRY),
		SADDLEMAKER (3, Skill.FARMING),
		BRICKMAKER (3.2f, Skill.CARPENTRY),
		PROSPECTOR (3.4f, Skill.RIVERWORK),
		TRAPPER (3.6f, Skill.TRACKING),
		SURVEYOR (3.8f, Skill.RIVERWORK),
		SHOEMAKER (4, Skill.SEWING),
		JOURNALIST (4.1f, Skill.NONE),
		PRINTER (4.2f, Skill.COMMERCE),
		BUTCHER (4.3f, Skill.COOKING),
		BAKER (4.4f, Skill.COOKING),
		TAILOR (4.5f, Skill.SEWING),
		FARMER (4.5f, Skill.FARMING),
		PASTOR (4.6f, Skill.NONE),
		ARTIST (4.8f, Skill.MUSICAL),
		TEACHER (5, Skill.NONE);
		
		private final float moneyMultiplier;
		private final Skill startingSkill;
		
		Profession(float moneyMultiplier, Skill startSkill){
			this.moneyMultiplier = moneyMultiplier;
			this.startingSkill = startSkill;
		}
		
		public float getMoneyMultiplier(){
			return moneyMultiplier;
		}
		
		public Skill getStartingSkill(){
			return startingSkill;
		}
	}
}
