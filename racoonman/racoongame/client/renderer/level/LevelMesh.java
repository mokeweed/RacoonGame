package racoonman.racoongame.client.renderer.level;

import java.util.ArrayList;
import java.util.List;

import racoonman.racoongame.client.renderer.mesh.Mesh;
import racoonman.racoongame.client.renderer.mesh.MeshUtil;
import racoonman.racoongame.client.renderer.texture.Texture;
import racoonman.racoongame.util.Image;
import racoonman.racoongame.util.MathUtil;
import racoonman.racoongame.util.Util;

public class LevelMesh {
	private static final int MAX_COLOR = 255 * 255 * 255;
	private static final float START_X = -0.5F;
	private static final float START_Z = -0.5F;
	private float minY;
	private float maxY;
	private Mesh mesh;
	
	public LevelMesh(float minY, float maxY, Image image, Texture texture, int textureIncrement) {
		this.minY = minY;
		this.maxY = maxY;
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		float incx = getXLength() / (width - 1);
        float incz = getZLength() / (height - 1);

        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        
        for (int row = 0; row < height; row++) {
        	for (int col = 0; col < width; col++) {
        		// Create vertex for current position
        		positions.add(START_X + col * incx); // x
                positions.add(this.getHeight(col, row, width, image)); //y
                positions.add(START_Z + row * incz); //z

                // Set texture coordinates
                textCoords.add((float) textureIncrement * (float) col / (float) width);
                textCoords.add((float) textureIncrement * (float) row / (float) height);

                // Create indices
                if (col < width - 1 && row < height - 1) {
                    int leftTop = row * width + col;
                    int leftBottom = (row + 1) * width + col;
                    int rightBottom = (row + 1) * width + col + 1;
                    int rightTop = row * width + col + 1;

                    indices.add(leftTop);
                    indices.add(leftBottom);
                    indices.add(rightTop);

                    indices.add(rightTop);
                    indices.add(leftBottom);
                    indices.add(rightBottom);
                }
            }
        }
        float[] posArr = Util.unboxFloats(positions.toArray(Float[]::new));
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        float[] textCoordsArr = Util.unboxFloats(textCoords.toArray(Float[]::new));
        float[] normalsArr = MathUtil.calcNormals(posArr, width, height);
        this.mesh = MeshUtil.createMesh(posArr, textCoordsArr, normalsArr, indicesArr);
//        Material material = new Material(texture, 0.0f);
//        mesh.setMaterial(material);	
	}
	
	public Mesh getMesh() {
		return this.mesh;
	}
	
	public static float getXLength() {
		return Math.abs(-START_X * 2);
	}

	public static float getZLength() {
		return Math.abs(-START_Z * 2);
	}
	
	private float getHeight(int x, int z, int width, Image image) {
	    return this.minY + Math.abs(this.maxY - this.minY) * ((float) image.getARGB(x, z) / (float) MAX_COLOR);
	}
}
