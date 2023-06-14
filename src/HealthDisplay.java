package src;

// Import necessary libraries
import java.awt.*;
import java.awt.geom.*;

import src.Objects.Player;

// HealthDisplay class
public class HealthDisplay {
    // Variables for x, y, health, player, and healthBar
    private int x;
    private int y;
    private double health;
    private Player player;
    private Rectangle2D.Double healthBar;

    // Constructor
    public HealthDisplay(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;

        this.health = player.getHealth();
        this.healthBar = new Rectangle2D.Double(0, 0, 100, 0);
    }

    // update method
    public void update() {
        health = player.getHealth();
    }

    // draw method to draw the health heart
    public void draw(Graphics2D g){
        // heart border
        g.setColor(java.awt.Color.BLACK);
        g.fill(heart(x - 5, y - 5, 60, false, null));
        g.setColor(java.awt.Color.RED);

        // the area to remove from the hearts
        healthBar.height = 94 - (94 * (health / 20));
        
        // the heart removing the healthBar
        Area healthArea = new Area(heart(x, y, 50, true, healthBar));
        g.fill(healthArea); // fill the heart
    }

    // heart method to create the heart
    private Shape heart(int x, int y, int size, boolean hasMask, Rectangle2D.Double mask) {
        // bottom triangle
        TriangleShape triangle = new TriangleShape(new Point2D.Double(47, 94),
        new Point2D.Double(94, 30), new Point2D.Double(0, 30));

        // circles
        Ellipse2D.Double leftCircle = new Ellipse2D.Double(0, 0, 50, 50);
        Ellipse2D.Double rightCircle = new Ellipse2D.Double(44, 0, 50, 50);
        
        // rectangles for masking
        Rectangle2D.Double bottomRectangle = new Rectangle2D.Double(0, 30, 94, 50);
        Rectangle2D.Double topRectangle = new Rectangle2D.Double(0, 0, 94, 20);

        // temp mask areas
        Area tempRectArea = new Area(bottomRectangle);
        tempRectArea.subtract(new Area(triangle));

        Area tempCircleArea = new Area(topRectangle);
        tempCircleArea.subtract(new Area(rightCircle));
        tempCircleArea.subtract(new Area(leftCircle));

        Area tempArea = new Area(tempRectArea);
        tempArea.add(tempCircleArea);

        // heart area
        Area heartArea = new Area(triangle);
        heartArea.add(new Area(leftCircle));
        heartArea.add(new Area(rightCircle));
        heartArea.subtract(tempArea);
        if (hasMask) {
            heartArea.subtract(new Area(mask));
        }

        // transform the heart
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.scale(size / 100.0, size / 100.0);

        return transform.createTransformedShape(heartArea);
    }
}

// TriangleShape class
class TriangleShape extends Path2D.Double {
  public TriangleShape(Point2D... points) {
    moveTo(points[0].getX(), points[0].getY());
    lineTo(points[1].getX(), points[1].getY());
    lineTo(points[2].getX(), points[2].getY());
    closePath();
  }
}
