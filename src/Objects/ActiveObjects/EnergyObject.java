package src.Objects.ActiveObjects;

import java.awt.*;

import src.GameManager;
import src.Components.EnergyChangeComponent;

public class EnergyObject extends ActiveObject{

    public EnergyObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, GameManager gameManager){
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isPhysicsObject, gameManager);

        super.setChange(1.0);

        addComponent(new EnergyChangeComponent(this), -1);
    }
}
