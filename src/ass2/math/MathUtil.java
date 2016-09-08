package ass2.math;

import ass2.math.Vector3;

/**
 * A collection of useful math methods.
 * This was copy pasted from assignment 1.
 * TODO: Replace all 2D matrix arrays with specialised classes.
 *
 * @author malcolmr
 */
public class MathUtil {

	/**
	 * Normalise an angle to the range [-180, 180)
	 *
	 * @param angle The angle.
	 * @return The normalised angle.
	 */
	static public double normaliseAngle(double angle) {
		return ((angle + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
	}

	/**
	 * Clamp a value to the given range
	 *
	 * @param value The value.
	 * @param min The minimum value.
	 * @param max The maximum value.
	 * @return
	 */

	public static double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * Multiply two matrices
	 *
	 * @param p A 3x3 matrix
	 * @param q A 3x3 matrix
	 * @return The multiplied matrix.
	 */
	public static double[][] multiply3(double[][] p, double[][] q) {

		double[][] m = new double[3][3];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				m[i][j] = 0;
				for (int k = 0; k < 3; k++) {
				   m[i][j] += p[i][k] * q[k][j];
				}
			}
		}

		return m;
	}

	/**
	 * Multiplies two 4x4 matrices.
	 * @param p A 4x4 matrix
	 * @param q A 4x4 matrix
	 * @return The multiplied matrix.
	 */
	public static double[][] multiply4(double[][] p, double[][] q) {
		double[][] m = new double[4][4];

		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				m[i][j] = 0;
				for (int k = 0; k < 4; ++k) {
					m[i][j] += p[i][k] * q[k][j];
				}
			}
		}

		return m;
	}

	/**
	 * Multiply a vector by a matrix
	 *
	 * @param m A 3x3 matrix
	 * @param v A 3x1 vector
	 * @return The multiplied matrix.
	 */
	public static double[] multiply3(double[][] m, double[] v) {

		double[] u = new double[3];

		for (int i = 0; i < 3; i++) {
			u[i] = 0;
			for (int j = 0; j < 3; j++) {
				u[i] += m[i][j] * v[j];
			}
		}

		return u;
	}

	/**
	 * A 2D translation matrix for the given offset vector
	 *
	 * @param v The x and y translation coordinates {x, y}.
	 * @return
	 * The translation matrix in the following form.
	 * [[1,0,x]
	 *  [0,1,y]
	 *  [0,0,1]]
	 */
	public static double[][] translationMatrix3(double[] v) {
		double[][] returnMatrix = new double[3][3];

		returnMatrix[0][0] = 1;
		returnMatrix[0][1] = 0;
		returnMatrix[0][2] = v[0];

		returnMatrix[1][0] = 0;
		returnMatrix[1][1] = 1;
		returnMatrix[1][2] = v[1];

		returnMatrix[2][0] = 0;
		returnMatrix[2][1] = 0;
		returnMatrix[2][2] = 1;

		return returnMatrix;
	}

	/**
	 * Creates a 4x4 matrix representing a 3D translation vector.
	 * @param v The x, y, and z translation coordinates.
	 * @return
	 * The translation matrix in the following form.
	 * [[1 , 0, 0, x]
	 *  [0 , 1, 0, y]
	 *  [0 , 0, 1, z]
	 *  [0 , 0, 0, 1]]
	 */
	public static double[][] translationMatrix4(Vector3 v) {
		double[][] returnMatrix = new double[4][4];

		returnMatrix[0][0] = 1;
		returnMatrix[0][1] = 0;
		returnMatrix[0][2] = 0;
		returnMatrix[0][3] = v.x;

		returnMatrix[1][0] = 0;
		returnMatrix[1][1] = 1;
		returnMatrix[1][2] = 0;
		returnMatrix[1][3] = v.y;

		returnMatrix[2][0] = 0;
		returnMatrix[2][1] = 0;
		returnMatrix[2][2] = 1;
		returnMatrix[2][3] = v.z;

		returnMatrix[3][0] = 0;
		returnMatrix[3][1] = 0;
		returnMatrix[3][2] = 0;
		returnMatrix[3][3] = 1;

		return returnMatrix;
	}

	/**
	 * Creates a Vector3 that extracts the properties from a transformation matrix.
	 * @param translationMatrix The translation matrix.
	 * @return The translation vector.
	 */
	public static Vector3 translationMatrixToVector(double[][] translationMatrix) {
		double x = translationMatrix[0][3];
		double y = translationMatrix[1][3];
		double z = translationMatrix[2][3];
		return new Vector3(x, y, z);
	}

	/**
	 * A 2D rotation matrix for the given angle
	 *
	 * @param angle in degrees
	 * @return
	 * The rotation matrix in the following form.
	 * [[cos(a),-sin(a),0]
	 *  [sin(a), cos(a),0]
	 *  [     0,      0,1]]
	 */
	public static double[][] rotationMatrix(double angle) {
		double[][] returnMatrix = new double[3][3];

		returnMatrix[0][0] = Math.cos(Math.toRadians(angle));
		returnMatrix[0][1] = -Math.sin(Math.toRadians(angle));
		returnMatrix[0][2] = 0;

		returnMatrix[1][0] = Math.sin(Math.toRadians(angle));
		returnMatrix[1][1] = Math.cos(Math.toRadians(angle));
		returnMatrix[1][2] = 0;

		returnMatrix[2][0] = 0;
		returnMatrix[2][1] = 0;
		returnMatrix[2][2] = 1;

		return returnMatrix;
	}

	/**
	 * Creates a 4x4 matrix representing a 3D rotation vector.
	 * It involves multiplying the X, Y, and Z rotation matrices together.
	 * @param rotation
	 * @return
	 * The rotation matrix in the following form:
	 * [[ cos(y)cos(z), cos(x)sin(z) + sin(x)sin(y)cos(z), sin(x)sin(z) - cos(x)sin(y)cos(z), 0]
	 *  [-cos(y)cos(z), cos(x)cos(z) - sin(x)sin(y)sin(z), sin(x)cos(z) + cos(x)sin(y)sin(z), 0]
	 *  [       sin(y),                     -sin(x)cos(y),                      cos(x)cos(y), 0]
	 *  [            0,                                 0,                                 0, 1]]
	 */
	public static double[][] rotationMatrixXYZ(Vector3 rotation) {
		return MathUtil.multiply4(MathUtil.multiply4(MathUtil.rotationMatrixX(rotation.x), MathUtil.rotationMatrixY(rotation.y)), MathUtil.rotationMatrixZ(rotation.z));
	}

	/**
	 * Creates a 4x4 matrix representing the X angle of a rotation vector.
	 * @param xAngle The x angle in degrees.
	 * @return
	 * The rotation matrix in the following form:
	 * [[1,      0,       0, 0]
	 *  [0, cos(x), -sin(x), 0]
	 *  [0, sin(x),  cos(x), 0]
	 *  [0,      0,       0, 1]]
	 */
	public static double[][] rotationMatrixX(double xAngle) {
		double[][] returnMatrix = new double[4][4];

		returnMatrix[0][0] = 1;
		returnMatrix[0][1] = 0;
		returnMatrix[0][2] = 0;
		returnMatrix[0][3] = 0;

		returnMatrix[1][0] = 0;
		returnMatrix[1][1] = Math.cos(Math.toRadians(xAngle));
		returnMatrix[1][2] = -Math.sin(Math.toRadians(xAngle));
		returnMatrix[1][3] = 0;

		returnMatrix[2][0] = 0;
		returnMatrix[2][1] = Math.sin(Math.toRadians(xAngle));
		returnMatrix[2][2] = Math.cos(Math.toRadians(xAngle));
		returnMatrix[2][3] = 0;

		returnMatrix[3][0] = 0;
		returnMatrix[3][1] = 0;
		returnMatrix[3][2] = 0;
		returnMatrix[3][3] = 1;

		return returnMatrix;
	}

	/**
	 * Creates a 4x4 matrix representing the Y angle of a rotation vector.
	 * @param yAngle The y angle in degrees.
	 * @return
	 * The rotation matrix in the following form:
	 * [[ cos(y), 0, sin(y), 0]
	 *  [      0, 1,      0, 0]
	 *  [-sin(y), 0, cos(y), 0]
	 *  [      0, 0,      0, 1]]
	 */
	public static double[][] rotationMatrixY(double yAngle) {
		double[][] returnMatrix = new double[4][4];

		returnMatrix[0][0] = Math.cos(Math.toRadians(yAngle));
		returnMatrix[0][1] = 0;
		returnMatrix[0][2] = Math.sin(Math.toRadians(yAngle));
		returnMatrix[0][3] = 0;

		returnMatrix[1][0] = 0;
		returnMatrix[1][1] = 1;
		returnMatrix[1][2] = 0;
		returnMatrix[1][3] = 0;

		returnMatrix[2][0] = -Math.sin(Math.toRadians(yAngle));
		returnMatrix[2][1] = 0;
		returnMatrix[2][2] = Math.cos(Math.toRadians(yAngle));
		returnMatrix[2][3] = 0;

		returnMatrix[3][0] = 0;
		returnMatrix[3][1] = 0;
		returnMatrix[3][2] = 0;
		returnMatrix[3][3] = 1;

		return returnMatrix;
	}

	/**
	 * Creates a 4x4 matrix representing the Z angle of a rotation vector.
	 * @param zAngle The z angle in degrees.
	 * @return
	 * The rotation matrix in the following form:
	 * [[cos(z), -sin(z), 0, 0]
	 *  [sin(z),  cos(z), 0, 0]
	 *  [     0,       0, 1, 0]
	 *  [     0,       0, 0, 1]]
	 */
	public static double[][] rotationMatrixZ(double zAngle) {
		double[][] returnMatrix = new double[4][4];

		returnMatrix[0][0] = Math.cos(Math.toRadians(zAngle));
		returnMatrix[0][1] = -Math.sin(Math.toRadians(zAngle));
		returnMatrix[0][2] = 0;
		returnMatrix[0][3] = 0;

		returnMatrix[1][0] = Math.sin(Math.toRadians(zAngle));
		returnMatrix[1][1] = Math.cos(Math.toRadians(zAngle));
		returnMatrix[1][2] = 0;
		returnMatrix[1][3] = 0;

		returnMatrix[2][0] = 0;
		returnMatrix[2][1] = 0;
		returnMatrix[2][2] = 1;
		returnMatrix[2][3] = 0;

		returnMatrix[3][0] = 0;
		returnMatrix[3][1] = 0;
		returnMatrix[3][2] = 0;
		returnMatrix[3][3] = 1;

		return returnMatrix;
	}

	/**
	 * Decomposes a rotation matrix and returns the rotation vector that corresponds to its properties.
	 *
	 * The magic of grabbing these values was figured out from these two StackOverflow answers.
	 * http://stackoverflow.com/questions/15022630/how-to-calculate-the-angle-from-roational-matrix
	 * http://gamedev.stackexchange.com/questions/50963/how-to-extract-euler-angles-from-transformation-matrix
	 *
	 * @param rotationMatrix The rotation matrix to convert.
	 * @return The rotation vector.
	 */
	public static Vector3 rotationMatrixToVector(double[][] rotationMatrix) {
		if (rotationMatrix[0][0] == 1.0 || rotationMatrix[0][0] == -1.0) {
			double x = 0;
			double y = 0;
			double z = Math.toDegrees(Math.atan2(rotationMatrix[0][2], rotationMatrix[2][3]));
			return new Vector3(x, y, z);
		} else {
			double x = Math.toDegrees(Math.atan2(rotationMatrix[2][1], rotationMatrix[2][2]));
			double y = Math.toDegrees(Math.atan2(-rotationMatrix[2][0], Math.sqrt(rotationMatrix[2][1] * rotationMatrix[2][1] + rotationMatrix[2][2] * rotationMatrix[2][2])));
			double z = Math.toDegrees(Math.atan2(rotationMatrix[1][0], rotationMatrix[0][0]));
			return new Vector3(x, y, z);
		}
	}

	/**
	 *
	 * @param scale The scale.
	 * @return
	 * The scale matrix in the following form.
	 * [[scale,    0,0]
	 *  [    0,scale,0]
	 *  [    0,    0,1]]
	 */
	public static double[][] scaleMatrix3(double scale) {
		double[][] returnMatrix = new double[3][3];

		returnMatrix[0][0] = scale;
		returnMatrix[0][1] = 0;
		returnMatrix[0][2] = 0;

		returnMatrix[1][0] = 0;
		returnMatrix[1][1] = scale;
		returnMatrix[1][2] = 0;

		returnMatrix[2][0] = 0;
		returnMatrix[2][1] = 0;
		returnMatrix[2][2] = 1;

		return returnMatrix;
	}

	/**
	 * Creates a 4x4 matrix representing a scale vector.
	 * @param scale The scale vector.
	 * @return
	 * The scale matrix in the following form:
	 * [[x, 0, 0, 0]
	 *  [0, y, 0, 0]
	 *  [0, 0, z, 0]
	 *  [0, 0, 0, 1]]
	 */
	public static double[][] scaleMatrix4(Vector3 scale) {
		double[][] returnMatrix = new double[4][4];

		returnMatrix[0][0] = scale.x;
		returnMatrix[0][1] = 0;
		returnMatrix[0][2] = 0;
		returnMatrix[0][3] = 0;

		returnMatrix[1][0] = 0;
		returnMatrix[1][1] = scale.y;
		returnMatrix[1][2] = 0;
		returnMatrix[1][3] = 0;

		returnMatrix[2][0] = 0;
		returnMatrix[2][1] = 0;
		returnMatrix[2][2] = scale.z;
		returnMatrix[2][3] = 0;

		returnMatrix[3][0] = 0;
		returnMatrix[3][1] = 0;
		returnMatrix[3][2] = 0;
		returnMatrix[3][3] = 1;

		return returnMatrix;
	}

	/**
	 * Creates a Vector3 that represents a given scale matrix.
	 * @param scaleMatrix The scale matrix to convert.
	 * @return The scale vector.
	 */
	public static Vector3 scaleMatrixToVector(double[][] scaleMatrix) {
		double x = scaleMatrix[0][0];
		double y = scaleMatrix[1][1];
		double z = scaleMatrix[2][2];
		return new Vector3(x, y, z);
	}

}
