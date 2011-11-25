
package component.hud;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.Button;
import component.Component;
import component.Label;
import component.Panel;
import component.Label.VerticalAlignment;
import component.Positionable.ReferencePoint;
import component.hud.TrailHUD.Mode;
import component.sprite.Sprite;

import core.ConstantStore;
import core.FontStore;
import core.ImageStore;


/**
 * the hud for the hunt scene
 */
public class HuntHUD extends HUD {
	private static final int MARGIN = 10;
	
	private static final int BUTTON_HEIGHT = HEIGHT - (2 * MARGIN);
	
	private static final int INFO_WIDTH = 200;
	
	private Button menuButton;
	
	private Button inventoryButton;
	private Button campButton;
	
	private Label timeLabel;
	private Label dateLabel;
	private Label notificationLabel;
	
	private Panel huntPanel;
	
	private Queue<String> notificationQueue;

	/**
	 * 
	 * @param context
	 * @param listener
	 */
	public HuntHUD(GUIContext context, ComponentListener listener) {
		super(context, context.getWidth(), HEIGHT);

		notificationQueue = new LinkedList<String>();
		
		int panelWidth = context.getWidth() - INFO_WIDTH - MARGIN * 2;
		
		huntPanel = makeHuntPanel(panelWidth, listener);
		
		add(huntPanel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
	
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
	/*	
		timeLabel = new Label(context, INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		add(timeLabel, notificationLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.BOTTOMLEFT, MARGIN, - MARGIN / 2);
		
		dateLabel = new Label(context, INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		add(dateLabel, notificationLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.TOPLEFT, MARGIN, MARGIN / 2);
	*/	
		setBackgroundColor(Color.gray);
		setBevelWidth(2);
		setBevel(Component.BevelType.OUT);
		setBottomBorderWidth(2);
		setBorderColor(Color.black);
	}

	public Panel makeHuntPanel(int width, ComponentListener listener) {
		Panel panel = new Panel(container, width, HEIGHT);
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		Label menuLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("HUNT_SCENE", "CAMP"));
		
		Sprite fireSprite = new Sprite(container, 48, ImageStore.get().getImage("CAMP_ICON"));
/**		
		menuButton = new Button(container, menuLabel.getWidth() + (2 * MARGIN), BUTTON_HEIGHT, menuLabel);
		menuButton.setSprite(fireSprite);
		menuButton.setShowLabel(false);
		menuButton.addListener(listener);
		panel.add(menuButton, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, MARGIN, MARGIN);
		
		menuButton.setTooltipEnabled(true);
		menuButton.setTooltipMessage(ConstantStore.get("TRAIL_SCENE", "CAMP"));
		
		int notificationWidth = width - menuButton.getWidth() -  MARGIN * 2;
		
		notificationLabel = new Label(container, notificationWidth, BUTTON_HEIGHT, fieldFont, Color.white, "");
		notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
		notificationLabel.setBackgroundColor(Color.black);
		panel.add(notificationLabel, menuButton.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, MARGIN, 0);
	*/
		
		Button buttons[] = new Button[2];

		int buttonWidth = (container.getWidth() - MARGIN * 2 - MARGIN * (buttons.length - 1) - INFO_WIDTH - MARGIN) / buttons.length;
		
		Label inventoryLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("HUNT_SCENE", "INVENTORY"));
		inventoryButton = new Button(container, buttonWidth, BUTTON_HEIGHT, inventoryLabel);
		inventoryButton.addListener(listener);
				
		Label leaveLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("HUNT_SCENE", "CAMP"));
		campButton = new Button(container, buttonWidth, BUTTON_HEIGHT, leaveLabel);
		campButton.addListener(listener);
				
		buttons[0] = campButton;
		buttons[1] = inventoryButton;
		
		panel.addAsRow(Arrays.asList(buttons).iterator(),
				panel.getPosition(ReferencePoint.TOPLEFT),
				MARGIN,
				MARGIN,
				MARGIN);
		

		return panel;
	} 
	
	/**
	 * Get the map button.
	 * @return Map button
	 */
	public Button getCampButton() {
		return campButton;
	}
	/**
	 * Get the map button.
	 * @return Map button
	 */
	public Button getInventoryButton() {
		return inventoryButton;
	}
}
