package racoonman.racoongame.player;

import racoonman.racoongame.core.input.InputTickContext;
import racoonman.racoongame.core.tick.TickContext;

public class PlayerTickContext implements TickContext {
	private InputTickContext inputTickCtx;
	
	private PlayerTickContext(InputTickContext inputTickCtx) {
		this.inputTickCtx = inputTickCtx;
	}
	
	public InputTickContext getInputTickContext() {
		return this.inputTickCtx;
	}
	
	public static PlayerTickContext of(InputTickContext inputTickCtx) {
		return new PlayerTickContext(inputTickCtx);
	}
}
