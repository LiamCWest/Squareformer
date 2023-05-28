package src.Objects;

import java.awt.*;

import src.GameManager;
import src.Components.*;

public class Objective extends GameObject{
    public Objective(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, GameManager gameManager) {
        super(x, y, colors, shapes, shapeQ, image, false, false, false, gameManager);

        addComponent(new ObjectiveComponent(this), -1);
    }
}
