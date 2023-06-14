package src.Objects.ActiveObjects;

// Import necessary libraries
import java.awt.*;

import src.GameManager;
import src.Components.HealthChangeComponent;

// DamageObject classs
public class DamageObject extends ActiveObject{
    // Constructor
    public DamageObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, GameManager gameManager){
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isPhysicsObject, gameManager);

        // Set change to -0.1
        super.setChange(-0.5);

        // Add health change component
        addComponent(new HealthChangeComponent(this), -1);
    }
}
