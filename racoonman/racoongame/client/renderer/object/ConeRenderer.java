package racoonman.racoongame.client.renderer.object;

import racoonman.racoongame.client.renderer.core.Material;
import racoonman.racoongame.client.renderer.mesh.Mesh;
import racoonman.racoongame.client.renderer.mesh.OBJLoader;
import racoonman.racoongame.client.renderer.texture.Texture;
import racoonman.racoongame.object.Cone;
import racoonman.racoongame.object.GameObject;

public class ConeRenderer implements ObjectRenderer<Cone> {
	private static final Mesh MESH = OBJLoader.loadMeshFromOBJ("cone");
	private static final Material MATERIAL = new Material(Texture.getTexture("stone"), Texture.getTexture("log_n"), 10.0F);
	
	@Override
	public Mesh getMesh() {
		return MESH;
	}

	@Override
	public Material getMaterial() {
		return MATERIAL;
	}
	
	@Override
	public float getYOffset(GameObject<?> toRender) {
		return 0.23F;
	}
}
