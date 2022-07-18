package pendulum.src;

// Imports window and graphic libs
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class App extends JFrame implements Runnable{

    public static double timePasssed = 0f;
    public int FPS = 0;

    public Pendulum pendulum = new Pendulum();

    // Initialize the application
    public App() {
        createWindow();

        new Thread(this).run();
    }

    // Creates window
    public void createWindow() {
        this.setSize(500, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.setTitle("Pendulum");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addComponentListener(pendulum);
        pendulum.frameX = this.getX();
        pendulum.frameY = this.getY();

        this.setVisible(true);
    }

    // Engine
    public void run() {
        final int FRAMES_PER_SECOND = 120,
                  TICKS_PER_SECOND = 120;

        int frames = 0;

        double MILLI_TO_SECONDS = 1000,
               delta_frame = 0f,
               delta_tick = 0f;

        long now = System.currentTimeMillis(),
             last = System.currentTimeMillis(),
             timer = System.currentTimeMillis();

        while(true) {

            // calculates the readyness of frame and tick
            now = System.currentTimeMillis();
            double time = (now - last) / MILLI_TO_SECONDS;
            delta_frame += time * FRAMES_PER_SECOND;
            delta_tick += time * TICKS_PER_SECOND;
            last = now;
            
            // checks if render is ready
            if(delta_frame > 1) {
                render();
                frames++;
                timePasssed = delta_frame / FRAMES_PER_SECOND; 
                delta_frame--;
            }

            // checks if tick is ready
            if(delta_tick > 1) {
                tick();
                delta_tick--;
            }

            // collects info about FPS every 1s
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                FPS = frames;
                frames = 0;
            }

        } // End of engine
    }

    // renders the graphics
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(2);
            return;
        }

        Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();

        g2.addRenderingHints(new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        ));

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        pendulum.render(g2);

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        g2.setColor(Color.WHITE);
        g2.setFont(font);

        g2.drawString("FPS: "+FPS, 20, 50);

        g2.dispose();
        bs.show();
    }

    // tick calculates for the graphics 
    public void tick() {
        pendulum.tick();
    }

    public static void main(String[] args) {
        new App();
    }
} // End of class
