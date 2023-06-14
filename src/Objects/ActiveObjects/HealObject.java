package src.Objects.ActiveObjects;

// Import necessary libraries
import java.awt.*;

import src.GameManager;
import src.Components.HealthChangeComponent;

// HealObject class
public class HealObject extends ActiveObject{
    public HealObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, GameManager gameManager){
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isPhysicsObject, gameManager);

        // Set health change to 5
        super.setChange(5.0);

        // Add health change component
        addComponent(new HealthChangeComponent(this), -1);
    }
}
