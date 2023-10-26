package mainEngine;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;

public class MainCore {
	public static void main(String[] args) throws IOException, LWJGLException {
		DisplayManager.createDisplay();
		while(!Display.isCloseRequested()) {
			DisplayManager.updateDisplay();
		}
		DisplayManager.closeDisplay();
	}
}
