package pathfinding.grid_graph;

import pathfinding.Node;

public abstract class GridNode<NeighborT extends GridNode<NeighborT>> implements Node<NeighborT> {

	public final int x;
	public final int y;

	public GridNode(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
