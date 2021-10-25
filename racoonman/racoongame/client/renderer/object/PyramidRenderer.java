package racoonman.racoongame.client.renderer.object;

import racoonman.racoongame.client.renderer.core.Material;
import racoonman.racoongame.client.renderer.mesh.Mesh;
import racoonman.racoongame.client.renderer.mesh.OBJLoader;
import racoonman.racoongame.client.renderer.texture.Texture;
import racoonman.racoongame.object.GameObject;
import racoonman.racoongame.object.Pyramid;

public class PyramidRenderer implements ObjectRenderer<Pyramid> {
	private static final Mesh MESH = OBJLoader.loadMeshFromOBJ("pyramid");
	private static final Material MATERIAL = new Material(Texture.getTexture("stone"));
	
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
		return 0.0415F;
	}
}
