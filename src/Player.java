package src;
// imports
import java.awt.*;
import src.Components.*;

// player class extending the game object class
public class Player extends GameObject {
    // private variables for jumping and moving
    private GrappleComponent grapple;
    
    // constructor
    Player(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isMovable, boolean isPhysicsObject, GameManager gameManager) {
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isMovable, isPhysicsObject, gameManager); // Call the super constructor from GameObject
        
        addComponent(new GrappleComponent(this, gameManager), getComponents().size()-2); // Add the grapple component
        addComponent(new PlayerMovementComponent(this), getComponents().size()-3); // Add the player movement component
        
        this.grapple = getComponent(GrappleComponent.class); // Create the grapple
    }

    @Override
    public void draw(Graphics2D g) {
        // draw the grapple
        grapple.draw(g);
        // call the normal draw method from GameObject
        super.draw(g);
    }

    public void grapple(){
        if(grapple.isGrappling()) grapple.endGrapple();
        else grapple.startGrapple();
    }
}