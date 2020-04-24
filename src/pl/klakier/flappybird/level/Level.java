package pl.klakier.flappybird.level;

import pl.klakier.flappybird.Main;
import pl.klakier.flappybird.graphics.Shader;
import pl.klakier.flappybird.graphics.VertexArray;

public class Level {

	private VertexArray background;

	public Level() {
		float quarterWidth = Main.quarterWidth;
		float aspectRatio = Main.aspectRatio;
		
		// @formatter:off
		float[] vertices = new float[] { 
				quarterWidth, quarterWidth / aspectRatio, 0.0f, 
				quarterWidth, quarterWidth / aspectRatio, 0.0f, 
				quarterWidth, quarterWidth / aspectRatio, 0.0f, 
				quarterWidth, quarterWidth / aspectRatio, 0.0f 
		};

		byte[] indices = new byte[] { 
				0, 1, 2, 
				2, 3, 0 
		};
		
		float[] texCoord = new float[] { 
				0, 1, 
				0, 0, 
				1, 0, 
				1, 1
		};
		// @formatter:on
		
		background = new VertexArray(vertices, indices, texCoord);
	}
	
	public void render() {
		Shader.BG.bind();
		background.render();
		Shader.BG.unBind();
	}
}
