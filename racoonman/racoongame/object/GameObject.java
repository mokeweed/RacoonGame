package racoonman.racoongame.object;

import java.util.EnumSet;
import java.util.Set;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3i;

import racoonman.racoongame.core.Direction;
import racoonman.racoongame.core.collision.ObjectAABB;
import racoonman.racoongame.core.tick.TickContext;
import racoonman.racoongame.core.tick.TickingObject;
import racoonman.racoongame.level.Level;
import racoonman.racoongame.level.TileGraph;

public abstract class GameObject<T extends TickContext> implements TickingObject<T> {
	protected Level level;
	
	protected ObjectDataTracker dataTracker;
	
	protected Vector3f motion;
	
	protected Vector3f pos;
	
	protected Quaternionf rotation;
	protected Quaternionf prevRotation;
	protected Direction horizontalFacingDirection;
	protected Direction verticalFacingDirection;
	
	protected Vector3f scale;
	
	protected ObjectAABB aabb;
	protected Set<Direction> collidingFaces;
	
	public GameObject(Level level, Vector3f pos) {
		this.level = level;

		this.dataTracker = new ObjectDataTracker();
		this.registerObjectDataTypes();
		
		this.motion = new Vector3f();
		
		this.pos = pos;
		
		this.rotation = new Quaternionf();
		this.prevRotation = new Quaternionf();
		this.horizontalFacingDirection = Direction.NORTH;
		
		this.scale = new Vector3f(1.0F);

		this.aabb = ObjectAABB.of(1.0F, 1.0F);
		this.collidingFaces = EnumSet.noneOf(Direction.class);
	}
	
	@Override
	public void tick(T tickContext) {
		this.tickRotation(tickContext);
		this.tickCollisions(tickContext);
		this.tickMotion(tickContext);
	}
	
	protected void tickRotation(T ctx) {
		//limit yaw rotation from 0 to 360
		if(this.rotation.y > 360.0F)
    		this.rotation.y = 0.0F;
    	if(this.rotation.y < 0.0F)
    		this.rotation.y = 360.0F;
    	
    	//limit pitch rotations from -90 to 90
    	if(this.rotation.x > 90.0F)
			this.rotation.x = 90.0F;
		if(this.rotation.x < -90.0F)
			this.rotation.x = -90.0F;
		
		this.horizontalFacingDirection = Direction.getHorizontalFace(this.rotation);
    	this.verticalFacingDirection = Direction.getVerticalFace(this.rotation);
	
    	this.prevRotation.set(this.rotation);
	}
	
	protected void tickCollisions(T ctx) {
		this.aabb.setPos(this.pos);

		Vector3f fallPos = new Vector3f(this.pos);
		Vector3i tileCoords = new Vector3i(Math.round(fallPos.x), Math.round(fallPos.y), Math.round(fallPos.z));
		TileGraph graph = this.level.getTileGraph();

		this.collidingFaces.clear();
		if(graph.contains(tileCoords.x, tileCoords.y, tileCoords.z)) {
			this.aabb.calcCollisions(this.level.getTileGraph(), this.collidingFaces);
			
			if(!this.collidingFaces.contains(Direction.UP))
				this.addYMotion(-0.08F);
			if(this.collidingFaces.contains(Direction.NORTH)) {
				this.setYMotion(0.0F);
			}
		} else {	
			this.addYMotion(-0.08F);
		}
	}
	
	protected void tickMotion(T ctx) {
		this.move(this.motion.x, this.motion.y, this.motion.z);
		this.motion.set(0.0F, 0.0F, 0.0F);
			
//		this.prevPos.set(this.pos);
//		if(graph.contains(tileCoords.x, tileCoords.y, tileCoords.z)) {
//			TileInstance tile = graph.get(tileCoords.x, tileCoords.y, tileCoords.z);
//			
//			if(!tile.getType().canCollide() || !tile.doesAABBCollide(fallPos)) {
//				this.pos.set(this.pos.x, fallPos.y, this.pos.z);
//			}
//		} else {
//			this.pos = fallPos;
//		}
	}
	
	public void rotate(float roll, float pitch, float yaw) {
		this.rotation.add(roll, pitch, yaw, 0.0f);
	}
	
	public void setMotion(float x, float y, float z) {
		this.motion.set(x, y, z);
	}
	
	public void setXMotion(float mot) {
		this.motion.set(mot, this.motion.y, this.motion.z);
	}
	
	public void setYMotion(float mot) {
		this.motion.set(this.motion.x, mot, this.motion.z);
	}
	
	public void setZMotion(float mot) {
		this.motion.set(this.motion.x, this.motion.y, mot);
	}
	
	public void addXMotion(float mot) {
		this.motion.add(mot, 0.0F, 0.0F);
	}
	
	public void addYMotion(float mot) {
		this.motion.add(0.0F, mot, 0.0F);
	}
	
	public void addZMotion(float mot) {
		this.motion.add(0.0F, 0.0F, mot);
	}
	
	public void move(float x, float y, float z) {
		if (z != 0) {
            this.pos.x += ((float)Math.sin(Math.toRadians(this.rotation.y)) * -1.0f * z);
            this.pos.z += ((float)Math.cos(Math.toRadians(this.rotation.y)) * z);
        }
        if (x != 0) {
            this.pos.x += ((float)Math.sin(Math.toRadians(this.rotation.y - 90)) * -1.0f * x);
            this.pos.z += ((float)Math.cos(Math.toRadians(this.rotation.y - 90)) * x);
        }
        this.pos.y += y;
	}
	
	public void setRotation(float roll, float pitch, float yaw) {
		this.rotation.set(roll, pitch, yaw, 1.0F);
	}
	
	public void setPosition(float x, float y, float z) {
		this.pos.set(x, y, z);
	}	

	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}
	
	public Quaternionf getRotation() {
		return this.rotation;
	}
	
	public Vector3f getPosition() {
		return this.pos;
	}
	
	public Vector3f getScale() {
		return this.scale;
	}
	
	public Direction getHorizontalFacingDirection() {
		return this.horizontalFacingDirection;
	}
	
	public Direction getVerticalFacingDirection() {
		return this.verticalFacingDirection;
	}
	
	protected void registerObjectDataTypes() {
	}
}
