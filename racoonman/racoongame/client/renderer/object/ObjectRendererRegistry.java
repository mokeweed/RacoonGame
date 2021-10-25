package racoonman.racoongame.client.renderer.object;

import java.util.HashMap;
import java.util.Map;

import racoonman.racoongame.object.GameObject;

public class ObjectRendererRegistry {
	private static final Map<Class<? extends GameObject<?>>, ObjectRenderer<? extends GameObject<?>>> RENDERERS = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <T extends GameObject<?>> ObjectRenderer<T> getRenderer(Class<GameObject<?>> cls) {
		return (ObjectRenderer<T>) RENDERERS.get(cls);
	}
	
	public static void registerRenderer(Class<? extends GameObject<?>> type, ObjectRenderer<? extends GameObject<?>> renderer) {
		RENDERERS.put(type, renderer);
	}
	
	public static void destroyAllRenderers() {
		RENDERERS.values().forEach(ObjectRenderer::destroy);
	}
}
