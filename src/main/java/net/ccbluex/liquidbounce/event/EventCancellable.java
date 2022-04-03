package net.ccbluex.liquidbounce.event;

/**
 * Abstract example implementation of the Cancellable interface.
 *
 * @author DarkMagician6
 * @since August 27, 2013
 */
public abstract class EventCancellable extends Event implements Cancellable {
	public static int a = 1;
	public boolean cancelled;

	protected EventCancellable() {
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean state) {
		cancelled = state;
	}

}
