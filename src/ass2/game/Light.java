package ass2.game;

import ass2.math.Vector3;
import com.jogamp.opengl.GL2;

public class Light extends GameObject {
	public Material material;
	// This is a number such as GL2.GL_LIGHT0.
	public int lightNumber;
	public LightType type;

	public Light(GameObject parent, int lightNumber, LightType type) {
		super(parent);
		material = new Material();
		this.lightNumber = lightNumber;
		this.type = type;
	}

	public void setLight(GL2 gl) {
		if (enabled) {
			// This is copied from the week 6 code.
			// rotate the light
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glEnable(GL2.GL_LIGHTING);
			gl.glEnable(lightNumber);
			gl.glEnable(GL2.GL_NORMALIZE);

			gl.glPushMatrix();
			gl.glLoadIdentity();

			Vector3 globalPosition = getGlobalPositionVector();
			Vector3 globalRotation = getGlobalRotationVector();
			Vector3 globalScale = getGlobalScaleVector();

			gl.glScaled(globalScale.x, globalScale.y, globalScale.z);
			gl.glRotated(globalRotation.x, 1.0, 0.0, 0.0);
			gl.glRotated(globalRotation.y, 0.0, 1.0, 0.0);
			gl.glRotated(globalRotation.z, 0.0, 0.0, 1.0);

			float[] pos = new float[]{(float) globalPosition.x, (float) globalPosition.y, (float) globalPosition.z};
			gl.glLightfv(lightNumber, GL2.GL_POSITION, pos, 0);
			gl.glPopMatrix();

			float[] ambient = new float[]{material.ambient.x, material.ambient.y, material.ambient.z};
			gl.glLightfv(lightNumber, GL2.GL_AMBIENT, ambient, 0);

			float[] diffuse = new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z};
			gl.glLightfv(lightNumber, GL2.GL_DIFFUSE, diffuse, 0);

			float[] specular = new float[]{material.specular.x, material.specular.y, material.specular.z};
			gl.glLightfv(lightNumber, GL2.GL_SPECULAR, specular, 0);
		} else {
			gl.glDisable(lightNumber);
		}
	}
}
