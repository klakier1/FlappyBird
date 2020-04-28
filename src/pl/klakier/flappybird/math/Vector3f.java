package pl.klakier.flappybird.math;

public class Vector3f {

	public float x, y, z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public Vector3f add(Vector3f vec) {
		return new Vector3f(this.x + vec.x, this.y + vec.y, this.z + vec.z);
	}

	public Vector3f add(float x, float y, float z) {
		return new Vector3f(this.x + x, this.y + y, this.z + z);
	}
}
