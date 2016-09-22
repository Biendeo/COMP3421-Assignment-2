package ass2.spec;

import java.awt.Dimension;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import ass2.game.Drawable;
import ass2.game.GameObject;
import ass2.game.Material;
import ass2.game.Texture;
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
	private List<Tree> myTrees;
	private List<Road> myRoads;
	private Vector3f mySunlight;
	private Material material;
	private Texture texture;

	private int vaoIndex;
	private int vertexIndexBuffer;

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

		gl.glDrawElements(GL2.GL_TRIANGLES, (mySize.width - 1) * (mySize.height - 1) * 6, GL2.GL_UNSIGNED_INT, 0);

		gl.glBindVertexArray(0);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}

	@Override
	public void initialize(GL2 gl) {
		texture = new Texture(gl, "src/ass2/textures/grass01.jpg", true);

		int[] vertexArray = new int[1];
		gl.glGenVertexArrays(1, vertexArray, 0);
		vaoIndex = vertexArray[0];

		int[] buffers = new int[4];
		gl.glGenBuffers(4, buffers, 0);

		int vertexBufferIndex = buffers[0];
		int vertexOrderBufferIndex = buffers[1];
		int normalBufferIndex = buffers[2];
		int textureBufferIndex = buffers[3];

		// Format:
		// [x1, y1, z1, x2, y2, z2, ... ]
		// Vertices are added by z-row.
		float[] vertices = new float[(mySize.width) * (mySize.height) * 3];

		int[] vertexOrders = new int[(mySize.width - 1) * (mySize.height - 1) * 6];

		// Format:
		// [x1, y1, z1, x2, y2, z2, ... ]
		// The +0 face is added, then the +1 face. This is then added by z-row.
		float[] normals = new float[(mySize.width - 1) * (mySize.height - 1) * 6 * 3];

		// Format:
		// [0, 1, 2, 3, 1, 2, ... ]
		// The +0 face is added, then the +1 face. This is then added by z-row.
		float[] texturePositions = new float[(mySize.width - 1) * (mySize.height - 1) * 6 * 2];

		for (int x = 0; x < mySize.width; ++x) {
			for (int z = 0; z < mySize.height; ++z) {
				Vector3 corner = new Vector3(x, altitude(x, z), z);

				vertices[(x * (mySize.height) + z)] = (float)corner.x;
				vertices[(x * (mySize.height) + z) + 1] = (float)corner.y;
				vertices[(x * (mySize.height) + z) + 2] = (float)corner.z;
			}
		}

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

				vertexOrders[((x * (mySize.height - 1)) + z) * 6] = (x * (mySize.height)) + z;
				vertexOrders[((x * (mySize.height - 1)) + z) * 6 + 1] = (x * (mySize.height)) + z + 1;
				vertexOrders[((x * (mySize.height - 1)) + z) * 6 + 2] = ((x + 1) * (mySize.height)) + z;
				vertexOrders[((x * (mySize.height - 1)) + z) * 6 + 3] = ((x + 1) * (mySize.height)) + z + 1;
				vertexOrders[((x * (mySize.height - 1)) + z) * 6 + 4] = (x * (mySize.height)) + z + 1;
				vertexOrders[((x * (mySize.height - 1)) + z) * 6 + 5] = ((x + 1) * (mySize.height)) + z;

				normals[((x * (mySize.height - 1)) + z) * 18] = (float) bottomLeftCross.x;
				normals[((x * (mySize.height - 1)) + z) * 18 + 1] = (float) bottomLeftCross.y;
				normals[((x * (mySize.height - 1)) + z) * 18 + 2] = (float) bottomLeftCross.z;
				normals[((x * (mySize.height - 1)) + z) * 18 + 3] = (float) bottomLeftCross.x;
				normals[((x * (mySize.height - 1)) + z) * 18 + 4] = (float) bottomLeftCross.y;
				normals[((x * (mySize.height - 1)) + z) * 18 + 5] = (float) bottomLeftCross.z;
				normals[((x * (mySize.height - 1)) + z) * 18 + 6] = (float) bottomLeftCross.x;
				normals[((x * (mySize.height - 1)) + z) * 18 + 7] = (float) bottomLeftCross.y;
				normals[((x * (mySize.height - 1)) + z) * 18 + 8] = (float) bottomLeftCross.z;

				normals[((x * (mySize.height - 1)) + z) * 18 + 9] = (float) topRightCross.x;
				normals[((x * (mySize.height - 1)) + z) * 18 + 10] = (float) topRightCross.y;
				normals[((x * (mySize.height - 1)) + z) * 18 + 11] = (float) topRightCross.z;
				normals[((x * (mySize.height - 1)) + z) * 18 + 12] = (float) topRightCross.x;
				normals[((x * (mySize.height - 1)) + z) * 18 + 13] = (float) topRightCross.y;
				normals[((x * (mySize.height - 1)) + z) * 18 + 14] = (float) topRightCross.z;
				normals[((x * (mySize.height - 1)) + z) * 18 + 15] = (float) topRightCross.x;
				normals[((x * (mySize.height - 1)) + z) * 18 + 16] = (float) topRightCross.y;
				normals[((x * (mySize.height - 1)) + z) * 18 + 17] = (float) topRightCross.z;

				texturePositions[((x * (mySize.height - 1)) + z) * 12] = 0.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 1] = 0.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 2] = 0.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 3] = 1.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 4] = 1.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 5] = 0.0f;

				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 6] = 1.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 7] = 1.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 8] = 0.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 9] = 1.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 10] = 1.0f;
				texturePositions[((x * (mySize.height - 1)) + z) * 12 + 11] = 0.0f;

			}
		}

		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
		IntBuffer vertexOrderBuffer = Buffers.newDirectIntBuffer(vertexOrders);
		FloatBuffer normalBuffer = Buffers.newDirectFloatBuffer(normals);
		FloatBuffer textureBuffer = Buffers.newDirectFloatBuffer(texturePositions);

		gl.glBindVertexArray(vaoIndex);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexBufferIndex);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GL2.GL_STATIC_DRAW);
		gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, 0, 0);

		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, vertexOrderBufferIndex);
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, vertexOrders.length * 4, vertexOrderBuffer, GL2.GL_STATIC_DRAW);
		gl.glIndexPointer(GL2.GL_INT, 0, null);
		gl.glEnableClientState(GL2.GL_INDEX_ARRAY);
		gl.glVertexAttribPointer(1, 3, GL2.GL_UNSIGNED_INT, false, 0, 0);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, normalBufferIndex);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, normals.length * 4, normalBuffer, GL2.GL_STATIC_DRAW);
		gl.glNormalPointer(GL2.GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glVertexAttribPointer(2, 3, GL2.GL_FLOAT, false, 0, 0);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, textureBufferIndex);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, texturePositions.length * 4, textureBuffer, GL2.GL_STATIC_DRAW);
		gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexAttribPointer(3, 3, GL2.GL_FLOAT, false, 0, 0);

		vertexIndexBuffer = vertexOrderBufferIndex;
	}

	@Override
	public void dispose(GL2 gl) {
		texture.release(gl);

		int[] vaoArray = new int[1];
		vaoArray[0] = vaoIndex;
		gl.glDeleteVertexArrays(1, vaoArray, 0);
	}
}
