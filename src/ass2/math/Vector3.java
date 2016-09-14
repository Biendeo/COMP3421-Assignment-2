package ass2.math;

/**
 * This class just stores three doubles in a single object. It can be used with new MathUtil
 * functions to convert to translation, rotation, or scale matrices. It can also be used to
 * indicate either a 3D vector, or a point in 3D space (although you should handle these two
 * types carefully).
 * 
 * The usage of it is similar to other Vector3 classes in engines such as the Unity Engine, but
 * all this code is original.
 * 
 * @author Thomas Moffet, z5061905
 *
 */
public class Vector3 {
	public double x;
	public double y;
	public double z;
	
	/**
	 * Constructs a Vector3 defaulting to (0.0, 0.0, 0.0).
	 */
	public Vector3() {
		this(0.0, 0.0, 0.0);
	}
	
	/**
	 * Constructs a Vector3 in the form of (x, y, 0.0).
	 * @param x
	 * @param y
	 */
	public Vector3(double x, double y) {
		this(x, y, 0.0);
	}
	
	/**
	 * Constructs a Vector3 in the form of (x, y, z).
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a new Vector3 with the same values.
	 */
	public Vector3 clone() {
		Vector3 returnVector = new Vector3();
		returnVector.x = x;
		returnVector.y = y;
		returnVector.z = z;
		
		return returnVector;
	}
	
	/**
	 * Checks for equality between two Vector3 objects.
	 */
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		} else {
			Vector3 v = (Vector3)o;
			return (v.x == x && v.y == y && v.z == z);
		}
	}
	
	/**
	 * Adds the properties of a Vector3 to this one (the + operator).
	 * @param v
	 * @return
	 */
	public Vector3 add(Vector3 v) {
		return add(this, v);
	}

	/**
	 * Adds the properties of a Vector3 to this one in place (the += operator).
	 * @param v
	 * @return This object.
	 */
	public Vector3 addSelf(Vector3 v) {
		Vector3 a = add(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Adds the properties of a Vector3 to this one (the + operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3 add(Vector3 left, Vector3 right) {
		Vector3 returnVector = new Vector3();
		returnVector.x = left.x + right.x;
		returnVector.y = left.y + right.y;
		returnVector.z = left.z + right.z;
		
		return returnVector;
	}
	
	/**
	 * Subtracts the properties of a Vector3 from this one (the - operator).
	 * @param v
	 * @return
	 */
	public Vector3 subtract(Vector3 v) {
		return subtract(this, v);
	}
	
	/**
	 * Subtracts the properties of a Vector3 from this one in place (the -= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector3 subtractSelf(Vector3 v) {
		Vector3 a = subtract(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Subtracts the properties of a Vector3 from this one (the - operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3 subtract(Vector3 left, Vector3 right) {
		Vector3 returnVector = new Vector3();
		returnVector.x = left.x - right.x;
		returnVector.y = left.y - right.y;
		returnVector.z = left.z - right.z;
		
		return returnVector;
	}
	
	/**
	 * Multiplies all values by the given double (the * operator).
	 * @param s
	 * @return
	 */
	public Vector3 multiply(double s) {
		return multiply(this, s);
	}
	
	/**
	 * Multiplies all values by the given double in place (the *= operator).
	 * @param s
	 * @return This object.
	 */
	public Vector3 multiplySelf(double s) {
		Vector3 a = multiply(this, s);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Multiplies all values by the given double (the * operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3 multiply(Vector3 left, double right) {
		Vector3 returnVector = new Vector3();
		returnVector.x = left.x * right;
		returnVector.y = left.y * right;
		returnVector.z = left.z * right;
		
		return returnVector;
	}
	
	/**
	 * Multiplies each property by its corresponding field in the other vector (the * operator).
	 * @param v
	 * @return
	 */
	public Vector3 multiply(Vector3 v) {
		return multiply(this, v);
	}

	/**
	 * Multipies each property by its corresponding field in the other vector in place (the *= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector3 multiplySelf(Vector3 v) {
		Vector3 a = multiply(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Multiplies each property by its corresponding field in the other vector (the * operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3 multiply(Vector3 left, Vector3 right) {
		Vector3 returnVector = new Vector3();
		returnVector.x = left.x * right.x;
		returnVector.y = left.y * right.y;
		returnVector.z = left.z * right.z;
		
		return returnVector;
	}
	
	/**
	 * Divides all values by the given double (the / operator).
	 * @param s
	 * @return
	 */
	public Vector3 divide(double s) {
		return divide(this, s);
	}
	
	/**
	 * Divides all values by the given double in place (the /= operator).
	 * @param s
	 * @return This object.
	 */
	public Vector3 divideSelf(double s) {
		Vector3 a = divide(this, s);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Divides all values by the given double (the / operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3 divide(Vector3 left, double right) {
		Vector3 returnVector = new Vector3();
		returnVector.x = left.x / right;
		returnVector.y = left.y / right;
		returnVector.z = left.z / right;
		
		return returnVector;
	}
	
	/**
	 * Divides each property by its corresponding field in the other vector (the / operator).
	 * @param v
	 * @return
	 */
	public Vector3 divide(Vector3 v) {
		return divide(this, v);
	}

	/**
	 * Divides each property by its corresponding field in the other vector in place (the /= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector3 divideSelf(Vector3 v) {
		Vector3 a = divide(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Divides each property by its corresponding field in the other vector (the / operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3 divide(Vector3 left, Vector3 right) {
		Vector3 returnVector = new Vector3();
		returnVector.x = left.x / right.x;
		returnVector.y = left.y / right.y;
		returnVector.z = left.z / right.z;
		
		return returnVector;
	}
	
	/**
	 * Returns the Vector3 where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @return
	 */
	public Vector3 invert() {
		return invert(this);
	}
	
	/**
	 * In places the Vector3 where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @return This object.
	 */
	public Vector3 invertSelf() {
		Vector3 a  = invert(this);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Returns the Vector3 where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @param term
	 * @return
	 */
	public static Vector3 invert(Vector3 term) {
		return divide(new Vector3(1.0, 1.0, 1.0), term);
	}

	public Vector3 cross(Vector3 v) {
		return cross(this, v);
	}

	public Vector3 crossSelf(Vector3 v) {
		Vector3 a = cross(this, v);
		x = a.x;
		y = a.y;
		z = a.z;

		return this;
	}

	public static Vector3 cross(Vector3 left, Vector3 right) {
		Vector3 returnVector = new Vector3();
		returnVector.x = left.y * right.z - right.y * left.z;
		returnVector.y = left.z * right.x - right.z * left.x;
		returnVector.z = left.x * right.y - right.x * left.y;

		return returnVector;
	}
	
	/**
	 * Computes the overall distance achieved by this vector.
	 * @return
	 */
	public double modulus() {
		return Math.sqrt(x * x + y * y + z * z);
	}
}
