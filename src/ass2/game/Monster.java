package ass2.game;

import ass2.math.Vector3;
import ass2.spec.Terrain;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.ArrayList;
import java.util.List;

/**
 * A monster that wanders around the terrain with a given path.
 */
public class Monster extends GameObject implements Drawable, Updatable {

	private Terrain terrain;

	private static final double height = 1.0;

	private double pathLength;

	private ArrayList<Vector3> path;
	private double speed;
	private int currentIndex;

	public Monster(GameObject parent, Terrain terrain, List<Vector3> path, double speed) {
		super(parent);
		this.terrain = terrain;
		setMonsterMovementPattern(path, speed);
		transform.position = new Vector3(path.get(0).x, 0.0, path.get(0).z);
		transform.position.y = terrain.altitude(transform.position.x, transform.position.z);
	}

	/**
	 * Sets the movement pattern of the monster.
	 * @param path
	 * The points the the monster moves to. The Y values of these are ignored, since it is
	 * determined from the terrain.
	 * @param speed
	 * The speed at which the monster moves to these.
	 */
	public void setMonsterMovementPattern(List<Vector3> path, double speed) {
		this.path = new ArrayList<>(path);
		this.speed = speed;
		calculatePathLength();
		currentIndex = this.path.size() - 1;
	}

	private int nextIndex(int currentIndex) {
		int nextIndex = currentIndex + 1;
		if (nextIndex == path.size()) {
			nextIndex = 0;
		}
		return nextIndex;
	}

	private double calculatePathLength() {
		pathLength = 0.0;
		for (int i = 0; i < path.size(); ++i) {
			Vector3 currentPoint = path.get(i);
			Vector3 nextPoint = path.get(nextIndex(i));
			currentPoint.y = 0.0;
			nextPoint.y = 0.0;
			Vector3 difference = nextPoint.subtract(currentPoint);
			pathLength += difference.modulus();
		}
		return pathLength;
	}

	/**
	 * Returns the distance vector the the next point that the monster has to go to. The Y value
	 * is 0 for ease in calculation later.
	 * @return
	 */
	private Vector3 vectorToNextPoint() {
		int nextIndex = nextIndex(currentIndex);

		Vector3 nextPoint = path.get(nextIndex);
		Vector3 currentPosition = transform.position.clone();
		nextPoint.y = 0.0;
		currentPosition.y = 0.0;

		return nextPoint.subtract(currentPosition);
	}

	private void rotateToNextPoint() {
		Vector3 movementVector = vectorToNextPoint();

		// TODO: Figure out if that's actually facing the right direction.
		transform.rotation.y = Math.toDegrees(Math.tan(movementVector.z / movementVector.x));
	}

	private void moveTowardsNextPoint(double amount) {
		// This gets rid of stack overflows with large amounts.
		while (amount >= pathLength) {
			amount -= pathLength;
		}

		Vector3 movementVector = vectorToNextPoint();

		if (movementVector.modulus() < amount) {
			currentIndex = nextIndex(currentIndex);
			transform.position = path.get(currentIndex);
			rotateToNextPoint();
			moveTowardsNextPoint(amount - movementVector.modulus());
		} else {
			Vector3 movementAmount = movementVector.divide(movementVector.modulus()).multiply(amount);
			transform.position.addSelf(movementAmount);
		}
	}

	@Override
	public void update(double dt) {
		rotateToNextPoint();
		moveTowardsNextPoint(dt * speed);
		transform.position.y = terrain.altitude(transform.position.x, transform.position.z) + height / 2;
	}

	@Override
	public void initialize(GL2 gl) {

	}

	@Override
	public void dispose(GL2 gl) {

	}

	@Override
	public void draw(GL2 gl) {
		GLUT glut = new GLUT();
		glut.glutSolidSphere(height / 2, 8, 8);
	}
}
