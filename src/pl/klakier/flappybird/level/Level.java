package pl.klakier.flappybird.level;

import pl.klakier.flappybird.Main;
import pl.klakier.flappybird.graphics.Shader;
import pl.klakier.flappybird.graphics.Texture;
import pl.klakier.flappybird.graphics.VertexArray;
import pl.klakier.flappybird.math.Matrix4f;
import pl.klakier.flappybird.math.Vector3f;

public class Level {

	private Bird bird;
	private VertexArray vaoBackground;.
	private Texture texBackground;
	private int map = 0;
	public static float quarterWidth;
	public static float aspectRatio;
	private float textureRatio;
	private float tex_half_height;
	private float tex_half_width;
	private float xScroll = 0.0f;
	private float xScrollStep = 0.0f;

	public Level() {
		texBackground = new Texture("res/bg.jpeg", 1);

		quarterWidth = Main.quarterWidth;
		aspectRatio = Main.aspectRatio;
		textureRatio = (float) texBackground.getWidth() / (float) texBackground.getHeight();
		tex_half_height = quarterWidth / aspectRatio;
		tex_half_width = tex_half_height * textureRatio;
		xScrollStep = tex_half_width / 60;
		map = (int) (quarterWidth / (tex_half_width)) + 2;

		// @formatter:off
		float[] vertices = new float[] { 
				-quarterWidth,                       quarterWidth / aspectRatio, 0.0f, 
				-quarterWidth,                      -quarterWidth / aspectRatio, 0.0f, 
				-quarterWidth + tex_half_width * 2, -quarterWidth / aspectRatio, 0.0f, 
				-quarterWidth + tex_half_width * 2,  quarterWidth / aspectRatio, 0.0f 
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

		vaoBackground = new VertexArray(vertices, indices, texCoord);

		texBackground.bind(1);
		Shader.BG.bind();
		Shader.BG.setUniform1i("tex", 1);
		Shader.BG.setUniformMat4f("pr_matrix", Main.pr_matrix);
		Shader.BG.setUniformMat4f("mv_matrix", Matrix4f.identity());
		Shader.BG.unBind();

		bird = new Bird();

	}

	public void onUpdate() {
		xScroll -= xScrollStep;
		if (xScroll < -(tex_half_width * 2))
			xScroll = 0.0f;
		
		bird.onUpdate();
	}

	public void render() {

		bird.render();

		Shader.BG.bind();
		vaoBackground.bind();
		for (int i = 0; i < map; i++) {
			Vector3f translation = new Vector3f((float) i * (tex_half_width * 2) + xScroll, 0, 0);
			Shader.BG.setUniformMat4f("mv_matrix", Matrix4f.translate(translation));
			vaoBackground.draw();
		}
		vaoBackground.unBind();
		Shader.BG.unBind();

		bird.render();

	}

	public float getxScrollStep() {
		return xScrollStep;
	}

	public void setxScrollStep(float xScrollStep) {
		this.xScrollStep = xScrollStep;
	}
}
