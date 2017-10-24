package ass2.spec;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import ass2.game.*;
import ass2.math.Vector3;
import ass2.math.Vector3f;
import ass2.math.Vector4f;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener {

	private GLJPanel panel;

	private long myTime;

	private Terrain myTerrain;
	private Camera currentCamera;
	private List<Light> lights;
	private List<Texture> textures;

	private double dt;

	public Game(Terrain terrain) {
		super("Assignment 2");
		myTime = 0L;
		myTerrain = terrain;
		lights = new ArrayList<Light>();

		Light mainLight = new Light(GameObject.ROOT, GL2.GL_LIGHT0, LightType.DIRECTIONAL);
		mainLight.material.diffuse = new Vector4f(0.8f, 0.8f, 0.8f, 1.0f);
		mainLight.material.specular = new Vector4f(0.8f, 0.8f, 0.8f, 1.0f);
		// When this is a directional light, this value does nothing.
		mainLight.transform.position = new Vector3(5.0, 8.0, -5.0);
		Vector3f sunlightDirection = terrain.getSunlight();
		// When this is a point light, this value does nothing.
		mainLight.transform.rotation = new Vector3(sunlightDirection.x, sunlightDirection.y, sunlightDirection.z);
		lights.add(mainLight);
	}

	/**
	 * Run the game.
	 *
	 */
	public void run() {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setStencilBits(8);
		panel = new GLJPanel();
		panel.addGLEventListener(this);
 
		// Add an animator to call 'display' at 60fps
		// NOTE: I commented this out and replaced it with an unlocked framerate to measure frame
		// times. Everything should be controlled by that anyway.
		///FPSAnimator animator = new FPSAnimator(60);
		Animator animator = new Animator();
		animator.add(panel);
		animator.start();

		getContentPane().add(panel);
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Input input = new Input();
		panel.addKeyListener(input);
		panel.addMouseListener(input);
		panel.addMouseMotionListener(input);
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
		PlayerController player = new PlayerController(GameObject.ROOT, terrain);
		game.currentCamera = new Camera(player);
		player.setCamera(game.currentCamera);
		player.transform.position = new Vector3(-7.0, 3.0, 5.0);
		player.transform.rotation = new Vector3(0.0, -90.0, 0.0);

		game.run();
	}

	/**
	 * Updates every GameObject and computes the delta time for them.
	 */
	private void update() {

		if (!GameObject.ROOT.isEnabled()) {
			System.exit(0);
		}

		// compute the time since the last frame
		long time = System.nanoTime();
		dt = (time - myTime) / 1000000000.0;
		myTime = time;

		setTitle("Assignment 2 - FPS: " + Double.toString(1 / dt));

		// Update the input.
		Input.updateState();

		if (Input.getMouseLock()) {
			Input.recenterMouse(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight());
		}

		myTerrain.setPortalCamera(currentCamera);

		GameObject.ROOT.tryUpdate(dt);
	}

	/**
	 * Sets up all the objects that have currently not yet been initialized.
	 * @param gl
	 */
	public void initializeObjects(GL2 gl) {
		ArrayList<Drawable> uninitializedObjects = new ArrayList<Drawable>(GameObject.UNINITIALIZED_OBJECTS);

		for (Drawable o : uninitializedObjects) {
			o.initialize(gl);
			GameObject.UNINITIALIZED_OBJECTS.remove(o);
		}
	}

	/**
	 * Destroys all resources that need to be disposed.
	 * @param gl
	 */
	public void disposeObjects(GL2 gl) {
		ArrayList<Drawable> undisposedObjects = new ArrayList<Drawable>(GameObject.UNDISPOSED_OBJECTS);

		for (Drawable o : undisposedObjects) {
			o.dispose(gl);
			GameObject.UNDISPOSED_OBJECTS.remove(o);
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		initializeObjects(gl);
		disposeObjects(gl);

		// update the objects
		update();

		// set the view matrix based on the camera position
		currentCamera.setView(gl);

		// draw the scene tree
		// Set up the lighting.
		for (Light l : lights) {
			l.setLight(gl);
		}

		GameObject.ROOT.tryDraw(gl);

		currentCamera.postProcess(gl, dt);
		
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
