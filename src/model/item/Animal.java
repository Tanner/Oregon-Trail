package model.item;

import java.util.ArrayList;
import java.util.List;

import model.Item;

public class Animal extends Item {

	private int moveFactor;
	
	public Animal(ITEM_TYPE type) {
		super(type);
		this.moveFactor = 2;
	}
	
	public List<Item> killForFood() {
		int numberOf = (int) (this.getWeight() / ITEM_TYPE.MEAT.getWeight());
		List<Item> itemList = new ArrayList<Item>();
		for(int i = 0; i < numberOf/2; i++) {
			itemList.add(new Item(ITEM_TYPE.MEAT));
		}		
		return itemList;
	}
	
	public int getMoveFactor() {
		return moveFactor;
	}
}
