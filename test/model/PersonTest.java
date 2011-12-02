package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersonTest {
	@Test
	public void setProfessionFromNull() {
		Person alice = new Person("Alice");
		assertTrue(alice.getProfession() == null);
		alice.setProfession(Profession.BAKER);
		assertTrue(alice.getProfession() == Profession.BAKER);
	}

	@Test
	public void professionWithStartingItem() {
		Person alice = new Person("Alice");
		assertTrue(alice.getProfession() == null);
		alice.setProfession(Profession.BAKER);
		assertTrue(alice.getProfession() == Profession.BAKER);
		assertTrue(alice.removeItemFromInventory(alice.getProfession().getStartingItem(), 1).get(0).getType() == alice.getProfession().getStartingItem());
	}
	
	@Test
	public void changeToSameProfession() {
		Person alice = new Person("Alice");
		assertTrue(alice.getProfession() == null);
		alice.setProfession(Profession.BAKER);
		assertTrue(alice.getProfession() == Profession.BAKER);
		alice.setProfession(Profession.BAKER);
		assertTrue(alice.getProfession() == Profession.BAKER);
	}
	
	@Test
	public void setProfessionWithoutStartingItem() {
		Person alice = new Person("Alice");
		assertTrue(alice.getProfession() == null);
		alice.setProfession(Profession.ARTIST);
		assertTrue(alice.getInventory().getPopulatedSlots().isEmpty());
	}
	
	@Test
	public void changeToProfessionWithoutItemFromExistingWithItem() {
		Person alice = new Person("Alice");
		alice.setProfession(Profession.BAKER);
		assertTrue(alice.removeItemFromInventory(alice.getProfession().getStartingItem(), 1).get(0).getType() == alice.getProfession().getStartingItem());
		alice.setProfession(Profession.BAKER);
		alice.setProfession(Profession.ARTIST);
		assertTrue(alice.getInventory().getPopulatedSlots().isEmpty());
		assertTrue(alice.getProfession()== Profession.ARTIST);
	}
	
	@Test
	public void changeToProfessionWithItemFromExistingWithoutItem() {
		Person alice = new Person("Alice");
		alice.setProfession(Profession.ARTIST);
		assertTrue(alice.getInventory().getPopulatedSlots().isEmpty());
		alice.setProfession(Profession.BAKER);
		assertTrue(alice.getProfession()== Profession.BAKER);
		assertTrue(alice.removeItemFromInventory(alice.getProfession().getStartingItem(), 1).get(0).getType() == alice.getProfession().getStartingItem());
	}
	
	@Test
	public void changeToProfessionWithoutItemFromExistingWithoutItem() {
		Person alice = new Person("Alice");
		alice.setProfession(Profession.ARTIST);
		assertTrue(alice.getInventory().getPopulatedSlots().isEmpty());
		alice.setProfession(Profession.PASTOR);
		assertTrue(alice.getProfession()== Profession.PASTOR);
		assertTrue(alice.getInventory().getPopulatedSlots().isEmpty());
	}
	
	@Test
	public void changeToProfessionWithItemFromExistingWithItem() {
		Person alice = new Person("Alice");
		alice.setProfession(Profession.WHEELWRIGHT);
		assertTrue(alice.removeItemFromInventory(alice.getProfession().getStartingItem(), 1).get(0).getType() == alice.getProfession().getStartingItem());
		alice.setProfession(Profession.BAKER);
		assertTrue(alice.getProfession()== Profession.BAKER);
		assertTrue(alice.removeItemFromInventory(alice.getProfession().getStartingItem(), 1).get(0).getType() == alice.getProfession().getStartingItem());
	}
}
