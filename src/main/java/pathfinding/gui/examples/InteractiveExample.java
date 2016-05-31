package pathfinding.gui.examples;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pathfinding.AStar;
import pathfinding.DistanceFunction;
import pathfinding.grid.RandomGridUtils;
import pathfinding.grid.UniformGrid;
import pathfinding.grid_graph.GridNode;
import pathfinding.grid_graph.UniformGridGraph;
import pathfinding.gui.GridDrawer;

import java.util.LinkedList;
import java.util.Random;

public class InteractiveExample extends Application {

	private int goalX;
	private int goalY;

	@Override
	public void start(Stage primaryStage) {
//		UniformGrid grid = GridFileUtils.loadGridFileUniform("grids/sc2/aridwastes.txt");
//		int startX = 48;
//		int startY = 28;

		Random random = new Random(3);
		UniformGrid grid = RandomGridUtils.createDiamondSquareUniformGrid(256, 256, 32, 0.41, random);
		int startX = 48;
		int startY = 28;

		double heuristicWeight = 1;
		System.out.println("Grid size: " + grid.getWidth() + "x" + grid.getHeight());

		GridDrawer gridDrawer = new GridDrawer(4, 4);
		Canvas canvas = new Canvas(grid.getWidth() * gridDrawer.getTileWidth(), grid.getHeight() * gridDrawer.getTileHeight());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gridDrawer.drawUniformGrid(gc, grid);

		canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
			int goalX = (int) (e.getX() / gridDrawer.getTileWidth());
			int goalY = (int) (e.getY() / gridDrawer.getTileHeight());
			if (goalX == this.goalX && goalY == this.goalY) return;
			this.goalX = goalX;
			this.goalY = goalY;

			if (grid.getTile(goalX, goalY)) return;

			UniformGridGraph gridGraph = new UniformGridGraph(grid, startX, startY, goalX, goalY);

			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

			gridDrawer.drawUniformGrid(gc, grid);
//			gridDrawer.drawGridGraph(gc, gridGraph.getNodes());
			gridDrawer.drawTile(gc, Color.CYAN, startX, startY);
			gridDrawer.drawTile(gc, Color.CYAN, goalX, goalY);
			LinkedList<? extends GridNode> path = AStar.findPath(gridGraph.getNodes(), gridGraph.getStartNode(), gridGraph.getGoalNode(), DistanceFunction.EUCLIDEAN, heuristicWeight, new GridDrawer.AStarHooks(gridDrawer, gc));
			if (path != null) {
				gridDrawer.drawPath(gc, path);
			} else {
				System.out.println("No path found");
			}
		});

		Group root = new Group();
		root.getChildren().add(canvas);
		primaryStage.setTitle(InteractiveExample.class.getSimpleName());
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
