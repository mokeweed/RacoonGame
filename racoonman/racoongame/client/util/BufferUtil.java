package racoonman.racoongame.client.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import racoonman.racoongame.util.Util;

public class BufferUtil {

	public static FloatBuffer wrap(float[] data) {
		return Util.make(MemoryUtil.memAllocFloat(data.length), (buffer) -> {
			buffer.put(data);
			buffer.flip();
		});
	}
	
	public static IntBuffer wrap(int[] data) {
		return Util.make(MemoryUtil.memAllocInt(data.length), (buffer) -> {
			buffer.put(data);
			buffer.flip();
		});
	}
}
