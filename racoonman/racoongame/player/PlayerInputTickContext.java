package racoonman.racoongame.player;

import racoonman.racoongame.core.tick.TickContext;

public class PlayerInputTickContext implements TickContext {
	private float deltaX;
	private float deltaZ;
	
	private PlayerInputTickContext(float deltaX, float deltaZ) {
		this.deltaX = deltaX;
		this.deltaZ = deltaZ;
	}
	
	public float getXMouseDelta() {
		return this.deltaX;
	}
	
	public float getZMouseDelta() {
		return this.deltaZ;
	}
	
	public static PlayerInputTickContext of(float deltaX, float deltaZ) {
		return new PlayerInputTickContext(deltaX, deltaZ);
	}
}
