package racoonman.racoongame.level.tile;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class Tile {
	private TileType type;
	private Vector3i pos;
	private Vector3f scale;
	
	public Tile(TileType type, int x, int y, int z) {
		this(type, new Vector3i(x, y, z), new Vector3f(0.5F, 0.5F, 0.5F));
	}
	
	public Tile(TileType type, Vector3i pos) {
		this(type, pos, new Vector3f(1.0F, 1.0F, 1.0F));
	}
	
	public Tile(TileType type, Vector3i pos, Vector3f scale) {
		this.type = type;
		this.pos = pos;
		this.scale = scale;
	}

	public Vector3i getPosition() {
		return this.pos;
	}

	public void setScale(float scaleX, float scaleY, float scaleZ) {
		this.scale.set(scaleX, scaleY, scaleZ);
	}
	
	public void setScale(Vector3f scale) {
		this.scale.set(scale.x, scale.y, scale.z);
	}

	public Vector3f getScale() {
		return this.scale;
	}

	public TileType getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return this.type.toString();
	}
}
