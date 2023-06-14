package src.Objects;

// Import necessary libraries
import java.awt.*;

// TextObject class
public class TextObject extends GameObject{
    // Variables for text, x, y, and size
    private String text;
    private int x, y, size;

    // Constructor
    public TextObject(int x, int y, String text, int size){
        super(x, y, new Color[]{Color.BLACK}, new Polygon[]{new Polygon(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, 4)}, true, null, false, false, false, null);
        
        this.x = x;
        this.y = y;
        this.size = size;
        this.text = text;
    }

    // draw method to draw text
    @Override
    public void draw(Graphics2D g2d){
        g2d.setColor(super.getColors()[0]);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, size)); 
        g2d.drawString(text, x, y);
    }

    // override update method to do nothing
    @Override
    public void update(){
        // Do nothing
    }
}
