package ass2.spec;

import ass2.game.Drawable;
import ass2.game.GameObject;
import ass2.game.Material;
import ass2.math.Vector3;
import ass2.math.Vector3f;
import ass2.math.Vector4f;
import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree extends GameObject implements Drawable {
	private Material material;

	/**
	 * Constructs a new tree at the given co-ords.
	 * @param x The x co-ord.
	 * @param y The y co-ord.
	 * @param z The z co-ord.
	 */
	public Tree(GameObject parent, double x, double y, double z) {
		super(parent);
		this.transform.position = new Vector3(x, y, z);
		material = new Material();
		material.diffuse = new Vector4f(0.9f, 0.6f, 0.2f, 0.0f);
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
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[]{material.ambient.x, material.ambient.y, material.ambient.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{material.specular.x, material.specular.y, material.specular.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, new float[]{material.phong.x, material.phong.y, material.phong.z}, 0);
		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glNormal3d(0.0, 0.0, -1.0);
		gl.glVertex3d(-0.1, 0.0, 0.0);
		gl.glVertex3d(0.1, 0.0, 0.0);
		gl.glVertex3d(0.0, 1.0, 0.0);
		gl.glNormal3d(1.0, 0.0, 0.0);
		gl.glVertex3d(0.0, 0.0, -0.1);
		gl.glVertex3d(0.0, 0.0, 0.1);
		gl.glVertex3d(0.0, 1.0, 0.0);
		gl.glEnd();
	}

	@Override
	public void initialize(GL2 gl) {

	}

	@Override
	public void dispose(GL2 gl) {

	}
}
