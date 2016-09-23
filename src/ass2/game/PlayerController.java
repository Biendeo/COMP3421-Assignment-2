package ass2.game;

import ass2.math.Vector3;

import java.awt.event.KeyEvent;

/**
 * Handles the player character's input and appearance.
 */
public class PlayerController extends GameObject implements Updatable {
	private double movementSpeed = 6.0;
	private double turnSpeed = 120.0;
	private double mouseSensitivityX = 120.0;
	private double mouseSensitivityY = 120.0;

	private boolean noClip;
	private boolean mouseLook;

	// This camera should be a child object to this player.
	private Camera camera;

	/**
	 * Constructs a player.
	 * @param parent The parent GameObject.
	 */
	public PlayerController(GameObject parent) {
		super(parent);
		noClip = false;
		mouseLook = Input.getMouseLock();
		camera = null;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	@Override
	public void update(double dt) {
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

		turnRight(mouseSensitivityX * Input.getMouseDeltaX() / 100.0);
		lookUp(mouseSensitivityY * Input.getMouseDeltaY() / 100.0);

		System.out.println(Double.toString(camera.transform.rotation.x) + ", " + Double.toString(camera.transform.rotation.y) + ", " + Double.toString(camera.transform.rotation.z));

		if (Input.getKeyDown(KeyEvent.VK_V)) {
			toggleNoClip();
		}

		if (Input.getKeyDown(KeyEvent.VK_ALT)) {
			System.out.println("ALT PRESSED");
			Input.toggleMouseLock();
			System.out.println("Mouse lock is: " + Boolean.toString(Input.getMouseLock()));
		}

		if (Input.getKeyDown(KeyEvent.VK_ESCAPE)) {
			GameObject.ROOT.setEnabled(false);
		}

		// TODO: Balance player on Terrain.
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
			camera.transform.rotation.addSelf(new Vector3(-1.0, 0.0, 0.0).multiplySelf(rate));
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
}
