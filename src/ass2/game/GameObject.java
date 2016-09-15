package ass2.game;

import ass2.math.MathUtil;
import ass2.math.Vector3;
import com.jogamp.opengl.GL2;

import java.util.ArrayList;

/**
 * An object in the game world.
 */
public class GameObject {
	public static final ArrayList<GameObject> ALL_OBJECTS = new ArrayList<GameObject>();
	public static final GameObject ROOT = new GameObject(null);

	protected GameObject parent;
	protected ArrayList<GameObject> children;

	public Transform transform;

	protected boolean enabled;


	/**
	 * Constructs a new GameObject at the origin.
	 * @param parent
	 * The parent of this object. For root objects, make sure it is GameObject.ROOT.
	 */
	public GameObject(GameObject parent) {
		this.parent = parent;
		if (parent != null) {
			this.parent.children.add(this);
		}

		this.children = new ArrayList<GameObject>();

		if (ALL_OBJECTS == null) {
			System.err.println("ALL_OBJECTS is null.");
		}

		ALL_OBJECTS.add(this);

		this.transform = new Transform();
		this.enabled = true;
	}

	/**
	 * Deletes this GameObject and all of its children.
	 */
	public void delete() {
		ArrayList<GameObject> childrenClonedList = new ArrayList<GameObject>(children);
		for (GameObject g : childrenClonedList) {
			g.delete();
		}
		ALL_OBJECTS.remove(this);
		if (parent != null) {
			parent.children.remove(this);
		}
	}

	public void tryUpdate(double dt) {
		if (enabled) {
			if (this instanceof Updatable) {
				((Updatable)this).update(dt);
			}
			ArrayList<GameObject> childrenClonedList = new ArrayList<GameObject>(children);
			for (GameObject o : childrenClonedList) {
				o.tryUpdate(dt);
			}
		}
	}

	/**
	 * Establishes the model view matrix and draws this object if it is drawable.
	 * @param gl The GL object.
	 */
	public void tryDraw(GL2 gl) {
		if (enabled) {

			gl.glPushMatrix();
			gl.glTranslated(transform.position.x, transform.position.y, transform.position.z);
			gl.glRotated(transform.rotation.x, 1.0, 0.0, 0.0);
			gl.glRotated(transform.rotation.y, 0.0, 1.0, 0.0);
			gl.glRotated(transform.rotation.z, 0.0, 0.0, 1.0);
			gl.glScaled(transform.scale.x, transform.scale.y, transform.scale.z);

			if (this instanceof Drawable) {
				((Drawable)this).draw(gl);
			}
			ArrayList<GameObject> childrenClonedList = new ArrayList<GameObject>(children);
			for (GameObject o : childrenClonedList) {
				o.tryDraw(gl);
			}

			gl.glPopMatrix();
		}
	}

	/**
	 * Compute the object's position in world coordinates's and returns it as a Vector3.
	 *
	 * @return The world coordinates as a Vector3 (x,y,z)
	 */
	public Vector3 getGlobalPositionVector() {
		Vector3 parentGlobalPosition = ((this != GameObject.ROOT) ? parent.getGlobalPositionVector() : new Vector3());
		Vector3 parentGlobalRotation = ((this != GameObject.ROOT) ? parent.getGlobalRotationVector() : new Vector3());
		Vector3 parentGlobalScale = ((this != GameObject.ROOT) ? parent.getGlobalScaleVector() : new Vector3(1.0, 1.0, 1.0));

		double[][] localPositionMatrix = MathUtil.translationMatrix4(transform.position.clone());

		// I didn't really know how to use these matrices effectively.
		double[][] parentGlobalRotationMatrix = MathUtil.rotationMatrixXYZ(parentGlobalRotation);

		double[][] rotatedTranslationMatrix = MathUtil.multiply4(parentGlobalRotationMatrix, localPositionMatrix);

		// TODO: Use a matrix for this.
		Vector3 intermediateVector = MathUtil.translationMatrixToVector(rotatedTranslationMatrix);
		intermediateVector.multiplySelf(parentGlobalScale);

		Vector3 finalVector = intermediateVector.add(parentGlobalPosition);

		return finalVector;
	}

	/**
	 * Compute the object's rotation in the global coordinate frame.
	 *
	 * @return The global rotation of the object in a Vector3
	 */
	public Vector3 getGlobalRotationVector() {
		if (this == GameObject.ROOT) {
			return transform.rotation.clone();
		} else {
			double[][] globalRotationMatrix = MathUtil.rotationMatrixXYZ(parent.getGlobalRotationVector());
			double[][] rotationMatrix = MathUtil.rotationMatrixXYZ(transform.rotation.clone());

			double[][] multipliedMatrix = MathUtil.multiply4(rotationMatrix, globalRotationMatrix);

			return MathUtil.rotationMatrixToVector(multipliedMatrix);
		}
	}

	/**
	 * Computes the object's global scale.
	 *
	 * @return The global scale of the object in a Vector3.
	 */
	public Vector3 getGlobalScaleVector() {
		if (this == GameObject.ROOT) {
			return transform.scale.clone();
		} else {
			double[][] globalScaleMatrix = MathUtil.scaleMatrix4(parent.getGlobalScaleVector());
			double[][] scaleMatrix = MathUtil.scaleMatrix4(transform.scale.clone());

			double[][] multipliedMatrix = MathUtil.multiply4(scaleMatrix, globalScaleMatrix);

			return MathUtil.scaleMatrixToVector(multipliedMatrix);
		}
	}



	/**
	 * Change the parent of a game object.
	 *
	 * @param parent
	 */
	public void setParent(GameObject parent) {
		// This is copy-pasted from my assignment 1.
		Vector3 globalPosition = getGlobalPositionVector();
		Vector3 globalRotation = getGlobalRotationVector();
		Vector3 globalScale = getGlobalScaleVector();

		this.parent.children.remove(this);
		this.parent = parent;
		this.parent.children.add(this);

		Vector3 parentGlobalPosition = parent.getGlobalPositionVector();
		Vector3 parentGlobalRotation = parent.getGlobalRotationVector();
		Vector3 parentGlobalScale = parent.getGlobalScaleVector();

		Vector3 parentGlobalRotationInverted = parentGlobalRotation.multiply(-1);

		Vector3 globalPositionDifference = globalPosition.subtract(parentGlobalPosition);

		double[][] parentGlobalRotationMatrix = MathUtil.rotationMatrixXYZ(parentGlobalRotationInverted);

		// TODO: Use a matrix for this.
		Vector3 globalPositionDifferenceScaled = globalPositionDifference.clone();
		globalPositionDifferenceScaled.multiplySelf(parentGlobalScale.invert());

		double[][] globalPositionDifferenceScaledMatrix = MathUtil.translationMatrix4(globalPositionDifferenceScaled);

		double[][] globalRotatedMatrix = MathUtil.multiply4(parentGlobalRotationMatrix, globalPositionDifferenceScaledMatrix);

		transform.position = MathUtil.translationMatrixToVector(globalRotatedMatrix);

		transform.rotation = globalRotation.subtract(parentGlobalRotation);
		transform.scale = globalScale.divide(parentGlobalScale);
	}
}
