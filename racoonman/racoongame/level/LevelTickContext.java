package racoonman.racoongame.level;

import racoonman.racoongame.core.tick.TickContext;

public class LevelTickContext implements TickContext {
	private Level level;
	
	private LevelTickContext(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return this.level;
	}
	
	public static LevelTickContext of(Level level) {
		return new LevelTickContext(level);
	}
}
