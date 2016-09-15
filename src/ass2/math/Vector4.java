package ass2.math;

/**
 * This class just stores four doubles in a single object. It can be used to store four data fields easily such as colours or quaternion rotation values.
 * 
 * The usage of it is similar to other Vector4 classes in engines such as the Unity Engine, but
 * all this code is original.
 * 
 * @author Thomas Moffet, z5061905
 *
 */
public class Vector4 {
	public double x;
	public double y;
	public double z;
	public double w;

	/**
	 * Constructs a Vector4 defaulting to (0.0, 0.0, 0.0, 0.0).
	 */
	public Vector4() {
		this(0.0, 0.0, 0.0, 0.0);
	}

	/**
	 * Constructs a Vector4 in the form of (x, y, z, w).
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Vector4(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Creates a new Vector4 with the same values.
	 */
	public Vector4 clone() {
		Vector4 returnVector = new Vector4();
		returnVector.x = x;
		returnVector.y = y;
		returnVector.z = z;
		returnVector.w = w;
		
		return returnVector;
	}
	
	/**
	 * Checks for equality between two Vector4 objects.
	 */
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		} else {
			Vector4 v = (Vector4)o;
			return (v.x == x && v.y == y && v.z == z && v.w == w);
		}
	}
	
	/**
	 * Adds the properties of a Vector4 to this one (the + operator).
	 * @param v
	 * @return
	 */
	public Vector4 add(Vector4 v) {
		return add(this, v);
	}

	/**
	 * Adds the properties of a Vector4 to this one in place (the += operator).
	 * @param v
	 * @return This object.
	 */
	public Vector4 addSelf(Vector4 v) {
		Vector4 a = add(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Adds the properties of a Vector4 to this one (the + operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector4 add(Vector4 left, Vector4 right) {
		Vector4 returnVector = new Vector4();
		returnVector.x = left.x + right.x;
		returnVector.y = left.y + right.y;
		returnVector.z = left.z + right.z;
		returnVector.w = left.w + right.w;
		
		return returnVector;
	}
	
	/**
	 * Subtracts the properties of a Vector4 from this one (the - operator).
	 * @param v
	 * @return
	 */
	public Vector4 subtract(Vector4 v) {
		return subtract(this, v);
	}
	
	/**
	 * Subtracts the properties of a Vector4 from this one in place (the -= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector4 subtractSelf(Vector4 v) {
		Vector4 a = subtract(this, v);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Subtracts the properties of a Vector4 from this one (the - operator).
	 * @param left
	 * @param right
	 * @return
	 */
	public static Vector4 subtract(Vector4 left, Vector4 right) {
		Vector4 returnVector = new Vector4();
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
	public Vector4 multiply(double s) {
		return multiply(this, s);
	}
	
	/**
	 * Multiplies all values by the given double in place (the *= operator).
	 * @param s
	 * @return This object.
	 */
	public Vector4 multiplySelf(double s) {
		Vector4 a = multiply(this, s);
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
	public static Vector4 multiply(Vector4 left, double right) {
		Vector4 returnVector = new Vector4();
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
	public Vector4 multiply(Vector4 v) {
		return multiply(this, v);
	}

	/**
	 * Multipies each property by its corresponding field in the other vector in place (the *= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector4 multiplySelf(Vector4 v) {
		Vector4 a = multiply(this, v);
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
	public static Vector4 multiply(Vector4 left, Vector4 right) {
		Vector4 returnVector = new Vector4();
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
	public Vector4 divide(double s) {
		return divide(this, s);
	}
	
	/**
	 * Divides all values by the given double in place (the /= operator).
	 * @param s
	 * @return This object.
	 */
	public Vector4 divideSelf(double s) {
		Vector4 a = divide(this, s);
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
	public static Vector4 divide(Vector4 left, double right) {
		Vector4 returnVector = new Vector4();
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
	public Vector4 divide(Vector4 v) {
		return divide(this, v);
	}

	/**
	 * Divides each property by its corresponding field in the other vector in place (the /= operator).
	 * @param v
	 * @return This object.
	 */
	public Vector4 divideSelf(Vector4 v) {
		Vector4 a = divide(this, v);
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
	public static Vector4 divide(Vector4 left, Vector4 right) {
		Vector4 returnVector = new Vector4();
		returnVector.x = left.x / right.x;
		returnVector.y = left.y / right.y;
		returnVector.z = left.z / right.z;
		returnVector.w = left.w / right.w;

		return returnVector;
	}
	
	/**
	 * Returns the Vector4 where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @return
	 */
	public Vector4 invert() {
		return invert(this);
	}
	
	/**
	 * In places the Vector4 where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @return This object.
	 */
	public Vector4 invertSelf() {
		Vector4 a  = invert(this);
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
		
		return this;
	}
	
	/**
	 * Returns the Vector4 where each property is the reciprocal of itself (similar to a ^-1 operator).
	 * 
	 * @param term
	 * @return
	 */
	public static Vector4 invert(Vector4 term) {
		return divide(new Vector4(1.0, 1.0, 1.0, 1.0), term);
	}
	
	/**
	 * Computes the overall distance achieved by this vector.
	 * @return
	 */
	public double modulus() {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}
}
