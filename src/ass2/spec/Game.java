package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import ass2.game.*;
import ass2.math.Vector3;
import ass2.math.Vector4f;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener {

	private long myTime;

	private Terrain myTerrain;
	private Camera currentCamera;
	private List<Light> lights;

	public Game(Terrain terrain) {
		super("Assignment 2");
		myTime = 0L;
		myTerrain = terrain;
		lights = new ArrayList<Light>();

		// This is currently a point light just to demonstrate the effect.
		Light mainLight = new Light(GameObject.ROOT, GL2.GL_LIGHT0, LightType.POINT);
		mainLight.material.diffuse = new Vector4f(0.8f, 0.8f, 0.8f, 1.0f);
		mainLight.transform.position = new Vector3(5.0, 8.0, -5.0);
		lights.add(mainLight);

	}

	/**
	 * Run the game.
	 *
	 */
	public void run() {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLJPanel panel = new GLJPanel();
		panel.addGLEventListener(this);
 
		// Add an animator to call 'display' at 60fps
		FPSAnimator animator = new FPSAnimator(60);
		animator.add(panel);
		animator.start();

		getContentPane().add(panel);
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		panel.addKeyListener(new Input());
	}

	/**
	 * Load a level file and display it.
	 *
	 * @param args - The first argument is a level file in JSON format
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Terrain terrain = LevelIO.load(new File(args[0]));
		Game game = new Game(terrain);
		PlayerController player = new PlayerController(GameObject.ROOT);
		game.currentCamera = new Camera(player);
		player.transform.position = new Vector3(5.0, 3.0, 10.0);
		player.transform.rotation = new Vector3(0.0, 0.0, 0.0);

		game.run();
	}

	/**
	 * Updates every GameObject and computes the delta time for them.
	 */
	private void update() {

		// compute the time since the last frame
		long time = System.currentTimeMillis();
		double dt = (time - myTime) / 1000.0;
		myTime = time;

		// Update the input.
		Input.updateKeyboardState();

		GameObject.ROOT.tryUpdate(dt);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// update the objects
		update();

		// set the view matrix based on the camera position
		currentCamera.setView(gl);

		// draw the scene tree
		// Set up the lighting.
		for (Light l : lights) {
			l.setLight(gl, currentCamera);
		}

		GameObject.ROOT.tryDraw(gl);
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void init(GLAutoDrawable drawable) {

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

		// tell the camera and the mouse that the screen has reshaped
		GL2 gl = drawable.getGL().getGL2();

		currentCamera.reshape(gl, x, y, width, height);
		
	}
}
