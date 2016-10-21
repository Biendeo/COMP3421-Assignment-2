package ass2.game;

import com.jogamp.opengl.GL2;

/**
 * A portal object that sits in the world.
 */
public class Portal extends GameObject implements Drawable {
	private double width;
	private double height;

	private Portal connection;
	private Camera activeCamera;

	public static int portalViewDepth = 3;

	public Portal(GameObject parent, double width, double height) {
		super(parent);

		this.width = width;
		this.height = height;
		activeCamera = null;
	}

	public Portal getConnection() {
		return connection;
	}

	public static void connectPortals(Portal portal1, Portal portal2) {
		portal1.connection = portal2;
		portal2.connection = portal1;
	}

	@Override
	public void initialize(GL2 gl) {

	}

	@Override
	public void dispose(GL2 gl) {

	}

	@Override
	public void draw(GL2 gl) {
		if (activeCamera == null) {
			gl.glColor4d(1.0, 0.5, 1.0, 0.7);

			gl.glBegin(GL2.GL_TRIANGLE_STRIP);

			gl.glVertex3d(-width / 2, height / 2, 0.0);
			gl.glVertex3d(width / 2, height / 2, 0.0);
			gl.glVertex3d(-width / 2, -height / 2, 0.0);
			gl.glVertex3d(width / 2, -height / 2, 0.0);

			gl.glEnd();
		}
	}
}
