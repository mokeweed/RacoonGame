package racoonman.racoongame.level.gen;

import java.util.Random;

import org.joml.Vector3i;

import racoonman.racoongame.level.TileGraph;
import racoonman.racoongame.level.tile.TileType;

public class RockGenerator {

	public static void genRock(TileGraph graph, Vector3i startingPos, Random genRand) {
		for(int x = 0; x < 2; ++x) {
			Vector3i xPos = new Vector3i(startingPos).add(x, 0, 0);
			for(int z = 0; z < 2; ++z) {
				if(genRand.nextInt(4) == 0)
					graph.set(xPos, TileType.STONE);
				xPos.add(0, 0, 1);
			}
		}
		
	}
}
