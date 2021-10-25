package racoonman.racoongame.player;

import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;

import racoonman.racoongame.core.Direction;
import racoonman.racoongame.core.collision.ObjectAABB;
import racoonman.racoongame.core.input.KeyboardHandler;
import racoonman.racoongame.level.Level;
import racoonman.racoongame.level.tile.TileType;
import racoonman.racoongame.object.Cone;
import racoonman.racoongame.object.GameObject;
import racoonman.racoongame.object.ObjectDataTracker;
import racoonman.racoongame.object.Pyramid;
import racoonman.racoongame.util.MathUtil;

public class ClientPlayer extends GameObject<PlayerInputTickContext> {
	private static final float FORWARD_SPEED = 0.0385F;
	private static final float BACKWARD_SPEED = 0.03F;
	
	private static final float CROUCHING_EYE_LEVEL = 0.6F;
	private static final float EYE_LEVEL = 1.6F;
    
	private static final int MAX_JUMP_COOLDOWN = 90;
	private static final int MAX_JUMP_TIME = 15;
	
	public ClientPlayer(Level level, Vector3f pos) {
		super(level, pos);
		this.aabb = ObjectAABB.of(1.0F, 5.0F);
	}
	
	@Override
	public void tick(PlayerInputTickContext ctx) {
		super.tick(ctx);
//		Vector3f rotVec = getRotationVec(this.rotation.x, this.rotation.y);
//		Vector3f eyeVec = new Vector3f(this.getPosition()).add(0.0F, this.getEyeHeight(), 0.0F);
//		Vector3f endVec = eyeVec.add(rotVec.mul(6));
//		
//		System.out.println(endVec);	
	}
	
	//TODO replace Math.sin & Math.cos functions with quicker implementations
	protected static Vector3f getRotationVec(float pitch, float yaw) {
        float f = (float)Math.cos(Math.toRadians(yaw) - (float)Math.PI);
        float f1 = (float)Math.sin(Math.toRadians(yaw) - (float)Math.PI);
        float f2 = (float)-Math.cos(Math.toRadians(-pitch));
        float f3 = (float)Math.sin(Math.toRadians(-pitch));
        return new Vector3f(f1 * f2, f3, f * f2);
    }
	
	public void jump() {
		Vector3d adjustedPos = new Vector3d(this.pos).sub(0.0D, 0.1D, 0.0D);
		if(this.getJumpCooldown() == 0 && this.onGround(adjustedPos)) {
			this.setJumpCooldown(MAX_JUMP_COOLDOWN);
			this.setJumpTime(MAX_JUMP_TIME);
			this.setJumping(true);
		}
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public ObjectDataTracker getDataTracker() {
		return this.dataTracker;
	}
	
	public float getEyeHeight() {
		return this.dataTracker.getDataValue("crouching", Boolean.class) ? CROUCHING_EYE_LEVEL : EYE_LEVEL;
	}
	
	public boolean onGround(Vector3d vec) {
		return this.collidingFaces.contains(Direction.UP);
	}
	
	public int getJumpTime() {
		return this.dataTracker.getDataValue("jumpTime", Integer.class);
	}
	
	public void setJumpTime(int jumpTime) {
		this.dataTracker.setDataValue("jumpTime", jumpTime);
	}
	
	public int getJumpCooldown() {
		return this.dataTracker.getDataValue("jumpCooldown", Integer.class);
	}
	
	public void setJumpCooldown(int cooldown) {
		this.dataTracker.setDataValue("jumpCooldown", cooldown);
	}
	
	public boolean isJumping() {
		return this.dataTracker.getDataValue("jumping", Boolean.class);
	}
	
	public void setJumping(boolean jumping) {
		this.dataTracker.setDataValue("jumping", jumping);
	}
	
	public boolean isCrouching() {
		return this.dataTracker.getDataValue("crouching", Boolean.class);
	}
	
	public void setCrouching(boolean crouching) {
		this.dataTracker.setDataValue("crouching", crouching);
	}
	
	public boolean isRunning() {
		return this.dataTracker.getDataValue("running", Boolean.class);
	}
	
	public void setRunning(boolean running) {
		this.dataTracker.setDataValue("running", running);
	}
	
	public boolean isNoClip() {
		return this.dataTracker.getDataValue("noClip", Boolean.class);
	}
	
	public void setNoClip(boolean noClip) {
		this.dataTracker.setDataValue("noClip", noClip);
	}	

	public void addDefaultKeyListeners(KeyboardHandler handler) {
		//moves the player forward
		handler.addKeyCallback(GLFW.GLFW_KEY_W, () -> {
			this.setZMotion(-FORWARD_SPEED);
		});

		//moves the player to the left
		handler.addKeyCallback(GLFW.GLFW_KEY_A, () -> {
			this.setXMotion(-FORWARD_SPEED);
		});
		
		//moves the player backwards
		handler.addKeyCallback(GLFW.GLFW_KEY_S, () -> {
			this.setZMotion(BACKWARD_SPEED);
		});
		
		//moves the player to the right
		handler.addKeyCallback(GLFW.GLFW_KEY_D, () -> {
			this.setXMotion(FORWARD_SPEED);
		});
		
		//TODO toggles player sprint
		handler.addKeyCallback(GLFW.GLFW_KEY_Q, () -> {
			if(this.motion.z != 0.0F)
				this.addZMotion(-(FORWARD_SPEED/2));
		});
		
		//moves the player up if no clip is enabled, or makes the player jump if its not enabled
		handler.addKeyCallback(GLFW.GLFW_KEY_SPACE, () -> {
			if(!this.isNoClip()) {
				this.jump();
			} else this.addYMotion(BACKWARD_SPEED);
		});
		
		//moves the player down if no clip is enabled
		handler.addKeyCallback(GLFW.GLFW_KEY_LEFT_SHIFT, () -> {
			if(this.isNoClip())
				this.addYMotion(-BACKWARD_SPEED);
		});
		
		//toggles no clip
		handler.addKeyCallback(GLFW.GLFW_KEY_C, () -> {
			this.setNoClip(!this.isNoClip());
		}, 20);
		
		//add a pyramid at the player's positions
		handler.addKeyCallback(GLFW.GLFW_KEY_F, () -> {
			Pyramid pyramid = new Pyramid(this.level, this.pos.get(new Vector3f()));
			pyramid.setScale(1.5F, 1.5F, 1.5F);
			this.placeObject(pyramid);
		}, 50);
		
		//add a cone at the player's position
		handler.addKeyCallback(GLFW.GLFW_KEY_R, () -> {
			Cone cone = new Cone(this.level, this.pos.get(new Vector3f()));
			cone.setScale(1.5F, 1.5F, 1.5F);
			this.placeObject(cone);
		}, 50);
		
		//reset position
		handler.addKeyCallback(GLFW.GLFW_KEY_T, () -> {
			Vector3i spawnPos = this.level.getSpawnPos();
			this.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
		});
		
		//sets the tile at the player's position to stone
		handler.addKeyCallback(GLFW.GLFW_KEY_G, () -> {
			this.level.setTile((int)(this.pos.x), (int)(this.pos.y + 1), (int)(this.pos.z), TileType.PLANKS);
		});
	}
	
	private void placeObject(GameObject<?> obj) {
		obj.setRotation(this.rotation.x, this.rotation.y, this.rotation.z);
		this.level.getObjectManager().addObject(obj);
	}
	
	@Override
	protected void tickRotation(PlayerInputTickContext ctx) {
		this.rotate((float)ctx.getZMouseDelta(), (float)ctx.getXMouseDelta(), 0.0F);
		Quaternionf rotDelta = MathUtil.deltaF(this.rotation, this.prevRotation);
    	this.rotation.add((float)rotDelta.x, (float)rotDelta.y, (float)rotDelta.z, 1.0F);
    	super.tickRotation(ctx);
	}
	
	@Override
	protected void tickCollisions(PlayerInputTickContext ctx) {
		if(!this.isNoClip()) {
			if(!this.isJumping()) {
				super.tickCollisions(ctx);
			}
		}
			
		if(this.getJumpCooldown() > 0)
			this.setJumpCooldown(this.getJumpCooldown() - 1);
		 
		if(this.getJumpTime() > 0) {
			this.setJumpTime(this.getJumpTime() - 1);
			this.addYMotion(0.18F);
		} else this.setJumping(false);
	}

	@Override
	protected void registerObjectDataTypes() {
		this.setCrouching(false);
		this.setJumping(false);
		this.setNoClip(false);
		this.setRunning(false);
		this.setJumpCooldown(0);
		this.setJumpTime(0);
	}
}
