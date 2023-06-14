package src.Objects.ActiveObjects;

// Import necessary libraries
import java.awt.*;

import src.GameManager;
import src.Objects.GameObject;

// ActiveObject class
public class ActiveObject extends GameObject{
    // Variables for change and visability
    private double change;
    private boolean isVisable = true;

    // Constructor
    public ActiveObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, GameManager gameManager){
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isPhysicsObject, false, gameManager);
    }

    // Getters and setters
    public double getChange(){
        return change;
    }

    public void setChange(double change){
        this.change = change;
    }

    public boolean isVisable(){
        return isVisable;
    }

    public void setVisable(boolean visable){
        isVisable = visable;
    }


    // Draw method
    @Override
    public void draw(Graphics2D g){
        if(isVisable) super.draw(g);
    }
}
