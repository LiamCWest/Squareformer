package src.Objects;

import java.awt.*;

public class TextObject extends GameObject{
    private String text;
    private int x, y, size;

    public TextObject(int x, int y, String text, int size){
        super(x, y, new Color[]{Color.BLACK}, new Polygon[]{new Polygon(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, 4)}, true, null, false, false, false, null);
        
        this.x = x;
        this.y = y;
        this.size = size;
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g2d){
        g2d.setColor(super.getColors()[0]);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, size)); 
        g2d.drawString(text, x, y);
    }

    @Override
    public void update(){
        // Do nothing
    }
}
