package ass2.math;

/**
 * This class just stores four floats in a single object. It can be used to store four data fields easily such as colours or quaternion rotation values.
 * 
 * The usage of it is similar to other Vector4f classes in engines such as the Unity Engine, but
 * all this code is original.
 * 
 * @author Thomas Moffet, z5061905
 *
 */
public class Vector4f {
	public float x;
	public float y;
	public float z;
	public float w;

	/**
	 * Constructs a Vector4f defaulting to (0.0, 0.0, 0.0, 0.0).
	 */
	public Vector4f() {
		this(0.0f, 0.0f, 0.0f, 0.0f);
	}

	/**
	 * Constructs a Vector4f in the form of (x, y, z, w).
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Creates a new Vector4f with the same values.
	 */
	public Vector4f clone() {
		Vector4f returnVector = new Vector4f();
		returnVector.x = x;
		returnVector.y = y;
		returnVector.z = z;
		returnVector.w = w;
		
		return returnVector;
	}
	
	/**
	 * Checks for equality between two Vector4f objects.
	 */
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		} else {
			Vector4f v = (Vector4f)o;
			return (v.x == x && v.y == y && v.z == z && v.w == w);
		}
	}
	
	/**
	 * Adds the properties of a Vector4f to this one (the + operator).
	 * @param v
	 * @return
	 */
	public Vector4f add(Vector4f v) {
		return add(this, v);
	}

	/**
	 * Adds the properties of a Vector4f to this one in place (the += operator).
	 * @param v
	 * @return This object.
	 */
	public Vector4f addSelf(Vector4f v) {
		Vector4f a = add(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Adds the properties of a Vector4f to this one (the + operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector4f add(Vector4f left, Vector4f right) {
		Vector4f returnVector = new Vector4f();
		returnVector.x = left.x + right.x;
		returnVector.y = left.y + right.y;
		returnVector.z = left.z + right.z;
		returnVector.w = left.w + right.w;
		
		return returnVector;
	}
	
	/**
	 * Subtracts the properties of a Vector4f from this one (the - operator).
	 * @param v
	 * @return
	 */
	public Vector4f subtract(Vector4f v) {
		return subtract(this, v);
	}
	
	/**
	 * Subtracts the properties of a Vector4f from this one in place (the -= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector4f subtractSelf(Vector4f v) {
		Vector4f a = subtract(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Subtracts the properties of a Vector4f from this one (the - operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector4f subtract(Vector4f left, Vector4f right) {
		Vector4f returnVector = new Vector4f();
		returnVector.x = left.x - right.x;
		returnVector.y = left.y - right.y;
		returnVector.z = left.z - right.z;
		returnVector.w = left.w - right.w;

		return returnVector;
	}
	
	/**
	 * Multiplies all values by the given double (the * operator).
	 * @param s
	 * @return
	 */
	public Vector4f multiply(float s) {
		return multiply(this, s);
	}
	
	/**
	 * Multiplies all values by the given double in place (the *= operator).
	 * @param s
	 * @return This object.
	 */
	public Vector4f multiplySelf(float s) {
		Vector4f a = multiply(this, s);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Multiplies all values by the given double (the * operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector4f multiply(Vector4f left, float right) {
		Vector4f returnVector = new Vector4f();
		returnVector.x = left.x * right;
		returnVector.y = left.y * right;
		returnVector.z = left.z * right;
		returnVector.w = left.w * right;
		
		return returnVector;
	}
	
	/**
	 * Multiplies each property by its corresponding field in the other vector (the * operator).
	 * @param v
	 * @return
	 */
	public Vector4f multiply(Vector4f v) {
		return multiply(this, v);
	}

	/**
	 * Multipies each property by its corresponding field in the other vector in place (the *= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector4f multiplySelf(Vector4f v) {
		Vector4f a = multiply(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Multiplies each property by its corresponding field in the other vector (the * operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector4f multiply(Vector4f left, Vector4f right) {
		Vector4f returnVector = new Vector4f();
		returnVector.x = left.x * right.x;
		returnVector.y = left.y * right.y;
		returnVector.z = left.z * right.z;
		returnVector.w = left.w * right.w;
		
		return returnVector;
	}
	
	/**
	 * Divides all values by the given double (the / operator).
	 * @param s
	 * @return
	 */
	public Vector4f divide(float s) {
		return divide(this, s);
	}
	
	/**
	 * Divides all values by the given double in place (the /= operator).
	 * @param s
	 * @return This object.
	 */
	public Vector4f divideSelf(float s) {
		Vector4f a = divide(this, s);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Divides all values by the given double (the / operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector4f divide(Vector4f left, float right) {
		Vector4f returnVector = new Vector4f();
		returnVector.x = left.x / right;
		returnVector.y = left.y / right;
		returnVector.z = left.z / right;
		returnVector.w = left.w / right;

		return returnVector;
	}
	
	/**
	 * Divides each property by its corresponding field in the other vector (the / operator).
	 * @param v
	 * @return
	 */
	public Vector4f divide(Vector4f v) {
		return divide(this, v);
	}

	/**
	 * Divides each property by its corresponding field in the other vector in place (the /= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector4f divideSelf(Vector4f v) {
		Vector4f a = divide(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Divides each property by its corresponding field in the other vector (the / operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector4f divide(Vector4f left, Vector4f right) {
		Vector4f returnVector = new Vector4f();
		returnVector.x = left.x / right.x;
		returnVector.y = left.y / right.y;
		returnVector.z = left.z / right.z;
		returnVector.w = left.w / right.w;

		return returnVector;
	}
	
	/**
	 * Returns the Vector4f where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @return
	 */
	public Vector4f invert() {
		return invert(this);
	}
	
	/**
	 * In places the Vector4f where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @return This object.
	 */
	public Vector4f invertSelf() {
		Vector4f a  = invert(this);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Returns the Vector3 where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @param term
	 * @return
	 */
	public static Vector4f invert(Vector4f term) {
		return divide(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), term);
	}
	
	/**
	 * Computes the overall distance achieved by this vector.
	 * @return
	 */
	public double modulus() {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}
}
