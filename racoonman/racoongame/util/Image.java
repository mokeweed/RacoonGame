package racoonman.racoongame.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import racoonman.racoongame.client.renderer.texture.Texture;

public class Image {
	private ByteBuffer data;
	private int width;
	private int height;

	private Image(ByteBuffer data, int width, int height) {
		this.data = data;
		this.width = width;
		this.height = height;
	}
	
	public ByteBuffer getData() {
		return this.data;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}

	public int getARGB(int x, int z) {
		byte r = this.getRed(x, z);
		byte g = this.getGreen(x, z);
		byte b = this.getBlue(x, z);
		byte a = this.getAlpha(x, z);
		
		return ((0xFF & a) << 24) | ((0xFF & r) << 16)
         | ((0xFF & g) << 8) | (0xFF & b);
	}
	
	public byte getRed(int x, int z) {
		return this.getByteAtIndex(x, z, 0);
	}

	public byte getGreen(int x, int z) {
		return this.getByteAtIndex(x, z, 1);
	}

	public byte getBlue(int x, int z) {
		return this.getByteAtIndex(x, z, 2);
	}

	public byte getAlpha(int x, int z) {
		return this.getByteAtIndex(x, z, 3);
	}

	private byte getByteAtIndex(int x, int z, int channel) {
		return this.data.get(x * 4 + channel + z * 4 * this.width);
	}
	
	public void release() {
		MemoryUtil.memFree(this.data);
	}
	
	public static Image loadImage(String path) {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			try(InputStream in = Texture.class.getResourceAsStream(path)) {
				if(in == null)
					throw new IOException("Resource [" + path + "] does not exist");
				byte[] imageBytes = in.readAllBytes();
				
				ByteBuffer imageBuf = MemoryUtil.memAlloc(imageBytes.length);
				imageBuf.put(imageBytes);
				imageBuf.flip();
				
			    IntBuffer wBuf = stack.mallocInt(1);
			    IntBuffer hBuf = stack.mallocInt(1);
			    
			    IntBuffer avChannels = stack.mallocInt(1);
			    ByteBuffer decodedImage = STBImage.stbi_load_from_memory(imageBuf, wBuf, hBuf, avChannels, 4);

			    if(decodedImage == null)
			    	throw new IOException("Unable to load image [" + path + "], reason [" + STBImage.stbi_failure_reason() + "]");
			    
			    MemoryUtil.memFree(imageBuf);
			    return new Image(decodedImage, wBuf.get(), hBuf.get());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
