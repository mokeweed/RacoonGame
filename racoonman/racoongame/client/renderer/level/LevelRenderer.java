package racoonman.racoongame.client.renderer.level;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import racoonman.racoongame.core.tick.TickingObject;
import racoonman.racoongame.level.Level;
import racoonman.racoongame.level.tile.Tile;

public class LevelRenderer implements TickingObject<LevelRenderTickContext> {
	private WorldRenderInfo renderInfo;
	private Queue<Tile> newTiles;
	
	public LevelRenderer(Level level) {
		this.renderInfo = new WorldRenderInfo(level);
		this.newTiles = new ConcurrentLinkedQueue<>();
		level.setTileEventCallback(this.newTiles::add);
	}
	
	@Override
	public void tick(LevelRenderTickContext ctx) {
		this.renderInfo.render(ctx.getViewMatrix(), ctx.getTransformation(), ctx.getCamera());
		
		for(int i = 0; i < 5; i++) {
			Tile next;
			if((next = this.newTiles.poll()) != null)
				this.renderInfo.addTile(next);
		}
	}
}
