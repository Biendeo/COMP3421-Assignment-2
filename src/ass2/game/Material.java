package ass2.game;

import ass2.math.Vector4f;

/**
 * Stores properties about how an object should be coloured.
 */
public class Material {
	public Vector4f ambient;
	public Vector4f diffuse;
	public Vector4f specular;
	public Vector4f phong;

	public Material() {
		ambient = new Vector4f();
		diffuse = new Vector4f();
		specular = new Vector4f();
		phong = new Vector4f();
	}
}
