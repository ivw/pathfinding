package pathfinding.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pathfinding.AStar;
import pathfinding.grid.NonUniformGrid;
import pathfinding.grid.UniformGrid;
import pathfinding.grid_graph.GridNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GridDrawer {

	private final int tileWidth;
	private final int tileHeight;

	public GridDrawer(int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void drawUniformGrid(GraphicsContext gc, UniformGrid grid) {
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				if (!grid.getTile(x, y)) continue;

				drawTile(gc, Color.BLACK, x, y);
			}
		}
	}

	public void drawNonUniformGrid(GraphicsContext gc, NonUniformGrid grid) {
		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				Color color;
				double cost = grid.getTile(x, y);
				if (cost == Double.POSITIVE_INFINITY) {
					color = Color.BLACK;
				} else if (cost < 1) {
					color = Color.GREEN.interpolate(Color.WHITE, cost);
				} else if (cost > 1) {
					color = Color.RED.interpolate(Color.WHITE, 1.0 / cost);
				} else {
					continue;
				}

				drawTile(gc, color, x, y);
			}
		}
	}

	public void drawTile(GraphicsContext gc, Color color, int x, int y) {
		gc.setFill(color);
		gc.fillRect(
				x * tileWidth,
				y * tileHeight,
				tileWidth,
				tileHeight
		);
	}

	public void drawNode(GraphicsContext gc, Color color, GridNode node) {
		drawTile(gc, color, node.x, node.y);
	}

	public void drawEdge(GraphicsContext gc, Color color, int lineWidth, GridNode nodeA, GridNode nodeB) {
		gc.setStroke(color);
		gc.setLineWidth(lineWidth);
		gc.strokeLine(
				(nodeA.x + 0.5) * tileWidth,
				(nodeA.y + 0.5) * tileHeight,
				(nodeB.x + 0.5) * tileWidth,
				(nodeB.y + 0.5) * tileHeight
		);
	}

	public void drawPath(GraphicsContext gc, LinkedList<? extends GridNode> path) {
		gc.setStroke(Color.GREEN);
		gc.setLineWidth(3);

		gc.beginPath();
		Iterator<? extends GridNode> pathIterator = path.iterator();
		GridNode first = pathIterator.next();
		gc.moveTo(
				(first.x + 0.5) * tileWidth,
				(first.y + 0.5) * tileHeight
		);
		while (pathIterator.hasNext()) {
			GridNode node = pathIterator.next();
			gc.lineTo(
					(node.x + 0.5) * tileWidth,
					(node.y + 0.5) * tileHeight
			);
		}
		gc.stroke();
	}

	public <NodeT extends GridNode<NodeT>> void drawGridGraph(GraphicsContext gc, List<? extends NodeT> nodes) {
		// Draw edges
		for (int i = 0; i < nodes.size(); i++) {
			NodeT nodeA = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++) {
				NodeT nodeB = nodes.get(j);

				if (nodeA.getEdgeLengthTo(nodeB, null) != Double.POSITIVE_INFINITY) {
					drawEdge(gc, Color.GRAY, 1, nodeA, nodeB);
				}
			}
		}

		// Draw nodes
		for (NodeT node : nodes) {
			drawNode(gc, Color.ORANGE, node);
		}
	}

	public static class AStarHooks implements AStar.Hooks<GridNode> {

		private final GridDrawer gridDrawer;
		private final GraphicsContext gc;

		public AStarHooks(GridDrawer gridDrawer, GraphicsContext gc) {
			this.gridDrawer = gridDrawer;
			this.gc = gc;
		}

		@Override
		public void onNodeVisit(GridNode node, GridNode previousNode) {
			gridDrawer.drawTile(gc, Color.RED, node.x, node.y);
		}

		@Override
		public void onEdgeVisit(GridNode node, GridNode neighborNode) {
			gridDrawer.drawEdge(gc, Color.GRAY, 1, node, neighborNode);
		}

	}

}
