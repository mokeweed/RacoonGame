package racoonman.racoongame.client.renderer.mesh;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class OBJLoader {
	
	public static final Mesh loadMeshFromOBJ(String modelName) {
		try(InputStream in = OBJLoader.class.getResourceAsStream("/racoongame/models/" + modelName + ".obj")) {
			if(in == null)
				throw new IOException("Resource [" + modelName + "] does not exist");
			String contents = new String(in.readAllBytes());
			List<Vector3f> vertices = new ArrayList<>();
			List<Vector2f> textures = new ArrayList<>();
			List<Vector3f> normals = new ArrayList<>();
			List<Face> faces = new ArrayList<>();
			
			contents.lines().forEach((line) -> {
				String[] tokens = line.split("\\s+");
			    switch (tokens[0]) {
			        case "v":
			            // Geometric vertex
			            Vector3f vec3f = new Vector3f(
			                Float.parseFloat(tokens[1]),
			                Float.parseFloat(tokens[2]),
			                Float.parseFloat(tokens[3]));
			            vertices.add(vec3f);
			            break;
			        case "vt":
			            // Texture coordinate
			            Vector2f vec2f = new Vector2f(
			                Float.parseFloat(tokens[1]),
			                Float.parseFloat(tokens[2]));
			            textures.add(vec2f);
			            break;
			        case "vn":
			            // Vertex normal
			            Vector3f vec3fNorm = new Vector3f(
			                Float.parseFloat(tokens[1]),
			                Float.parseFloat(tokens[2]),
			                Float.parseFloat(tokens[3]));
			            normals.add(vec3fNorm);
			            break;
			        case "f":
			            Face face = new Face(tokens[1], tokens[2], tokens[3]);
			            faces.add(face);
			            break;
			        default:
			            // Ignore other lines
			            break;
			    }
			});
			return reorderLists(vertices, textures, normals, faces);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private static Mesh reorderLists(List<Vector3f> posList, List<Vector2f> textCoordList,
		List<Vector3f> normList, List<Face> facesList) {

		List<Integer> indices = new ArrayList<>();
		// Create position array in the order it has been declared
		float[] posArr = new float[posList.size() * 3];
		int i = 0;
		for (Vector3f pos : posList) {
			posArr[i * 3] = pos.x;
			posArr[i * 3 + 1] = pos.y;
			posArr[i * 3 + 2] = pos.z;
			i++;
		}
		
		float[] textCoordArr = new float[posList.size() * 2];
		float[] normArr = new float[posList.size() * 3];
		
		for (Face face : facesList) {
			IndexGroup[] faceVertexIndices = face.getFaceVertexIndices();
			for (IndexGroup indValue : faceVertexIndices) {
				processFaceVertex(indValue, textCoordList, normList,
						indices, textCoordArr, normArr);
			}	
		}	
		    
		int[] indicesArr = indices.stream().mapToInt((v) -> v).toArray();
		return MeshUtil.createMesh(posArr, textCoordArr, normArr, indicesArr);
	}

	private static void processFaceVertex(IndexGroup indices, List<Vector2f> textCoordList,
		List<Vector3f> normList, List<Integer> indicesList,
		float[] texCoordArr, float[] normArr) {
		
		// Set index for vertex coordinates
		int posIndex = indices.getIndexPos();
		indicesList.add(posIndex);
		
		// Reorder texture coordinates
		if (indices.getTexCoordIndex()>= 0) {
			Vector2f textCoord = textCoordList.get(indices.getTexCoordIndex());
			texCoordArr[posIndex * 2] = textCoord.x;
			texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
		}
		
		if (indices.getNormalIndex() >= 0) {
			// Reorder vectornormals
			Vector3f vecNorm = normList.get(indices.getNormalIndex());
			
			normArr[posIndex * 3] = vecNorm.x;
			normArr[posIndex * 3 + 1] = vecNorm.y;
			normArr[posIndex * 3 + 2] = vecNorm.z;
		}
	}
	
	protected static class IndexGroup {
		protected static final int NONE = -1;
		private int indexPosition;
		private int texCoordIndex;
		private int normalIndex;

		public IndexGroup() {
			this.indexPosition = NONE;
			this.texCoordIndex = NONE;
			this.normalIndex = NONE;
		}
		
		public void setIndexPos(int indexPos) {
			this.indexPosition = indexPos;
		}
		
		public int getIndexPos() {
			return this.indexPosition;
		}
		
		public void setTexCoordIndex(int texCoordIndex) {
			this.texCoordIndex = texCoordIndex;
		}
		
		public int getTexCoordIndex() {
			return this.texCoordIndex;
		}

		public void setNormalIndex(int normalIndex) {
			this.normalIndex = normalIndex;
		}
		
		public int getNormalIndex() {
			return this.normalIndex;
		}
	}
	
	protected static class Face {
	    private IndexGroup[] groups = new IndexGroup[3];

	    public Face(String v1, String v2, String v3) {
	    	this.groups = new IndexGroup[3];
	    	this.groups[0] = parseLine(v1);
	    	this.groups[1] = parseLine(v2);
	    	this.groups[2] = parseLine(v3);
	    }

	    private IndexGroup parseLine(String line) {
	        IndexGroup group = new IndexGroup();

	        String[] lineTokens = line.split("/");
	        int length = lineTokens.length;
	        group.setIndexPos(Integer.parseInt(lineTokens[0]) - 1);

	        if (length > 1) {
	            String textCoord = lineTokens[1];
	            group.setTexCoordIndex(textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IndexGroup.NONE);
	            if (length > 2) {
	                group.setNormalIndex(Integer.parseInt(lineTokens[2]) - 1);
	            }
	        }

	        return group;
	    }

	    public IndexGroup[] getFaceVertexIndices() {
	        return this.groups;
	    }
	}
}
