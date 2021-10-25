package racoonman.racoongame.client.renderer;

import racoonman.racoongame.client.camera.Camera;
import racoonman.racoongame.core.tick.TickContext;
import racoonman.racoongame.level.Level;
import racoonman.racoongame.player.ClientPlayer;

public class GameRenderTickContext implements TickContext {
	private RenderOptions options;
	private ClientPlayer player;
	private Level level;
	private Camera camera;
	
	private GameRenderTickContext(RenderOptions options, ClientPlayer player, Level level, Camera camera) {
		this.options = options;
		this.player = player;
		this.level = level;
		this.camera = camera;
	}
	
	public RenderOptions getRenderOptions() {
		return this.options;
	}
	
	public ClientPlayer getPlayer() {
		return this.player;
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public static GameRenderTickContext of(RenderOptions options, ClientPlayer player, Level level, Camera camera) {
		return new GameRenderTickContext(options, player, level, camera);
	}
}
