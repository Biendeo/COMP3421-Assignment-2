package ass2.game;

import com.jogamp.opengl.GL2;

/**
 * Indicates that an object can be represented on the screen.
 */
public interface Drawable {
	public void draw(GL2 gl);
}
