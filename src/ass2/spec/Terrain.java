package ass2.spec;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ass2.game.Drawable;
import ass2.game.GameObject;
import ass2.game.Material;
import ass2.math.Vector3;
import ass2.math.Vector3f;
import ass2.math.Vector4f;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain extends GameObject implements Drawable {

	private Dimension mySize;
	private double[][] myAltitude;
	private List<Tree> myTrees;
	private List<Road> myRoads;
	private Vector3f mySunlight;
	private Material material;
	/**
	 * Create a new terrain
	 *
	 * @param width The number of vertices in the x-direction
	 * @param depth The number of vertices in the z-direction
	 */

	public Terrain(int width, int depth) {
		super(GameObject.ROOT);
		mySize = new Dimension(width, depth);
		myAltitude = new double[width][depth];
		myTrees = new ArrayList<Tree>();
		myRoads = new ArrayList<Road>();
		mySunlight = new Vector3f();

		material = new Material();
		material.ambient = new Vector4f(0.3f, 0.3f, 0.3f, 1.0f);
		material.diffuse = new Vector4f(0.3f, 0.9f, 0.1f, 1.0f);
		material.specular = new Vector4f(0.1f, 0.1f, 0.1f, 0.1f);

	}

	public Terrain(Dimension size) {
		this(size.width, size.height);
	}

	public Dimension size() {
		return mySize;
	}

	public List<Tree> trees() {
		return myTrees;
	}

	public List<Road> roads() {
		return myRoads;
	}

	public Vector3f getSunlight() {
		return mySunlight;
	}

	/**
	 * Set the sunlight direction.
	 *
	 * Note: the sun should be treated as a directional light, without a position
	 *
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setSunlightDir(float dx, float dy, float dz) {
		mySunlight.x = dx;
		mySunlight.y = dy;
		mySunlight.z = dz;
	}

	public void setSunlightDir(Vector3f delta) {
		mySunlight = delta.clone();
	}



	/**
	 * Resize the terrain, copying any old altitudes.
	 *
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height) {
		mySize = new Dimension(width, height);
		double[][] oldAlt = myAltitude;
		myAltitude = new double[width][height];

		for (int i = 0; i < width && i < oldAlt.length; i++) {
			for (int j = 0; j < height && j < oldAlt[i].length; j++) {
				myAltitude[i][j] = oldAlt[i][j];
			}
		}
	}

	/**
	 * Get the altitude at a grid point
	 *
	 * @param x
	 * @param z
	 * @return
	 */
	public double getGridAltitude(int x, int z) {
		return myAltitude[x][z];
	}

	/**
	 * Set the altitude at a grid point
	 *
	 * @param x
	 * @param z
	 * @return
	 */
	public void setGridAltitude(int x, int z, double h) {
		myAltitude[x][z] = h;
	}

	/**
	 * Get the altitude at an arbitrary point.
	 * Non-integer points should be interpolated from neighbouring grid points
	 *
	 * TO BE COMPLETED
	 *
	 * @param x
	 * @param z
	 * @return
	 */
	public double altitude(double x, double z) {
		double altitude = 0.0;

		boolean integerX = false;
		boolean integerZ = false;
		int x1 = 0;
		int x2 = 0;
		int z1 = 0;
		int z2 = 0;

		if ((x < 0 || x > mySize.width || z < 0 || z > mySize.height)) {
			return altitude;
		}

		x1 = (int)Math.floor(x);
		x2 = (int)Math.ceil(x);
		z1 = (int)Math.floor(z);
		z2 = (int)Math.ceil(z);

		// TODO: This is not correct, it just floors the value.
		altitude = myAltitude[x1][z1];

		return altitude;
	}

	/**
	 * Add a tree at the specified (x,z) point.
	 * The tree's y coordinate is calculated from the altitude of the terrain at that point.
	 *
	 * @param x
	 * @param z
	 */
	public void addTree(double x, double z) {
		double y = altitude(x, z);
		Tree tree = new Tree(this, x, y, z);
		myTrees.add(tree);
	}


	/**
	 * Add a road.
	 *
	 * @param width
	 * @param spine
	 */
	public void addRoad(double width, double[] spine) {
		Road road = new Road(width, spine);
		myRoads.add(road);
	}


	@Override
	public void draw(GL2 gl) {
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glColor3d(0.0, 0.0, 0.0);
		for (int x = 0; x < mySize.width - 1; ++x) {
			for (int z = 0; z < mySize.height - 1; ++z) {
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[]{material.ambient.x, material.ambient.y, material.ambient.z}, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z}, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{material.specular.x, material.specular.y, material.specular.z}, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, new float[]{material.phong.x, material.phong.y, material.phong.z}, 0);

				// Experiment the lighting with this.
				GLUT glut = new GLUT();
				//glut.glutSolidSphere(2.0, 10, 10);

				Vector3 bottomLeft = new Vector3(x, altitude(x, z), z);
				Vector3 bottomRight = new Vector3(x + 1, altitude(x + 1, z), z);
				Vector3 topLeft = new Vector3(x, altitude(x, z + 1), z + 1);
				Vector3 topRight = new Vector3(x + 1, altitude(x + 1, z + 1), z + 1);

				Vector3 bottomLeftCross = topLeft.subtract(bottomLeft).cross(bottomRight.subtract(bottomLeft));
				bottomLeftCross.divideSelf(bottomLeftCross.modulus());
				Vector3 topRightCross = bottomRight.subtract(topRight).cross(topLeft.subtract(topRight));
				topRightCross.divideSelf(topRightCross.modulus());

				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glNormal3d(bottomLeftCross.x, bottomLeftCross.y, bottomLeftCross.z);
				gl.glVertex3d(x, altitude(x, z), z);
				gl.glVertex3d(x + 1, altitude(x + 1, z), z);
				gl.glVertex3d(x, altitude(x, z + 1), z + 1);
				gl.glNormal3d(topRightCross.x, topRightCross.y, topRightCross.z);
				gl.glVertex3d(x + 1, altitude(x + 1, z), z);
				gl.glVertex3d(x, altitude(x, z + 1), z + 1);
				gl.glVertex3d(x + 1, altitude(x + 1, z + 1), z + 1);
				gl.glEnd();

			}
		}
	}
}
