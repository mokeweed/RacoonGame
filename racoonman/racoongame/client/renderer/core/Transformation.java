package racoonman.racoongame.client.renderer.core;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import racoonman.racoongame.client.camera.Camera;

public class Transformation {
	private static final Vector3f DEFAULT_SCALE = new Vector3f(1.0F, 1.0F, 1.0F);
	private static final float Z_NEAR = 0.1F;
	private static final float Z_FAR = 1000.0F;
	private Matrix4f viewMat;
	private Matrix4f projectionMat;
	private Matrix4f worldMat;
	
	public Transformation() {
		this.viewMat = new Matrix4f();
		this.projectionMat = new Matrix4f();
		this.worldMat = new Matrix4f();
	}
	
	public Matrix4f getViewMatrix(Camera camera) {
	    Vector3f cameraPos = camera.getPosition();
	    Vector3f rotation = camera.getRotation();

	    this.viewMat.rotation((float)Math.toRadians(rotation.x), new Vector3f(1.0F, 0.0F, 0.0F))
	        .rotate((float)Math.toRadians(rotation.y), new Vector3f(0.0F, 1.0F, 0.0F));
	    this.viewMat.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
	    return this.viewMat;
	}
	
	public Matrix4f getProjectionMatrix(float fov, float width, float height) {
        float aspectRatio = width / height;
        this.projectionMat.setPerspective(fov, aspectRatio, Z_NEAR, Z_FAR);
        return this.projectionMat;
    }

	public Matrix4f getModelViewMatrix(Vector3f pos, Matrix4f viewMatrix) {
		return this.getModelViewMatrix(pos, DEFAULT_SCALE, viewMatrix);
	}
	
    public Matrix4f getModelViewMatrix(Vector3f pos, Vector3f scale, Matrix4f viewMatrix) {
    	this.worldMat
    		.translation(pos)
    		.scale(scale.x, scale.y, scale.z);
    	Matrix4f viewCurr = new Matrix4f(viewMatrix);
    	return viewCurr.mul(this.worldMat);
    }
	
    public Matrix4f getModelViewMatrix(Vector3i pos, Vector3f scale, Matrix4f viewMatrix) {
    	this.worldMat
    		.translation(pos.x, pos.y, pos.z)
    		.scale(scale.x, scale.y, scale.z);
    	Matrix4f viewCurr = new Matrix4f(viewMatrix);
    	return viewCurr.mul(this.worldMat);
    }
}
