package src.Objects.ActiveObjects;

import java.awt.*;

import src.GameManager;
import src.Objects.GameObject;

public class ActiveObject extends GameObject{
    private double change;
    private boolean isVisable = true;

    public ActiveObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, GameManager gameManager){
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isPhysicsObject, false, gameManager);
    }

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

    @Override
    public void draw(Graphics2D g){
        if(isVisable) super.draw(g);
    }
}
