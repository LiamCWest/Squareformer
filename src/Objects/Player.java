package src.Objects;
// imports
import java.awt.*;

import src.GameManager;
import src.Components.*;

// player class extending the game object class
public class Player extends GameObject {
    // private variables for jumping and moving
    private GrappleComponent grapple;
    private double energy = 10.0;
    private double health = 10.0;
    private int energyDelay = 0;
    
    // constructor
    public Player(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, GameManager gameManager) {
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isPhysicsObject, true, gameManager); // Call the super constructor from GameObject
        
        addComponent(new PlayerMovementComponent(this), getComponents().size()-3); // Add the player movement component
        addComponent(new GrappleComponent(this, gameManager), getComponents().size()-2); // Add the grapple component
        
        this.grapple = getComponent(GrappleComponent.class); // Create the grapple
    }

    @Override
    public void draw(Graphics2D g) {
        // draw the grapple
        grapple.draw(g);
        // call the normal draw method from GameObject
        super.draw(g);
    }

    @Override
    public void update() {
        if(energy < 10 && !(grapple.isGrappling()) && energyDelay == 0) energy += 0.0005;
        if (energy > 10) energy = 10;
        if (grapple.isGrappling()) energyDelay = 500;
        else if (energyDelay > 0) energyDelay--;
        // call the normal update method from GameObject
        super.update();
    }

    // method to grapple
    public void grapple(){
        if(grapple.isGrappling()) grapple.endGrapple();
        else grapple.startGrapple();
    }

    // method to change the energy
    public void changeEnergy(double energyDelta){
        energy += energyDelta;
    }

    // method to change the health
    public void changeHealth(double healthDelta){
        health += healthDelta;
    }

    // getters and setters
    public double getEnergy(){
        return energy;
    }

    public double getHealth(){
        return health;
    }

    public void setEnergy(double energy){
        this.energy = energy;
    }

    public void setHealth(double health){
        this.health = health;
    }
}