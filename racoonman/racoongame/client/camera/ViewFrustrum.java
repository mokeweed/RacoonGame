package racoonman.racoongame.client.camera;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector3f;

import com.google.common.collect.ImmutableSet;

import racoonman.racoongame.client.renderer.level.TileRenderer.Face;

public class ViewFrustrum {
	//TODO remove
	private Set<Face> visibleFaces;
	
	public ViewFrustrum() {
		this.visibleFaces = new HashSet<>();
	}
	
	public Set<Face> getVisibleFaces(Camera camera) {
		return ImmutableSet.of();
	}
	
	public static Face getVerticalFace(Vector3f rot) {
		return rot.x > 180.0F ? Face.UP : Face.DOWN;
	}
}
