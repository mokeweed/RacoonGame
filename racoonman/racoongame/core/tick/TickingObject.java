package racoonman.racoongame.core.tick;

public interface TickingObject<T extends TickContext> {
	void tick(T tickContext);
	
	@SuppressWarnings("unchecked")
	default void tick() {
		this.tick((T) EmptyTickContext.INSTANCE);
	}
}
