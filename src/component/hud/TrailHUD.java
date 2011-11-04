package component.hud;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import model.datasource.HUDDataSource;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.Button;
import component.Component;
import component.Label;
import component.Panel;
import component.Component.BevelType;
import component.Label.VerticalAlignment;
import component.Positionable.ReferencePoint;
import component.sprite.Sprite;

import core.ConstantStore;
import core.FontStore;
import core.ImageStore;

/**
 * A HUD holds quick information for the player in {@code TrailScene}.
 */
public class TrailHUD extends HUD {
	public static enum Mode { TRAIL, CAMP };
	private static Mode currentMode;
	
	private static final int MARGIN = 10;
	
	private static final int BUTTON_HEIGHT = HEIGHT - (2 * MARGIN);
	
	private static final int INFO_WIDTH = 200;
	
	private Button menuButton;
	
	private Button inventoryButton;
	private Button mapButton;
	private Button huntButton;
	private Button leaveButton;
	
	private Label timeLabel;
	private Label dateLabel;
	private Label notificationLabel;
	
	private Panel trailPanel;
	private Panel campPanel;
	
	private Queue<String> notificationQueue;
	
	private HUDDataSource data;
	
	/**
	 * Constructs a HUD with a {@code GUIContext} and {@code HUDDataSource}.
	 * @param context Context
	 * @param data Data source to use
	 */
	public TrailHUD(GUIContext context, HUDDataSource data, Mode mode, ComponentListener listener) {
		super(context, context.getWidth(), HEIGHT);
		
		this.currentMode = mode;
		this.data = data;
		
		notificationQueue = new LinkedList<String>();
		
		int panelWidth = context.getWidth() - INFO_WIDTH - MARGIN * 2;
		
		campPanel = makeCampPanel(panelWidth, listener);
		trailPanel = makeTrailPanel(panelWidth, listener);
		
		add(campPanel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		add(trailPanel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		setMode(Mode.TRAIL);
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		timeLabel = new Label(context, INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		add(timeLabel, notificationLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.BOTTOMLEFT, MARGIN, - MARGIN / 2);
		
		dateLabel = new Label(context, INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, "");
		add(dateLabel, notificationLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.TOPLEFT, MARGIN, MARGIN / 2);
		
		setBackgroundColor(Color.gray);
		setBevelWidth(2);
		setBevel(Component.BevelType.OUT);
		setBottomBorderWidth(2);
		setBorderColor(Color.black);
	}
	
	public Panel makeTrailPanel(int width, ComponentListener listener) {
		Panel panel = new Panel(container, width, HEIGHT);
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		Label menuLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("TRAIL_SCENE", "CAMP"));
		
		Sprite fireSprite = new Sprite(container, 48, ImageStore.get().getImage("CAMP_ICON"));
		
		menuButton = new Button(container, menuLabel.getWidth() + (2 * MARGIN), BUTTON_HEIGHT, menuLabel);
		menuButton.setSprite(fireSprite);
		menuButton.setShowLabel(false);
		menuButton.addListener(listener);
		panel.add(menuButton, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, MARGIN, MARGIN);
		
		int notificationWidth = width - menuButton.getWidth() -  MARGIN * 2;
		
		notificationLabel = new Label(container, notificationWidth, BUTTON_HEIGHT, fieldFont, Color.white, "");
		notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
		notificationLabel.setBackgroundColor(Color.black);
		panel.add(notificationLabel, menuButton.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, MARGIN, 0);
		
		return panel;
	}
	
	public Panel makeCampPanel(int width, ComponentListener listener) {
		Panel panel = new Panel(container, width, HEIGHT);
		
		Button buttons[] = new Button[4];
		
		int buttonWidth = (container.getWidth() - MARGIN * 2 - MARGIN * (buttons.length - 1) - INFO_WIDTH - MARGIN) / buttons.length;
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);

		Label inventoryLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("TRAIL_SCENE", "INVENTORY"));
		inventoryButton = new Button(container, buttonWidth, BUTTON_HEIGHT, inventoryLabel);
		inventoryButton.addListener(listener);
		
		Label mapLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("TRAIL_SCENE", "MAP"));
		mapButton = new Button(container, buttonWidth, BUTTON_HEIGHT, mapLabel);
		mapButton.addListener(listener);
		
		Label huntLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("TRAIL_SCENE", "HUNT"));
		huntButton = new Button(container, buttonWidth, BUTTON_HEIGHT, huntLabel);
		huntButton.addListener(listener);
		
		Label leaveLabel = new Label(container, buttonWidth, fieldFont, Color.white, ConstantStore.get("TRAIL_SCENE", "LEAVE"));
		leaveButton = new Button(container, buttonWidth, BUTTON_HEIGHT, leaveLabel);
		leaveButton.addListener(listener);
				
		buttons[0] = leaveButton;
		buttons[1] = inventoryButton;
		buttons[2] = mapButton;
		buttons[3] = huntButton;
		
		panel.addAsRow(Arrays.asList(buttons).iterator(),
				panel.getPosition(ReferencePoint.TOPLEFT),
				MARGIN,
				MARGIN,
				MARGIN);
		
		return panel;
	}
	
	/**
	 * Update the party information on the screen (date and money).
	 */
	public void updatePartyInformation(String time, String date) {
		setTime(time);
		setDate(date);
	}
	
	/**
	 * Update the notification bar.
	 */
	public void updateNotifications() {
		if (!notificationQueue.isEmpty()) {
			setNotification(notificationQueue.remove());
		}
	}
	
	/**
	 * Set the date label's contents.
	 * @param time New string from date label
	 */
	public void setTime(String time) {
		timeLabel.setText(time);
	}
	
	/**
	 * Set the date label's contents.
	 * @param date Amount of money to display
	 */
	public void setDate(String date) {
		dateLabel.setText(date);
	}
	
	/**
	 * Adds a notification onto the queue.
	 * @param message New message to show
	 */
	public void addNotification(String message) {
		notificationQueue.add(message);
	}

	/**
	 * Adds a bunch of notification onto the queue.
	 * @param messages List of new messages to show
	 */
	public void addNotifications(List<String> messages) {
		for (String message : messages) {
			addNotification(message);
		}
	}
	
	public boolean isNotificationsEmpty() {
		return notificationQueue.isEmpty();
	}
	
	/**
	 * Set the notification for the notification bar.
	 * @param message New message to show
	 */
	private void setNotification(String message) {
		notificationLabel.setText(message);
	}
	
	/**
	 * Return the current mode.
	 * @return Current mode
	 */
	public Mode getMode() {
		return currentMode;
	}
	
	/**
	 * Set the current mode.
	 * @param mode New mode
	 */
	public void setMode(Mode mode) {
		currentMode = mode;
		
		if (currentMode == Mode.CAMP) {
			campPanel.setVisible(true);
			trailPanel.setVisible(false);
		} else if (currentMode == Mode.TRAIL) {
			campPanel.setVisible(false);
			trailPanel.setVisible(true);
		}
	}
	
	/**
	 * Get the menu button.
	 * @return Menu button
	 */
	public Button getMenuButton() {
		return menuButton;
	}
	
	/**
	 * Get the inventory button.
	 * @return Inventory button
	 */
	public Button getInventoryButton() {
		return inventoryButton;
	}
	
	/**
	 * Get the map button.
	 * @return Map button
	 */
	public Button getMapButton() {
		return mapButton;
	}
	
	/**
	 * Get the hunt button.
	 * @return Hunt button
	 */
	public Button getHuntButton() {
		return huntButton;
	}
	
	/**
	 * Get the leave button.
	 * @return Leave button
	 */
	public Button getLeaveButton() {
		return leaveButton;
	}
}
