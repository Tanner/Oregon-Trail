package component;

/**
 * Interface that can be implemented by classes that can be disabled.
 */
public interface Disableable {
	/**
	 * Return whether disabled.
	 * @return {@code true} if disabled, {@code false} if not
	 */
	public boolean isDisabled();
	
	/**
	 * Set whether this is disabled.
	 * @param disabled Set disabled if {@code true}, set enabled if {@code false}
	 */
	public void setDisabled(boolean disabled);
}
