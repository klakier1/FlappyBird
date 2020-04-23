package pl.klakier.flappybird;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.zip.InflaterInputStream;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import pl.klakier.flappybird.input.Input;

import org.lwjgl.glfw.GLFWErrorCallback;

public class Main implements Runnable {

	// The window handle
	private long window;
	private int width = 800;
	private int height = 600;

	private Thread thread;
	private Boolean running = false;

	public static void main(String[] args) {
		new Main().start();

	}

	public void start() {
		running = true;
		thread = new Thread(this, "Flappy");
		thread.start();

	}

	@Override
	public void run() {
		init();
		while (running) {
			update();
			render();

			if (glfwWindowShouldClose(window))
				running = false;
		}
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(width, height, "Flappy Bird", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		// Center the window
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

		if (window == NULL) {
			throw new IllegalStateException("Unable to create window");
		}

		glfwSetKeyCallback(window, new Input());
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		
		//Enable depth test
		glEnable(GL_DEPTH_TEST);
		
		System.out.print(glGetString(GL_VERSION));

	}

	private void update() {
		glfwPollEvents();
		
		if(Input.isPressed(GLFW_KEY_ESCAPE))
			glfwSetWindowShouldClose(window, true);
		
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

				
		glfwSwapBuffers(window); // swap the color buffers
	}

}
