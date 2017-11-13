package ass2.game;

import ass2.math.Vector3;
import ass2.math.Vector4;
import ass2.math.Vector4f;
import com.jogamp.opengl.GL2;

import java.util.ArrayList;

/**
 * Handles viewing the world.
 */
public class Camera extends GameObject {
	public Vector4f clearColor;

	public double nearPlane;
	public double farPlane;
	public double fov;

	private int width;
	private int height;

	private ArrayList<Integer> postProcessingShaders;
	private int postProcessTextures[];

	private double time;
	private boolean initialisedShaders;

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
		postProcessingShaders = new ArrayList<Integer>();
		time = 0.0;
		initialisedShaders = false;
	}

	/**
	 * Positions the world view to where the camera is.
	 * @param gl The GL object.
	 */
	public void setView(GL2 gl) {
		// This is mostly copied from my assignment 1.
		if (!initialisedShaders) {
			try {
				postProcessingShaders.add(Shader.initShaders(gl, getClass().getResourceAsStream("/shaders/postgeneric_vert.glsl"), getClass().getResourceAsStream("/shaders/depthshade_frag.glsl")));
				postProcessTextures = new int[postProcessingShaders.size() * 2];
				gl.glGenTextures(postProcessTextures.length, postProcessTextures, 0);

				for (int i : postProcessTextures) {
					generatePostProcessBuffer(gl, i);
				}
				gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
				initialisedShaders = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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
	 * @param dy
	 * @param width
	 * @param height
	 */
	public void reshape(GL2 gl, int x, int y, int width, int height) {

		// match the projection aspect ratio to the viewport
		// to avoid stretching

		this.width = width;
		this.height = height;

		if (postProcessTextures != null) {
			gl.glDeleteTextures(postProcessTextures.length, postProcessTextures, 0);
		}

		if (postProcessingShaders.size() > 0) {
			postProcessTextures = new int[postProcessingShaders.size() * 2];
			gl.glGenTextures(postProcessTextures.length, postProcessTextures, 0);

			for (int i : postProcessTextures) {
				generatePostProcessBuffer(gl, i);
			}
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		}
	}

	private void generatePostProcessBuffer(GL2 gl, int textureID) {
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureID);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, width, height, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, null);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
	}

	public void postProcess(GL2 gl, double dt) {
		time += dt;

		if (postProcessTextures != null) {
			gl.glDisable(GL2.GL_LIGHTING);
			gl.glEnable(GL2.GL_TEXTURE_2D);
			gl.glDepthMask(false);

			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);

			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();

			for (int i = 0; i < postProcessTextures.length; i += 2) {

				gl.glActiveTexture(GL2.GL_TEXTURE0);
				gl.glBindTexture(GL2.GL_TEXTURE_2D, postProcessTextures[i]);
				gl.glCopyTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, 0, 0, width, height, 0);

				gl.glActiveTexture(GL2.GL_TEXTURE2);
				gl.glEnable(GL2.GL_TEXTURE_2D);
				gl.glBindTexture(GL2.GL_TEXTURE_2D, postProcessTextures[i + 1]);
				gl.glCopyTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT24, 0, 0, width, height, 0);

				gl.glUseProgram(postProcessingShaders.get(i));

				if (i == 0) {
					int textureLoc = gl.glGetUniformLocation(postProcessingShaders.get(i), "fbo_texture");
					gl.glUniform1i(textureLoc, 0);
					int depthTextureLoc = gl.glGetUniformLocation(postProcessingShaders.get(i), "depth_texture");
					gl.glUniform1i(depthTextureLoc, 2);
				}

				gl.glBegin(GL2.GL_QUADS);
				gl.glTexCoord2d(0.0, 0.0);
				gl.glVertex3d(-1.0, -1.0, 0.0);
				gl.glTexCoord2d(1.0, 0.0);
				gl.glVertex3d(1.0, -1.0, 0.0);
				gl.glTexCoord2d(1.0, 1.0);
				gl.glVertex3d(1.0, 1.0, 0.0);
				gl.glTexCoord2d(0.0, 1.0);
				gl.glVertex3d(-1.0, 1.0, 0.0);
				gl.glEnd();
			}

			gl.glUseProgram(0);
			gl.glActiveTexture(GL2.GL_TEXTURE0);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
			gl.glActiveTexture(GL2.GL_TEXTURE2);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
			gl.glActiveTexture(GL2.GL_TEXTURE0);
			gl.glEnable(GL2.GL_LIGHTING);
			gl.glEnable(GL2.GL_DEPTH_TEST);
			gl.glDisable(GL2.GL_TEXTURE_2D);
			gl.glDepthMask(true);
		}
	}
}
