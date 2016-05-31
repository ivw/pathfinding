package pathfinding.grid;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class GridFileUtils {

	private GridFileUtils() {
	}

	public static UniformGrid loadGridFileUniform(String fileName) {
		try (Scanner scanner = new Scanner(Paths.get(fileName))) {
			int width = scanner.nextInt();
			int height = scanner.nextInt();
			UniformGrid grid = new UniformGrid(width, height);
			for (int i = 0; i < grid.getSize(); i++) {
				grid.setTile(i, scanner.nextInt() != 0);
			}
			return grid;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// loads NonUniformGrid from a uniform grid file, for testing.
	public static NonUniformGrid loadGridFileNonUniform(String fileName) {
		try (Scanner scanner = new Scanner(Paths.get(fileName))) {
			int width = scanner.nextInt();
			int height = scanner.nextInt();
			NonUniformGrid grid = new NonUniformGrid(width, height);
			for (int i = 0; i < grid.getSize(); i++) {
				grid.setTile(i, scanner.nextInt() == 0 ? 1 : Double.POSITIVE_INFINITY);
			}
			return grid;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void saveUniformGridToFile(UniformGrid grid, String fileName) {
		Path path = Paths.get(fileName);
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.write(String.valueOf(grid.getWidth()));
			writer.write(" ");
			writer.write(String.valueOf(grid.getHeight()));
			writer.newLine();

			for (int i = 0; i < grid.getSize(); i++) {
				writer.write(grid.getTile(i) ? "1 " : "0 ");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
