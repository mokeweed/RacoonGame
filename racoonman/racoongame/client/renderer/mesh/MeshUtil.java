package racoonman.racoongame.client.renderer.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import racoonman.racoongame.util.Util;

public class MeshUtil {
	
	//TODO merge with mesh class
	private static void bindIndices(int[] data) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
	}

	private static int genVAO() {
		return Util.make(GL30.glGenVertexArrays(), GL30::glBindVertexArray);
	}
	
	public static void writeToBuffer(int attrIdx, int dimensions, float[] data) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);		
		GL20.glVertexAttribPointer(attrIdx, dimensions, GL11.GL_FLOAT, false, 0, 0);	
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static Mesh createMesh(float[] positions, float[] uvs, float[] normals, int[] indices) {
		int vao = genVAO();
		writeToBuffer(0, 3, positions);
		writeToBuffer(1, 2, uvs);
		writeToBuffer(2, 3, normals);
		bindIndices(indices);
		GL30.glBindVertexArray(0);
		return new Mesh(vao, indices.length);
	}
}
