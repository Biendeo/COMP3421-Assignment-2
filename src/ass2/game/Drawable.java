package ass2.game;

import com.jogamp.opengl.GL2;

/**
 * Indicates that an object can be represented on the screen.
 */
public interface Drawable {
	/**
	 * Sets up details that need to be added to the program (textures, VBOs, etc.).
	 * @param gl The GL object.
	 */
	public void initialize(GL2 gl);

	/**
	 * Disposes properties about this object (textures, VBOs, etc.).
	 * @param gl The GL object.
	 */
	public void dispose(GL2 gl);

	/**
	 * Draws the object in the current co-ordinate frame.
	 * This should automatically happen if the GameObject implements this.
	 * @param gl The GL object.
	 */
	public void draw(GL2 gl);
}
