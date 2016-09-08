package ass2.spec;

import ass2.math.Vector3;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

	private Vector3 myPos;

	/**
	 * Constructs a new tree at the given co-ords.
	 * @param x The x co-ord.
	 * @param y The y co-ord.
	 * @param z The z co-ord.
	 */
	public Tree(double x, double y, double z) {
		myPos = new Vector3(x, y, z);
	}

	/**
	 * Constructs a new tree at the given position.
	 * @param position The position of the tree.
	 */
	public Tree(Vector3 position) {
		myPos = position.clone();
	}

	/**
	 * Gives the position of the tree.
	 * @return The tree's position.
	 */
	public Vector3 getPosition() {
		return myPos.clone();
	}


}
