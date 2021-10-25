package racoonman.racoongame.object;

import org.joml.Vector3f;

import racoonman.racoongame.core.tick.EmptyTickContext;
import racoonman.racoongame.level.Level;

public class Cone extends GameObject<EmptyTickContext> {

	public Cone(Level level, Vector3f pos) {
		super(level, pos);
	}
}
