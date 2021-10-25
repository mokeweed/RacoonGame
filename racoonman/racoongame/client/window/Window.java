package racoonman.racoongame.client.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import racoonman.racoongame.core.tick.TickingObject;
import racoonman.racoongame.util.Image;

public class Window implements TickingObject<WindowTickContext> {
	private static boolean initialized;
	private boolean isOpen;
	private long handle;
	private int width;
	private int height;
	private boolean fullscreen;
	private Sync sync;
	private boolean resized;
	
	private Window(long window, int width, int height, boolean fullscreen) {
		this.handle = window;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.sync = new Sync();
		
		GLFW.glfwSetFramebufferSizeCallback(window, (w, newW, newH) -> {
			this.width = newW;
			this.height = newH;
			this.resized = true;
		});
	}
	
	@Override
	public void tick(WindowTickContext ctx) {
		this.isOpen = !GLFW.glfwWindowShouldClose(this.handle);
		this.sync.sync(ctx.getMaxFps());
		GLFW.glfwPollEvents();
	}
	
	public boolean isOpen() {
		return this.isOpen;
	}

	public long getHandle() {
		return this.handle;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public boolean isResized() {	
		return this.resized;
	}
	
	public void setResized(boolean resized) {
		this.resized = resized;
	}
	
	public void clearBuffers() {
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(this.handle);
	}
	
	public void makeCurrent() {
		GLFW.glfwMakeContextCurrent(this.handle);
	}
	
	public void show() {
		GLFW.glfwShowWindow(this.handle);
		this.isOpen = true;
	}
	
	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(this.handle, title);
	}
	
	public boolean isFullscreen() {
		return this.fullscreen;
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
	public void setIcon(Image img) {
		GLFW.glfwSetWindowIcon(this.handle, new GLFWImage.Buffer(img.getData()));
	}
	
	public void setIcon(String name) {
		Image icon = Image.loadImage("/racoongame/textures/misc/" + name + ".png");
		GLFWImage image = GLFWImage.malloc(); 
		GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
		image.set(icon.getWidth(), icon.getHeight(), icon.getData());
		imagebf.put(0, image);
		GLFW.glfwSetWindowIcon(this.handle, imagebf);
	}
	
	public static Window createFullscreen(String title) {
		maybeInit();//must be called before glfwGetVideoMode
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		return createDisplay(title, videoMode.width(), videoMode.height(), true);
	}
	
	public static Window createWindow(String title, int width, int height) {
		return createDisplay(title, width, height, false);
	}
	
	private static Window createDisplay(String title, int width, int height, boolean fullscreen) {
		maybeInit();
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		long window = GLFW.glfwCreateWindow(width, height, title, fullscreen ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL, MemoryUtil.NULL);
		Window display = new Window(window, width, height, fullscreen);
		display.makeCurrent();
		GL.createCapabilities();
		return display;
	}
	 
	private static void maybeInit() {
		if(!initialized)
			if(!(initialized = GLFW.glfwInit()))
				throw new IllegalStateException("Unable to initialize GLFW");
	}
}
