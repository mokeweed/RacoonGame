package racoonman.racoongame.object;

import org.joml.Vector3f;

import racoonman.racoongame.core.tick.EmptyTickContext;
import racoonman.racoongame.level.Level;

public class Pyramid extends GameObject<EmptyTickContext> {

	public Pyramid(Level level, Vector3f pos) {
		super(level, pos);
	}
}
