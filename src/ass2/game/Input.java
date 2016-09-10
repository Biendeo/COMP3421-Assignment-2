package ass2.game;

import javafx.scene.input.KeyCode;

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

	public static boolean getKeyDown(KeyCode key) {
		if (!previousKeyboardState[key.ordinal()] && currentKeyboardState[key.ordinal()]) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean getKeyUp(KeyCode key) {
		if (previousKeyboardState[key.ordinal()] && !currentKeyboardState[key.ordinal()]) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean getKey(KeyCode key) {
		if (currentKeyboardState[key.ordinal()]) {
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
		futureKeyboardState[KeyCode.getKeyCode(KeyEvent.getKeyText(e.getKeyCode())).ordinal()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		futureKeyboardState[KeyCode.getKeyCode(KeyEvent.getKeyText(e.getKeyCode())).ordinal()] = false;
	}
}
