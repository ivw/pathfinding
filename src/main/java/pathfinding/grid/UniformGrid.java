package pathfinding.grid;

public class UniformGrid {

	/**
	 * Each tile is a boolean, which denotes whether the tile is blocked or not.
	 */
	private final boolean[] tiles;

	private final int width;
	private final int height;

	public UniformGrid(int width, int height, boolean[] tiles) {
		if (tiles.length != width * height)
			throw new IllegalArgumentException("invalid array length");

		this.tiles = tiles;

		this.width = width;
		this.height = height;
	}

	public UniformGrid(int width, int height) {
		this.tiles = new boolean[width * height];

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

	public boolean[] getTiles() {
		return tiles;
	}

	public boolean getTile(int i) {
		return tiles[i];
	}

	public boolean getTile(int x, int y) {
		return tiles[y * width + x];
	}

	public void setTile(int i, boolean tileCost) {
		tiles[i] = tileCost;
	}

	public void setTile(int x, int y, boolean tileCost) {
		setTile(y * width + x, tileCost);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
