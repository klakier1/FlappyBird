package pl.klakier.flappybird.graphics;

import pl.klakier.flappybird.math.Matrix4f;
import pl.klakier.flappybird.math.Vector3f;
import pl.klakier.flappybird.utils.BufferUtils;
import pl.klakier.flappybird.utils.ShaderUtils;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class Shader {

	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;

	public static Shader BG;
	public static Shader BIRD;
	public static Shader PIPE;
	public static Shader FADE;

	private boolean bound = false;
	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();

	public Shader(String vertPath, String fragPath) {
		ID = ShaderUtils.load(vertPath, fragPath);
	}

	public static int loadAll() {
		BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
		if (BG.ID == -1)
			return -1;

		BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
		if (BIRD.ID == -1)
			return -2;

		PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
		if (PIPE.ID == -1)
			return -3;

		FADE = new Shader("shaders/fade.vert", "shaders/fade.frag");
		if (FADE.ID == -1)
			return -4;

		return 0;
	}

	public void bind() {
		glUseProgram(ID);
		bound = true;
	}

	public void unBind() {
		glUseProgram(0);
		bound = false;
	}

	public int getUniformLocation(String name) {
		Integer result = 0;

		result = locationCache.get(name);
		if (result != null)
			return result;

		result = glGetUniformLocation(ID, name);
		if (result != -1) {
			locationCache.put(name, result);
		} else {
			System.err.println("Could not find uniform variable " + name);
		}

		return result;
	}

	public void setUniform1i(String name, int v0) {
		if (!bound)
			bind();
		glUniform1i(getUniformLocation(name), v0);
	}

	public void setUniform1f(String name, float v0) {
		if (!bound)
			bind();
		glUniform1f(getUniformLocation(name), v0);
	}

	public void setUniform2f(String name, float x, float y) {
		if (!bound)
			bind();
		glUniform2f(getUniformLocation(name), x, y);
	}

	public void setUniform3f(String name, Vector3f vec) {
		if (!bound)
			bind();
		glUniform3f(getUniformLocation(name), vec.x, vec.y, vec.z);
	}

	public void setUniformMat4f(String name, Matrix4f mat) {
		if (!bound)
			bind();
		glUniformMatrix4fv(getUniformLocation(name), false, mat.toFloatBuffer());
	}
}
