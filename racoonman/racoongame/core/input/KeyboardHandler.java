package racoonman.racoongame.core.input;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import racoonman.racoongame.core.tick.TickingObject;

public class KeyboardHandler implements TickingObject<InputTickContext> {
	private Table<Integer, Integer, CallbackInstance> keyboardCallbacks;
	private Table<Integer, Integer, CallbackInstance> mouseCallbacks;
	
	public KeyboardHandler() {
		this.keyboardCallbacks = HashBasedTable.create();
		this.mouseCallbacks = HashBasedTable.create();
	}

	@Override
	public void tick(InputTickContext tickContext) {
		long windowHandle = tickContext.getHandle();
		
		this.keyboardCallbacks.cellSet().forEach((cell) -> {
			int status = GLFW.glfwGetKey(windowHandle, cell.getRowKey());
			
			if(status == cell.getColumnKey())
				cell.getValue().onPress();
			if(cell.getValue().getPrevTickStatus() == GLFW.GLFW_PRESS && status != GLFW.GLFW_PRESS) {
				cell.getValue().prevTickStatus = status;
				cell.getValue().onRelease();
			}
			cell.getValue().update();
		});
		
		this.mouseCallbacks.cellSet().forEach((cell) -> {
			if(GLFW.glfwGetMouseButton(windowHandle, cell.getRowKey()) == cell.getColumnKey());
				cell.getValue().onPress();
			cell.getValue().update();
		});
	}
	
	public void addKeyCallback(int key, Callback pressCallback, int cooldown) {
		this.addKeyCallback(key, GLFW.GLFW_PRESS, pressCallback, () -> {}, cooldown);
	}
	
	public void addKeyCallback(int key, Callback pressCallback) {
		this.addKeyCallback(key, GLFW.GLFW_PRESS, pressCallback, () -> {}, 0);
	}
	
	public void addKeyCallback(int key, Callback onKeyPressCallback, Callback onKeyUnpressCallback) {
		this.addKeyCallback(key, GLFW.GLFW_PRESS, onKeyPressCallback, onKeyUnpressCallback, 0);
	}
	
	private void addKeyCallback(int key, int action, Callback keyReleaseCallback, Callback releaseCallback, int interval) {
		this.keyboardCallbacks.put(key, action, new CallbackInstance(keyReleaseCallback, releaseCallback, interval));
	}
	
	public void addMouseCallback(int key, Callback pressCallback, int cooldown) {
		this.addMouseCallback(key, GLFW.GLFW_PRESS, pressCallback, () -> {}, cooldown);
	}
	
	public void addMouseCallback(int key, Callback pressCallback) {
		this.addMouseCallback(key, GLFW.GLFW_PRESS, pressCallback, () -> {}, 0);
	}
	
	public void addMouseCallback(int key, Callback pressCallback, Callback releaseCallback) {
		this.addMouseCallback(key, GLFW.GLFW_PRESS, pressCallback, releaseCallback, 0);
	}

	private void addMouseCallback(int button, int action, Callback pressCallback, Callback releaseCallback, int interval) {
		this.mouseCallbacks.put(button, action, new CallbackInstance(pressCallback, releaseCallback, interval));
	}	
	
	public static class CallbackInstance {
		private Callback pressCallback;
		private Callback releaseCallback;
		//Some keys, like w, a, s, and d, must be updated every tick. Others, such as toggle keys, however, must be given an interval before they are able to be pressed again
		private int interval;
		private int cooldown;
		private int prevTickStatus;
		
		public CallbackInstance(Callback pressCallback, Callback releaseCallback, int interval) {
			this.pressCallback = pressCallback;
			this.releaseCallback = releaseCallback;
			this.interval = interval;
		}
		
		public void onPress() {
			if(this.cooldown == 0) {
				this.pressCallback.invoke();
				this.cooldown = this.interval;
				this.prevTickStatus = GLFW.GLFW_PRESS;
			}
		}
		
		public void onRelease() {
			this.releaseCallback.invoke();
		}
		
		public void setPrevTickStatus(int newStatus) {
			this.prevTickStatus = newStatus;
		}
		
		public int getPrevTickStatus() {
			return this.prevTickStatus;
		}
		
		public void update() {
			if(this.cooldown > 0)
				this.cooldown--;
		}
	}
	
	public static interface Callback {
		void invoke();
	}
}
