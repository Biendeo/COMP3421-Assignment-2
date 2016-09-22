package ass2.game;

import ass2.math.Vector3;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.event.KeyEvent;

/**
 * A light object. Can be either directional or point.
 *
 * NOTE: The implemented interfaces are for experimenting. Remove them when submitting.
 */
public class Light extends GameObject implements Updatable, Drawable {
	public Material material;
	// This is a number such as GL2.GL_LIGHT0.
	public int lightNumber;
	public LightType type;
	// This is purely for debugging.
	private boolean visibleModel;

	/**
	 * Constructs a new light in the scene.
	 * @param parent The parent GameObject.
	 * @param lightNumber The light number (this should be GL2.GL_LIGHT0 or similar).
	 * @param type The type of light (point, directional, spot).
	 */
	public Light(GameObject parent, int lightNumber, LightType type) {
		super(parent);
		material = new Material();
		this.lightNumber = lightNumber;
		this.type = type;
		this.visibleModel = true;
	}

	/**
	 * Establishes the light in the world and applies its light properties.
	 * @param gl The GL object.
	 */
	public void setLight(GL2 gl) {
		if (enabled) {
			int lightType = 1;
			if (type == LightType.DIRECTIONAL) {
				lightType = 0;
			}
			// This is copied from the week 6 code.
			// rotate the light
			//gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glEnable(GL2.GL_LIGHTING);
			gl.glEnable(lightNumber);
			gl.glEnable(GL2.GL_NORMALIZE);

			float[] ambient = new float[]{material.ambient.x, material.ambient.y, material.ambient.z};
			gl.glLightfv(lightNumber, GL2.GL_AMBIENT, ambient, 0);

			float[] diffuse = new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z};
			gl.glLightfv(lightNumber, GL2.GL_DIFFUSE, diffuse, 0);

			float[] specular = new float[]{material.specular.x, material.specular.y, material.specular.z};
			gl.glLightfv(lightNumber, GL2.GL_SPECULAR, specular, 0);


			Vector3 lightVector = new Vector3();
			if (type == LightType.POINT) {
				lightVector = getGlobalPositionVector();
			} else if (type == LightType.DIRECTIONAL) {
				lightVector = getGlobalRotationVector();
			}

			float[] pos = new float[]{(float)lightVector.x, (float)lightVector.y, (float)lightVector.z, lightType};
			gl.glLightfv(lightNumber, GL2.GL_POSITION, pos, 0);
			System.out.println(Double.toString(transform.rotation.x) + ", " + Double.toString(transform.rotation.y) + ", " + Double.toString(transform.rotation.z));
			System.out.println(Double.toString(lightVector.x) + ", " + Double.toString(lightVector.y) + ", " + Double.toString(lightVector.z));

		} else {
			gl.glDisable(lightNumber);
		}
	}

	public void update(double dt) {
		double movementSpeed = 4.0;
		double rotationSpeed = 60.0;
		if (Input.getKeyDown(KeyEvent.VK_SPACE)) {
			if (type == LightType.POINT) {
				type = LightType.DIRECTIONAL;
			} else {
				type = LightType.POINT;
			}
		}

		if (type == LightType.POINT) {
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
		} else if (type == LightType.DIRECTIONAL) {
			if (Input.getKey(KeyEvent.VK_I)) {
				transform.rotation.addSelf(new Vector3(0.0, 0.0, -1.0).multiplySelf(dt * rotationSpeed));
			} else if (Input.getKey(KeyEvent.VK_K)) {
				transform.rotation.addSelf(new Vector3(0.0, 0.0, -1.0).multiplySelf(-dt * rotationSpeed));
			}

			if (Input.getKey(KeyEvent.VK_J)) {
				transform.rotation.addSelf(new Vector3(-1.0, 0.0, 0.0).multiplySelf(dt * rotationSpeed));
			} else if (Input.getKey(KeyEvent.VK_L)) {
				transform.rotation.addSelf(new Vector3(-1.0, 0.0, 0.0).multiplySelf(-dt * rotationSpeed));
			}

			if (Input.getKey(KeyEvent.VK_O)) {
				transform.rotation.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(dt * rotationSpeed));
			} else if (Input.getKey(KeyEvent.VK_P)) {
				transform.rotation.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(-dt * rotationSpeed));
			}
		}
	}

	@Override
	public void draw(GL2 gl) {
		if (visibleModel) {
			GLUT glut = new GLUT();
			if (type == LightType.POINT) {
				glut.glutSolidSphere(0.5, 8, 8);
			} else if (type == LightType.DIRECTIONAL) {
				gl.glRotated(90.0, 0.0, 1.0, 0.0);
				glut.glutSolidCone(0.5, 1.0, 8, 8);
			}
		}
	}

	@Override
	public void initialize(GL2 gl) {

	}

	@Override
	public void dispose(GL2 gl) {

	}
}
