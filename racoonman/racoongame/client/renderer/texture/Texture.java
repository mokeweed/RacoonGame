package racoonman.racoongame.client.renderer.texture;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import racoonman.racoongame.util.Image;

public class Texture {
	private static final Map<String, Texture> CACHE = new HashMap<>();
	private int width;
	private int height;
	private int texId;

	private Texture(int width, int height, int id) {
		this.width = width;
		this.height = height;
		this.texId = id;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getId() {
		return this.texId;
	}
	
	public void bind(int textureId) {
		GL13.glActiveTexture(textureId);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.texId);
	}

	public void unBind() {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
	}
	
	public static Texture getTexture(String texture) {
		if(CACHE.containsKey(texture))
			return CACHE.get(texture);
		
		Image image = Image.loadImage("/racoongame/textures/" + texture + ".png");
		
		int width = image.getWidth();
		int height = image.getHeight();
		int texId = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, texId);
		
		//tell opengl how to unpack bytes
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		//set the texture parameters, can be GL_LINEAR or GL_NEAREST
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		//upload texture
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getData());
		
		//generate Mip Map
		glGenerateMipmap(GL_TEXTURE_2D);
		
		//data has been uploaded to the graphics card, so its buffer can be released
		image.release();
		return new Texture(width, height, texId);
	}
}
