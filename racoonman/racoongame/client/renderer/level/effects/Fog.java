package racoonman.racoongame.client.renderer.level.effects;

import org.joml.Vector3f;

public class Fog {
	private boolean enabled;
	private Vector3f color;
	private float density;
	
	public Fog(boolean active, Vector3f color, float density) {
		this.enabled = active;
		this.color = color;
		this.density = density;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public Vector3f getColor() {
		return this.color;
	}
	
	public void setDensity(float density) {
		this.density = density;
	}
	
	public float getDensity() {
		return this.density;
	}
}
