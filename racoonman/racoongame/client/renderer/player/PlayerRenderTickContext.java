package racoonman.racoongame.client.renderer.player;

import org.joml.Matrix4f;

import racoonman.racoongame.client.camera.Camera;
import racoonman.racoongame.client.renderer.RenderOptions;
import racoonman.racoongame.client.renderer.RenderTickContext;
import racoonman.racoongame.client.renderer.core.Transformation;
import racoonman.racoongame.client.window.Window;
import racoonman.racoongame.player.ClientPlayer;

public class PlayerRenderTickContext extends RenderTickContext {
	private ClientPlayer player;
	
	private PlayerRenderTickContext(RenderOptions options, ClientPlayer player, Camera camera, Matrix4f projectionMatrix, Matrix4f viewMatrix, Transformation transformation, Window display) {
		super(options, camera, projectionMatrix, viewMatrix, transformation, display);
		this.player = player;
	}
	
	public ClientPlayer getPlayer() {
		return this.player;
	}
	
	public static PlayerRenderTickContext of(ClientPlayer player, RenderTickContext ctx) {
		return new PlayerRenderTickContext(ctx.getRenderOptions(), player, ctx.getCamera(), ctx.getProjectionMatrix(), ctx.getViewMatrix(), ctx.getTransformation(), ctx.getDisplay());
	}
}
