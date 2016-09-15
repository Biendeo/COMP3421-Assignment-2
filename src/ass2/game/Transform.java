package ass2.game;

import ass2.math.Vector3;

/**
 * A struct-like class that handles the translate, rotation, and scale of something.
 */
public class Transform {
	public Vector3 position;
	public Vector3 rotation;
	public Vector3 scale;

	/**
	 * Constructs a transform with default 0 components (and 1 scale).
	 */
	public Transform() {
		position = new Vector3(0.0, 0.0, 0.0);
		rotation = new Vector3(0.0, 0.0, 0.0);
		scale = new Vector3(1.0, 1.0, 1.0);
	}
}
