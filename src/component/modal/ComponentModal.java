package component.modal;

import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.GUIContext;

import component.Component;
import component.Label;
import component.Panel;
import component.Positionable;
import core.ConstantStore;
import core.FontStore;

public class ComponentModal<T extends Component> extends Modal {
	private T component;

	/**
	 * Constructs a {@code Modal} with a listener, message, and component.
	 * @param context The GUI context
	 * @param listener The listener
	 * @param message The text for the message
	 * @param component Component to add
	 */
	public ComponentModal(GUIContext context, ModalListener listener, String message, int buttonCount, T component) {
		super(context, listener, message, buttonCount);
		
		this.component = component;
		
		setCancelButtonIndex(0);
		
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		this.messageLabel = new Label(container, DEFAULT_LABEL_WIDTH, fieldFont, Color.white, message);
		messageLabel.setAlignment(Label.Alignment.CENTER);
		
		int panelWidth = PADDING * 2 + Math.max(component.getWidth(), messageLabel.getWidth());
		int panelHeight = PADDING * 4 + messageLabel.getHeight() + component.getHeight() + BUTTON_HEIGHT;
		panel = new Panel(container, panelWidth, panelHeight, ConstantStore.COLORS.get("MODAL"));
		panel.setBorderColor(ConstantStore.COLORS.get("MODAL_BORDER"));
		panel.setBorderWidth(2);
		panel.setLocation((getWidth() - panel.getWidth()) / 2, (getHeight() - panel.getHeight() / 2));

		createButtons();
		
		panel.add(messageLabel, panel.getPosition(Positionable.ReferencePoint.TOPCENTER), Positionable.ReferencePoint.TOPCENTER, 0, PADDING);
		panel.add(component, messageLabel.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, PADDING);
		
		panel.addAsRow(Arrays.asList(buttons).iterator(),
				panel.getPosition(Positionable.ReferencePoint.BOTTOMLEFT),
				PADDING,
				-PADDING - BUTTON_HEIGHT,
				PADDING);
		
		add(panel, getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
	}
	
	
	/**
	 * Returns the component.
	 * @return The component
	 */
	public T getComponent() {
		return component;
	}
}
