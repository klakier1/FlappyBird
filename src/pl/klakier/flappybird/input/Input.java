package pl.klakier.flappybird.input;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {

	private static int[] keyStates = new int[1024];
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		// TODO Auto-generated method stub
		keyStates[key] = action;
	}

	public static int getState(int key) {
		return keyStates[key];
	}
	
	public static boolean isPressed(int key) {
		return keyStates[key] != GLFW_RELEASE;
	}
	
}
