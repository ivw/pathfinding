package pathfinding;

public interface Node<NeighborT extends Node<NeighborT>> {

	/**
	 * @return Returns the edge length to the given node, or Double.POSITIVE_INFINITY if there is no edge to the node.
	 */
	double getEdgeLengthTo(NeighborT node, NeighborT previousNode);

}
