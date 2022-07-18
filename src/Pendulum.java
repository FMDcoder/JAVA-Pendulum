package pendulum.src;

// imports graphics and tools
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

public class Pendulum implements ComponentListener {

    public Point center = new Point(250, 250);

    public int 
        center_radius = 20, 
        sphere_radius = 40,
        radius = 100 + center_radius + sphere_radius,
        frameX = 0,
        frameY = 0;

    public double angle = Math.PI / 2,
                  speed = 0,
                  acc = 0;

    public boolean needShake = false;

    public long time = System.currentTimeMillis();

    // renders the ball
    public void render(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillOval(
            center.x - center_radius, 
            center.y - center_radius,
            center_radius * 2, 
            center_radius * 2
        );

        int px = (int)(Math.sin(angle) * radius + center.x),
            py = (int)(Math.cos(angle) * radius + center.y);

        g2.setStroke(new BasicStroke(10));
        g2.drawLine(
            center.x,
            center.y,
            px,
            py
        );

        g2.setColor(Color.BLUE);
        g2.fillOval(
            px - center_radius * 2,
            py - center_radius * 2,
            sphere_radius * 2,
            sphere_radius * 2
        );
        boolean speedSlow = speed > -0.01 && speed < 0.01,
                angleLow = (angle % (2 * Math.PI)) > -0.01 && (angle % (2 * Math.PI)) < 0.01;

        // when the ball is stopping in the middle we tell the user to shake the window
        if((speedSlow && angleLow) || needShake) {
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
            g2.setColor(Color.WHITE);
            g2.setFont(font);

            String shake = "SHAKE THE WINDOW TO SPIN IT";
            FontMetrics fm = g2.getFontMetrics();
            
            int xp = (500 - fm.stringWidth(shake)) / 2;

            int t = (int)((System.currentTimeMillis() - time) % 500) / 250;

            g2.setColor(t == 1 ? Color.BLACK : Color.WHITE);
            g2.drawString(shake, xp, 200);
            needShake = true;
        } 
    }

    public void tick() {
        acc = -1 * Math.sin(angle) / radius;
        speed += acc;
        angle += speed;
        
        speed *= 0.99;
    }

    @Override
    public void componentResized(ComponentEvent e) {}

    // gives ball swing from moving around window
    @Override
    public void componentMoved(ComponentEvent e) {
        JFrame frame = (JFrame)e.getSource();
        float x = frame.getX() - frameX;
        
        double s = (x / Toolkit.getDefaultToolkit().getScreenSize().width) / 2;
        speed += s;

        frameX = frame.getX();
        frameY = frame.getY();
        needShake = false;
    }

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
} // End of class
