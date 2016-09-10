package ass2.game;

import ass2.math.Vector3;
import com.jogamp.opengl.GL2;

/**
 * Handles viewing the world.
 */
public class Camera extends GameObject {
	// TODO: Replace with Vector4f.
	private float[] clearColor;

	private double nearPlane;
	private double farPlane;
	private double fov;

	public Camera(GameObject parent) {
		super(parent);

		clearColor = new float[4];
		clearColor[0] = 0.5f;
		clearColor[1] = 0.5f;
		clearColor[2] = 0.9f;
		nearPlane = 0.1;
		farPlane = 1000.0;
		fov = 85.0;

		this.transform.scale = new Vector3(1.0, 1.0, 1.0);
	}

	// TODO: Background getter setter.

	/**
	 * Positions the world view to where the camera is.
	 * @param gl The GL object.
	 */
	public void setView(GL2 gl) {
		// This is mostly copied from my assignment 1.
		gl.glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glClearDepth(farPlane);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		Vector3 globalTranslate = getGlobalPositionVector();
		Vector3 globalRotate = getGlobalRotationVector();
		Vector3 globalScale = getGlobalScaleVector();

		gl.glScaled(1.0 / globalScale.x, 1.0 / globalScale.y, 1.0 / globalScale.z);
		gl.glRotated(-globalRotate.z, 0.0, 0.0, 1.0);
		gl.glRotated(-globalRotate.y, 0.0, 1.0, 0.0);
		gl.glRotated(-globalRotate.x, 1.0, 0.0, 0.0);
		gl.glTranslated(-globalTranslate.x, -globalTranslate.y, -globalTranslate.z);
	}


	public void reshape(GL2 gl, int x, int y, int width, int height) {

		// match the projection aspect ratio to the viewport
		// to avoid stretching

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		double top, bottom, left, right;

		if (width > height) {
			double aspect = (1.0 * width) / height;
			top = 1.0;
			bottom = -1.0;
			left = -aspect;
			right = aspect;
		} else {
			double aspect = (1.0 * height) / width;
			top = aspect;
			bottom = -aspect;
			left = -1.0;
			right = 1.0;
		}

		// TODO: The near-plane is too close for this.
		gl.glFrustum(left, right, bottom, top, 1 / Math.tan(Math.toRadians(fov / 2)), farPlane);
	}
}