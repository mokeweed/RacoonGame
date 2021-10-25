package racoonman.racoongame.client.renderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import racoonman.racoongame.client.camera.Camera;
import racoonman.racoongame.client.camera.ViewFrustrum;
import racoonman.racoongame.client.renderer.core.Destroyable;
import racoonman.racoongame.client.renderer.core.Transformation;
import racoonman.racoongame.client.renderer.level.LevelRenderTickContext;
import racoonman.racoongame.client.renderer.level.LevelRenderer;
import racoonman.racoongame.client.renderer.light.LightRenderer;
import racoonman.racoongame.client.renderer.object.ConeRenderer;
import racoonman.racoongame.client.renderer.object.GlobalObjectRenderer;
import racoonman.racoongame.client.renderer.object.ObjectRendererRegistry;
import racoonman.racoongame.client.renderer.object.PyramidRenderer;
import racoonman.racoongame.client.renderer.player.PlayerRenderTickContext;
import racoonman.racoongame.client.renderer.player.PlayerRenderer;
import racoonman.racoongame.client.renderer.shader.LevelShader;
import racoonman.racoongame.client.window.Window;
import racoonman.racoongame.core.tick.TickingObject;
import racoonman.racoongame.level.Level;
import racoonman.racoongame.object.Cone;
import racoonman.racoongame.object.Pyramid;

public class GameRenderer implements TickingObject<GameRenderTickContext>, Destroyable {
	private Window display;
	private LevelRenderer levelRenderer;
	private PlayerRenderer playerRenderer; 
	private GlobalObjectRenderer objectRenderer;
	private Transformation transformation;
	private LightRenderer lightRenderer;
	private ViewFrustrum frustum;
	
	public GameRenderer(Window display, Level level) {
		this.display = display;
		this.levelRenderer = new LevelRenderer(level);
		this.playerRenderer = new PlayerRenderer();
		this.objectRenderer = new GlobalObjectRenderer();
		this.transformation = new Transformation();
		this.lightRenderer = new LightRenderer();
	
		this.frustum = new ViewFrustrum();
	}
	
	@Override
	public void tick(GameRenderTickContext ctx) {
		this.display.clearBuffers();
		
		if(this.display.isResized()) {
			GL11.glViewport(0, 0, this.display.getWidth(), this.display.getHeight());
			this.display.setResized(false);
		}
		
		Camera camera = ctx.getCamera();
		
		RenderOptions options = ctx.getRenderOptions();
		options.updateOptions(this::updateRenderOptions);

		Matrix4f projectionMatrix = this.transformation.getProjectionMatrix((float)Math.toRadians(options.getFov()), this.display.getWidth(), this.display.getHeight());
		Matrix4f viewMatrix = this.transformation.getViewMatrix(camera);
		
		RenderTickContext parent = RenderTickContext.of(ctx.getRenderOptions(), camera, projectionMatrix, viewMatrix, this.transformation, this.display);
		
		LevelRenderTickContext levelCtx = LevelRenderTickContext.of(this.frustum.getVisibleFaces(camera), ctx.getLevel(), parent);
		PlayerRenderTickContext playerCtx = PlayerRenderTickContext.of(ctx.getPlayer(), parent);

		LevelShader.INSTANCE.setUniform("projectionMatrix", projectionMatrix);

		this.playerRenderer.tick(playerCtx);
		this.levelRenderer.tick(levelCtx);	
		this.objectRenderer.tick(playerCtx);
		//this.lightRenderer.tick(playerCtx);
		
		this.display.swapBuffers();
	}

	public void init() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

//		GL11.glEnable(GL11.GL_CULL_FACE);
//		GL11.glCullFace(GL11.GL_BACK);

        LevelShader.INSTANCE.bind();

		LevelShader.INSTANCE.setUniform("texture_sampler", 0);
		//LevelShader.INSTANCE.setUniform("normal_sampler", 1);
		
		ObjectRendererRegistry.registerRenderer(Pyramid.class, new PyramidRenderer());
		ObjectRendererRegistry.registerRenderer(Cone.class, new ConeRenderer());
	}
	
	public void updateRenderOptions(RenderOptions options) {
		LevelShader.INSTANCE.setFogUniforms(options.getFog());
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, options.getPolygonMode());
	}
	
	@Override
	public void destroy() {
		ObjectRendererRegistry.destroyAllRenderers();
		LevelShader.INSTANCE.destroy();

	    GL20.glDisableVertexAttribArray(0);
	    GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);

	    GL30.glBindVertexArray(0);
	}
}
