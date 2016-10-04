package ass2.game;

import ass2.math.Vector3;
import ass2.spec.Terrain;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.event.KeyEvent;

/**
 * Handles the player character's input and appearance.
 */
public class PlayerController extends GameObject implements Updatable, Drawable {
	private double movementSpeed = 6.0;
	private double turnSpeed = 120.0;
	private double mouseSensitivityX = 120.0;
	private double mouseSensitivityY = 120.0;

	private double height = 1.0;
	private double cameraDistance = 3.0;

	private boolean noClip;
	private boolean mouseLook;
	private boolean thirdPerson;

	// This camera should be a child object to this player.
	private Camera camera;

	private Terrain terrain;

	/**
	 * Constructs a player.
	 * @param parent The parent GameObject.
	 */
	public PlayerController(GameObject parent, Terrain terrain) {
		super(parent);
		this.terrain = terrain;
		noClip = false;
		mouseLook = false;
		thirdPerson = false;
		// mouseLook = Input.getMouseLock();
		camera = null;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	@Override
	public void update(double dt) {
		if (mouseLook) {
			turnRight(mouseSensitivityX * Input.getMouseDeltaX() / 100.0);
			lookDown(mouseSensitivityY * Input.getMouseDeltaY() / 100.0);
		}

		if (Input.getKey(KeyEvent.VK_W) || Input.getKey(KeyEvent.VK_UP)) {
			if (noClip) {
				moveForwardNoClip(dt * movementSpeed);
			} else {
				moveForward(dt * movementSpeed);
			}
		} else if (Input.getKey(KeyEvent.VK_S) || Input.getKey(KeyEvent.VK_DOWN)) {
			if (noClip) {
				moveBackwardNoClip(dt * movementSpeed);
			} else {
				moveBackward(dt * movementSpeed);
			}
		}

		if (Input.getKey(KeyEvent.VK_A)) {
			strafeLeft(dt * movementSpeed);
		} else if (Input.getKey(KeyEvent.VK_D)) {
			strafeRight(dt * movementSpeed);
		}

		if (noClip) {
			if (Input.getKey(KeyEvent.VK_R)) {
				flyUp(dt * movementSpeed);
			} else if (Input.getKey(KeyEvent.VK_F)) {
				flyDown(dt * movementSpeed);
			}
		}

		if (Input.getKey(KeyEvent.VK_LEFT)) {
			turnLeft(dt * turnSpeed);
		} else if (Input.getKey(KeyEvent.VK_RIGHT)) {
			turnRight(dt * turnSpeed);
		}

		if (Input.getKey(KeyEvent.VK_T)) {
			lookUp(dt * turnSpeed);
		} else if (Input.getKey(KeyEvent.VK_G)) {
			lookDown(dt * turnSpeed);
		}

		if (thirdPerson) {
			positionThirdPersonCamera();
		}

		if (Input.getKeyDown(KeyEvent.VK_V)) {
			toggleNoClip();
			System.out.println("Noclip is: " + Boolean.toString(noClip));
		}

		if (Input.getKeyDown(KeyEvent.VK_M)) {
			mouseLook = !mouseLook;
			System.out.println("Mouse look is: " + Boolean.toString(mouseLook));

			//Input.toggleMouseLock();
			//System.out.println("Mouse lock is: " + Boolean.toString(Input.getMouseLock()));
		}

		if (Input.getKeyDown(KeyEvent.VK_COMMA)) {
			toggleThirdPerson();
			System.out.println("Third person is: " + Boolean.toString(thirdPerson));
		}

		if (Input.getKeyDown(KeyEvent.VK_ESCAPE)) {
			GameObject.ROOT.setEnabled(false);
		}

		if (!noClip) {
			transform.position.y = terrain.altitude(transform.position.x, transform.position.z) + height;
		}
	}

	private void moveForward(double rate) {
		double mainDirection = Math.cos(Math.toRadians(transform.rotation.y));
		double sideDirection = Math.sin(Math.toRadians(transform.rotation.y));
		transform.position.addSelf(new Vector3(-sideDirection, 0.0, -mainDirection).multiplySelf(rate));
	}

	private void moveBackward(double rate) {
		moveForward(-rate);
	}

	private void moveForwardNoClip(double rate) {
		double mainDirection = Math.cos(Math.toRadians(transform.rotation.y));
		double sideDirection = Math.sin(Math.toRadians(transform.rotation.y));
		double flyDirection = Math.sin(Math.toRadians(camera.transform.rotation.x));
		transform.position.addSelf(new Vector3(-sideDirection, 0.0, -mainDirection).multiplySelf(rate * (1 - Math.abs(flyDirection))));
		transform.position.addSelf(new Vector3(0.0, flyDirection, 0.0).multiplySelf(rate));
	}

	private void moveBackwardNoClip(double rate) {
		moveForwardNoClip(-rate);
	}

	private void strafeLeft(double rate) {
		double mainDirection = Math.cos(Math.toRadians(transform.rotation.y));
		double sideDirection = Math.sin(Math.toRadians(transform.rotation.y));
		transform.position.addSelf(new Vector3(-mainDirection, 0.0, sideDirection).multiplySelf(rate));
	}

	private void strafeRight(double rate) {
		strafeLeft(-rate);
	}

	private void turnLeft(double rate) {
		transform.rotation.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(rate));
	}

	private void turnRight(double rate) {
		turnLeft(-rate);
	}

	private void lookUp(double rate) {
		if (camera != null) {
			camera.transform.rotation.addSelf(new Vector3(1.0, 0.0, 0.0).multiplySelf(rate));
			if (camera.transform.rotation.x > 90.0) {
				camera.transform.rotation.x = 90.0;
			} else if (camera.transform.rotation.x < -90.0) {
				camera.transform.rotation.x = -90.0;
			}
		}
	}

	private void lookDown(double rate) {
		lookUp(-rate);
	}

	private void flyUp(double rate) {
		transform.position.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(rate));
	}

	private void flyDown(double rate) {
		flyUp(-rate);
	}

	private void toggleNoClip() {
		noClip = !noClip;
	}

	private void toggleThirdPerson() {
		thirdPerson = !thirdPerson;
		if (!thirdPerson) {
			camera.transform.position = new Vector3(0.0, 0.0, 0.0);
		} else {
			positionThirdPersonCamera();
		}
	}

	private void positionThirdPersonCamera() {
		double angleRads = Math.toRadians(camera.transform.rotation.x);

		double cameraHeight = -Math.sin(angleRads);
		double cameraHorizontalDistance = Math.abs(Math.cos(angleRads));

		camera.transform.position = new Vector3(0.0, cameraHeight, cameraHorizontalDistance).multiply(cameraDistance);
	}

	@Override
	public void initialize(GL2 gl) {

	}

	@Override
	public void dispose(GL2 gl) {

	}

	@Override
	public void draw(GL2 gl) {
		// TODO: Work this with the portals.
		if (thirdPerson) {
			GLUT glut = new GLUT();
			glut.glutSolidTeapot(1.0);
		}
	}
}
