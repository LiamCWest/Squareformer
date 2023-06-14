package src.Objects.ActiveObjects;

// Import necessary libraries
import java.awt.*;

import src.GameManager;
import src.Components.EnergyChangeComponent;

// EnergyObject class
public class EnergyObject extends ActiveObject{

    // Constructor
    public EnergyObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, GameManager gameManager){
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isPhysicsObject, gameManager);

        // Set energy change
        super.setChange(1.0);
        
        // Add energy change component
        addComponent(new EnergyChangeComponent(this), -1);
    }
}
