package component.hud;

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
import component.sprite.Sprite;

import core.ConstantStore;
import core.FontStore;
import core.ImageStore;

/**
 * A HUD holds quick information for the player in {@code TownScene}.
 */
public class TownHUD extends HUD {
	private static final int MARGIN = 10;
	
	private static final int BUTTON_HEIGHT = HEIGHT - (2 * MARGIN);
	
	private static final int INFO_WIDTH = 200;
	
	private Button trailButton;
	
	private Label timeLabel;
	private Label dateLabel;
	private Label notificationLabel;
	
	private Queue<String> notificationQueue;
	
	/**
	 * Constructs a HUD with a {@code GUIContext} and {@code HUDDataSource}.
	 * @param context Context
	 */
	public TownHUD(GUIContext context, ComponentListener listener) {
		super(context, context.getWidth(), HEIGHT);
		
		int panelWidth = context.getWidth() - INFO_WIDTH - MARGIN * 2;
		
		Panel panel = new Panel(container, panelWidth, HEIGHT);
	
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		Label menuLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("TOWN_SCENE", "TRAIL"));
		
		Sprite fireSprite = new Sprite(container, 48, ImageStore.get().getImage("CAMP_ICON"));
		
		trailButton = new Button(container, menuLabel.getWidth() + (2 * MARGIN), BUTTON_HEIGHT, menuLabel);
		trailButton.setSprite(fireSprite);
		trailButton.setShowLabel(false);
		trailButton.addListener(listener);
		panel.add(trailButton, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, MARGIN, MARGIN);
		
		int notificationWidth = panelWidth - trailButton.getWidth() -  MARGIN * 2;
		
		notificationLabel = new Label(container, notificationWidth, BUTTON_HEIGHT, fieldFont, Color.white, "");
		notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
		notificationLabel.setBackgroundColor(Color.black);
		panel.add(notificationLabel, trailButton.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, MARGIN, 0);
		
		add(panel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
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
	
	/**
	 * Update the party information on the screen (date and money).
	 */
	public void updatePartyInformation(String time, String date) {
		setTime(time);
		setDate(date);
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
	
	public boolean isNotificationsEmpty() {
		return notificationQueue.isEmpty();
	}
	
	/**
	 * Set the notification for the notification bar.
	 * @param message New message to show
	 */
	public void setNotification(String message) {
		notificationLabel.setText(message);
	}
	
	/**
	 * Get the trail button.
	 * @return Trail button
	 */
	public Button getTrailButton() {
		return trailButton;
	}
}
