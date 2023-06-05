package src.Objects.ActiveObjects;

import java.awt.*;

import src.GameManager;
import src.Components.HealthChangeComponent;

public class DamageObject extends ActiveObject{
    public DamageObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, GameManager gameManager){
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isPhysicsObject, gameManager);

        super.setChange(-0.1);

        addComponent(new HealthChangeComponent(this), -1);
    }
}
