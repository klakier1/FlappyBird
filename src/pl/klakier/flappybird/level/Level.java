package pl.klakier.flappybird.level;

import pl.klakier.flappybird.Main;
import pl.klakier.flappybird.graphics.Shader;
import pl.klakier.flappybird.graphics.Texture;
import pl.klakier.flappybird.graphics.VertexArray;
import pl.klakier.flappybird.math.Matrix4f;
import pl.klakier.flappybird.math.Vector3f;

public class Level {

	private Fade fade;
	private Bird bird;
	private VertexArray vaoBackground;
	private Texture texBackground;
	private int map = 0;
	public static float quarterWidth;
	public static float aspectRatio;
	private float textureRatio;
	private float tex_half_height;
	private float tex_half_width;
	private float xScroll = 0.0f;
	private float xScrollStep = 0.0f;
	private boolean animate = true;
	boolean gameOver;
	public Level() {
		
		gameOver = false;
		texBackground = new Texture("res/bg.jpeg", 1);

		quarterWidth = Main.quarterWidth;
		aspectRatio = Main.aspectRatio;
		textureRatio = (float) texBackground.getWidth() / (float) texBackground.getHeight();
		tex_half_height = quarterWidth / aspectRatio;
		tex_half_width = tex_half_height * textureRatio;
		xScrollStep = tex_half_width / 160;
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
		Pipe.create();
		fade = new Fade(1500f);
	}

	public void onUpdate() {
		if (animate) {
			xScroll -= xScrollStep;
			if (xScroll < -(tex_half_width * 2))
				xScroll = 0.0f;
		}

		if (collision()) {
			this.setAnimate(false);
			Pipe.setAnimate(false);
			bird.setControl(false);
			gameOver = true;
		}

		Pipe.onUpdate();
		bird.onUpdate();
	}

	public void render() {

		Shader.BG.bind();
		Shader.BG.setUniform2f("birdPos", bird.getPosVec().x, bird.getPosVec().y);
		vaoBackground.bind();
		for (int i = 0; i < map; i++) {
			Vector3f translation = new Vector3f((float) i * (tex_half_width * 2) + xScroll, 0, 0);
			Shader.BG.setUniformMat4f("mv_matrix", Matrix4f.translate(translation));
			vaoBackground.draw();
		}
		vaoBackground.unBind();
		Shader.BG.unBind();

		Pipe.render(bird.getPosVec());
		bird.render();

		if (fade != null) {
			if (fade.isDone())
				fade = null;
			else
				fade.render();
		}

	}

	public boolean collision() {
		float bxL = bird.getPosVec().x - bird.getSize() * 0.8f / 2;
		float bxR = bird.getPosVec().x + bird.getSize() * 0.8f / 2;
		float byB = bird.getPosVec().y - bird.getSize() * 0.8f / 2;
		float byT = bird.getPosVec().y + bird.getSize() * 0.8f / 2;

		float pxL, pxR, pyT, pyB;
		final Pipe[] pipes = Pipe.getPipes();

		for (int i = 0; i < Pipe.getCount(); i++)
			pipes[i].setHasCollision(false);

		for (int i = 0; i < Pipe.getCount(); i += 2) {
			pxL = pipes[i].getPosition().x - Pipe.getWidth() / 2;
			pxR = pipes[i].getPosition().x + Pipe.getWidth() / 2;
			pyT = pipes[i].getPosition().y + Pipe.getGap() / 2; // BOTTOM OF UPPER PIPE
			pyB = pipes[i].getPosition().y - Pipe.getGap() / 2;

			if (pxL < bxR && pxR > bxL) {
				if (pyT < byT) {
					pipes[i].setHasCollision(true);
					return true;
				} else if (pyB > byB) {
					pipes[i + 1].setHasCollision(true);
					return true;
				}
			}
		}

		return false;
	}

	public float getxScrollStep() {
		return xScrollStep;
	}

	public void setxScrollStep(float xScrollStep) {
		this.xScrollStep = xScrollStep;
	}

	public boolean isAnimate() {
		return animate;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
}
