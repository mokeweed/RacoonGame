package racoonman.racoongame.client.renderer.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import racoonman.racoongame.client.renderer.core.Destroyable;

public class Mesh implements Destroyable {
	private int vaoId;
	private int indiceCount;
	
	public Mesh(int vaoId, int indiceCount) {
		this.vaoId = vaoId;
		this.indiceCount = indiceCount;
	}
	
	public void bind() {
		GL30.glBindVertexArray(this.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
	}

	public void render() {
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.getIndiceCount(), GL11.GL_UNSIGNED_INT, 0);
	}
	
	public void unBind() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	public int getVaoId() {
		return this.vaoId;
	}
	
	public int getIndiceCount() {
		return this.indiceCount;
	}

	@Override
	public void destroy() {
		GL20.glDeleteBuffers(this.vaoId);
	}
}
