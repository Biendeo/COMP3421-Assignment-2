package ass2.game;

import ass2.math.Vector3;
import javafx.scene.input.KeyCode;

public class PlayerController extends GameObject implements Updatable {
	private double movementSpeed = 6.0;
	private double turnSpeed = 120.0;

	public PlayerController(GameObject parent) {
		super(parent);
	}

	@Override
	public void update(double dt) {
		double mainDirection = Math.cos(Math.toRadians(transform.rotation.y));
		double sideDirection = Math.sin(Math.toRadians(transform.rotation.y));

		if (Input.getKey(KeyCode.W) || Input.getKey(KeyCode.UP)) {
			transform.position.addSelf(new Vector3(-sideDirection, 0.0, -mainDirection).multiplySelf(dt * movementSpeed));
		} else if (Input.getKey(KeyCode.S) || Input.getKey(KeyCode.DOWN)) {
			transform.position.addSelf(new Vector3(-sideDirection, 0.0, -mainDirection).multiplySelf(-dt * movementSpeed));
		}

		if (Input.getKey(KeyCode.A)) {
			transform.position.addSelf(new Vector3(-mainDirection, 0.0, sideDirection).multiplySelf(dt * movementSpeed));
		} else if (Input.getKey(KeyCode.D)) {
			transform.position.addSelf(new Vector3(-mainDirection, 0.0, sideDirection).multiplySelf(-dt * movementSpeed));
		}

		if (Input.getKey(KeyCode.R)) {
			transform.position.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(dt * movementSpeed));
		} else if (Input.getKey(KeyCode.F)) {
			transform.position.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(-dt * movementSpeed));
		}

		if (Input.getKey(KeyCode.LEFT)) {
			transform.rotation.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(dt * turnSpeed));
		} else if (Input.getKey(KeyCode.RIGHT)) {
			transform.rotation.addSelf(new Vector3(0.0, 1.0, 0.0).multiplySelf(-dt * turnSpeed));
		}
	}
}
