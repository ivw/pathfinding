package pathfinding.grid;

public class NonUniformGrid {

	/**
	 * Each tile is a double, which denotes the cost of moving on that tile. If the cost is POSITIVE_INFINITY, then it's a blocked tile.
	 */
	private final double[] tiles;

	private final int width;
	private final int height;

	public NonUniformGrid(int width, int height, double[] tiles) {
		if (tiles.length != width * height)
			throw new IllegalArgumentException("invalid array length");

		this.tiles = tiles;

		this.width = width;
		this.height = height;
	}

	public NonUniformGrid(int width, int height) {
		this.tiles = new double[width * height];

		this.width = width;
		this.height = height;
	}

	public int getSize() {
		return tiles.length;
	}

	public int coordToIndex(int x, int y) {
		return y * width + x;
	}

	public int indexToX(int i) {
		return i % width;
	}

	public int indexToY(int i) {
		return i / width;
	}

	public double[] getTiles() {
		return tiles;
	}

	public double getTile(int i) {
		return tiles[i];
	}

	public double getTile(int x, int y) {
		return tiles[y * width + x];
	}

	public void setTile(int i, double tileCost) {
		tiles[i] = tileCost;
	}

	public void setTile(int x, int y, double tileCost) {
		setTile(y * width + x, tileCost);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getLowestTileCost() {
		double lowestTileCost = Double.POSITIVE_INFINITY;
		for (double tileCost : tiles) {
			if (tileCost < lowestTileCost) {
				lowestTileCost = tileCost;
			}
		}
		return lowestTileCost;
	}

}
