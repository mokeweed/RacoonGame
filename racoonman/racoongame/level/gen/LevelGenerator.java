package racoonman.racoongame.level.gen;

import java.util.Random;

import org.joml.Vector3i;

import racoonman.racoongame.level.Level;
import racoonman.racoongame.level.TileGraph;
import racoonman.racoongame.level.gen.noise.NoiseGenerator;
import racoonman.racoongame.level.tile.TileType;
import racoonman.racoongame.util.Util;

public class LevelGenerator {
	private static final Random LEVEL_GEN_RAND = new Random();
	private static final NoiseGenerator NOISE_GEN = new NoiseGenerator();	
	
	public static Level genLevel(int width, int height) {
		return Util.make(new Level(width, height), (level) -> {
			TileGraph graph = level.getTileGraph();

			//generate the bottom layer so that there are no gaps to fall into the void
			for(int x = 0; x < graph.getWidth(); x++) {
				for(int y = 0; y < graph.getHeight(); y++){
					for(int z = 0; z < graph.getWidth(); z++) {
						graph.set(x, 0, z, TileType.GRASS);
					}
				}
			}
			
			//generate the stone mounds
			for(int x = 0; x < graph.getWidth(); x++) {
				for(int y = 1; y < graph.getHeight(); y++) {
					for(int z = 0; z < graph.getWidth(); z++) {
						double noise = NOISE_GEN.noise(x, y, z, 30);
						if(noise < 0.10D)
							graph.set(x, y, z, TileType.AIR);
						else graph.set(x, y, z, TileType.STONE);
					}
				}
			}
			
			//generate grass on top of them
			for(int x = 0; x < graph.getWidth(); x++) {
				for(int y = graph.getHeight(); y > 0; y--) {
					for(int z = 0; z < graph.getWidth(); z++) {
						if(graph.contains(x, y, z) && graph.get(x, y, z).getType() == TileType.STONE) {
							if(!graph.contains(x, y + 1, z) || graph.get(x, y + 1, z).getType() == TileType.AIR) {
								graph.set(x, y, z, TileType.GRASS);
							}
						}
					}
				}
			}

			for(int i = 0; i < 50; i++) {
				int randX = LEVEL_GEN_RAND.nextInt(graph.getWidth());
				int randZ = LEVEL_GEN_RAND.nextInt(graph.getWidth());
				int farEdgeXDelta = graph.getWidth() - randX;
				int farEdgeZDelta = graph.getWidth() - randZ;
			
				if((randX > 5 && randZ > 5) && (farEdgeXDelta > 5 && farEdgeZDelta > 5)) {
					if(graph.get(randX, 2, randZ).getType() == TileType.AIR)
						TreeGenerator.generate(graph, new Vector3i(randX, LEVEL_GEN_RAND.nextInt(10) + 3, randZ));
				}
			}
			
			for(int i = 0; i < 20; i++) {
				int randX = LEVEL_GEN_RAND.nextInt(graph.getWidth());
				int randZ = LEVEL_GEN_RAND.nextInt(graph.getWidth());
		
				RockGenerator.genRock(graph, new Vector3i(randX, 1, randZ), LEVEL_GEN_RAND);
			}

			createSpawnPosition(level);
		});
	}

	private static void createSpawnPosition(Level level) {
		TileGraph graph = level.getTileGraph();
		Vector3i spawnPos = new Vector3i(1, 0, 1);
		
		do {
			spawnPos.add(0, 1, 0);
		} while(!graph.contains(spawnPos) || graph.get(spawnPos).getType() != TileType.AIR);

		level.setSpawnPos(spawnPos);
	}
}
