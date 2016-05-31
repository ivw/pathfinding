package pathfinding.grid_graph;

import pathfinding.Node;
import pathfinding.grid.UniformGrid;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class UniformGridGraph {

	private final List<UniformGridNode> nodes;

	public UniformGridNode startNode;

	public UniformGridNode goalNode;

	public UniformGridGraph(UniformGrid grid, int startX, int startY, int goalX, int goalY) {
		nodes = new ArrayList<>(/* capacity guess: */ grid.getWidth() * grid.getHeight() / 2);

		startNode = new UniformGridNode(grid, startX, startY);
		nodes.add(startNode);

		goalNode = new UniformGridNode(grid, goalX, goalY);
		nodes.add(goalNode);

		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				if (x == startX && y == startY) continue;
				if (x == goalX && y == goalY) continue;

				if (grid.getTile(x, y)) continue;

				UniformGridCornerNode cornerNode = new UniformGridCornerNode(grid, x, y);
				if (cornerNode.isUnnecessary()) continue;
				nodes.add(cornerNode);
			}
		}
	}

	public List<UniformGridNode> getNodes() {
		return nodes;
	}

	public UniformGridNode getStartNode() {
		return startNode;
	}

	public UniformGridNode getGoalNode() {
		return goalNode;
	}

	private static class UniformGridNode extends GridNode<UniformGridNode> implements Node<UniformGridNode> {

		public final UniformGrid grid;

		public UniformGridNode(UniformGrid grid, int x, int y) {
			super(x, y);

			if (grid.getTile(x, y)) throw new IllegalArgumentException("node can't be a blocked tile");

			this.grid = grid;
		}

		protected boolean allowsAngleTo(UniformGridNode node) {
			return true;
		}

		@Override
		public double getEdgeLengthTo(UniformGridNode node, UniformGridNode previousNode) {
			if (!this.allowsAngleTo(node) || !node.allowsAngleTo(this))
				return Double.POSITIVE_INFINITY;

			final int dx = Math.abs(node.x - x);
			final int dy = Math.abs(node.y - y);

			final int xIncr = node.x > x ? 1 : -1;
			final int yIncr = node.y > y ? 1 : -1;

			int xCur = x;
			int yCur = y;
			int error = dx - dy;
			for (int n = dx + dy; n > 0; n--) {
				if (error > 0 || (error == 0 && grid.getTile(xCur, yCur + yIncr))) {
					xCur += xIncr;
					error -= dy * 2;
				} else {
					yCur += yIncr;
					error += dx * 2;
				}

				if (grid.getTile(xCur, yCur)) return Double.POSITIVE_INFINITY;
			}
			return Math.sqrt(dx * dx + dy * dy);
		}

	}

	private static class UniformGridCornerNode extends UniformGridNode {

		public final boolean left;
		public final boolean right;
		public final boolean top;
		public final boolean bottom;

		public final EnumSet<Octant> allowedAngles;

		public UniformGridCornerNode(UniformGrid grid, int x, int y) {
			super(grid, x, y);

			left = x <= 0 || grid.getTile(x - 1, y);
			right = x >= grid.getWidth() - 1 || grid.getTile(x + 1, y);
			top = y <= 0 || grid.getTile(x, y - 1);
			bottom = y >= grid.getHeight() - 1 || grid.getTile(x, y + 1);

			boolean topLeft = x <= 0 || y <= 0 || grid.getTile(x - 1, y - 1);
			boolean topRight = x >= grid.getWidth() - 1 || y <= 0 || grid.getTile(x + 1, y - 1);
			boolean bottomLeft = x <= 0 || y >= grid.getHeight() - 1 || grid.getTile(x - 1, y + 1);
			boolean bottomRight = x >= grid.getWidth() - 1 || y >= grid.getHeight() - 1 || grid.getTile(x + 1, y + 1);

			allowedAngles = EnumSet.noneOf(Octant.class);
			if (!top && !bottom) {
				if (left) {
					if (!topLeft) {
						allowedAngles.add(Octant.D);
						allowedAngles.add(Octant.H);
						allowedAngles.add(Octant.D_E);
						allowedAngles.add(Octant.G_H);
					}
					if (!bottomLeft) {
						allowedAngles.add(Octant.A);
						allowedAngles.add(Octant.E);
						allowedAngles.add(Octant.H_A);
						allowedAngles.add(Octant.E_F);
					}
				}
				if (right) {
					if (!topRight) {
						allowedAngles.add(Octant.A);
						allowedAngles.add(Octant.E);
						allowedAngles.add(Octant.A_B);
						allowedAngles.add(Octant.D_E);
					}
					if (!bottomRight) {
						allowedAngles.add(Octant.D);
						allowedAngles.add(Octant.H);
						allowedAngles.add(Octant.C_D);
						allowedAngles.add(Octant.H_A);
					}
				}
			}
			if (!left && !right) {
				if (top) {
					if (!topLeft) {
						allowedAngles.add(Octant.C);
						allowedAngles.add(Octant.G);
						allowedAngles.add(Octant.B_C);
						allowedAngles.add(Octant.G_H);
					}
					if (!topRight) {
						allowedAngles.add(Octant.B);
						allowedAngles.add(Octant.F);
						allowedAngles.add(Octant.A_B);
						allowedAngles.add(Octant.F_G);
					}
				}
				if (bottom) {
					if (!bottomLeft) {
						allowedAngles.add(Octant.B);
						allowedAngles.add(Octant.F);
						allowedAngles.add(Octant.E_F);
						allowedAngles.add(Octant.B_C);
					}
					if (!bottomRight) {
						allowedAngles.add(Octant.C);
						allowedAngles.add(Octant.G);
						allowedAngles.add(Octant.C_D);
						allowedAngles.add(Octant.F_G);
					}
				}
			}
		}

		public boolean isUnnecessary() {
			return allowedAngles.isEmpty();
		}

		@Override
		public double getEdgeLengthTo(UniformGridNode node, UniformGridNode previousNode) {
			if (previousNode != null && !allowsAngleChange(x - previousNode.x, y - previousNode.y, node.x - x, node.y - y))
				return Double.POSITIVE_INFINITY;

			return super.getEdgeLengthTo(node, previousNode);
		}

		@Override
		protected boolean allowsAngleTo(UniformGridNode node) {
			return allowedAngles.contains(Octant.get(node.x - x, node.y - y));
		}

		@SuppressWarnings("Duplicates")
		private boolean allowsAngleChange(int dxOld, int dyOld, int dxNew, int dyNew) {
			int angleChange = dxOld * dyNew - dxNew * dyOld;
			// angleChange > 0 means clockwise

			if (top) {
				if (dxOld > 0 && dxNew > 0) {
					if (angleChange < 0) return true;
				} else if (dxOld < 0 && dxNew < 0) {
					if (angleChange > 0) return true;
				}
			}
			if (bottom) {
				if (dxOld > 0 && dxNew > 0) {
					if (angleChange > 0) return true;
				} else if (dxOld < 0 && dxNew < 0) {
					if (angleChange < 0) return true;
				}
			}
			if (right) {
				if (dyOld > 0 && dyNew > 0) {
					if (angleChange < 0) return true;
				} else if (dyOld < 0 && dyNew < 0) {
					if (angleChange > 0) return true;
				}
			}
			if (left) {
				if (dyOld > 0 && dyNew > 0) {
					if (angleChange > 0) return true;
				} else if (dyOld < 0 && dyNew < 0) {
					if (angleChange < 0) return true;
				}
			}
			return false;
		}

	}

}
