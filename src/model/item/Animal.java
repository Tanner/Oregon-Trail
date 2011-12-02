package model.item;

import java.util.ArrayList;
import java.util.List;

//import component.PartyMemberDataSource;
import core.ConstantStore;

import model.Conditioned;
import model.Item;

@SuppressWarnings("serial")
public class Animal extends Item implements Conditioned {
	private double moveFactor;
	
	private boolean dead;
	
	public Animal(ItemType type) {
		super(type);
		this.moveFactor = Double.parseDouble(ConstantStore.get("ITEMS", type + "_MOVE_FACTOR"));
	}
	
	public List<Item> killForFood() {
		int numberOf = (int) (this.getWeight() / ItemType.MEAT.getWeight());
		List<Item> itemList = new ArrayList<Item>();
		for(int i = 0; i < (numberOf / 10); i++) {
			itemList.add(new Item(ItemType.MEAT));
		}
		return itemList;
	}
	
	public double getMoveFactor() {
		return moveFactor;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

//	@Override
	public boolean isDead() {
		return dead;
	}
}
