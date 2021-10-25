package racoonman.racoongame.core.collision;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class TileAABB {
	private double width;
	private double height;
	
	private TileAABB(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public boolean collides(Vector3i thisPos, Vector3f collidingPos) {
		//top collisions
		double offsetY = thisPos.y + this.height;
		
		return (collidingPos.y - offsetY) < 0.0D;
		
		//bottom collisions
		
		
		//north collisions
		
		//east collisions
		
		
		//south collisions
		
		
		//west collisions
	}
	
	public static TileAABB of(double width, double height) {
		return new TileAABB(width, height);
	}
}
