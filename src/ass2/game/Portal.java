package ass2.game;

import ass2.math.Vector3;
import ass2.spec.Terrain;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * A portal object that sits in the world.
 */
public class Portal extends GameObject implements Drawable {
	private double width;
	private double height;

	private Portal connection;
	private Camera activeCamera;

	private Terrain terrain;

	private static int currentPortalViewDepth = 0;
	public static int portalViewDepth = 1;

	public Portal(GameObject parent, Terrain terrain, double width, double height) {
		super(parent);

		this.terrain = terrain;
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

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setActiveCamera(Camera camera) {
		activeCamera = camera;
	}

	@Override
	public void draw(GL2 gl) {
		if (activeCamera == null || currentPortalViewDepth == portalViewDepth) {
			gl.glColor4d(1.0, 0.5, 1.0, 0.7);

			gl.glBegin(GL2.GL_TRIANGLE_STRIP);

			gl.glVertex3d(-width / 2, height / 2, 0.0);
			gl.glVertex3d(width / 2, height / 2, 0.0);
			gl.glVertex3d(-width / 2, -height / 2, 0.0);
			gl.glVertex3d(width / 2, -height / 2, 0.0);

			gl.glEnd();
		} else {
			++currentPortalViewDepth;

			gl.glDisable(GL2.GL_LIGHTING);
			gl.glEnable(GL2.GL_STENCIL_TEST);

			// This draws the portal to the stencil buffer.
			gl.glColor4d(1.0, 1.0, 1.0, 0.3);
			gl.glStencilMask(0xFF);
			gl.glStencilFunc(GL2.GL_NEVER, 0, 0xFF);
			gl.glStencilOp(GL2.GL_INCR, GL2.GL_KEEP, GL2.GL_KEEP);
			gl.glEnable(GL2.GL_BLEND);
			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			gl.glColorMask(true, true, true, true);
			gl.glDepthMask(false);
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			gl.glVertex3d(-width / 2, height / 2, 0.0);
			gl.glVertex3d(-width / 2, -height / 2, 0.0);
			gl.glVertex3d(width / 2, height / 2, 0.0);
			gl.glVertex3d(width / 2, -height / 2, 0.0);
			gl.glEnd();

			gl.glColorMask(true, true, true, true);
			gl.glDepthMask(true);
			gl.glStencilMask(0x00);

			/*
			// This triangle should not draw where the portal is.
			gl.glStencilMask(0x00);
			gl.glStencilFunc(GL2.GL_LEQUAL, 1, 0xFF);
			gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);
			gl.glColorMask(true, true, true, true);
			gl.glDepthMask(true);
			gl.glColor3d(1.0, 0.0, 1.0);
			gl.glBegin(GL2.GL_TRIANGLES);
			gl.glNormal3d(0.0, 0.0, 1.0);
			gl.glVertex3d(-5.0, -5.0, -1.0);
			gl.glVertex3d(5.0, -5.0, -1.0);
			gl.glVertex3d(0.0, 5.0, -1.0);
			gl.glEnd();

			// This triangle should only be drawn where the portal is.
			gl.glStencilFunc(GL2.GL_NOTEQUAL, 1, 0xFF);
			gl.glColor3d(0.0, 1.0, 1.0);
			gl.glBegin(GL2.GL_TRIANGLES);
			gl.glNormal3d(0.0, 0.0, 1.0);
			gl.glVertex3d(5.0, 5.0, -2.0);
			gl.glVertex3d(-5.0, 5.0, -2.0);
			gl.glVertex3d(0.0, -5.0, -2.0);
			gl.glEnd();
			*/

			gl.glEnable(GL2.GL_LIGHTING);
			gl.glDisable(GL2.GL_BLEND);
			gl.glDisable(GL2.GL_STENCIL_TEST);



			/*
			gl.glColorMask(false, false, false, false);

			gl.glStencilFunc(GL2.GL_ALWAYS, 1, 0xFF);
			gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);

			gl.glBegin(GL2.GL_TRIANGLE_STRIP);

			gl.glVertex3d(-width / 2, height / 2, 0.0);
			gl.glVertex3d(width / 2, height / 2, 0.0);
			gl.glVertex3d(-width / 2, -height / 2, 0.0);
			gl.glVertex3d(width / 2, -height / 2, 0.0);

			gl.glEnd();

			gl.glColorMask(true, true, true, true);
			gl.glDepthMask(true);

			gl.glPushMatrix();

			Camera oldCamera = activeCamera;

			Camera tempCamera = new Camera(GameObject.ROOT);
			Vector3 cameraDifference = oldCamera.getGlobalPositionVector().subtract(getGlobalPositionVector());
			double rotationDifference = getGlobalRotationVector().y - connection.getGlobalRotationVector().y;
			Vector3 oldCameraRotation = oldCamera.getGlobalRotationVector();
			Vector3 rotatedCamera = new Vector3(oldCameraRotation.x, oldCameraRotation.y + rotationDifference, oldCameraRotation.z);
			Vector3 finalCameraPosition = cameraDifference.add(connection.getGlobalPositionVector());
			tempCamera.transform.position = finalCameraPosition;
			tempCamera.transform.rotation = rotatedCamera;

			System.out.println(Double.toString(tempCamera.getGlobalPositionVector().x) + ", " + Double.toString(tempCamera.getGlobalPositionVector().y) + ", " + Double.toString(tempCamera.getGlobalPositionVector().z));

			terrain.setPortalCamera(tempCamera);

			tempCamera.setView(gl);

			GameObject.ROOT.tryDraw(gl);

			tempCamera.delete();

			terrain.setPortalCamera(oldCamera);

			gl.glPopMatrix();
			*/

			--currentPortalViewDepth;
		}
	}

	private void drawRecursivePortal(GL2 gl) {
		gl.glColorMask(false, false, false, false);
		gl.glDepthMask(false);

		gl.glDisable(GL2.GL_DEPTH_TEST);

		gl.glEnable(GL2.GL_STENCIL_TEST);

		gl.glStencilFunc(GL2.GL_NOTEQUAL, currentPortalViewDepth, 0xFF);

		gl.glStencilOp(GL2.GL_INCR, GL2.GL_KEEP, GL2.GL_KEEP);

		gl.glStencilMask(0xFF);

		Camera tempCamera = new Camera(connection);
		tempCamera.transform.position = new Vector3(0.0, 0.0, -2.0);

		gl.glPushMatrix();

		tempCamera.setView(gl);

		if (currentPortalViewDepth == portalViewDepth) {
			gl.glColorMask(true, true, true, true);
			gl.glDepthMask(true);

			gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);

			gl.glEnable(GL2.GL_DEPTH_TEST);

			gl.glEnable(GL2.GL_STENCIL_TEST);

			gl.glStencilMask(0x00);

			gl.glStencilFunc(GL2.GL_EQUAL, currentPortalViewDepth + 1, 0xFF);
		} else {
			++currentPortalViewDepth;
			drawRecursivePortal(gl);
			--currentPortalViewDepth;
		}

		tempCamera.delete();

		gl.glColorMask(false, false, false, false);
		gl.glDepthMask(false);

		gl.glEnable(GL2.GL_STENCIL_TEST);

		GameObject.ROOT.tryDraw(gl);

		gl.glPopMatrix();
	}
}
