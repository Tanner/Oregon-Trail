package scene;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.*;
import component.*;
import component.Positionable.ReferencePoint;
import core.*;

/**
 * A scene that displays an inventory, and allows the user to
 * purchase items.
 */
public class StoreScene extends Scene {
	
	public static final SceneID ID = SceneID.Store;
	private final int PADDING = 20;
	private final int BUTTON_WIDTH = 130;
	private final int BUTTON_HEIGHT = 90;
	
	private CountingButton[] storeInventory;
	private Button cancelButton, inventoryButton, buyButton;
	private Panel storeInventoryButtons;
	private Label[] itemDescription;
	String tempDescription = "This is an item description.\nIt is a good item, maybe a sonic screwdriver.\n\nYep.";
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		createComponents();
		
		storeInventoryButtons.addAsGrid(storeInventory, mainLayer.getPosition(ReferencePoint.TopLeft), 4, 4, 0, 0, PADDING, PADDING);
		
		mainLayer.add(storeInventoryButtons, mainLayer.getPosition(ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft, PADDING, PADDING);
		mainLayer.add(cancelButton, storeInventoryButtons.getPosition(ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		mainLayer.add(inventoryButton, storeInventoryButtons.getPosition(ReferencePoint.BottomRight),Positionable.ReferencePoint.TopRight, 0, PADDING);
		
		mainLayer.addAsColumn(itemDescription, storeInventoryButtons.getPosition(ReferencePoint.TopRight), PADDING, 0, PADDING);
		
		Vector2f buyPosition = new Vector2f((int)itemDescription[6].getPosition(ReferencePoint.BottomCenter).getX(),(int)inventoryButton.getPosition(ReferencePoint.TopRight).getY());
		mainLayer.add(buyButton, buyPosition, Positionable.ReferencePoint.TopCenter, 0, 0);
		
		
		backgroundLayer.add(new Panel(container, new Color(0x7094FF)));
	}
	
	/**
	 * Create all on-screen components for the scene.
	 */
	private void createComponents() {
		Label tempLabel;
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);
		UnicodeFont h2 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);
		
		//Create grid of store inventory buttons
		storeInventory = new CountingButton[16];
		for (int i = 0; i < storeInventory.length; i++) {
			tempLabel = new Label(container, fieldFont, Color.white, "Item " + i);
			storeInventory[i] = new CountingButton(container, BUTTON_WIDTH, BUTTON_HEIGHT, tempLabel);
		}
		storeInventoryButtons = new Panel(container, BUTTON_WIDTH * 4 + PADDING * 3, BUTTON_HEIGHT * 4 + PADDING * 3);
		
		//Create cancel & inventory buttons
		tempLabel = new Label(container, h2, Color.white, "Cancel");
		cancelButton = new Button(container, BUTTON_WIDTH*2,BUTTON_HEIGHT,tempLabel);
		tempLabel = new Label(container, h2, Color.white, "Inventory");
		inventoryButton = new Button(container, BUTTON_WIDTH*2,BUTTON_HEIGHT,tempLabel);
		
		//Create item description text labels
		itemDescription = new Label[7];
		int labelWidth = mainLayer.getWidth() - (int) storeInventoryButtons.getPosition(ReferencePoint.TopRight).getX() - PADDING*3;
		itemDescription[0] = new Label(container, labelWidth, fieldFont, Color.white, "Item Name");
		itemDescription[0].setAlignment(Label.Alignment.Center);
		itemDescription[1] = new Label(container, labelWidth, 173, fieldFont, Color.white, tempDescription);
		itemDescription[1].setVerticalAlignment(Label.VerticalAlignment.Top);
		itemDescription[2] = new Label(container, labelWidth, fieldFont, Color.white, "Weight: 10 lbs");
		itemDescription[2].setAlignment(Label.Alignment.Center);
		itemDescription[3] = new Label(container, labelWidth, fieldFont, Color.white, "Cost: $10.00");
		itemDescription[3].setAlignment(Label.Alignment.Center);
		itemDescription[4] = new Label(container, labelWidth, fieldFont, Color.white, "Quantity: 2");
		itemDescription[4].setAlignment(Label.Alignment.Center);
		itemDescription[5] = new Label(container, labelWidth, fieldFont, Color.white, "Total Weight: 20 lbs");
		itemDescription[5].setAlignment(Label.Alignment.Center);
		itemDescription[6] = new Label(container, labelWidth, fieldFont, Color.white, "Total Cost: $20.00");
		itemDescription[6].setAlignment(Label.Alignment.Center);
		
		//Create buy button
		tempLabel = new Label(container, h2, Color.white, "Buy");
		buyButton = new Button(container, BUTTON_WIDTH*2, BUTTON_HEIGHT, tempLabel);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}

	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			GameDirector.sharedSceneDelegate().sceneDidEnd(this);
		}
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
