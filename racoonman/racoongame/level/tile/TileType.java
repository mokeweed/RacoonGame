package racoonman.racoongame.level.tile;

import racoonman.racoongame.client.renderer.core.Material;
import racoonman.racoongame.client.renderer.texture.Texture;
import racoonman.racoongame.core.collision.TileAABB;
import racoonman.racoongame.util.Util;

public class TileType {
	public static final TileType AIR = new TileType("air");
	public static final TileType GRASS = new TileType("grass", TileAABB.of(1.0D, 1.0D), new Material(Texture.getTexture("grass")));
	public static final TileType LEAVES = Util.make(new TileType("leaves", TileAABB.of(1.0D, 1.0D), Texture.getTexture("leaves")), (type) -> {
		type.transparent = true;
	});
	public static final TileType LOG = new TileType("log", TileAABB.of(1.0D, 1.0D), Texture.getTexture("log"));
	public static final TileType STONE = new TileType("stone", TileAABB.of(1.0D, 1.0D),  new Material(Texture.getTexture("stone")));
	public static final TileType PLANKS = new TileType("planks", TileAABB.of(1.0D, 1.0D),  new Material(Texture.getTexture("planks")));
	private TileAABB aabb;
	private Material material;//TODO move this
	private boolean hasModel;
	private boolean transparent;
	private String name;

	public TileType(String name) {
		this(name, null, null, false);
	}
	
	public TileType(String name, TileAABB aabb) {
		this(name, aabb, null, false);
	}
	
	public TileType(String name, TileAABB aabb, Texture texture) {
		this(name, aabb, new Material(texture, 0.1F));
	}
	
	public TileType(String name, TileAABB aabb, Material material) {
		this(name, aabb, material, true);
	}
	
	public TileType(String name, TileAABB aabb, Material material, boolean hasModel) {
		this.name = name;
		this.aabb = aabb;
		this.material = material;
		this.hasModel = hasModel;
	}

	public String getName() {
		return this.name;
	}
	
	public TileAABB getBoundingBox() {
		return this.aabb;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public boolean hasModel() {
		return this.hasModel;
	}
	
	public boolean isTransparent() {
		return this.transparent;
	}
	
	public boolean canCollide() {
		return this.aabb != null;
	}
	
	@Override
	public String toString() {
		return "[" + this.name + "]";
	}
}
