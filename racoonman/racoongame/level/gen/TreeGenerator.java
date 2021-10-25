package racoonman.racoongame.level.gen;

import org.joml.Vector3i;

import racoonman.racoongame.client.RacoonGameClient;
import racoonman.racoongame.level.TileGraph;
import racoonman.racoongame.level.tile.TileType;

public class TreeGenerator {
	
	public static void generate(TileGraph graph, Vector3i startingPos) {
		Vector3i trunk = new Vector3i(startingPos.x, 9, startingPos.z);

		for(int y = 0; y < RacoonGameClient.RAND.nextInt(4) + 8; y++) {
			graph.set(trunk.set(trunk.x, y, trunk.z), TileType.LOG);
		}
		
		Vector3i leaves = new Vector3i(trunk.sub(2, 0, 2));
		graph.set(leaves, TileType.LEAVES);
		
		for(int x = 0; x < 5; ++x) {
			Vector3i xPos = new Vector3i(leaves).add(x, 0, 0);
			for(int z = 0; z < 5; ++z) {
				graph.set(xPos, TileType.LEAVES);
				xPos.add(0, 0, 1);
			}
		}
		
		for(int x = 1; x < 4; ++x) {
			Vector3i xPos = new Vector3i(leaves).add(x, 1, 1);
			for(int z = 1; z < 4; ++z) {
				graph.set(xPos, TileType.LEAVES);
				xPos.add(0, 0, 1);
			}
		}
	}
}
