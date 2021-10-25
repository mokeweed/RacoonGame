package racoonman.racoongame.client.camera;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import racoonman.racoongame.core.Direction;
import racoonman.racoongame.core.tick.TickingObject;
import racoonman.racoongame.player.ClientPlayer;

public class Camera implements TickingObject<CameraTickContext> {
	private Vector3f position;
    private Vector3f rotation;
    private	Direction horizontalFace;
    private Direction verticalFace;
    
    public Camera() {
        this.position = new Vector3f(0.0F, 0.0F, 0.0F);
        this.rotation = new Vector3f(0.0F, 0.0F, 0.0F);
        this.horizontalFace = Direction.NORTH;
        this.verticalFace = Direction.DOWN;
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    @Override
    public void tick(CameraTickContext ctx) {
    	ClientPlayer player = ctx.getPlayer();
    	
    	this.horizontalFace = player.getHorizontalFacingDirection();
    	this.verticalFace = player.getVerticalFacingDirection();
    	
    	Vector3f position = player.getPosition();
    	this.setPosition(position.x, (position.y + ctx.getPlayer().getEyeHeight()), position.z);
    	
    	Quaternionf newRot = player.getRotation();
    	this.setRotation(newRot.x, newRot.y, newRot.z);
    }
    
    public Direction getHorizontalFacingDirection() {
    	return this.horizontalFace;
    }
    
    public Direction getVerticalFacingDirection() {
    	return this.verticalFace;
    }
    
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float pitch, float yaw, float roll) {
    	this.rotation.set(pitch, yaw, roll);
    }
    
    public Vector3f getPosition() {
    	return this.position;
    }
    
    public Vector3f getRotation() {
    	return this.rotation;
    }
}