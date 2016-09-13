package ass2.math;

/**
 * This class just stores three floats in a single object. It can be used with new MathUtil
 * functions to convert to translation, rotation, or scale matrices. It can also be used to
 * indicate either a 3D vector, or a point in 3D space (although you should handle these two
 * types carefully).
 * 
 * The usage of it is similar to other Vector3f classes in engines such as the Unity Engine, but
 * all this code is original.
 * 
 * @author Thomas Moffet, z5061905
 *
 */
public class Vector3f {
	public float x;
	public float y;
	public float z;

	/**
	 * Constructs a Vector3f defaulting to (0.0, 0.0, 0.0).
	 */
	public Vector3f() {
		this(0.0f, 0.0f, 0.0f);
	}

	/**
	 * Constructs a Vector3f in the form of (x, y, 0.0).
	 * @param x
	 * @param y
	 */
	public Vector3f(float x, float y) {
		this(x, y, 0.0f);
	}

	/**
	 * Constructs a Vector3f in the form of (x, y, z).
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a new Vector3f with the same values.
	 */
	public Vector3f clone() {
		Vector3f returnVector = new Vector3f();
		returnVector.x = x;
		returnVector.y = y;
		returnVector.z = z;
		
		return returnVector;
	}
	
	/**
	 * Checks for equality between two Vector3f objects.
	 */
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		} else {
			Vector3f v = (Vector3f)o;
			return (v.x == x && v.y == y && v.z == z);
		}
	}
	
	/**
	 * Adds the properties of a Vector3f to this one (the + operator).
	 * @param v
	 * @return
	 */
	public Vector3f add(Vector3f v) {
		return add(this, v);
	}

	/**
	 * Adds the properties of a Vector3f to this one in place (the += operator).
	 * @param v
	 * @return This object.
	 */
	public Vector3f addSelf(Vector3f v) {
		Vector3f a = add(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Adds the properties of a Vector3f to this one (the + operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3f add(Vector3f left, Vector3f right) {
		Vector3f returnVector = new Vector3f();
		returnVector.x = left.x + right.x;
		returnVector.y = left.y + right.y;
		returnVector.z = left.z + right.z;
		
		return returnVector;
	}
	
	/**
	 * Subtracts the properties of a Vector3f from this one (the - operator).
	 * @param v
	 * @return
	 */
	public Vector3f subtract(Vector3f v) {
		return subtract(this, v);
	}
	
	/**
	 * Subtracts the properties of a Vector3f from this one in place (the -= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector3f subtractSelf(Vector3f v) {
		Vector3f a = subtract(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Subtracts the properties of a Vector3f from this one (the - operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3f subtract(Vector3f left, Vector3f right) {
		Vector3f returnVector = new Vector3f();
		returnVector.x = left.x - right.x;
		returnVector.y = left.y - right.y;
		returnVector.z = left.z - right.z;
		
		return returnVector;
	}
	
	/**
	 * Multiplies all values by the given float (the * operator).
	 * @param s
	 * @return
	 */
	public Vector3f multiply(float s) {
		return multiply(this, s);
	}
	
	/**
	 * Multiplies all values by the given float in place (the *= operator).
	 * @param s
	 * @return This object.
	 */
	public Vector3f multiplySelf(float s) {
		Vector3f a = multiply(this, s);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Multiplies all values by the given float (the * operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3f multiply(Vector3f left, float right) {
		Vector3f returnVector = new Vector3f();
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
	public Vector3f multiply(Vector3f v) {
		return multiply(this, v);
	}

	/**
	 * Multipies each property by its corresponding field in the other vector in place (the *= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector3f multiplySelf(Vector3f v) {
		Vector3f a = multiply(this, v);
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
	public static Vector3f multiply(Vector3f left, Vector3f right) {
		Vector3f returnVector = new Vector3f();
		returnVector.x = left.x * right.x;
		returnVector.y = left.y * right.y;
		returnVector.z = left.z * right.z;
		
		return returnVector;
	}
	
	/**
	 * Divides all values by the given float (the / operator).
	 * @param s
	 * @return
	 */
	public Vector3f divide(float s) {
		return divide(this, s);
	}
	
	/**
	 * Divides all values by the given float in place (the /= operator).
	 * @param s
	 * @return This object.
	 */
	public Vector3f divideSelf(float s) {
		Vector3f a = divide(this, s);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Divides all values by the given float (the / operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector3f divide(Vector3f left, float right) {
		Vector3f returnVector = new Vector3f();
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
	public Vector3f divide(Vector3f v) {
		return divide(this, v);
	}

	/**
	 * Divides each property by its corresponding field in the other vector in place (the /= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector3f divideSelf(Vector3f v) {
		Vector3f a = divide(this, v);
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
	public static Vector3f divide(Vector3f left, Vector3f right) {
		Vector3f returnVector = new Vector3f();
		returnVector.x = left.x / right.x;
		returnVector.y = left.y / right.y;
		returnVector.z = left.z / right.z;
		
		return returnVector;
	}
	
	/**
	 * Returns the Vector3f where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @return
	 */
	public Vector3f invert() {
		return invert(this);
	}
	
	/**
	 * In places the Vector3f where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @return This object.
	 */
	public Vector3f invertSelf() {
		Vector3f a  = invert(this);
		x = a.x;
		y = a.y;
		z = a.z;
		
		return this;
	}
	
	/**
	 * Returns the Vector3f where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @param term
	 * @return
	 */
	public static Vector3f invert(Vector3f term) {
		return divide(new Vector3f(1.0f, 1.0f, 1.0f), term);
	}
	
	/**
	 * Computes the overall distance achieved by this vector.
	 * @return
	 */
	public double modulus() {
		return Math.sqrt(x * x + y * y + z * z);
	}
}
