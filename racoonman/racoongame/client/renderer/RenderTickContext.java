package racoonman.racoongame.client.renderer;

import org.joml.Matrix4f;

import racoonman.racoongame.client.camera.Camera;
import racoonman.racoongame.client.renderer.core.Transformation;
import racoonman.racoongame.client.window.Window;
import racoonman.racoongame.core.tick.TickContext;

public class RenderTickContext implements TickContext {
	private RenderOptions options;
	private Camera camera;
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Transformation transformation;
	private Window display;
	
	protected RenderTickContext(RenderOptions options, Camera camera, Matrix4f projectionMatrix, Matrix4f viewMatrix, Transformation transformation, Window display) {
		this.options = options;
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = viewMatrix;
		this.transformation = transformation;
		this.display = display;
	}
	
	public RenderOptions getRenderOptions() {
		return this.options;
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}
	
	public Matrix4f getViewMatrix() {
		return this.viewMatrix;
	}
	
	public Transformation getTransformation() {
		return this.transformation;
	}
	
	public Window getDisplay() {
		return this.display;
	}
	
	public static RenderTickContext of(RenderOptions options, Camera camera, Matrix4f projectionMatrix, Matrix4f viewMatrix, Transformation transformation, Window display) {
		return new RenderTickContext(options, camera, projectionMatrix, viewMatrix, transformation, display);
	}
}
