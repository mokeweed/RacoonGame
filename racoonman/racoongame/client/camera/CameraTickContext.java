package racoonman.racoongame.client.camera;

import racoonman.racoongame.core.tick.TickContext;
import racoonman.racoongame.player.ClientPlayer;

public class CameraTickContext implements TickContext {
	private ClientPlayer player;
	
	private CameraTickContext(ClientPlayer player) {
		this.player = player;
	}
	
	public ClientPlayer getPlayer() {
		return this.player;
	}
	
	public static CameraTickContext of(ClientPlayer player) {
		return new CameraTickContext(player);
	}
}
