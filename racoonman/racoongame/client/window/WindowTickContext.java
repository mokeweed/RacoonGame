package racoonman.racoongame.client.window;

import racoonman.racoongame.core.tick.TickContext;

public class WindowTickContext implements TickContext {
	private int fps;
	
	private WindowTickContext(int fps) {
		this.fps = fps;
	}
	
	public int getMaxFps() {
		return this.fps;
	}
	
	public static WindowTickContext of(int fps) {
		return new WindowTickContext(fps);
	}
}