package racoonman.racoongame.client.renderer.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import racoonman.racoongame.client.renderer.core.Destroyable;
import racoonman.racoongame.client.renderer.core.Material;
import racoonman.racoongame.client.renderer.core.Transformation;
import racoonman.racoongame.client.renderer.mesh.Mesh;
import racoonman.racoongame.client.renderer.shader.LevelShader;
import racoonman.racoongame.object.GameObject;

public interface ObjectRenderer<T extends GameObject<?>> extends Destroyable {
	Mesh getMesh();
	
	Material getMaterial();
	
	default void destroy() {
		this.getMesh().destroy();
	}
	
	default float getYOffset(GameObject<?> toRender) {
		return 0.0F;
	}
	
	default void render(Matrix4f viewMatrix, Transformation transformation, T obj) {
		Matrix4f modelViewMatrix = transformation.getModelViewMatrix(obj.getPosition().get(new Vector3f()), obj.getScale(), viewMatrix).translate(0.0F, this.getYOffset(obj), 0.0F);
		LevelShader.INSTANCE.setUniform("modelViewMatrix", modelViewMatrix);
	
		Material tex = this.getMaterial();
		Mesh mesh = this.getMesh();

		tex.bind();
		mesh.render();
		tex.unBind();
	}
}
