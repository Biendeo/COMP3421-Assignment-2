package ass2.game;

import ass2.math.Vector3;

/**
 * A struct-like class that handles the translate, rotation, and scale of something.
 */
public class Transform {
	public Vector3 position;
	// TODO: Convert this to a quaternion later.
	public Vector3 rotation;
	public Vector3 scale;

	public Transform() {
		position = new Vector3();
		rotation = new Vector3();
		scale = new Vector3();
	}
}
