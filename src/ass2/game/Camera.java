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

	public Camera(GameObject parent) {
		super(parent);

		clearColor = new float[4];
		clearColor[0] = 0.5f;
		clearColor[1] = 0.5f;
		clearColor[2] = 0.9f;
		nearPlane = 0.01;
		farPlane = 1000.0;
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

		// This is commented out because the getGlobal functions aren't in the program yet.
		/*
		Vector3 globalTranslate = getGlobalPositionVector();
		Vector3 globalRotate = getGlobalRotationVector();
		Vector3 globalScale = getGlobalScaleVector();

		gl.glScaled(1.0 / globalScale.x, 1.0 / globalScale.y, 1.0 / globalScale.z);
		gl.glRotated(-globalRotate.z, 0.0, 0.0, 1.0);
		gl.glRotated(-globalRotate.y, 0.0, 1.0, 0.0);
		gl.glRotated(-globalRotate.x, 1.0, 0.0, 0.0);
		gl.glTranslated(-globalTranslate.x, -globalTranslate.y, -globalTranslate.z);
		*/
	}
}
