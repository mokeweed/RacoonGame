package racoonman.racoongame.client.renderer;

import java.util.function.Consumer;

import racoonman.racoongame.client.renderer.level.effects.Fog;

public class RenderOptions {
	private Fog fog;
	private float fov;
	private int polygonMode;
	private boolean antialias;
	private boolean cullFaces;
	private boolean updated;
	
	public RenderOptions(Fog fog, float fov, int polygonMode, boolean antialias, boolean cullFaces) {
		this.fog = fog;
		this.fov = fov;
		this.polygonMode = polygonMode;
		this.antialias = antialias;
		this.cullFaces = cullFaces;
		this.updated = true;//update on first tick
	}
	
	public void enabledFog() {
		this.fog.setEnabled(true);
		this.setUpdated(true);
	}
	
	public void disableFog() {
		this.fog.setEnabled(false);
		this.setUpdated(true);
	}
	
	public void setFog(Fog fog) {
		this.fog = fog;
		this.setUpdated(true);
	}
	
	public Fog getFog() {
		return this.fog;
	}
	
	public void setFov(float fov) {
		this.fov = fov;
		this.setUpdated(true);
	}
	
	public float getFov() {
		return this.fov;
	}
	
	public void setPolygonMode(int mode) {
		this.polygonMode = mode;
		this.setUpdated(true);
	}
	
	public int getPolygonMode() {
		return this.polygonMode;
	}
	
	public void setAntialias(boolean antialias) {
		this.antialias = antialias;
		this.setUpdated(true);
	}
	
	public boolean getShouldAntialias() {
		return this.antialias;
	}
	
	public void updateOptions(Consumer<RenderOptions> options) {
		if(this.isUpdated()) {
			options.accept(this);
			this.setUpdated(false);
		}
	}
	
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	
	public boolean isUpdated() {
		return this.updated;
	}
}
