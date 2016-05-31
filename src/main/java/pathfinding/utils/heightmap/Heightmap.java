package pathfinding.utils.heightmap;

import java.util.Random;

public class Heightmap {

	protected final int width;
	protected final int height;

	private final double[] values;

	protected Heightmap(int sideSize) {
		this(sideSize, sideSize);
	}

	protected Heightmap(int width, int height) {
		this(width, height, new double[width * height]);
	}

	protected Heightmap(int width, int height, double[] values) {
		this.width = width;
		this.height = height;

		this.values = new double[width * height];
	}

	public double[] getValues() {
		return values;
	}

	public double get(int x, int y) {
		return get(y * width + x);
	}

	public double get(int i) {
		return values[i];
	}

	public void set(int x, int y, double b) {
		set(y * width + x, b);
	}

	public void set(int i, double b) {
		values[i] = b;
	}

	public void normalize() {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (double value : values) {
			if (value < min) {
				min = value;
			}
			if (value > max) {
				max = value;
			}
		}

		double scale = max - min;
		for (int i = 0; i < values.length; i++) {
			values[i] = (values[i] - min) / scale;
		}
	}

	public void scatter(Random random, double multiplier) {
		for (int i = 0; i < values.length; i++) {
			values[i] += (random.nextDouble() * 2 - 1) * multiplier;
		}
	}

}
