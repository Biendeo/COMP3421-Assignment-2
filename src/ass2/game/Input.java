package ass2.game;

import java.awt.*;
import java.awt.event.*;

/**
 * Handles input with a singleton design. Allows for a "get keyboard state" rather than implementing
 * listeners every time.
 */
public class Input implements KeyListener, MouseListener, MouseMotionListener {
	private static int keyCount = 256;
	private static boolean[] previousKeyboardState = new boolean[keyCount];
	private static boolean[] currentKeyboardState = new boolean[keyCount];
	private static boolean[] futureKeyboardState = new boolean[keyCount];

	private static int mouseButtonCount = 16;
	private static boolean[] previousMouseState = new boolean[mouseButtonCount];
	private static boolean[] currentMouseState = new boolean[mouseButtonCount];
	private static boolean[] futureMouseState = new boolean[mouseButtonCount];

	private static int previousMouseX = 0;
	private static int previousMouseY = 0;
	private static int currentMouseX = 0;
	private static int currentMouseY = 0;
	private static int futureMouseX = 0;
	private static int futureMouseY = 0;

	private static boolean mouseLock = false;

	/**
	 * Stores the pending keyboard state as the current one, and shifts that as the previous one.
	 * This should be the first thing to be called when working with a new frame.
	 */
	public static void updateState() {
		previousKeyboardState = currentKeyboardState;
		currentKeyboardState = futureKeyboardState;
		futureKeyboardState = currentKeyboardState.clone();
		
		previousMouseState = currentMouseState;
		currentMouseState = futureMouseState;
		futureMouseState = currentMouseState.clone();

		previousMouseX = currentMouseX;
		previousMouseY = currentMouseY;
		currentMouseX = futureMouseX;
		currentMouseY = futureMouseY;
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
	/**
	 * Returns whether the mouse button was pressed down this frame.
	 * @param mouseButton The mouse button that was pressed.
	 * @return Whether it was pressed down this frame.
	 */
	public static boolean getMouseButtonDown(int mouseButton) {
		if (!previousMouseState[mouseButton] && currentMouseState[mouseButton]) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns whether a mouse button was released this frame.
	 * @param mouseButton The mouse button that was released.
	 * @return Whether it was released at this frame.
	 */
	public static boolean getMouseButtonUp(int mouseButton) {
		if (previousMouseState[mouseButton] && !currentMouseState[mouseButton]) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns whether a mouse button is currently being held down.
	 * @param mouseButton The mouse button that is being held down.
	 * @return Whether it is being held down.
	 */
	public static boolean getMouseButton(int mouseButton) {
		if (currentMouseState[mouseButton]) {
			return true;
		} else {
			return false;
		}
	}

	public static int getMouseX() {
		return currentMouseX;
	}

	public static int getMouseY() {
		return currentMouseY;
	}

	public static int getMouseDeltaX() {
		return currentMouseX - previousMouseX;
	}

	public static int getMouseDeltaY() {
		return currentMouseY - previousMouseY;
	}

	public static boolean getMouseLock() {
		return mouseLock;
	}

	public static void toggleMouseLock() {
		mouseLock = !mouseLock;
	}

	public static void recenterMouse(int windowX, int windowY, int windowWidth, int windowHeight) {
		Robot robot;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			return;
		}

		robot.mouseMove(windowX + windowWidth / 2, windowY + windowHeight / 2);
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

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		futureMouseState[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		futureMouseState[e.getButton()] = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		futureMouseX = e.getX();
		futureMouseY = e.getY();
	}
}
