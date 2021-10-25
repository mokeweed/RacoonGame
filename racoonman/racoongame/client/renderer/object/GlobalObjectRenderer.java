package racoonman.racoongame.client.renderer.object;

import racoonman.racoongame.client.renderer.mesh.Mesh;
import racoonman.racoongame.client.renderer.player.PlayerRenderTickContext;
import racoonman.racoongame.core.tick.TickingObject;

public class GlobalObjectRenderer implements TickingObject<PlayerRenderTickContext> {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void tick(PlayerRenderTickContext ctx) {
		ctx.getPlayer().getLevel().getObjectManager().getObjectsPerType().forEach((cls, objs) -> {
			ObjectRenderer renderer = ObjectRendererRegistry.getRenderer(cls);
			    
			if(renderer == null)
				throw new IllegalStateException("No renderer registered for object type [" + cls + "]");
			Mesh mesh = renderer.getMesh();
			mesh.bind();
			
			objs.forEach(obj -> renderer.render(ctx.getViewMatrix(), ctx.getTransformation(), obj));
			
			mesh.unBind();
		});
	}

}
