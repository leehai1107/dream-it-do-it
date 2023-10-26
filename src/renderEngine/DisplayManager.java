package renderEngine;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author green
 */
public class DisplayManager {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 60;
    
    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay() throws IOException, LWJGLException {

        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withProfileCompatibility(true);

        DisplayMode displayMode = Display.getDisplayMode();
        int screenWidth = displayMode.getWidth();
        int screenHeight = displayMode.getHeight();

        Vector2f position = displayPosition(screenWidth, screenHeight);
  
        


            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);

            Display.setTitle("MyGame!");
            Display.setLocation((int)position.x, (int)position.y); // Set the display position
            Display.setResizable(true);
            

//            loadIconImage("icon/icon.png");
//            loadIconImage("icon/iconsm.png");

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }
    
    private static Vector2f displayPosition(int width, int height){
    	Vector2f vec2 = new Vector2f(width, height);
        // Get the screen scaling factor
        GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        double scaleX = graphicsConfiguration.getDefaultTransform().getScaleX();
        double scaleY = graphicsConfiguration.getDefaultTransform().getScaleY();

        int desiredDisplayWidth = WIDTH; // Set the desired display width
        int desiredDisplayHeight = HEIGHT;// Set the desired display height
        

        int scaledDisplayWidth = (int) (desiredDisplayWidth * scaleX);
        int scaledDisplayHeight = (int) (desiredDisplayHeight * scaleY);
        

        int centerX = (width - scaledDisplayWidth) ;
        int centerY = (height - scaledDisplayHeight);

        vec2.set(centerX, centerY);
        
        return vec2;
        
    }

    public static void updateDisplay() {
        if (Display.wasResized()) {
            updateViewport();
        }
        Display.sync(FPS_CAP);
        Display.update();
        
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
    }
    
    public static float getFrameTimeSeconds(){
        return delta;
    }

    //this funct has not used
    private static void loadIconImage(String imagePath) throws IOException {
        BufferedImage iconImage = ImageIO.read(new File(imagePath));

        int width = iconImage.getWidth();
        int height = iconImage.getHeight();
        int[] pixels = new int[width * height];
        iconImage.getRGB(0, 0, width, height, pixels, 0, width);
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
                buffer.put((byte) (pixel & 0xFF));         // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component
            }
        }
        buffer.flip();

        Display.setIcon(new ByteBuffer[]{buffer});
    }

    private static void updateViewport() {
        int newWidth = Display.getWidth();
        int newHeight = Display.getHeight();
        GL11.glViewport(0, 0, newWidth, newHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, newWidth, newHeight, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public static void closeDisplay() {
        Display.destroy();
    }
    
    private static long getCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }
}

