package racoonman.racoongame.core.input;

import racoonman.racoongame.core.tick.TickContext;

public class InputTickContext implements TickContext {
	private double displayWidth;
	private double displayHeight;
	private long displayHandle;
	
	private InputTickContext(double displayWidth, double displayHeight, long displayHandle) {
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		this.displayHandle = displayHandle;
	}
	
	public double getDisplayWidth() {
		return this.displayWidth;
	}
	
	public double getDisplayHeight() {
		return this.displayHeight;
	}
	
	public long getHandle() {
		return this.displayHandle;
	}
	
	public static InputTickContext of(double displayWidth, double displayHeight, long displayHandle) {
		return new InputTickContext(displayWidth, displayHeight, displayHandle);
	}
}
