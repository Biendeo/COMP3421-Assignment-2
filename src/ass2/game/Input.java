package ass2.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles input with a singleton design. Allows for a "get keyboard state" rather than implementing
 * listeners every time.
 */
public class Input implements KeyListener {
	private static int keyCount = 256;
	private static boolean[] previousKeyboardState = new boolean[keyCount];
	private static boolean[] currentKeyboardState = new boolean[keyCount];
	private static boolean[] futureKeyboardState = new boolean[keyCount];

	/**
	 * Stores the pending keyboard state as the current one, and shifts that as the previous one.
	 * This should be the first thing to be called when working with a new frame.
	 */
	public static void updateKeyboardState() {
		previousKeyboardState = currentKeyboardState;
		currentKeyboardState = futureKeyboardState;
		futureKeyboardState = currentKeyboardState.clone();
	}

	/**
	 * Returns whether the key was pressed down this frame.
	 * @param key The key that was pressed.
	 * @return Whether it was pressed down this frame.
	 */
	public static boolean getKeyDown(int key) {
		if (!previousKeyboardState[key] && currentKeyboardState[key]) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns whether a key was released this frame.
	 * @param key The key that was released.
	 * @return Whether it was released at this frame.
	 */
	public static boolean getKeyUp(int key) {
		if (previousKeyboardState[key] && !currentKeyboardState[key]) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns whether a key is currently being held down.
	 * @param key The key that is being held down.
	 * @return Whether it is being held down.
	 */
	public static boolean getKey(int key) {
		if (currentKeyboardState[key]) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		futureKeyboardState[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		futureKeyboardState[e.getKeyCode()] = false;
	}
}
