package ass2.math;

import ass2.game.GameObject;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Some test cases for showing that object rotation in 3D space works correctly.
 * The test cases were based on rotations done in Unity, which may be different to how we want them
 * to be handled.
 */
public class RotationTest extends TestCase {

	private static final double EPSILON = 0.001;

	@Test
	public void testRotation1() {
		GameObject object = new GameObject(GameObject.ROOT);

		Vector3 rotation = object.transform.rotation.clone();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 0.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		object.transform.rotation.addSelf(new Vector3(0.0, 90.0, 0.0));

		rotation = object.transform.rotation.clone();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 90.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		GameObject childObject = new GameObject(object);

		rotation = childObject.transform.rotation.clone();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 0.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		rotation = childObject.getGlobalRotationVector();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 90.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		childObject.transform.rotation.addSelf(new Vector3(90.0, 0.0, 0.0));

		rotation = childObject.transform.rotation.clone();

		assertEquals(rotation.x, 90.0, EPSILON);
		assertEquals(rotation.y, 0.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		rotation = childObject.getGlobalRotationVector();

		System.out.println(Double.toString(rotation.x) + ", " + Double.toString(rotation.y) + ", " + Double.toString(rotation.z));

		assertEquals(rotation.x, 90.0, EPSILON);
		assertEquals(rotation.y, 90.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);
	}

	@Test
	public void testRotation2() {
		GameObject object = new GameObject(GameObject.ROOT);

		Vector3 rotation = object.transform.rotation.clone();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 0.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		object.transform.rotation.addSelf(new Vector3(0.0, 45.0, 0.0));

		rotation = object.transform.rotation.clone();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 45.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		GameObject childObject = new GameObject(object);

		rotation = childObject.transform.rotation.clone();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 0.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		rotation = childObject.getGlobalRotationVector();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 45.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		childObject.transform.rotation.addSelf(new Vector3(45.0, 0.0, 0.0));

		rotation = childObject.transform.rotation.clone();

		assertEquals(rotation.x, 45.0, EPSILON);
		assertEquals(rotation.y, 0.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		rotation = childObject.getGlobalRotationVector();

		System.out.println(Double.toString(rotation.x) + ", " + Double.toString(rotation.y) + ", " + Double.toString(rotation.z));

		assertEquals(rotation.x, 45.0, EPSILON);
		assertEquals(rotation.y, 45.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);
	}

	@Test
	public void testRotation3() {
		GameObject object = new GameObject(GameObject.ROOT);

		Vector3 rotation = object.transform.rotation.clone();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 0.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		object.transform.rotation.addSelf(new Vector3(0.0, 90.0, 90.0));

		rotation = object.transform.rotation.clone();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 90.0, EPSILON);
		assertEquals(rotation.z, 90.0, EPSILON);

		GameObject childObject = new GameObject(object);

		rotation = childObject.transform.rotation.clone();

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 0.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		rotation = childObject.getGlobalRotationVector();

		System.out.println(Double.toString(rotation.x) + ", " + Double.toString(rotation.y) + ", " + Double.toString(rotation.z));

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 90.0, EPSILON);
		assertEquals(rotation.z, 90.0, EPSILON);

		childObject.transform.rotation.addSelf(new Vector3(90.0, 0.0, 0.0));

		rotation = childObject.transform.rotation.clone();

		assertEquals(rotation.x, 90.0, EPSILON);
		assertEquals(rotation.y, 0.0, EPSILON);
		assertEquals(rotation.z, 0.0, EPSILON);

		rotation = childObject.getGlobalRotationVector();

		System.out.println(Double.toString(rotation.x) + ", " + Double.toString(rotation.y) + ", " + Double.toString(rotation.z));

		assertEquals(rotation.x, 0.0, EPSILON);
		assertEquals(rotation.y, 180.0, EPSILON);
		assertEquals(rotation.z, 90.0, EPSILON);
	}
}
