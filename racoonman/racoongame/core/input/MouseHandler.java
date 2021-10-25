package racoonman.racoongame.core.input;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

import racoonman.racoongame.core.tick.TickingObject;

public class MouseHandler implements TickingObject<InputTickContext> {
	private static final double MOUSE_UNITS = 5.0D;
	private boolean mouseLocked;
	
	private double newX;
	private double newY;
	private double prevX;
	private double prevY;
	private double deltaX;
	private double deltaY;
	
	private double sensitivity;
	
	@Override
	public void tick(InputTickContext ctx) {
		long handle = ctx.getHandle();
		double displayW = ctx.getDisplayWidth();
		double displayH = ctx.getDisplayHeight();
		
	    glfwSetInputMode(ctx.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	    
		if (!this.mouseLocked && glfwGetMouseButton(handle, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
            glfwSetCursorPos(handle, displayW/2, displayH/2);

            this.mouseLocked = true;
        }

        if (this.mouseLocked) {
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

            glfwGetCursorPos(handle, x, y);
            
            x.rewind();
            y.rewind();

            this.newX = x.get();
            this.newY = y.get();
            
            this.deltaX = this.newX - this.prevX;
            this.deltaY = this.newY - this.prevY;

            this.prevX = this.newX;
            this.prevY = this.newY;
        }
	}
	
	public double getDeltaX() {
		return (this.deltaX / MOUSE_UNITS) * this.sensitivity;
	}
	
	public double getDeltaY() {
		return (this.deltaY / MOUSE_UNITS) * this.sensitivity;
	}
	
	public void setSensitivity(double sensitivity) {
		this.sensitivity = 1.0;
	}
}	