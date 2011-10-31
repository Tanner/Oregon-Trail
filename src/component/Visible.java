package component;

public interface Visible {
	/**
	 * Return if visible and parent component(s) are visible.
	 * @return {@code true} if visible, {@code false} is not
	 */
	public boolean isVisible();
	
	/**
	 * Set this component to be visible or invisible.
	 * @param visible Enables visibility if {@code true}, disables if {@code false}
	 */
	public void setVisible(boolean visible);
}
