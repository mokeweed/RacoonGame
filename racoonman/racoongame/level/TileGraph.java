package racoonman.racoongame.level;

import java.util.function.Consumer;

import org.joml.Vector3i;

import racoonman.racoongame.level.tile.Tile;
import racoonman.racoongame.level.tile.TileType;

public class TileGraph {
	private int width;
	private int height;
	private Tile[][][] data;
	
	public TileGraph(int width, int height, TileType...typesPerRow) {
		this.width = width;
		this.height = height;
		this.data = new Tile[this.width][this.height][this.width];
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void set(Vector3i pos, TileType type) {
		this.set(pos.x, pos.y, pos.z, type);
	}
	
	public void set(int x, int y, int z, TileType type) {
		if(this.contains(x, y, z)) {
			this.data[x][y][z] = new Tile(type, x, y, z);
		}
	}
	
	public Tile get(Vector3i pos) {
		return this.get(pos.x, pos.y, pos.z);
	}
	
	public Tile get(int x, int y, int z) {
		if(!this.contains(x, y, z))
			return null;
		return this.data[x][y][z];
	}
	
	public boolean contains(Vector3i vec) {
		return this.contains(vec.x, vec.y, vec.z);
	}
	
	public boolean contains(int x, int y, int z) {
		return x < this.width && x >= 0 && 
			   y < this.height && y >= 0 && 
			   z < this.width && z >= 0;
	}
	
	public void forEach(Consumer<Tile> action) {
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				for(int z = 0; z < this.width; z++) {
					action.accept(this.get(x, y, z));
				}
			}
		}
	}
}
