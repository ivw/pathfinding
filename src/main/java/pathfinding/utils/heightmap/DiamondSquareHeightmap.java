package pathfinding.utils.heightmap;

import java.util.Random;

public class DiamondSquareHeightmap extends Heightmap {

	protected final Random random;

	protected final int featureSize;

	public DiamondSquareHeightmap(int width, int height, int featureSize, Random random) {
		super(width, height);

//		if (!IntMath.isPowerOfTwo(width)) throw new IllegalArgumentException("width must be a power of 2");
//		if (!IntMath.isPowerOfTwo(height)) throw new IllegalArgumentException("height must be a power of 2");
//		if (!IntMath.isPowerOfTwo(featureSize)) throw new IllegalArgumentException("featureSize must be a power of 2");

		this.featureSize = featureSize;

		this.random = random;

		generate();

		normalize();
	}

	// from the LevelGen code of Minicraft
	protected void generate() {
		for (int y = 0; y < width; y += featureSize) {
			for (int x = 0; x < width; x += featureSize) {
				displaceAndSetSample(x, y, 0, featureSize);
			}
		}

		for (int stepSize = featureSize; stepSize > 1; stepSize /= 2) {
			int halfStep = stepSize / 2;

			//diamond
			for (int y = 0; y < width; y += stepSize) {
				for (int x = 0; x < width; x += stepSize) {
					double a = getSample(x, y);
					double b = getSample(x + stepSize, y);
					double c = getSample(x, y + stepSize);
					double d = getSample(x + stepSize, y + stepSize);

					displaceAndSetSample(x + halfStep, y + halfStep, (a + b + c + d) / 4.0, stepSize);
				}
			}

			//square
			for (int y = 0; y < width; y += stepSize) {
				for (int x = 0; x < width; x += stepSize) {
					double a = getSample(x, y);
					double b = getSample(x + stepSize, y);
					double c = getSample(x, y + stepSize);
					double d = getSample(x + halfStep, y + halfStep);
					double e = getSample(x + halfStep, y - halfStep);
					double f = getSample(x - halfStep, y + halfStep);

					displaceAndSetSample(x + halfStep, y, (a + b + d + e) / 4.0, stepSize);
					displaceAndSetSample(x, y + halfStep, (a + c + d + f) / 4.0, stepSize);
				}
			}
		}
	}

	protected double displace(double value, int d) {
		return value + (random.nextDouble() - 0.5) * d;
	}

	protected void displaceAndSetSample(int x, int y, double value, int d) {
		setSample(x, y, displace(value, d));
	}

	protected double getSample(int x, int y) {
		return get(x & (width - 1), y & (height - 1));
	}

	protected void setSample(int x, int y, double value) {
		set(x & (width - 1), y & (height - 1), value);
	}

}
