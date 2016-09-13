package ass2.spec;

import ass2.game.Drawable;
import ass2.game.GameObject;
import ass2.math.Vector3;
import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree extends GameObject implements Drawable {

	/**
	 * Constructs a new tree at the given co-ords.
	 * @param x The x co-ord.
	 * @param y The y co-ord.
	 * @param z The z co-ord.
	 */
	public Tree(GameObject parent, double x, double y, double z) {
		super(parent);
		this.transform.position = new Vector3(x, y, z);
	}

	/**
	 * Constructs a new tree at the given position.
	 * @param position The position of the tree.
	 */
	public Tree(GameObject parent, Vector3 position) {
		super(parent);
		this.transform.position = position.clone();
	}

	/**
	 * Gives the position of the tree.
	 * @return The tree's position.
	 */
	public Vector3 getPosition() {
		return transform.position.clone();
	}

	@Override
	public void draw(GL2 gl) {
		// Trunk part.
		gl.glColor3d(0.8, 0.6, 0.2);

		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glVertex3d(-0.1, 0.0, 0.0);
		gl.glVertex3d(0.1, 0.0, 0.0);
		gl.glVertex3d(0.0, 1.0, 0.0);
		gl.glVertex3d(0.0, 0.0, -0.1);
		gl.glVertex3d(0.0, 0.0, 0.1);
		gl.glVertex3d(0.0, 1.0, 0.0);
		gl.glEnd();
	}
}
