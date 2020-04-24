package pl.klakier.flappybird.math;

import java.nio.FloatBuffer;

import pl.klakier.flappybird.utils.BufferUtils;

public class Matrix4f {

	public static final int DIMENSION = 4;
	public static final int SIZE = DIMENSION * DIMENSION;
	public float[] elements = new float[SIZE];

	public Matrix4f() {
		for (int i = 0; i < SIZE; i++) {
			elements[i] = 0.0f;
		}
	}

	public static Matrix4f identity() {
		Matrix4f result = new Matrix4f();

		result.elements[0 + 0 * DIMENSION] = 1;
		result.elements[1 + 1 * DIMENSION] = 1;
		result.elements[2 + 2 * DIMENSION] = 1;
		result.elements[3 + 3 * DIMENSION] = 1;

		return result;
	}

	public static Matrix4f orthographic(float left, float right, float top, float bottom, float near, float far) {
		Matrix4f result = new Matrix4f();

		result.elements[0 + 0 * DIMENSION] = 2.0f / (right - left);

		result.elements[1 + 1 * DIMENSION] = 2.0f / (top - bottom);

		result.elements[2 + 2 * DIMENSION] = -2.0f / (far - near);

		result.elements[0 + 3 * DIMENSION] = -((right + left) / (right - left));
		result.elements[1 + 3 * DIMENSION] = -((top + bottom) / (top - bottom));
		result.elements[2 + 3 * DIMENSION] = -((far + near) / (far - near));
		result.elements[3 + 3 * DIMENSION] = 1;

		return result;
	}

	public static Matrix4f translate(Vector3f vector) {
		Matrix4f result = identity();

		result.elements[0 + 3 * DIMENSION] = vector.x;
		result.elements[1 + 3 * DIMENSION] = vector.y;
		result.elements[2 + 3 * DIMENSION] = vector.z;

		return result;
	}

	public static Matrix4f rotateZ(float angdeg) {
		Matrix4f result = identity();

		float r = (float) Math.toRadians(angdeg);
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);

		result.elements[0 + 0 * DIMENSION] = cos;
		result.elements[1 + 0 * DIMENSION] = sin;
		result.elements[0 + 1 * DIMENSION] = -sin;
		result.elements[1 + 1 * DIMENSION] = cos;

		return result;
	}

	public Matrix4f multiply(Matrix4f matrix) {
		Matrix4f result = new Matrix4f();
		for (int y = 0; y < DIMENSION; y++) {
			for (int x = 0; x < DIMENSION; x++) {
				float sum = 0.0f;
				for (int e = 0; e < DIMENSION; e++) {
					sum += this.elements[x + e * DIMENSION] * matrix.elements[e + y * DIMENSION];
				}
				result.elements[x + y * DIMENSION] = sum;
			}
		}

		return result;
	}

	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(elements);
	}
}
