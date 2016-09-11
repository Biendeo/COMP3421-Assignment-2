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

	public static void updateKeyboardState() {
		previousKeyboardState = currentKeyboardState;
		currentKeyboardState = futureKeyboardState;
		futureKeyboardState = currentKeyboardState.clone();
	}

	public static boolean getKeyDown(int key) {
		if (!previousKeyboardState[key] && currentKeyboardState[key]) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean getKeyUp(int key) {
		if (previousKeyboardState[key] && !currentKeyboardState[key]) {
			return true;
		} else {
			return false;
		}
	}

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
