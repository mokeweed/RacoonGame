package racoonman.racoongame.client.renderer.level;

import org.joml.Vector3f;
import org.joml.Vector3i;

import racoonman.racoongame.client.camera.Camera;
import racoonman.racoongame.client.renderer.core.Material;
import racoonman.racoongame.client.renderer.mesh.Mesh;
import racoonman.racoongame.client.renderer.mesh.MeshUtil;
import racoonman.racoongame.client.renderer.shader.LevelShader;
import racoonman.racoongame.core.Direction;
import racoonman.racoongame.level.Level;
import racoonman.racoongame.level.TileGraph;
import racoonman.racoongame.level.tile.Tile;

public class TileRenderer {
	public static final float[] UVS = {
		 0, 0,
		 1, 0,
		 1, 1,
		 0, 1
	};

	public static void render(Camera camera, Tile square, Level level) {
		Vector3i tilePos = square.getPosition();
		Vector3i offsetPos = new Vector3i(tilePos);
		TileGraph graph = level.getTileGraph();
		
		for(Face face : Face.values()) {
			if(shouldRenderFace(square, face, camera)) {
				//reset position and add the offset
				offsetPos.set(tilePos).add(face.stepX, face.stepY, face.stepZ);
				Tile tile = graph.get(offsetPos);
				if(tile == null || tile.getType().isTransparent() || !tile.getType().hasModel()) {
					Mesh faceMesh = face.getMesh();
		
					Material mat = square.getType().getMaterial();
						
					mat.bind();
					faceMesh.bind();
					faceMesh.render();
					faceMesh.unBind();
					mat.unBind();
				}
			}
		}
	}
	
	public static boolean shouldRenderFace(Tile tile, Face face, Camera cam) {
		Vector3i tilePos = tile.getPosition();
		Vector3f tilePosF = new Vector3f(tilePos.x, tilePos.y, tilePos.z);
		Vector3f cameraPosition = cam.getPosition();// new Vector3f(2.0F, 2.0F, 2.0F);
		Direction direction = cam.getHorizontalFacingDirection();//Direction.NORTH;
		
		switch(face) {
			case NORTH: return tilePos.x < cameraPosition.x;
			case SOUTH: return tilePos.x > cameraPosition.x;
			
			case EAST: return tilePos.z < cameraPosition.z;
			case WEST: return tilePos.z > cameraPosition.z;
			
			case DOWN: return cameraPosition.y < tilePos.y;
			case UP: return tilePos.y < cameraPosition.y;
		
			default: return true;//TODO add facing dependent rendering
		}
	}
	
	public static enum Face {
		NORTH(
			new float[] { 
				1.0F,  1.0F, -1.0F,
				1.0F,  1.0F,  1.0F,
				1.0F, -1.0F,  1.0F,
				1.0F, -1.0F, -1.0F
			}, 
			new int[] {
				0, 1, 2,
				2, 3, 0
		}, new float[] {
			0.0F, -1.0F, 0.0F,
			0.0F, -1.0F, 0.0F
		}, UVS, 1, 0, 0),
		EAST(
			new float[] {
			   -1.0F,  1.0F, 1.0F,
			    1.0F,  1.0F, 1.0F,
			    1.0F, -1.0F, 1.0F,
			   -1.0F, -1.0F, 1.0F
			}, 
			new int[] {
				0, 1, 2,
				2, 3, 0
		}, new float[] {
			0.0F, 1.0F, 0.0F,
			0.0F, 1.0F, 0.0F
		}, UVS, 0, 0, 1),
		SOUTH(
			new float[] {
			   -1.0F,  1.0F, -1.0F,
			   -1.0F,  1.0F,  1.0F,
			   -1.0F, -1.0F,  1.0F,
			   -1.0F, -1.0F, -1.0F
			}, 
			new int[] {
				0, 1, 2,
				2, 3, 0
				
		}, new float[] {
				0.0F, 1.0F, 0.0F,
				0.0F, 1.0F, 0.0F
		}, UVS, -1, 0, 0),
		WEST(
			new float[] {
			  -1.0F,  1.0F, -1.0F,
			   1.0F,  1.0F, -1.0F,
			   1.0F, -1.0F, -1.0F,
			  -1.0F, -1.0F, -1.0F,
			}, 
			new int[] {
			   0, 1, 2,
			   2, 3, 0
		}, new float[] {
				0.0F, -1.0F, 0.0F,
				0.0F, -1.0F, 0.0F
		}, UVS, 0, 0, -1),
		UP(
			new float[] {
				 1.0F, 1.0F, -1.0F,
				 1.0F, 1.0F,  1.0F,
				-1.0F, 1.0F,  1.0F,
				-1.0F, 1.0F, -1.0F
			}, 
			new int[] {
				 0, 1, 2,
				 2, 3, 0
		}, new float[] {
				1, 0, 0,
				1, 0, 0
		}, UVS, 0, 1,  0),
		DOWN(
			new float[] {
				 1.0F, -1.0F, -1.0F,
				 1.0F, -1.0F,  1.0F,
				-1.0F, -1.0F,  1.0F,
				-1.0F, -1.0F, -1.0F
			},
			new int[] {
				0, 1, 2,
				2, 3, 0
		}, new float[] {
				-1.0F, 0.0F, 0.0F,
				-1.0F, 0.0F, 0.0F
		}, UVS, 0, -1, 0);
		private Mesh mesh;
		private int stepX;
		private int stepY;
		private int stepZ;
		
		private Face(float[] vertices, int[] indices, float[] normals, float[] uvs, int stepX, int stepY, int stepZ) {
			this.mesh = MeshUtil.createMesh(vertices, uvs, normals, indices);
			this.stepX = stepX;
			this.stepY = stepY;
			this.stepZ = stepZ;
		}
		
		public Mesh getMesh() {
			return this.mesh;
		}
		
		public int getStepX() {
			return this.stepX;
		}
		
		public int getStepY() {
			return this.stepY;
		}
		
		public int getStepZ() {
			return this.stepZ;
		}
	}
}
