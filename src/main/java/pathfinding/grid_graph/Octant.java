package pathfinding.grid_graph;

public enum Octant {
	A(+1, -1, +1),
	A_B(+1, -1, 0),
	B(+1, -1, -1),
	B_C(+1, 0, -1),
	C(+1, +1, -1),
	C_D(+1, +1, 0),
	D(+1, +1, +1),
	D_E(0, +1, +1),
	E(-1, +1, +1),
	E_F(-1, +1, 0),
	F(-1, +1, -1),
	F_G(-1, 0, -1),
	G(-1, -1, -1),
	G_H(-1, -1, 0),
	H(-1, -1, +1),
	H_A(0, -1, +1);

	private static final Octant[] lookup;

	static {
		lookup = new Octant[27];// 3^3 permutations
		for (Octant octant : values()) {
			lookup[getLookupIndex(octant.dxSign, octant.dySign, octant.dxySign)] = octant;
		}
	}

	public final int dxSign;
	public final int dySign;
	public final int dxySign;//the sign of Math.abs(dy) - Math.abs(dx)

	Octant(int dxSign, int dySign, int dxySign) {
		this.dxSign = dxSign;
		this.dySign = dySign;
		this.dxySign = dxySign;
	}

	public boolean intersects(int dx, int dy) {
		return Integer.signum(dx) == dxSign && Integer.signum(dy) == dySign && Integer.signum(Math.abs(dy) - Math.abs(dx)) == dxySign;
	}

	public Octant opposite() {
		return get(-dxSign, -dySign, dxySign);
	}

	public static Octant get(int dx, int dy) {
		return get(Integer.signum(dx), Integer.signum(dy), Integer.signum(Math.abs(dy) - Math.abs(dx)));
	}

	public static Octant get(int dxSign, int dySign, int dxySign) {
		return lookup[getLookupIndex(dxSign, dySign, dxySign)];
	}

	private static int getLookupIndex(int dxSign, int dySign, int dxySign) {
		return 13 + dxSign + dySign * 3 + dxySign * 9;
	}

}
