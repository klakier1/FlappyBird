package pl.klakier.flappybird.level;

import java.util.Random;

import pl.klakier.flappybird.Main;
import pl.klakier.flappybird.graphics.Shader;
import pl.klakier.flappybird.graphics.Texture;
import pl.klakier.flappybird.graphics.VertexArray;
import pl.klakier.flappybird.math.Matrix4f;
import pl.klakier.flappybird.math.Vector3f;

public class Pipe {

	private Vector3f position;
	private int top;

	private static float quarterWidth = Level.quarterWidth;
	private static float aspectRatio = Level.aspectRatio;
	private static float quarterHeight = quarterWidth / aspectRatio;
	private static VertexArray vaoPipe;
	private static Texture texPipe;
	private static final float OFFSET = quarterWidth * 0.2f;
	private static final float DISTANCE = quarterWidth * 0.3f;
	private static final float GAP = quarterHeight * 0.7f;
	private static final int COUNT = (int) ((quarterWidth / DISTANCE) + 1);
	private static final float HEIGHT = quarterHeight * 1.5f;
	private static final float WIDTH = quarterWidth * 0.18f;
	private static Pipe[] pipes;
	private static Random random = new Random();
	private static float X_SCROLL_STEP = quarterWidth / 140;

	public Pipe(Vector3f position, boolean isTop) {
		this.position = position;
		this.top = isTop ? 1 : 0;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public static void onUpdate() {
		for (int i = 0; i < COUNT * 2; i += 2) {
			pipes[i].position.x -= X_SCROLL_STEP;
			pipes[i + 1].position.x -= X_SCROLL_STEP;

			if (pipes[i].position.x < -quarterWidth - WIDTH) {
				pipes[i].position.x = COUNT * DISTANCE;
				pipes[i + 1].position.x = COUNT * DISTANCE;

				pipes[i].position.y = (random.nextFloat() - 0.5f);
				pipes[i + 1].position.y = pipes[i].getPosition().y - HEIGHT - GAP;
			}
		}

	}

	public static void render() {
		texPipe.bind(3);
		Shader.PIPE.bind();
		Shader.PIPE.setUniformMat4f("mv_matrix", Matrix4f.identity());
		vaoPipe.bind();
		for (Pipe pipe : pipes) {
			Shader.PIPE.setUniform1i("isTop", pipe.top);
			Shader.PIPE.setUniformMat4f("mv_matrix", Matrix4f.translate(pipe.position));
			vaoPipe.draw();
		}
		vaoPipe.unBind();
		Shader.PIPE.unBind();
	}

	public static void create() {
		texPipe = new Texture("res/pipe.png", 3);

		// @formatter:off
		float[] vertices = new float[] { 
				-WIDTH/2, HEIGHT + GAP/2, 0.0f, 
				-WIDTH/2,          GAP/2, 0.0f, 
				 WIDTH/2,          GAP/2, 0.0f,
				 WIDTH/2, HEIGHT + GAP/2, 0.0f 
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

		vaoPipe = new VertexArray(vertices, indices, texCoord);

		texPipe.bind(3);
		Shader.PIPE.bind();
		Shader.PIPE.setUniform1i("tex", 3);
		Shader.PIPE.setUniformMat4f("pr_matrix", Main.pr_matrix);
		Shader.PIPE.setUniformMat4f("mv_matrix", Matrix4f.identity());
		Shader.PIPE.unBind();

		pipes = new Pipe[COUNT * 2];
		for (int i = 0; i < COUNT * 2; i += 2) {
			pipes[i] = new Pipe(new Vector3f(OFFSET + i * DISTANCE, (random.nextFloat() - 0.5f), 0.0f), true);
			pipes[i + 1] = new Pipe(pipes[i].getPosition().add(0f, - HEIGHT - GAP, 0f), false);
		}

	}

}
