package ass2.game;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;

/**
 * An object in the game world.
 */
public class GameObject {
	public static final ArrayList<GameObject> ALL_OBJECTS = new ArrayList<GameObject>();
	public static final GameObject ROOT = new GameObject(null);

	private GameObject parent;
	private ArrayList<GameObject> children;

	private Transform transform;

	private boolean enabled;


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
			if (this.getClass().isAssignableFrom(Updatable.class)) {
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
			// TODO: Position the view frame.
			if (this.getClass().isAssignableFrom(Drawable.class)) {
				((Drawable)this).draw(gl);
			}
			ArrayList<GameObject> childrenClonedList = new ArrayList<GameObject>(children);
			for (GameObject o : childrenClonedList) {
				o.tryDraw(gl);
			}
		}
	}
}
