package src.Objects;

// Import necessary libraries
import java.awt.*;

import src.GameManager;
import src.Components.*;

// Objective class
public class Objective extends GameObject{
    // Constructor
    public Objective(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, GameManager gameManager) {
        super(x, y, colors, shapes, shapeQ, image, false, false, false, gameManager);

        // Add objective component
        addComponent(new ObjectiveComponent(this), -1);
    }
}
