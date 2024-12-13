import javax.swing.*;
import java.awt.*;


//James I tried to implement the light indicator we spoke about for the bottom panel.

public class LightIndicator extends JPanel {
    private int radius;
    private Color color;

    //constructor to set the radius of the circle.
    public LightIndicator(int radius, Color color) {
        this.radius = radius;
        this.color =color;
    }

    //sets the color of the circle and repaints the circle object.
    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);//call the superclass method ensure proper painting
        g.setColor(color);//set color of the circle.
        g.fillOval((getWidth() - radius) / 2, (getHeight() - radius)/2, radius * 2, radius * 2);
        g.setColor(Color.black);//set color of the circle.
        //Draw the circle at the center of the panel
        g.drawOval((getWidth() - radius) / 2, (getHeight() - radius)/2, radius * 2, radius * 2);
    }
}
