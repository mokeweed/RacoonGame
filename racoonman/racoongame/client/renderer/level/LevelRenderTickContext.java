package racoonman.racoongame.client.renderer.level;

import java.util.Set;

import org.joml.Matrix4f;

import racoonman.racoongame.client.camera.Camera;
import racoonman.racoongame.client.renderer.RenderOptions;
import racoonman.racoongame.client.renderer.RenderTickContext;
import racoonman.racoongame.client.renderer.core.Transformation;
import racoonman.racoongame.client.renderer.level.TileRenderer.Face;
import racoonman.racoongame.client.window.Window;
import racoonman.racoongame.core.tick.TickContext;
import racoonman.racoongame.level.Level;

public class LevelRenderTickContext extends RenderTickContext implements TickContext {
	private Level level;
	private Set<Face> visibleFaces;
	
	private LevelRenderTickContext(Set<Face> visibleFaces, RenderOptions options, Level level, Camera camera,  Matrix4f projectionMatrix, Matrix4f viewMatrix, Transformation transformation, Window display) {
		super(options, camera, projectionMatrix, viewMatrix, transformation, display);
		this.visibleFaces = visibleFaces;
		this.level = level;
	}
	
	public Set<Face> getVisibleFaces() {
		return this.visibleFaces;
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public static LevelRenderTickContext of(Set<Face> visibleFaces, Level level, RenderTickContext parent) {
		return new LevelRenderTickContext(visibleFaces, parent.getRenderOptions(), level, parent.getCamera(), parent.getProjectionMatrix(), parent.getViewMatrix(), parent.getTransformation(), parent.getDisplay());
	}
}
