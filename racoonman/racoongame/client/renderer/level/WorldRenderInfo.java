package racoonman.racoongame.client.renderer.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3i;

import racoonman.racoongame.client.camera.Camera;
import racoonman.racoongame.client.renderer.core.Transformation;
import racoonman.racoongame.client.renderer.level.TileRenderer.Face;
import racoonman.racoongame.client.renderer.mesh.Mesh;
import racoonman.racoongame.client.renderer.shader.LevelShader;
import racoonman.racoongame.level.Level;
import racoonman.racoongame.level.TileGraph;
import racoonman.racoongame.level.tile.Tile;
import racoonman.racoongame.level.tile.TileType;
import racoonman.racoongame.util.Util;

public class WorldRenderInfo {
	private TileGraph graph;
	private TypeCache cache;
	
	public WorldRenderInfo(Level level) {
		this.graph = level.getTileGraph();
		this.cache = new TypeCache();

		this.graph.forEach(this::addTile);
	}

	public void render(Matrix4f viewMatrix, Transformation transformation, Camera cam) {
		this.cache.getGroups().forEach((type, faceCache) -> {
			type.getMaterial().bind();
			faceCache.getGroups().forEach((face, group) -> {
				Mesh faceMesh = face.getMesh();
				faceMesh.bind();
				group.getTiles().forEach((info) -> {
					Tile tile = info.getTile();
					
					if(type.hasModel() && TileRenderer.shouldRenderFace(tile, face, cam)) {
						Matrix4f modelViewMatrix = transformation.getModelViewMatrix(tile.getPosition(), tile.getScale(), viewMatrix);
						LevelShader.INSTANCE.setUniform("modelViewMatrix", modelViewMatrix);

						faceMesh.render();
					}
				});
				faceMesh.unBind();
			});
			type.getMaterial().unBind();
		});
	}
	
	public void addTile(Tile tile) {
		if(tile.getType().hasModel()) {
			Vector3i tilePos = tile.getPosition();
			Vector3i offsetPos = new Vector3i(tilePos);
	
			for(Face face : Face.values()) {
				offsetPos.set(tilePos).add(face.getStepX(), face.getStepY(), face.getStepZ());
				Tile offsetTile = this.graph.get(offsetPos);
	
				if(offsetTile == null || tile.getType().isTransparent() || !offsetTile.getType().hasModel()) {
					this.cache.put(face, tile);
				}
			}	
		}
	}
	
	public static class TypeCache {
		private Map<TileType, FaceCache> cache;
		
		public TypeCache() {
			this.cache = new HashMap<>();
		}
		
		public void put(Face face, Tile tile) {
			TileType type = tile.getType();
			
			if(!this.cache.containsKey(type))
				this.cache.put(type, new FaceCache());
			this.cache.get(type).put(face, new TileInfo(tile));
		}
		
		public Map<TileType, FaceCache> getGroups() {
			return this.cache;
		}
	}
	
	public static class FaceCache {
		private Map<Face, TileGroup> groups;
		
		public FaceCache() {
			this.groups = Util.make(new HashMap<>(), (map) -> {
				for(Face face : Face.values())
					map.put(face, new TileGroup());
			});
		}
		
		public void put(Face face, TileInfo tile) {
			this.groups.get(face).addTile(tile);
		}
		
		public Map<Face, TileGroup> getGroups() {
			return this.groups;
		}
	}
	
	public static class TileGroup {
		private List<TileInfo> tiles;
		
		public TileGroup() {
			this.tiles = new ArrayList<>();
		}
		
		public void addTile(TileInfo tile) {
			this.tiles.add(tile);
		}
		
		public List<TileInfo> getTiles() {
			return this.tiles;
		}
	}
	
	public static class TileInfo {
		private Tile tile;

		public TileInfo(Tile tile) {
			this.tile = tile;
		}
		
		public Tile getTile() {
			return this.tile;
		}
	}
}
