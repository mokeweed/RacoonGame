package racoonman.racoongame.level;

import java.util.HashMap;
import java.util.Map;

public class LevelManager {
	private Map<String, Level> levels;
	
	public LevelManager() {
		this.levels = new HashMap<>();
	}
	
	public void makeLevel(String levelKey, Level level) {
		this.levels.put(levelKey, level);
	}
	
	public Level getLevel(String levelKey) {
		return this.levels.get(levelKey);
	}
}
