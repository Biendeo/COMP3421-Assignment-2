package ass2.game;

import ass2.math.MathUtil;
import ass2.math.Vector3;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.event.KeyEvent;

public class Light extends GameObject implements Updatable, Drawable {
	public Material material;
	// This is a number such as GL2.GL_LIGHT0.
	public int lightNumber;
	public LightType type;
	// This is purely for debugging.
	private boolean visibleModel;

	public Light(GameObject parent, int lightNumber, LightType type) {
		super(parent);
		material = new Material();
		this.lightNumber = lightNumber;
		this.type = type;
		this.visibleModel = true;
	}

	public void setLight(GL2 gl, Camera camera) {
		if (enabled) {
			int lightType = 1;
			if (type == LightType.DIRECTIONAL) {
				lightType = 0;
			}
			// This is copied from the week 6 code.
			// rotate the light
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glEnable(GL2.GL_LIGHTING);
			gl.glEnable(lightNumber);
			gl.glEnable(GL2.GL_NORMALIZE);

			float[] ambient = new float[]{material.ambient.x, material.ambient.y, material.ambient.z};
			gl.glLightfv(lightNumber, GL2.GL_AMBIENT, ambient, lightType);

			float[] diffuse = new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z};
			gl.glLightfv(lightNumber, GL2.GL_DIFFUSE, diffuse, lightType);

			float[] specular = new float[]{material.specular.x, material.specular.y, material.specular.z};
			gl.glLightfv(lightNumber, GL2.GL_SPECULAR, specular, lightType);


			gl.glPushMatrix();

			Vector3 globalPosition = getGlobalPositionVector();

			float[] pos = new float[]{(float)globalPosition.x, (float)globalPosition.y, (float)globalPosition.z, lightType};
			gl.glLightfv(lightNumber, GL2.GL_POSITION, pos, 0);
			gl.glPopMatrix();

		} else {
			gl.glDisable(lightNumber);
		}
	}

	public void update(double dt) {
		double movementSpeed = 4.0;

		if (Input.getKey(KeyEvent.VK_I)) {
			transform.position.addSelf(new Vector3(0.0, 0.0, -1.0).multiplySelf(dt * movementSpeed));
		} else if (Input.getKey(KeyEvent.VK_K)) {
			transform.position.addSelf(new Vector3(0.0, 0.0, -1.0).multiplySelf(-dt * movementSpeed));
		}

		if (Input.getKey(KeyEvent.VK_J)) {
			transform.position.addSelf(new Vector3(-1.0, 0.0, 0.0).multiplySelf(dt * movementSpeed));
		} else if (Input.getKey(KeyEvent.VK_L)) {
			transform.position.addSelf(new Vector3(-1.0, 0.0, 0.0).multiplySelf(-dt * movementSpeed));
		}

		if (Input.getKey(KeyEvent.VK_O)) {
			transform.position.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(dt * movementSpeed));
		} else if (Input.getKey(KeyEvent.VK_P)) {
			transform.position.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(-dt * movementSpeed));
		}

	}

	@Override
	public void draw(GL2 gl) {
		if (visibleModel) {
			GLUT glut = new GLUT();
			glut.glutSolidSphere(0.5, 8, 8);
		}
	}
}