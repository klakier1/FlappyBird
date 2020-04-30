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

import pl.klakier.flappybird.graphics.Shader;
import pl.klakier.flappybird.input.Input;
import pl.klakier.flappybird.level.Level;
import pl.klakier.flappybird.math.Matrix4f;

import org.lwjgl.glfw.GLFWErrorCallback;

public class Main implements Runnable {

	// The window handle
	private long window;
	public static float aspectRatio = 16.0f / 9.0f;
	public static float quarterWidth = 1.7f;
	private int width = 1280;
	private int height = (int) ((float) width / aspectRatio);
	public static final Matrix4f pr_matrix = Matrix4f.orthographic(-quarterWidth, quarterWidth,
			-quarterWidth / aspectRatio, quarterWidth / aspectRatio, -1.0f, 1.0f);

	private Thread thread;
	private Boolean running = false;

	private Level level;

	// Main timer - frames limit
	private long currentNanos;
	private long prevNanos;
	private final int FPS_LIMIT = 60;
	private double interval = Math.pow(10.0, 9.0) / FPS_LIMIT;

	// FPS
	private int fps;

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
		if (glfwWindowShouldClose(window))
			running = false;

		currentNanos = System.nanoTime();
		prevNanos = currentNanos - (long) interval;
		while (running) {

			while (currentNanos - prevNanos < interval) {
				currentNanos = System.nanoTime();
			}
			prevNanos = currentNanos;

			update();
			render();

			if (glfwWindowShouldClose(window))
				running = false;
		}

		glfwDestroyWindow(window);
		glfwTerminate();

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

		// Enable depth test
		// glEnable(GL_DEPTH_TEST);

		// Enable blend
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		System.out.println(glGetString(GL_VERSION));

		// Load Shader
		int result;
		if ((result = Shader.loadAll()) != 0) {
			System.err.println("Shader no " + Math.abs(result));
			glfwSetWindowShouldClose(window, true);
			return;
		}

		level = new Level();

	}

	private void update() {
		glfwPollEvents();

		level.onUpdate();

		if (Input.isPressed(GLFW_KEY_ESCAPE))
			glfwSetWindowShouldClose(window, true);

		if (level.isGameOver() && Input.isPressed(GLFW_KEY_SPACE))
			level = new Level();

	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

		level.render();

		if (glGetError() != 0)
			System.out.println(glGetError());

		glfwSwapBuffers(window); // swap the color buffers
	}

}
