package pathfinding.grid;

import pathfinding.utils.heightmap.DiamondSquareHeightmap;

import java.util.Random;

public class RandomGridUtils {

	private RandomGridUtils() {
	}

	public static UniformGrid createRandomUniformGrid(int width, int height, double blockedChance, Random random) {
		UniformGrid grid = new UniformGrid(width, height);
		for (int i = 0; i < grid.getSize(); i++) {
			grid.setTile(i, random.nextDouble() < blockedChance);
		}
		return grid;
	}

	public static NonUniformGrid createRandomNonUniformGrid(int width, int height, double blockedChance, double greenChance, double redChance, Random random) {
		NonUniformGrid grid = new NonUniformGrid(width, height);
		for (int i = 0; i < grid.getSize(); i++) {
			double cost;
			double randomDouble = random.nextDouble();
			if (randomDouble < blockedChance) {
				cost = Double.POSITIVE_INFINITY;
			} else if (randomDouble < blockedChance + greenChance) {
				cost = 0.5;
			} else if (randomDouble < blockedChance + greenChance + redChance) {
				cost = 2;
			} else {
				cost = 1;
			}
			grid.setTile(i, cost);
		}
		return grid;
	}

	public static UniformGrid createDiamondSquareUniformGrid(int width, int height, int featureSize, double blockedThreshold, Random random) {
		DiamondSquareHeightmap heightmap = new DiamondSquareHeightmap(width, height, featureSize, random);

		UniformGrid grid = new UniformGrid(width, height);
		for (int i = 0; i < grid.getSize(); i++) {
			grid.setTile(i, heightmap.get(i) < blockedThreshold);
		}
		return grid;
	}

	public static int getRandomOpenTile(UniformGrid grid, Random random) {
		int nrTries = 0;
		do {
			int i = random.nextInt(grid.getSize());
			if (!grid.getTile(i)) {
				return i;
			}
		} while (++nrTries < 1000);
		throw new IllegalArgumentException("no open tile found");
	}

	public static int getRandomOpenTile(NonUniformGrid grid, Random random) {
		int nrTries = 0;
		do {
			int i = random.nextInt(grid.getSize());
			if (grid.getTile(i) != Double.POSITIVE_INFINITY) {
				return i;
			}
		} while (++nrTries < 1000);
		throw new IllegalArgumentException("no open tile found");
	}

}
