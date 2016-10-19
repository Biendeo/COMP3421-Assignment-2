package ass2.game;

import ass2.math.Vector3;
import ass2.math.Vector4f;
import ass2.spec.Terrain;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A monster that wanders around the terrain with a given path.
 */
public class Monster extends GameObject implements Drawable, Updatable {

	private Terrain terrain;

	private static final double height = 0.05;

	private double pathLength;

	private ArrayList<Vector3> path;
	private double speed;
	private int currentIndex;

	private Texture diffuseTexture;
	private Texture normalTexture;
	private Texture specularTexture;

	private int vaoIndex;
	private int vertexCount;

	private Material material;
	private int shaderProgram;

	public Monster(GameObject parent, Terrain terrain, List<Vector3> path, double speed) {
		super(parent);
		this.terrain = terrain;
		setMonsterMovementPattern(path, speed);
		transform.position = new Vector3(path.get(0).x, 0.0, path.get(0).z);
		transform.position.y = terrain.altitude(transform.position.x, transform.position.z);
		material = new Material();
		material.diffuse = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	/**
	 * Sets the movement pattern of the monster.
	 * @param path
	 * The points the the monster moves to. The Y values of these are ignored, since it is
	 * determined from the terrain.
	 * @param speed
	 * The speed at which the monster moves to these.
	 */
	public void setMonsterMovementPattern(List<Vector3> path, double speed) {
		this.path = new ArrayList<>(path);
		this.speed = speed;
		calculatePathLength();
		currentIndex = this.path.size() - 1;
	}

	private int nextIndex(int currentIndex) {
		int nextIndex = currentIndex + 1;
		if (nextIndex == path.size()) {
			nextIndex = 0;
		}
		return nextIndex;
	}

	private double calculatePathLength() {
		pathLength = 0.0;
		for (int i = 0; i < path.size(); ++i) {
			Vector3 currentPoint = path.get(i);
			Vector3 nextPoint = path.get(nextIndex(i));
			currentPoint.y = 0.0;
			nextPoint.y = 0.0;
			Vector3 difference = nextPoint.subtract(currentPoint);
			pathLength += difference.modulus();
		}
		return pathLength;
	}

	/**
	 * Returns the distance vector the the next point that the monster has to go to. The Y value
	 * is 0 for ease in calculation later.
	 * @return
	 */
	private Vector3 vectorToNextPoint() {
		int nextIndex = nextIndex(currentIndex);

		Vector3 nextPoint = path.get(nextIndex);
		Vector3 currentPosition = transform.position.clone();
		nextPoint.y = 0.0;
		currentPosition.y = 0.0;

		return nextPoint.subtract(currentPosition);
	}

	private void rotateToNextPoint() {
		Vector3 movementVector = vectorToNextPoint();

		// TODO: Figure out if that's actually facing the right direction.
		//System.out.println(Double.toString(movementVector.x) + ", " + Double.toString(movementVector.y) + ", " + Double.toString(movementVector.z));
		transform.rotation.y = Math.toDegrees(Math.atan(movementVector.x / movementVector.z));
		//System.out.println(transform.rotation.y);
		if (Double.isNaN(transform.rotation.y)) {
			transform.rotation.y = 0;
		}
		if (movementVector.z < 0) {
			transform.rotation.y += 180.0;
		}
	}

	private void moveTowardsNextPoint(double amount) {
		// This gets rid of stack overflows with large amounts.
		while (amount >= pathLength) {
			amount -= pathLength;
		}

		Vector3 movementVector = vectorToNextPoint();

		if (movementVector.modulus() < amount) {
			currentIndex = nextIndex(currentIndex);
			transform.position = path.get(currentIndex);
			rotateToNextPoint();
			moveTowardsNextPoint(amount - movementVector.modulus());
		} else {
			Vector3 movementAmount = movementVector.divide(movementVector.modulus()).multiply(amount);
			transform.position.addSelf(movementAmount);
		}
	}

	@Override
	public void update(double dt) {
		rotateToNextPoint();
		moveTowardsNextPoint(dt * speed);
		transform.position.y = terrain.altitude(transform.position.x, transform.position.z) + height / 2;
	}

	@Override
	public void initialize(GL2 gl) {
		try {
			shaderProgram = Shader.initShaders(gl, getClass().getResourceAsStream("/shaders/cat_vert.glsl"), getClass().getResourceAsStream("/shaders/cat_frag.glsl"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		diffuseTexture = new Texture(gl, getClass().getResourceAsStream("/models/cat/cat_diff.jpg"), true);
		normalTexture = new Texture(gl, getClass().getResourceAsStream("/models/cat/cat_norm.jpg"), true);
		specularTexture = new Texture(gl, getClass().getResourceAsStream("/models/cat/cat_spec.jpg"), true);

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
		ArrayList<Float> vertices = new ArrayList<>();

		// Format:
		// [x1, y1, z1, x2, y2, z2, ... ]
		// The +0 face is added, then the +1 face. This is then added by z-row.
		ArrayList<Float> normals = new ArrayList<>();

		// Format:
		// [0, 1, 2, 3, 1, 2, ... ]
		// The +0 face is added, then the +1 face. This is then added by z-row.
		ArrayList<Float> texturePositions = new ArrayList<>();

		ArrayList<Float> vertexFinal = new ArrayList<>();
		ArrayList<Float> normalFinal = new ArrayList<>();
		ArrayList<Float> textureFinal = new ArrayList<>();

		BufferedReader modelFile = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/models/cat/cat.obj")));

		try {
			for (String line = modelFile.readLine(); line != null; line = modelFile.readLine()) {
				String[] lineSplit = line.split(" ");
				switch(lineSplit[0]) {
					case "v":
						vertices.add(Float.parseFloat(lineSplit[1]));
						vertices.add(Float.parseFloat(lineSplit[2]));
						vertices.add(Float.parseFloat(lineSplit[3]));
						break;
					case "vt":
						texturePositions.add(Float.parseFloat(lineSplit[1]));
						texturePositions.add(Float.parseFloat(lineSplit[2]));
						break;
					case "vn":
						normals.add(Float.parseFloat(lineSplit[1]));
						normals.add(Float.parseFloat(lineSplit[2]));
						normals.add(Float.parseFloat(lineSplit[3]));
						break;
					case "f":
						String[] vertex1 = lineSplit[1].split("/");
						String[] vertex2 = lineSplit[2].split("/");
						String[] vertex3 = lineSplit[3].split("/");

						vertexFinal.add(vertices.get((Integer.parseInt(vertex1[0]) - 1) * 3));
						vertexFinal.add(vertices.get((Integer.parseInt(vertex1[0]) - 1) * 3 + 1));
						vertexFinal.add(vertices.get((Integer.parseInt(vertex1[0]) - 1) * 3 + 2));

						textureFinal.add(texturePositions.get((Integer.parseInt(vertex1[1]) - 1) * 2));
						textureFinal.add(texturePositions.get((Integer.parseInt(vertex1[1]) - 1) * 2 + 1));

						normalFinal.add(normals.get((Integer.parseInt(vertex1[2]) - 1) * 3));
						normalFinal.add(normals.get((Integer.parseInt(vertex1[2]) - 1) * 3 + 1));
						normalFinal.add(normals.get((Integer.parseInt(vertex1[2]) - 1) * 3 + 2));

						vertexFinal.add(vertices.get((Integer.parseInt(vertex2[0]) - 1) * 3));
						vertexFinal.add(vertices.get((Integer.parseInt(vertex2[0]) - 1) * 3 + 1));
						vertexFinal.add(vertices.get((Integer.parseInt(vertex2[0]) - 1) * 3 + 2));

						textureFinal.add(texturePositions.get((Integer.parseInt(vertex2[1]) - 1) * 2));
						textureFinal.add(texturePositions.get((Integer.parseInt(vertex2[1]) - 1) * 2 + 1));

						normalFinal.add(normals.get((Integer.parseInt(vertex2[2]) - 1) * 3));
						normalFinal.add(normals.get((Integer.parseInt(vertex2[2]) - 1) * 3 + 1));
						normalFinal.add(normals.get((Integer.parseInt(vertex2[2]) - 1) * 3 + 2));

						vertexFinal.add(vertices.get((Integer.parseInt(vertex3[0]) - 1) * 3));
						vertexFinal.add(vertices.get((Integer.parseInt(vertex3[0]) - 1) * 3 + 1));
						vertexFinal.add(vertices.get((Integer.parseInt(vertex3[0]) - 1) * 3 + 2));

						textureFinal.add(texturePositions.get((Integer.parseInt(vertex3[1]) - 1) * 2));
						textureFinal.add(texturePositions.get((Integer.parseInt(vertex3[1]) - 1) * 2 + 1));

						normalFinal.add(normals.get((Integer.parseInt(vertex3[2]) - 1) * 3));
						normalFinal.add(normals.get((Integer.parseInt(vertex3[2]) - 1) * 3 + 1));
						normalFinal.add(normals.get((Integer.parseInt(vertex3[2]) - 1) * 3 + 2));

						break;
					case "mtllib":
						break;
				}
			}

			modelFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		float[] vertexArr = new float[vertexFinal.size()];

		for (int i = 0; i < vertexArr.length; ++i) {
			vertexArr[i] = vertexFinal.get(i);
		}

		vertexCount = vertexArr.length;

		float[] normalArr = new float[normalFinal.size()];

		for (int i = 0; i < normalArr.length; ++i) {
			normalArr[i] = normalFinal.get(i);
		}

		float[] textureArr = new float[textureFinal.size()];

		for (int i = 0; i < textureArr.length; ++i) {
			textureArr[i] = textureFinal.get(i);
		}

		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertexArr);
		FloatBuffer normalBuffer = Buffers.newDirectFloatBuffer(normalArr);
		FloatBuffer textureBuffer = Buffers.newDirectFloatBuffer(textureArr);

		gl.glBindVertexArray(vaoIndex);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexBufferIndex);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, vertexArr.length * 4, vertexBuffer, GL2.GL_STATIC_DRAW);
		gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, 0, 0);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, normalBufferIndex);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, normalArr.length * 4, normalBuffer, GL2.GL_STATIC_DRAW);
		gl.glNormalPointer(GL2.GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glVertexAttribPointer(1, 3, GL2.GL_FLOAT, false, 0, 0);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, textureBufferIndex);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, textureArr.length * 4, textureBuffer, GL2.GL_STATIC_DRAW);
		gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexAttribPointer(2, 3, GL2.GL_FLOAT, false, 0, 0);

	}

	@Override
	public void dispose(GL2 gl) {
		diffuseTexture.release(gl);
		normalTexture.release(gl);
		specularTexture.release(gl);

		int[] vaoArray = new int[1];
		vaoArray[0] = vaoIndex;
		gl.glDeleteVertexArrays(1, vaoArray, 0);
	}

	@Override
	public void draw(GL2 gl) {
		// The texture should only be enabled by this object.
		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glUseProgram(shaderProgram);

		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, diffuseTexture.getTextureId());

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

		gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertexCount);

		gl.glBindVertexArray(0);
		gl.glUseProgram(0);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}
}
