package ass2.game;

import ass2.math.Vector3;

import java.awt.event.KeyEvent;

/**
 * Handles the player character's input and appearance.
 */
public class PlayerController extends GameObject implements Updatable {
	private double movementSpeed = 6.0;
	private double turnSpeed = 120.0;

	/**
	 * Constructs a player.
	 * @param parent The parent GameObject.
	 */
	public PlayerController(GameObject parent) {
		super(parent);
	}

	@Override
	public void update(double dt) {
		double mainDirection = Math.cos(Math.toRadians(transform.rotation.y));
		double sideDirection = Math.sin(Math.toRadians(transform.rotation.y));

		if (Input.getKey(KeyEvent.VK_W) || Input.getKey(KeyEvent.VK_UP)) {
			transform.position.addSelf(new Vector3(-sideDirection, 0.0, -mainDirection).multiplySelf(dt * movementSpeed));
		} else if (Input.getKey(KeyEvent.VK_S) || Input.getKey(KeyEvent.VK_DOWN)) {
			transform.position.addSelf(new Vector3(-sideDirection, 0.0, -mainDirection).multiplySelf(-dt * movementSpeed));
		}

		if (Input.getKey(KeyEvent.VK_A)) {
			transform.position.addSelf(new Vector3(-mainDirection, 0.0, sideDirection).multiplySelf(dt * movementSpeed));
		} else if (Input.getKey(KeyEvent.VK_D)) {
			transform.position.addSelf(new Vector3(-mainDirection, 0.0, sideDirection).multiplySelf(-dt * movementSpeed));
		}

		if (Input.getKey(KeyEvent.VK_R)) {
			transform.position.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(dt * movementSpeed));
		} else if (Input.getKey(KeyEvent.VK_F)) {
			transform.position.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(-dt * movementSpeed));
		}

		if (Input.getKey(KeyEvent.VK_LEFT)) {
			transform.rotation.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(dt * turnSpeed));
		} else if (Input.getKey(KeyEvent.VK_RIGHT)) {
			transform.rotation.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(-dt * turnSpeed));
		}
	}
}
