package ass2.game;

import com.jogamp.opengl.GL2;

/**
 * Indicates that an object can be represented on the screen.
 */
public interface Drawable {
	/**
	 * Sets up details that need to be added to the program (textures, VBOs, etc.).
	 * @param gl
	 */
	public void initialize(GL2 gl);

	/**
	 * Draws the object in the current model view (this should be called except in GameObject).
	 * @param gl
	 */
	public void draw(GL2 gl);
}
