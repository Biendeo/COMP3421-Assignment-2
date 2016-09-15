package ass2.game;

import com.jogamp.opengl.GL2;

/**
 * Indicates that an object can be represented on the screen.
 */
public interface Drawable {
	/**
	 * Draws the object in the current co-ordinate frame.
	 * This should automatically happen if the GameObject implements this.
	 * @param gl The GL object.
	 */
	public void draw(GL2 gl);
}
