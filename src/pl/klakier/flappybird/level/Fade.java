package pl.klakier.flappybird.level;

import org.lwjgl.opengl.GL11;

import pl.klakier.flappybird.graphics.Shader;

public class Fade {

	private boolean done = false;
	private float time = 0f;
	private long startTime = 0;
	private float duration;

	Fade(float durationMs) {
		this.duration = durationMs / 1000f;
		startTime  = System.currentTimeMillis();
	}

	public void render() {
		time = ((System.currentTimeMillis() - startTime)) / 1000f;
		
		if (time < duration) {
			Shader.FADE.bind();
			Shader.FADE.setUniform1f("time", time);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
			Shader.FADE.unBind();
		} else
			done = true;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
}
