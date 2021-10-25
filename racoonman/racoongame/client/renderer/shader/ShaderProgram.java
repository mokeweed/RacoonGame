package racoonman.racoongame.client.renderer.shader;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import racoonman.racoongame.client.renderer.core.Destroyable;

public abstract class ShaderProgram implements Destroyable {
	protected int programId;
	protected int vertexId;
	protected int fragmentId;
	private Map<String, Integer> uniforms;
	
	public ShaderProgram(String vertexShader, String fragmentShader) {
		this.vertexId = ShaderLoader.loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		this.fragmentId = ShaderLoader.loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
		this.programId = GL20.glCreateProgram();
		this.uniforms = new HashMap<>();
		
		GL20.glAttachShader(this.programId, this.vertexId);
		GL20.glAttachShader(this.programId, this.fragmentId);
		this.bindAttributes();
		GL20.glLinkProgram(this.programId);
		GL20.glValidateProgram(this.programId);
		this.createUniforms();
	}

	public void bind() {
		GL20.glUseProgram(this.programId);
	}
		
	public void unBind() {
		GL20.glUseProgram(0);//default render mode
	}
	
	public void bindAttribute(int attribute, String varName) {
		GL20.glBindAttribLocation(this.programId, attribute, varName);
	}
	
	public void setUniform(String location, float f) {
		GL20.glUniform1f(this.getUniformLocation(location), f);
	}
	
	public void setUniform(String location, double f) {
		GL20.glUniform1f(this.getUniformLocation(location), (float)f);
	}
	
	public void setUniform(String location, Vector3f vec) {
		GL20.glUniform3f(this.getUniformLocation(location), vec.x, vec.y, vec.z);
	}
	
	public void setUniform(String location, Vector3d vec) {
		GL20.glUniform3f(this.getUniformLocation(location), (float)vec.x, (float)vec.y, (float)vec.z);
	}
	
	public void setUniform(String location, boolean b) {
		GL20.glUniform1i(this.getUniformLocation(location), b ? 1 : 0);
	}
	
	public void setUniform(String location, Matrix4f mat) {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buf = stack.mallocFloat(16);
			GL20.glUniformMatrix4fv(this.getUniformLocation(location), false, mat.get(buf));
		}
	}
	
	public void setUniform(String location, Vector4f vec) {
		GL20.glUniform4f(this.getUniformLocation(location), vec.x, vec.y, vec.z, vec.w);
	}

	public void createUniform(String uniform) {
		int location = GL20.glGetUniformLocation(this.programId, uniform);
		if(location == -1)
			throw new IllegalArgumentException("Unknown uniform [" + uniform + "]");
		this.uniforms.put(uniform, location);
	}

	@Override
	public void destroy() {
		this.unBind();
		GL20.glDeleteProgram(this.programId);
	}
	
	private int getUniformLocation(String name) {
		Integer uniform = this.uniforms.get(name);
		if(uniform == null)
			throw new IllegalArgumentException("Unknown uniform [" + name + "]");
		return uniform;
	}
	
	protected abstract void createUniforms();
	
	protected abstract void bindAttributes();
}
