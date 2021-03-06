package component;

import java.io.File;

import model.Condition;
import model.item.ItemType;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.sprite.Sprite;

import core.ConstantStore;

/**
 * A group consisting of a {@code CountingButton} and {@code ConditionBar}.
 * 
 * Both the {@code CountingButton} and the {@code ConditionBar} are linked to a specific item and item slot for an {@code Item}.
 */
public class SlotConditionGroup extends ComponentConditionGroup<Counter> {
	public static enum Mode {NORMAL, TRANSFER};
	private Mode currentMode;
	
	private ItemType item;
	
	/**
	 * Construct a {@code SlotConditionGroup} with a {@code GUIContext}, width, height, {@code Font}, and the pocket number.
	 * @param container The container for this {@code Component}
	 * @param width Width
	 * @param height Height
	 * @param font Font to use
	 * @param button Button to have
	 * @param pocketNumber The "pocket number" that the {@code Inventoried} has on it to use
	 */
	public SlotConditionGroup(GUIContext container, int width, int height, Font font, Counter button, Condition condition) {
		super(container, width, height, button, condition);
		
		button.addListener(new ButtonListener());
		
		setDisable(true);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(container, g);
	}
	
	/**
	 * Get the ItemType.
	 * @return ItemType
	 */
	public ItemType getItem() {
		return item;
	}
	
	/**
	 * Changes the contents of the group.
	 * @param item New ItemType
	 * @param name New name
	 * @param amount New quantity
	 * @param condition Condition to use
	 */
	public void changeContents(ItemType item, String name, int amount, Condition condition) {
		if (currentMode == Mode.TRANSFER) {
			return;
		}
		
		this.item = item;
		
		String itemImagePath = ConstantStore.PATH_ITEMS + item.toString().toLowerCase() + ".png";
		Sprite sprite = null;
		
		if (new File(itemImagePath).exists()) {
			try {
				sprite = new Sprite(container, 48, new Image(itemImagePath, false, Image.FILTER_NEAREST));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		
		component.setSprite(sprite);
		component.setText(name);
		if (sprite != null) {
			component.setShowSprite(true);
			component.setShowLabel(false);
		} else {
			component.setShowLabel(true);
			component.setShowSprite(false);
		}
		component.setCount(amount);
		component.setMax(amount);
		
		conditionBar.setCondition(condition);
		
		component.setTooltipEnabled(true);
		component.setTooltipMessage(name);
	}
	
	/**
	 * Get the current mode.
	 * @return Current mode
	 */
	public Mode getCurrentMode() {
		return currentMode;
	}

	/**
	 * Set the current mode.
	 * @param currentMode Current mode
	 */
	public void setCurrentMode(Mode currentMode) {
		this.currentMode = currentMode;
		
		if (currentMode == Mode.TRANSFER) {
			component.setText(ConstantStore.get("PARTY_INVENTORY_SCENE", "FREE"));
			component.setHideCount(true);
			component.setDisabled(false);
			
			component.setShowLabel(true);
			component.setShowSprite(false);
			
			conditionBar.setVisible(false);
		}
	}
	
	@Override
	public void setDisable(boolean disabled) {
		super.setDisable(disabled);
		
		if (disabled) {
			component.setText("None");
		}
		
		component.setShowLabel(disabled);
		component.setShowSprite(!disabled);
		component.setHideCount(disabled);
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent component) {
			notifyListeners();
		}
	}
}
