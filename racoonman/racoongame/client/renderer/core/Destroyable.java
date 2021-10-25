package racoonman.racoongame.client.renderer.core;

//For OpenGL objects that contain resources that must be destroyed upon exit, such as off heap buffers
public interface Destroyable {
	void destroy();
}
