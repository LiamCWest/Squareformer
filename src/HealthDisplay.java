package src;

import java.awt.*;
import java.awt.geom.*;

import src.Objects.Player;

public class HealthDisplay {
    private int x;
    private int y;
    private double health;
    private Player player;
    private Rectangle2D.Double healthBar;

    public HealthDisplay(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;

        this.health = player.getHealth();
        this.healthBar = new Rectangle2D.Double(0, 0, 100, 0);
    }

    public void update() {
        health = player.getHealth();
    }

    public void draw(Graphics2D g){
        g.setColor(java.awt.Color.BLACK);
        g.fill(heart(x - 5, y - 5, 60, false, null));
        g.setColor(java.awt.Color.RED);

        healthBar.height = 94 - (94 * (health / 20));
        
        Area healthArea = new Area(heart(x, y, 50, true, healthBar));
        g.fill(healthArea);
    }

    private Shape heart(int x, int y, int size, boolean hasMask, Rectangle2D.Double mask) {
        TriangleShape triangle = new TriangleShape(new Point2D.Double(47, 94),
        new Point2D.Double(94, 30), new Point2D.Double(0, 30));

        Ellipse2D.Double leftCircle = new Ellipse2D.Double(0, 0, 50, 50);
        Ellipse2D.Double rightCircle = new Ellipse2D.Double(44, 0, 50, 50);
        
        Rectangle2D.Double bottomRectangle = new Rectangle2D.Double(0, 30, 94, 50);
        Rectangle2D.Double topRectangle = new Rectangle2D.Double(0, 0, 94, 20);

        Area tempRectArea = new Area(bottomRectangle);
        tempRectArea.subtract(new Area(triangle));

        Area tempCircleArea = new Area(topRectangle);
        tempCircleArea.subtract(new Area(rightCircle));
        tempCircleArea.subtract(new Area(leftCircle));

        Area tempArea = new Area(tempRectArea);
        tempArea.add(tempCircleArea);

        Area heartArea = new Area(triangle);
        heartArea.add(new Area(leftCircle));
        heartArea.add(new Area(rightCircle));
        heartArea.subtract(tempArea);
        if (hasMask) {
            heartArea.subtract(new Area(mask));
        }

        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.scale(size / 100.0, size / 100.0);

        return transform.createTransformedShape(heartArea);
    }
}

class TriangleShape extends Path2D.Double {
  public TriangleShape(Point2D... points) {
    moveTo(points[0].getX(), points[0].getY());
    lineTo(points[1].getX(), points[1].getY());
    lineTo(points[2].getX(), points[2].getY());
    closePath();
  }
}
