package ass2.spec;

import java.awt.Dimension;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import ass2.game.*;

import ass2.math.Vector3;
import ass2.math.Vector3f;
import ass2.math.Vector4f;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;


/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain extends GameObject implements Drawable {

	private Dimension mySize;
	private double[][] myAltitude;
	private List<LSystemTrees> myTrees;
	private int numTreeGens;
	private List<Road> myRoads;
	private List<Portal> myPortals;
	private List<Monster> myMonsters;
	private Vector3f mySunlight;
	private Material material;
	private Texture texture;

	private int vaoIndex;

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
		myTrees = new ArrayList<LSystemTrees>();
		numTreeGens = 4;
		myRoads = new ArrayList<Road>();
		myPortals = new ArrayList<Portal>();
		myMonsters = new ArrayList<Monster>();
		mySunlight = new Vector3f();

		material = new Material();
		material.ambient = new Vector4f(0.3f, 0.3f, 0.3f, 1.0f);
		// The diffuse is modulated by the texture now, so this is all white.
		material.diffuse = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		material.specular = new Vector4f(0.1f, 0.1f, 0.1f, 0.1f);

		texture = null;
		vaoIndex = 0;
	}

	public Terrain(Dimension size) {
		this(size.width, size.height);
	}

	public Dimension size() {
		return mySize;
	}

	public List<LSystemTrees> trees() {
		return myTrees;
	}

	public List<Road> roads() {
		return myRoads;
	}

	public List<Portal> portals() {
		return myPortals;
	}

	public List<Monster> monsters() {
		return myMonsters;
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
	
	public void setTreeGens(int numTreeGens){
		this.numTreeGens = numTreeGens;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
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
	 * @param x
	 * @param z
	 * @return
	 */
	public double altitude(double x, double z) {
		// If the object is outside the boundaries, we just say it's 0.
		if ((x < 0 || x > mySize.width - 1 || z < 0 || z > mySize.height - 1)) {
			return 0.0;
		}

		int floorX = (int)Math.floor(x);
		int floorZ = (int)Math.floor(z);

		// If we've landed on a vertex, we just use the value because it's quicker.
		if (x == floorX && z == floorZ) {
			return myAltitude[floorX][floorZ];
		}

		// This determines whether the top-right triangle is used.
		// TODO: Double check that this works, I'm noticing some jumpy motion sometimes.
		boolean alternateTriangle = false;
		if ((x - floorX + z - floorZ) > 1.0) {
			alternateTriangle = true;
		}

		// This method is called barycentric coordinates.
		// Source: http://www.alecjacobson.com/weblog/?p=1596

		// We start by getting the vertex positions.
		Vector3 vertex1;
		if (alternateTriangle) {
			vertex1 = new Vector3(floorX + 1, myAltitude[floorX + 1][floorZ + 1], floorZ + 1);
		} else {
			vertex1 = new Vector3(floorX, myAltitude[floorX][floorZ], floorZ);
		}
		Vector3 vertex2 = new Vector3(floorX + 1, myAltitude[floorX + 1][floorZ], floorZ);
		Vector3 vertex3 = new Vector3(floorX, myAltitude[floorX][floorZ + 1], floorZ + 1);

		// Then we chuck in the formula, and out pops what we want.
		double determinant = (vertex2.z - vertex3.z) * (vertex1.x - vertex3.x) + (vertex3.x - vertex2.x) * (vertex1.z - vertex3.z);

		double l1 = ((vertex2.z - vertex3.z) * (x - vertex3.x) + (vertex3.x - vertex2.x) * (z - vertex3.z)) / determinant;
		double l2 = ((vertex3.z - vertex1.z) * (x - vertex3.x) + (vertex1.x - vertex3.x) * (z - vertex3.z)) / determinant;
		double l3 = 1.0 - l1 - l2;

		return l1 * vertex1.y + l2 * vertex2.y + l3 * vertex3.y;
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
		LSystemTrees tree = new LSystemTrees(this, x, y, z, "fA", "^fB>>>B>>>>>B", "[^^f>>>>>>A]", numTreeGens);
		myTrees.add(tree);
	}


	/**
	 * Add a road.
	 *
	 * @param width
	 * @param spine
	 */
	public void addRoad(double width, double[] spine) {
		double y = altitude(spine[0], spine[1]);
		Road road = new Road(this, width, spine, y);
		myRoads.add(road);
	}

	public void addPortalPair(double portal1X, double portal1Z, double portal1Angle, double portal2X, double portal2Z, double portal2Angle, double width, double height) {
		Portal portal1 = new Portal(this, this, width, height);
		portal1.transform.position = new Vector3(portal1X, altitude(portal1X, portal1Z) + height / 2, portal1Z);
		portal1.transform.rotation = new Vector3(0.0, portal1Angle, 0.0);
		Portal portal2 = new Portal(this, this, width, height);
		portal2.transform.position = new Vector3(portal2X, altitude(portal2X, portal2Z) + height / 2, portal2Z);
		portal2.transform.rotation = new Vector3(0.0, portal2Angle, 0.0);
		Portal.connectPortals(portal1, portal2);
		myPortals.add(portal1);
		myPortals.add(portal2);
	}

	public void addMonster(List<Vector3> path, double speed) {
		Monster monster = new Monster(this, this, path, speed);
		myMonsters.add(monster);
	}

	public void setPortalCamera(Camera camera) {
		for (Portal portal : myPortals) {
			portal.setActiveCamera(camera);
		}
	}

	/**
	 * Determines whether an object should be moved through a portal, and does it.
	 * @param object The object in question.
	 * @param previousPosition The global position of the object before.
	 */
	public void moveObjectThroughPortal(GameObject object, Vector3 previousPosition) {
		Vector3 futurePosition = object.getGlobalPositionVector();

		for (Portal p : myPortals) {
			Vector3 leftPoint = p.transform.position.clone();
			Vector3 rightPoint = p.transform.position.clone();
			leftPoint.addSelf(new Vector3(Math.cos(Math.toRadians(p.transform.rotation.y)) * -p.getWidth(), 0.0, Math.sin(Math.toRadians(p.transform.rotation.y)) * -p.getWidth()));
			rightPoint.addSelf(new Vector3(Math.cos(Math.toRadians(p.transform.rotation.y)) * p.getWidth(), 0.0, Math.sin(Math.toRadians(p.transform.rotation.y)) * p.getWidth()));

			// This line was done through expanded calculation.
			Vector3 p1 = previousPosition;
			Vector3 p2 = futurePosition;
			Vector3 p3 = leftPoint;
			Vector3 p4 = rightPoint;

			Vector3 intersection = new Vector3((((p1.x * p2.z - p1.z * p2.x) * (p3.x - p4.x) - (p1.x - p2.x) * (p3.x * p4.z - p3.z * p4.x)) / ((p1.x - p2.x) * (p3.z - p4.z) - (p1.z - p2.z) * (p3.x - p4.x))), 0.0, (((p1.x * p2.z - p1.z * p2.x) * (p3.z - p4.z) - (p1.y - p2.y) * (p3.x * p4.z - p3.z * p4.x)) / ((p1.x - p2.x) * (p3.z - p4.z) - (p1.z - p2.z) * (p3.x - p4.x))));

/*
			System.out.println("---");
			System.out.println(Double.toString(intersection.x) + ", " + Double.toString(intersection.y) + ", " + Double.toString(intersection.z));
			System.out.println(Double.toString(p1.x) + ", " + Double.toString(p1.y) + ", " + Double.toString(p1.z));
			System.out.println(Double.toString(p2.x) + ", " + Double.toString(p2.y) + ", " + Double.toString(p2.z));
			System.out.println(Double.toString(p3.x) + ", " + Double.toString(p3.y) + ", " + Double.toString(p3.z));
			System.out.println(Double.toString(p4.x) + ", " + Double.toString(p4.y) + ", " + Double.toString(p4.z));
*/
			// Skip if the intersection is outside the specified regions.

			if (Double.isNaN(intersection.x) || Double.isNaN(intersection.z)) {
				continue;
			} else if (Math.abs(p4.x - p3.x) < Math.abs(intersection.x - p3.x) || Math.abs(p4.x - p3.x) < Math.abs(intersection.x - p4.x)) {
				continue;
			} else if (Math.abs(p4.z - p3.z) < Math.abs(intersection.z - p3.z) || Math.abs(p4.z - p3.z) < Math.abs(intersection.z - p4.z)) {
				continue;
			} else if (Math.abs(p2.x - p1.x) < Math.abs(intersection.x - p1.x) || Math.abs(p2.x - p1.x) < Math.abs(intersection.x - p2.x)) {
				continue;
			} else if (Math.abs(p2.z - p1.z) < Math.abs(intersection.z - p1.z) || Math.abs(p2.z - p1.z) < Math.abs(intersection.z - p2.z)) {
				continue;
			}
			/*
			else if (leftPoint.x <= rightPoint.x && (leftPoint.x > intersection.x || intersection.x > rightPoint.x)) {
				System.out.println("2");
				continue;
			} else if (rightPoint.x > intersection.x || intersection.x > leftPoint.x) {
				System.out.println("3");
				continue;
			} else if (leftPoint.z <= rightPoint.z && (leftPoint.z > intersection.z || intersection.z > rightPoint.z)) {
				System.out.println("4");
				continue;
			} else if (rightPoint.z > intersection.z || intersection.z > leftPoint.z) {
				System.out.println("5");
				continue;
			} else if (previousPosition.x <= futurePosition.x && (previousPosition.x > intersection.x || intersection.x > futurePosition.x)) {
				System.out.println("6");
				continue;
			} else if (futurePosition.x > intersection.x || intersection.x > previousPosition.x) {
				System.out.println("7");
				continue;
			} else if (previousPosition.z <= futurePosition.z && (previousPosition.z > intersection.z || intersection.z > futurePosition.z)) {
				System.out.println("8");
				continue;
			} else if (futurePosition.z > intersection.z || intersection.z > previousPosition.z) {
				System.out.println("9");
				continue;
			}
			*/

			Vector3 remainingVector = futurePosition.subtract(intersection);

			Vector3 intersectionToPortalCentre = p.transform.position.subtract(intersection);
			Vector3 portalDifference = p.getConnection().transform.position.subtract(p.transform.position);
			double portalRotationDifference = p.getConnection().transform.rotation.y - p.transform.rotation.y;
			Vector3 rotatedIntersectionToPortalCentre = intersection.add(portalDifference).add(new Vector3(intersectionToPortalCentre.x * Math.cos(Math.toRadians(portalRotationDifference)) - intersectionToPortalCentre.z * Math.sin(Math.toRadians(portalRotationDifference)), 0.0, intersectionToPortalCentre.x * Math.sin(Math.toRadians(portalRotationDifference)) + intersectionToPortalCentre.z * Math.sin(Math.toRadians(portalRotationDifference))));

			Vector3 rotatedRemainingVector = new Vector3(remainingVector.x * Math.cos(Math.toRadians(portalRotationDifference)) - remainingVector.z * Math.sin(Math.toRadians(portalRotationDifference)), 0.0, remainingVector.x * Math.sin(Math.toRadians(portalRotationDifference)) + remainingVector.z * Math.cos(Math.toRadians(portalRotationDifference)));

			object.transform.position = p.getConnection().transform.position.add(rotatedIntersectionToPortalCentre).add(rotatedRemainingVector);
			object.transform.rotation.y += portalRotationDifference;

			rotatedRemainingVector.multiplySelf(0.001);

			moveObjectThroughPortal(object, p.getConnection().transform.position.add(rotatedIntersectionToPortalCentre.add(rotatedRemainingVector)));

			break;

		}
	}

	@Override
	public void draw(GL2 gl) {
		// The texture should only be enabled by this object.
		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureId());

		//gl.glColor3d(0.0, 0.0, 0.0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[]{material.ambient.x, material.ambient.y, material.ambient.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{material.specular.x, material.specular.y, material.specular.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, new float[]{material.phong.x, material.phong.y, material.phong.z}, 0);

		gl.glBindVertexArray(vaoIndex);
		gl.glEnableVertexAttribArray(0);
		gl.glEnableVertexAttribArray(1);
		// I'm not too sure why disabling this works but enabling doesn't.
		//gl.glEnableVertexAttribArray(2);

		gl.glDrawArrays(GL2.GL_TRIANGLES, 0, mySize.width * mySize.height * 18);

		gl.glBindVertexArray(0);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}

	@Override
	public void initialize(GL2 gl) {
		texture = new Texture(gl, getClass().getResourceAsStream("/textures/grass01.jpg"), true);

		int[] vertexArray = new int[1];
		gl.glGenVertexArrays(1, vertexArray, 0);
		vaoIndex = vertexArray[0];

		int[] buffers = new int[3];
		gl.glGenBuffers(3, buffers, 0);

		int vertexBufferIndex = buffers[0];
		int normalBufferIndex = buffers[1];
		int textureBufferIndex = buffers[2];

		// Format:
		// [x1, y1, z1, x2, y2, z2, ... ]
		// Vertices are added by z-row.
		float[] vertices = new float[(mySize.width) * (mySize.height) * 6 * 3];

		// Format:
		// [x1, y1, z1, x2, y2, z2, ... ]
		// The +0 face is added, then the +1 face. This is then added by z-row.
		float[] normals = new float[(mySize.width) * (mySize.height) * 6 * 3];

		// Format:
		// [0, 1, 2, 3, 1, 2, ... ]
		// The +0 face is added, then the +1 face. This is then added by z-row.
		float[] texturePositions = new float[(mySize.width) * (mySize.height) * 6 * 2];

		for (int x = 0; x < mySize.width - 1; ++x) {
			for (int z = 0; z < mySize.height - 1; ++z) {

				Vector3 bottomLeft = new Vector3(x, altitude(x, z), z);
				Vector3 bottomRight = new Vector3(x + 1, altitude(x + 1, z), z);
				Vector3 topLeft = new Vector3(x, altitude(x, z + 1), z + 1);
				Vector3 topRight = new Vector3(x + 1, altitude(x + 1, z + 1), z + 1);

				Vector3 bottomLeftCross = topLeft.subtract(bottomLeft).cross(bottomRight.subtract(bottomLeft));
				bottomLeftCross.divideSelf(bottomLeftCross.modulus());
				Vector3 topRightCross = bottomRight.subtract(topRight).cross(topLeft.subtract(topRight));
				topRightCross.divideSelf(topRightCross.modulus());

				vertices[((x * mySize.height) + z) * 18] = (float)bottomLeft.x;
				vertices[((x * mySize.height) + z) * 18 + 1] = (float)bottomLeft.y;
				vertices[((x * mySize.height) + z) * 18 + 2] = (float)bottomLeft.z;

				vertices[((x * mySize.height) + z) * 18 + 3] = (float)topLeft.x;
				vertices[((x * mySize.height) + z) * 18 + 4] = (float)topLeft.y;
				vertices[((x * mySize.height) + z) * 18 + 5] = (float)topLeft.z;

				vertices[((x * mySize.height) + z) * 18 + 6] = (float)bottomRight.x;
				vertices[((x * mySize.height) + z) * 18 + 7] = (float)bottomRight.y;
				vertices[((x * mySize.height) + z) * 18 + 8] = (float)bottomRight.z;

				vertices[((x * mySize.height) + z) * 18 + 9] = (float)topLeft.x;
				vertices[((x * mySize.height) + z) * 18 + 10] = (float)topLeft.y;
				vertices[((x * mySize.height) + z) * 18 + 11] = (float)topLeft.z;

				vertices[((x * mySize.height) + z) * 18 + 12] = (float)topRight.x;
				vertices[((x * mySize.height) + z) * 18 + 13] = (float)topRight.y;
				vertices[((x * mySize.height) + z) * 18 + 14] = (float)topRight.z;

				vertices[((x * mySize.height) + z) * 18 + 15] = (float)bottomRight.x;
				vertices[((x * mySize.height) + z) * 18 + 16] = (float)bottomRight.y;
				vertices[((x * mySize.height) + z) * 18 + 17] = (float)bottomRight.z;

				normals[((x * mySize.height) + z) * 18] = (float) bottomLeftCross.x;
				normals[((x * mySize.height) + z) * 18 + 1] = (float) bottomLeftCross.y;
				normals[((x * mySize.height) + z) * 18 + 2] = (float) bottomLeftCross.z;
				normals[((x * mySize.height) + z) * 18 + 3] = (float) bottomLeftCross.x;
				normals[((x * mySize.height) + z) * 18 + 4] = (float) bottomLeftCross.y;
				normals[((x * mySize.height) + z) * 18 + 5] = (float) bottomLeftCross.z;
				normals[((x * mySize.height) + z) * 18 + 6] = (float) bottomLeftCross.x;
				normals[((x * mySize.height) + z) * 18 + 7] = (float) bottomLeftCross.y;
				normals[((x * mySize.height) + z) * 18 + 8] = (float) bottomLeftCross.z;

				normals[((x * mySize.height) + z) * 18 + 9] = (float) topRightCross.x;
				normals[((x * mySize.height) + z) * 18 + 10] = (float) topRightCross.y;
				normals[((x * mySize.height) + z) * 18 + 11] = (float) topRightCross.z;
				normals[((x * mySize.height) + z) * 18 + 12] = (float) topRightCross.x;
				normals[((x * mySize.height) + z) * 18 + 13] = (float) topRightCross.y;
				normals[((x * mySize.height) + z) * 18 + 14] = (float) topRightCross.z;
				normals[((x * mySize.height) + z) * 18 + 15] = (float) topRightCross.x;
				normals[((x * mySize.height) + z) * 18 + 16] = (float) topRightCross.y;
				normals[((x * mySize.height) + z) * 18 + 17] = (float) topRightCross.z;

				texturePositions[((x * mySize.height) + z) * 12] = 0.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 1] = 0.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 2] = 0.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 3] = 1.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 4] = 1.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 5] = 0.0f;

				texturePositions[((x * mySize.height) + z) * 12 + 6] = 0.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 7] = 1.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 8] = 1.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 9] = 1.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 10] = 1.0f;
				texturePositions[((x * mySize.height) + z) * 12 + 11] = 0.0f;

			}
		}

		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
		FloatBuffer normalBuffer = Buffers.newDirectFloatBuffer(normals);
		FloatBuffer textureBuffer = Buffers.newDirectFloatBuffer(texturePositions);

		gl.glBindVertexArray(vaoIndex);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexBufferIndex);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GL2.GL_STATIC_DRAW);
		gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, 0, 0);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, normalBufferIndex);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, normals.length * 4, normalBuffer, GL2.GL_STATIC_DRAW);
		gl.glNormalPointer(GL2.GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glVertexAttribPointer(1, 3, GL2.GL_FLOAT, false, 0, 0);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, textureBufferIndex);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, texturePositions.length * 4, textureBuffer, GL2.GL_STATIC_DRAW);
		gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexAttribPointer(2, 3, GL2.GL_FLOAT, false, 0, 0);
	}

	@Override
	public void dispose(GL2 gl) {
		texture.release(gl);

		int[] vaoArray = new int[1];
		vaoArray[0] = vaoIndex;
		gl.glDeleteVertexArrays(1, vaoArray, 0);
	}
}
