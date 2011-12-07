package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersonTest {
	@Test
	public void setProfessionFromNull() {
		Person albert = new Person("Albert");
		assertTrue(albert.getProfession() == null);
		albert.setProfession(Profession.BAKER);
		assertTrue(albert.getProfession() == Profession.BAKER);
	}

	@Test
	public void professionWithStartingItem() {
		Person albert = new Person("Albert");
		assertTrue(albert.getProfession() == null);
		albert.setProfession(Profession.BAKER);
		assertTrue(albert.getProfession() == Profession.BAKER);
		assertTrue(albert.removeItemFromInventory(albert.getProfession().getStartingItem(), 1).get(0).getType() == albert.getProfession().getStartingItem());
	}
	
	@Test
	public void changeToSameProfession() {
		Person albert = new Person("Albert");
		assertTrue(albert.getProfession() == null);
		albert.setProfession(Profession.BAKER);
		assertTrue(albert.getProfession() == Profession.BAKER);
		albert.setProfession(Profession.BAKER);
		assertTrue(albert.getProfession() == Profession.BAKER);
	}
	
	@Test
	public void setProfessionWithoutStartingItem() {
		Person albert = new Person("Albert");
		assertTrue(albert.getProfession() == null);
		albert.setProfession(Profession.ARTIST);
		assertTrue(albert.getInventory().getPopulatedSlots().isEmpty());
	}
	
	@Test
	public void changeToProfessionWithoutItemFromExistingWithItem() {
		Person albert = new Person("Albert");
		albert.setProfession(Profession.BAKER);
		assertTrue(albert.removeItemFromInventory(albert.getProfession().getStartingItem(), 1).get(0).getType() == albert.getProfession().getStartingItem());
		albert.setProfession(Profession.BAKER);
		albert.setProfession(Profession.ARTIST);
		assertTrue(albert.getInventory().getPopulatedSlots().isEmpty());
		assertTrue(albert.getProfession()== Profession.ARTIST);
	}
	
	@Test
	public void changeToProfessionWithItemFromExistingWithoutItem() {
		Person albert = new Person("Albert");
		albert.setProfession(Profession.ARTIST);
		assertTrue(albert.getInventory().getPopulatedSlots().isEmpty());
		albert.setProfession(Profession.BAKER);
		assertTrue(albert.getProfession()== Profession.BAKER);
		assertTrue(albert.removeItemFromInventory(albert.getProfession().getStartingItem(), 1).get(0).getType() == albert.getProfession().getStartingItem());
	}
	
	@Test
	public void changeToProfessionWithoutItemFromExistingWithoutItem() {
		Person albert = new Person("Albert");
		albert.setProfession(Profession.ARTIST);
		assertTrue(albert.getInventory().getPopulatedSlots().isEmpty());
		albert.setProfession(Profession.PASTOR);
		assertTrue(albert.getProfession()== Profession.PASTOR);
		assertTrue(albert.getInventory().getPopulatedSlots().isEmpty());
	}
	
	@Test
	public void changeToProfessionWithItemFromExistingWithItem() {
		Person albert = new Person("Albert");
		albert.setProfession(Profession.WHEELWRIGHT);
		assertTrue(albert.removeItemFromInventory(albert.getProfession().getStartingItem(), 1).get(0).getType() == albert.getProfession().getStartingItem());
		albert.setProfession(Profession.BAKER);
		assertTrue(albert.getProfession()== Profession.BAKER);
		assertTrue(albert.removeItemFromInventory(albert.getProfession().getStartingItem(), 1).get(0).getType() == albert.getProfession().getStartingItem());
	}
}
