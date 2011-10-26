package component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import model.datasource.HUDDataSource;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.Label.VerticalAlignment;

import core.ConstantStore;
import core.FontManager;
import core.GameDirector;

/**
 * A HUD holds quick information for the player.
 */
public class HUD extends Component {
	private static final int MARGIN = 10;
	private static final int HEIGHT = 70;
	
	private static final int INFO_WIDTH = 200;
	
	private Button menuButton;
	private Label dateLabel;
	private Label moneyLabel;
	private Label notificationLabel;
	
	private Queue<String> notificationQueue;
	
	private HUDDataSource data;
	
	/**
	 * Constructs a HUD with a {@code GUIContext} and {@code HUDDataSource}.
	 * @param context Context
	 * @param data Data source to use
	 */
	public HUD(GUIContext context, HUDDataSource data, ComponentListener listener) {
		super(context, context.getWidth(), HEIGHT);
		
		this.data = data;
		
		notificationQueue = new LinkedList<String>();
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		
		int height = HEIGHT - (2 * MARGIN);
		
		Label menuLabel = new Label(context, fieldFont, Color.white, ConstantStore.get("HUD_SCENE", "MENU"));
		menuButton = new Button(context, menuLabel.getWidth() + (2 * MARGIN), height, menuLabel);
		menuButton.addListener(listener);
		add(menuButton, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, MARGIN, MARGIN);
		
		dateLabel = new Label(context, INFO_WIDTH, fieldFont, Color.white, "");
		add(dateLabel, getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPRIGHT, -MARGIN, MARGIN);
		
		moneyLabel = new Label(context, INFO_WIDTH, fieldFont, Color.white, "");
		add(moneyLabel, getPosition(ReferencePoint.BOTTOMRIGHT), ReferencePoint.BOTTOMRIGHT, -MARGIN, -MARGIN);
		
		int notificationWidth = context.getWidth() - menuButton.getWidth() - INFO_WIDTH -  MARGIN * 4;
		
		notificationLabel = new Label(context, notificationWidth, height, fieldFont, Color.white, "");
		notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
		notificationLabel.setBackgroundColor(Color.black);
		add(notificationLabel, menuButton.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, MARGIN, 0);
		
		setBackgroundColor(Color.gray);
		setBevelWidth(2);
		setBevel(Component.BevelType.OUT);
		setBottomBorderWidth(2);
		setBorderColor(Color.black);
		
		updatePartyInformation();
	}
	
	/**
	 * Update the party information on the screen (date and money).
	 */
	public void updatePartyInformation() {
		setDate("Fake Date");
		setMoney(data.getMoney());
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
	 * @param date New string from date label
	 */
	public void setDate(String date) {
		dateLabel.setText(date);
	}
	
	/**
	 * Set the money label's contents.
	 * @param money Amount of money to display
	 */
	public void setMoney(int money) {
		moneyLabel.setText(ConstantStore.get("GENERAL", "MONEY_SYMBOL") + String.format("%,d", money));
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
}
