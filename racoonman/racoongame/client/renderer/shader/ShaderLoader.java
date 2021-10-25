package racoonman.racoongame.client.renderer.shader;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderLoader {

	public static int loadShader(String path, int shaderType) {
		try(InputStream in = ShaderLoader.class.getResourceAsStream("/racoongame/shaders/" + path)) {
			if(in == null)
				throw new IllegalArgumentException("Unable to load shader [" + path + "]");
			String compiledShader = new String(in.readAllBytes());
			int shaderId = GL20.glCreateShader(shaderType);
			
			GL20.glShaderSource(shaderId, compiledShader);
			GL20.glCompileShader(shaderId);

			if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				System.err.println(GL20.glGetShaderInfoLog(shaderId, 1000));
				throw new IllegalStateException("Unable to compile shader [" + path + "]");
			}
			return shaderId;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
