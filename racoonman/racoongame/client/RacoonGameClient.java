package racoonman.racoongame.client;

import java.util.Random;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import racoonman.racoongame.client.camera.Camera;
import racoonman.racoongame.client.camera.CameraTickContext;
import racoonman.racoongame.client.renderer.GameRenderTickContext;
import racoonman.racoongame.client.renderer.GameRenderer;
import racoonman.racoongame.client.renderer.RenderOptions;
import racoonman.racoongame.client.renderer.level.effects.Fog;
import racoonman.racoongame.client.window.Window;
import racoonman.racoongame.client.window.WindowTickContext;
import racoonman.racoongame.core.input.InputTickContext;
import racoonman.racoongame.core.input.KeyboardHandler;
import racoonman.racoongame.core.input.MouseHandler;
import racoonman.racoongame.level.Level;
import racoonman.racoongame.level.LevelManager;
import racoonman.racoongame.level.TileGraph;
import racoonman.racoongame.level.gen.LevelGenerator;
import racoonman.racoongame.level.tile.TileType;
import racoonman.racoongame.player.ClientPlayer;
import racoonman.racoongame.player.PlayerInputTickContext;
import racoonman.racoongame.util.Util;

public class RacoonGameClient implements Runnable, AutoCloseable {
	public static final Random RAND = new Random();
	private static final int FPS = 144;
	private Window display;
	private LevelManager levelManager;
	private Camera camera;
	private RenderOptions options;
	private Thread renderThread;
	private MouseHandler mouseHandler;
	private KeyboardHandler keyboardHandler;
	
	public RacoonGameClient(String[] args) {
		this.display = Util.make(Window.createWindow("Racoon Game", 1200, 650), (window) -> window.setIcon("icon"));
		this.levelManager = Util.make(new LevelManager(), (levelManager) -> levelManager.makeLevel("main", 
				LevelGenerator.genLevel(40, 20)));
		this.camera = new Camera();
		this.options = new RenderOptions(new Fog(true, new Vector3f(0.4F, 0.4F, 0.4F), 0.06F), 80.0F, 
				GL11.GL_TRIANGLES, true, true);
	}
	
	@Override
	public void run() {
		GLFWErrorCallback.createPrint(System.err);
		this.display.show();
		
		Level level = this.levelManager.getLevel("main");
		Vector3i spawnPos = level.getSpawnPos();
		
		ClientPlayer player = new ClientPlayer(level, new Vector3f(spawnPos.x, spawnPos.y, spawnPos.z));
		this.mouseHandler = Util.make(new MouseHandler(), (handler) -> handler.setSensitivity(1.2F));
		this.keyboardHandler = Util.make(new KeyboardHandler(), this::addKeyCallbacks);
		player.addDefaultKeyListeners(this.keyboardHandler);
		
		//initialization is over, swap context to render thread
		GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
		this.renderThread = new Thread(() -> {
			this.display.makeCurrent();
			GL.createCapabilities();
			
			GameRenderer renderer = new GameRenderer(this.display, level);
			renderer.init();
	
			while(this.display.isOpen()) {
				renderer.tick(GameRenderTickContext.of(this.options, player, level, this.camera));
			}
			
			renderer.destroy();
		}, "Render Thread");
		this.renderThread.setDaemon(true);
		this.renderThread.start();
		
		while(this.display.isOpen()) {
			this.camera.tick(CameraTickContext.of(player));
			this.display.tick(WindowTickContext.of(FPS));
			
			InputTickContext inCtx = InputTickContext.of(this.display.getWidth(), this.display.getHeight(), this.display.getHandle());
			
			this.mouseHandler.tick(inCtx);
			this.keyboardHandler.tick(inCtx);
			
			level.tick();
			player.tick(PlayerInputTickContext.of((float)this.mouseHandler.getDeltaX(), (float)this.mouseHandler.getDeltaY()));
		}
	}

	@Override
	public void close() throws Exception {
		GLFW.glfwTerminate();
		GL.destroy();
	}
	
	public void addKeyCallbacks(KeyboardHandler keyboardHandler) {
		//toggle fog
		keyboardHandler.addKeyCallback(GLFW.GLFW_KEY_V, () -> {
			Fog fog = this.options.getFog();
			fog.setEnabled(!fog.isEnabled());
			this.options.setUpdated(true);
		}, 20);
		
		keyboardHandler.addKeyCallback(GLFW.GLFW_KEY_1, () -> {
			this.options.setPolygonMode(GL11.GL_FILL);
		});
		
		keyboardHandler.addKeyCallback(GLFW.GLFW_KEY_2, () -> {
			this.options.setPolygonMode(GL11.GL_LINE);
		});
		
		keyboardHandler.addKeyCallback(GLFW.GLFW_KEY_3, () -> {
			this.options.setPolygonMode(GL11.GL_POINT);
		});		
		
		keyboardHandler.addKeyCallback(GLFW.GLFW_KEY_4, () -> {
			this.options.setAntialias(true);
		});
		
		keyboardHandler.addKeyCallback(GLFW.GLFW_KEY_5, () -> {
			this.options.setAntialias(false);
		});
		
		keyboardHandler.addKeyCallback(GLFW.GLFW_KEY_LEFT, () -> {
			float fov = this.options.getFov();
			float newFov = fov - 10;
			
			if(newFov > 0) 
				this.options.setFov(newFov);
		}, 20);
		
		keyboardHandler.addKeyCallback(GLFW.GLFW_KEY_RIGHT, () -> {
			float fov = this.options.getFov();
			float newFov = fov + 10;
			
			if(newFov < 180) {
				this.options.setFov(newFov);
			}
		}, 20);
		
		//toggle zoom
		keyboardHandler.addKeyCallback(GLFW.GLFW_KEY_X, () -> {
			this.options.setFov(30.0F);
		}, () -> {
			this.options.setFov(80.0F);
		});
	}

	public static void main(String[] args) throws Exception {
		RacoonGameClient client = new RacoonGameClient(args);
		
		try {
			client.run();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}
}