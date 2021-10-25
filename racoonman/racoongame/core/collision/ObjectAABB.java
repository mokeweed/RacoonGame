package racoonman.racoongame.core.collision;

import java.util.Set;

import org.joml.Vector3f;
import org.joml.Vector3i;

import racoonman.racoongame.core.Direction;
import racoonman.racoongame.level.TileGraph;
import racoonman.racoongame.level.tile.Tile;

public class ObjectAABB {
	private Vector3f pos;
	private float width;
	private float height;
	
	private ObjectAABB(float width, float height) {
		this.pos = new Vector3f();
		this.width = width;
		this.height = height;
	}
	
	public Vector3f getPos() {
		return this.pos;
	}
	
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
	
	public float getWidth() {
		return this.width;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public void calcCollisions(TileGraph graph, Set<Direction> toAdd) {
		//top collisions
		Vector3i thisCoords = new Vector3i(Math.round(this.pos.x), Math.round(this.pos.y), Math.round(this.pos.z));
		
		Tile belowTile = graph.get(thisCoords);
		
		if(belowTile != null && belowTile.getType().canCollide()) {
			TileAABB tileBox = belowTile.getType().getBoundingBox();
			double tileUp = belowTile.getPosition().y + tileBox.getHeight();
			if((this.pos.y - tileUp) < 0.0D)
				toAdd.add(Direction.UP);
		}			
			//bottom collisions
		thisCoords.set(thisCoords.x, (int)(Math.floor(this.pos.y) + this.height), thisCoords.z);
		Tile aboveTile = graph.get(thisCoords.x, 6, thisCoords.z);
//		System.out.println("this [" + thisCoords.y + "]");
//		System.out.println("tile [" + aboveTile.getPosition().y + "]");
//		
		if(aboveTile != null && aboveTile.getType().canCollide()) {
			double tileBottom = aboveTile.getPosition().y;
			if(thisCoords.y > tileBottom) {
				toAdd.add(Direction.DOWN);
			}
		}
//		System.out.println(toAdd);
		//north collisions
//			double tileX = tile.getPosition().x + tileBox.getWidth();
//			if((this.pos.x - tileX) < 0.0D)
//				toAdd.add(Face.NORTH);
			//east collisions
			
			
			//south collisions
			
			
			//west collisions
	}
	
	public static ObjectAABB of(float width, float height) {
		return new ObjectAABB(width, height);
	}
}
