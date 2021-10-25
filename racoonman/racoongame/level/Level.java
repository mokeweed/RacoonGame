package racoonman.racoongame.level;

import org.joml.Vector3i;

import racoonman.racoongame.core.tick.EmptyTickContext;
import racoonman.racoongame.core.tick.TickingObject;
import racoonman.racoongame.level.tile.Tile;
import racoonman.racoongame.level.tile.TileType;
import racoonman.racoongame.object.ObjectManager;
import racoonman.racoongame.player.PlayerManager;

public class Level implements TickingObject<EmptyTickContext> {
	private TileGraph graph;
	private PlayerManager playerManager;
	private ObjectManager objManager;
	private TileEventCallback callback;
	private Vector3i spawnPos;
	
	public Level(int width, int height) {
		this(width, height, new Vector3i());
	}
	
	public Level(int width, int height, Vector3i spawnPos) {
		this.graph = new TileGraph(width, height, TileType.STONE, 
				TileType.STONE, TileType.GRASS, TileType.AIR, TileType.AIR, TileType.AIR, TileType.AIR);
		this.spawnPos = spawnPos;
		this.playerManager = new PlayerManager();
		this.objManager = new ObjectManager();
	}

	@Override
	public void tick(EmptyTickContext tickContext) {
		this.playerManager.tick();
		this.objManager.tick(LevelTickContext.of(this));
	}
	
	public void setTileEventCallback(TileEventCallback callback) {
		this.callback = callback;
	}
	
	public void setTile(Vector3i tilePos, TileType type) {
		this.setTile(tilePos.x, tilePos.y, tilePos.z, type);
	}
	
	public void setTile(int x, int y, int z, TileType type) {
		Tile tile = new Tile(type, x, y, z);
		this.callback.invoke(tile);
	}
	
	public TileGraph getTileGraph() {
		return this.graph;
	}
	
	public ObjectManager getObjectManager() {
		return this.objManager;
	}

	public Vector3i getSpawnPos() {
		return this.spawnPos;
	}
	
	public void setSpawnPos(Vector3i pos) {
		this.spawnPos = pos;
	}
	
	public static interface TileEventCallback {
		void invoke(Tile tile);
	}
}
