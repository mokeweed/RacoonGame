package racoonman.racoongame.util;

import java.util.ArrayList;
import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class MathUtil {
	
	public static Quaternionf deltaF(Quaternionf source, Quaternionf offset) {
		return source.get(new Quaternionf()).add(-offset.x, -offset.y, -offset.z, -offset.w);
	}
	
	public static Vector3f deltaF(Vector3f source, Vector3f offset) {
		return source.get(new Vector3f()).sub(offset);
	}
	
	public static Vector3d deltaD(Vector3d source, Vector3d sub) {
		return source.get(new Vector3d()).sub(sub);
	}

	public static Vector3f deltaF(Vector3d source, Vector3f sub) {
		return source.get(new Vector3f()).sub(sub);
	}
	
	public static Vector3f deltaF(Vector3f source, Vector3d sub) {
		return source.get(new Vector3f()).sub(sub.get(new Vector3f()));
	}
	
	public static Vector3f deltaF(Vector3d source, Vector3d sub) {
		return source.get(new Vector3f()).sub(sub.get(new Vector3f()));
	}
	
	public static float clamp(float val, float min, float max) {
	    return Math.max(min, Math.min(max, val));
	}
	
	public static boolean isNegative(double n) {
		return n < 0.0D;
	}
		
	public static float[] calcNormals(float[] posArr, int width, int height) {
	    Vector3f v0 = new Vector3f();
	    Vector3f v1 = new Vector3f();
	    Vector3f v2 = new Vector3f();
	    Vector3f v3 = new Vector3f();
	    Vector3f v4 = new Vector3f();
	    Vector3f v12 = new Vector3f();
	    Vector3f v23 = new Vector3f();
	    Vector3f v34 = new Vector3f();
	    Vector3f v41 = new Vector3f();
	    List<Float> normals = new ArrayList<>();
	    Vector3f normal = new Vector3f();
	    for (int row = 0; row < height; row++) {
	        for (int col = 0; col < width; col++) {
	            if (row > 0 && row < height -1 && col > 0 && col < width -1) {
	                int i0 = row*width*3 + col*3;
	                v0.x = posArr[i0];
	                v0.y = posArr[i0 + 1];
	                v0.z = posArr[i0 + 2];

	                int i1 = row*width*3 + (col-1)*3;
	                v1.x = posArr[i1];
	                v1.y = posArr[i1 + 1];
	                v1.z = posArr[i1 + 2];                    
	                v1 = v1.sub(v0);

	                int i2 = (row+1)*width*3 + col*3;
	                v2.x = posArr[i2];
	                v2.y = posArr[i2 + 1];
	                v2.z = posArr[i2 + 2];
	                v2 = v2.sub(v0);

	                int i3 = (row)*width*3 + (col+1)*3;
	                v3.x = posArr[i3];
	                v3.y = posArr[i3 + 1];
	                v3.z = posArr[i3 + 2];
	                v3 = v3.sub(v0);

	                int i4 = (row-1)*width*3 + col*3;
	                v4.x = posArr[i4];
	                v4.y = posArr[i4 + 1];
	                v4.z = posArr[i4 + 2];
	                v4 = v4.sub(v0);

	                v1.cross(v2, v12);
	                v12.normalize();

	                v2.cross(v3, v23);
	                v23.normalize();

	                v3.cross(v4, v34);
	                v34.normalize();

	                v4.cross(v1, v41);
	                v41.normalize();

	                normal = v12.add(v23).add(v34).add(v41);
	                normal.normalize();
	            } else {
	                normal.x = 0;
	                normal.y = 1;
	                normal.z = 0;
	            }
	            normal.normalize();
	            normals.add(normal.x);
	            normals.add(normal.y);
	            normals.add(normal.z);
	        }
	    }
	    return Util.unboxFloats(normals.toArray(Float[]::new));
	}
}
