package pathfinding;

import pathfinding.grid_graph.GridNode;

public interface DistanceFunction<NodeT extends Node> {

	DistanceFunction<GridNode> EUCLIDEAN = new DistanceFunction<GridNode>() {
		@Override public double distance(GridNode nodeA, GridNode nodeB) {
			int dx = nodeB.x - nodeA.x;
			int dy = nodeB.y - nodeA.y;
			return Math.sqrt(dx * dx + dy * dy);
		}
	};

	double distance(NodeT nodeA, NodeT nodeB);

}
