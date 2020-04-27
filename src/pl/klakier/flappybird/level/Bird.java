package pl.klakier.flappybird.level;

import pl.klakier.flappybird.Main;
import pl.klakier.flappybird.graphics.Shader;
import pl.klakier.flappybird.graphics.Texture;
import pl.klakier.flappybird.graphics.VertexArray;
import pl.klakier.flappybird.input.Input;
import pl.klakier.flappybird.math.Matrix4f;
import pl.klakier.flappybird.math.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

public class Bird {

	private VertexArray vaoBird;
	private Texture texBird;
	private float quarterWidth = Level.quarterWidth;
	private float aspectRatio = Level.aspectRatio;
	private float quarterHeight = quarterWidth / aspectRatio;
	private float size = quarterWidth / 10;
	private Vector3f posVec = new Vector3f();
	private float rotZ = 0;
	private float yScrollStepFallDown = size / 200;
	private float yScrollStepJump = size / 10;
	private float delta;

	public Bird() {
		texBird = new Texture("res/bird.png", 2);

		// @formatter:off
		float[] vertices = new float[] { 
				posVec.x - size/2, posVec.y + size/2, 0.0f, 
				posVec.x - size/2, posVec.y - size/2, 0.0f, 
				posVec.x + size/2, posVec.y - size/2, 0.0f,
				posVec.x + size/2, posVec.y + size/2, 0.0f 
		};

		byte[] indices = new byte[] { 
				0, 1, 2, 
				2, 3, 0 
		};
		
		float[] texCoord = new float[] { 
				0, 0, 
				0, 1, 
				1, 1, 
				1, 0
		};
		// @formatter:on

		vaoBird = new VertexArray(vertices, indices, texCoord);

		texBird.bind(2);
		Shader.BIRD.bind();
		Shader.BIRD.setUniform1i("tex", 2);
		Shader.BIRD.setUniformMat4f("pr_matrix", Main.pr_matrix);
		Shader.BIRD.setUniformMat4f("mv_matrix", Matrix4f.identity());
		Shader.BIRD.unBind();

	}

	public void onUpdate() {
		boolean outOfScreenDown = (posVec.y + size < -quarterHeight) ? true : false;

		if (!outOfScreenDown)
			posVec.y -= delta;
		if (Input.isPressed(GLFW_KEY_SPACE)) {
			delta = -yScrollStepJump;
			if (outOfScreenDown)
				posVec.y -= delta;
		} else {
			if (!outOfScreenDown)
				delta += yScrollStepFallDown;
		}

		rotZ = -delta * 90.0f / (quarterWidth / 10);
	}

	public void render() {
		Shader.BIRD.bind();
		Shader.BIRD.setUniformMat4f("mv_matrix", Matrix4f.translate(posVec).multiply(Matrix4f.rotateZ(rotZ)));
		vaoBird.render();
		Shader.BIRD.unBind();
	}
}
