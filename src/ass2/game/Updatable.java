package ass2.game;

/**
 * Indicates that an object changes every frame update.
 */
public interface Updatable {
	/**
	 * Updates the object. This should automatically be called for all GameObjects that implement
	 * this.
	 * @param dt The delta time.
	 */
	public void update(double dt);
}
