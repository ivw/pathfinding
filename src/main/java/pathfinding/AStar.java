package pathfinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class AStar {

	private AStar() {
	}

	public static <NodeT extends Node<NodeT>> LinkedList<? extends NodeT> findPath(Collection<? extends NodeT> graph, NodeT start, NodeT goal, DistanceFunction<? super NodeT> heuristic, double heuristicWeight, Hooks<? super NodeT> hooks) {
		if (start == goal) return null;

		Collection<NodeState<NodeT>> unvisitedNodeStates = new ArrayList<>(graph.size() + 2);

		NodeState<NodeT> startNodeState = new NodeState<>(start);
		startNodeState.distance = 0.0;
		startNodeState.heuristicDistanceToGoal = 0.0;// can just put this heuristic to zero, because it's never needed for the start node.
		// startNodeState doesn't have to be added to unvisitedNodeStates, because it's the initial value.

		NodeState<NodeT> goalNodeState = new NodeState<>(goal);
		goalNodeState.heuristicDistanceToGoal = 0.0;
		unvisitedNodeStates.add(goalNodeState);

		for (NodeT node : graph) {
			if (node != start && node != goal) {
				unvisitedNodeStates.add(new NodeState<>(node));
			}
		}

//		System.out.println("Node count: " + unvisitedNodeStates.size());

		NodeState<NodeT> nodeState = startNodeState;
		do {
			if (hooks != null) hooks.onNodeVisit(nodeState.node, nodeState.previousNodeState != null ? nodeState.previousNodeState.node : null);

			for (NodeState<NodeT> neighborNodeState : unvisitedNodeStates) {
				double edgeLength = nodeState.node.getEdgeLengthTo(neighborNodeState.node, nodeState.previousNodeState != null ? nodeState.previousNodeState.node : null);
				if (edgeLength == Double.POSITIVE_INFINITY)
					continue;

				if (hooks != null) hooks.onEdgeVisit(nodeState.node, neighborNodeState.node);

				if (nodeState.distance + edgeLength < neighborNodeState.distance) {
					neighborNodeState.distance = nodeState.distance + edgeLength;
					if (neighborNodeState.heuristicDistanceToGoal == Double.POSITIVE_INFINITY) {
						neighborNodeState.heuristicDistanceToGoal = heuristic != null ? heuristicWeight * heuristic.distance(neighborNodeState.node, goal) : 0;
					}
					neighborNodeState.previousNodeState = nodeState;
				}
			}

			if (unvisitedNodeStates.isEmpty()) break;
			nodeState = findMinPriorityNodeState(unvisitedNodeStates);
			unvisitedNodeStates.remove(nodeState);
		} while (nodeState != goalNodeState);

//		System.out.println("Unvisited count: " + unvisitedNodeStates.size());

		if (goalNodeState.previousNodeState == null)
			return null;// no path found

//		System.out.println("Distance: " + goalNodeState.distance);

		return backtracePath(goalNodeState);
	}

	private static <NodeT extends Node<NodeT>> LinkedList<NodeT> backtracePath(NodeState<NodeT> goalNodeState) {
		NodeState<NodeT> nodeState = goalNodeState;

		LinkedList<NodeT> path = new LinkedList<>();
		do {
			path.addFirst(nodeState.node);
			nodeState = nodeState.previousNodeState;
		} while (nodeState != null);

		return path;
	}

	private static <NodeT extends Node<NodeT>> NodeState<NodeT> findMinPriorityNodeState(Collection<NodeState<NodeT>> unvisitedNodeStates) {
		Iterator<NodeState<NodeT>> iterator = unvisitedNodeStates.iterator();

		NodeState<NodeT> minPriorityNodeState = iterator.next();
		double minPriority = minPriorityNodeState.getPriority();

		while (iterator.hasNext()) {
			NodeState<NodeT> nodeState = iterator.next();
			if (nodeState.getPriority() < minPriority) {
				minPriority = nodeState.getPriority();
				minPriorityNodeState = nodeState;
			}
		}
		return minPriorityNodeState;
	}

	private static class NodeState<NodeT extends Node<NodeT>> {

		public final NodeT node;

		/**
		 * Estimated distance to goal. Is POSITIVE_INFINITY if not computed yet (only possible if distance is also POSITIVE_INFINITY). Can be 0 if there is no heuristic.
		 */
		public double heuristicDistanceToGoal;

		/**
		 * The (tentative) total distance from the start node to this node.
		 */
		public double distance;

		/**
		 * The (tentative) previous node in the path from the start node to this node.
		 */
		public NodeState<NodeT> previousNodeState;

		public NodeState(NodeT node) {
			this.node = node;

			heuristicDistanceToGoal = Double.POSITIVE_INFINITY;

			distance = Double.POSITIVE_INFINITY;

			previousNodeState = null;
		}

		public double getPriority() {
			return distance + heuristicDistanceToGoal;
		}

	}

	public interface Hooks<NodeT extends Node> {
		void onNodeVisit(NodeT node, NodeT previousNode);

		void onEdgeVisit(NodeT node, NodeT neighborNode);
	}

}
