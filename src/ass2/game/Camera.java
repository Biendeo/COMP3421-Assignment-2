package ass2.game;

import ass2.math.Vector3;
import ass2.math.Vector4;
import ass2.math.Vector4f;
import com.jogamp.opengl.GL2;

/**
 * Handles viewing the world.
 */
public class Camera extends GameObject {
	public Vector4f clearColor;

	public double nearPlane;
	public double farPlane;
	public double fov;

	/**
	 * Constructs a camera object with
	 * @param parent
	 */
	public Camera(GameObject parent) {
		super(parent);
		clearColor = new Vector4f(0.5f, 0.5f, 0.9f, 1.0f);
		nearPlane = 0.1;
		farPlane = 1000.0;
		fov = 85.0;

		this.transform.scale = new Vector3(1.0, 1.0, 1.0);
	}

	/**
	 * Positions the world view to where the camera is.
	 * @param gl The GL object.
	 */
	public void setView(GL2 gl) {
		// This is mostly copied from my assignment 1.
		gl.glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
		gl.glClearStencil(0x00);

		gl.glEnable(GL2.GL_CULL_FACE);

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_STENCIL_TEST);

		gl.glColorMask(true, true, true, true);
		gl.glDepthMask(true);
		gl.glClearDepth(farPlane);
		gl.glStencilMask(0x00);
		gl.glStencilFunc(GL2.GL_EQUAL, 0x00, 0x00);
		gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);
		gl.glClear(GL2.GL_STENCIL_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_COLOR_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		Vector3 globalTranslate = getGlobalPositionVector();
		Vector3 globalRotate = getGlobalRotationVector();
		Vector3 globalScale = getGlobalScaleVector();

		gl.glScaled(1.0 / globalScale.x, 1.0 / globalScale.y, 1.0 / globalScale.z);
		gl.glRotated(-globalRotate.x, 1.0, 0.0, 0.0);
		gl.glRotated(-globalRotate.y, 0.0, 1.0, 0.0);
		gl.glRotated(-globalRotate.z, 0.0, 0.0, 1.0);
		gl.glTranslated(-globalTranslate.x, -globalTranslate.y, -globalTranslate.z);
	}

	/**
	 * Gets called whenever the window is resized, and handles representing the FOV and aspect.
	 * @param gl
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
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

		gl.glFrustum(left / 10, right / 10, bottom / 10, top / 10, 0.1 / Math.tan(Math.toRadians(fov / 2)), farPlane);
	}
}
