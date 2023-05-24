// imports
import java.awt.*;

// player class extending the game object class
public class Player extends GameObject {
    // private variables for jumping and moving
    private double jumpHeight = 200;
    private double currentJumpHeight = 0;
    private boolean isJumping = false;
    private boolean isFalling = false;
    private double jumpForce = -3;
    private double moveForce = 8;
    private Grapple grapple;

    // constructor
    Player(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isMovable, boolean isPhysicsObject, GameManager gameManager) {
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isMovable, isPhysicsObject, gameManager); // Call the super constructor from GameObject
        this.grapple = new Grapple(this, gameManager); // Create the grapple
    }

    // override the GameObject move method
    @Override
    public void move() {
        // if the player is grappling, move the grapple
        if(grapple.isGrappling()){
            super.setHasGravity(false);
            grapple.move();
        }
        else super.setHasGravity(true);

        // if the player is grounded, set the current jump height to 0 and set falling to false
        if (super.isGrounded()) {
            currentJumpHeight = 0;
            isFalling = false;
        }
        // if the player is not grounded and not jumping, set falling to true
        else if(!(isJumping)){
            isFalling = true;
        }

        // if the player is jumping, apply the jump force
        if (isJumping) {
            // if the current jump height is less than the jump height, apply the jump force
            if (currentJumpHeight < jumpHeight) {
                // calculate the jump force scaled to the current jump height
                double jumpForceScaled = jumpForce * (1 - currentJumpHeight / jumpHeight);
                setVelocity(getVelocity()[0], (int) Math.round(jumpForceScaled)); // set the velocity
                currentJumpHeight++; // increment the current jump height
            }
            // else set jumping to false and falling to true 
            else {
                isJumping = false;
                isFalling = true;
            }
        }
        // if the player is falling, apply a small force downwards
        else if (isFalling && super.hasGravity()) {
            setVelocity(getVelocity()[0], 0.05);
        }

        // call the normal move method from GameObject
        super.move();
    }

    @Override
    public void draw(Graphics2D g) {
        // draw the grapple
        grapple.draw(g);
        // call the normal draw method from GameObject
        super.draw(g);
    }

    // method to jump
    public void jump() {
        // if the player is grounded, set jumping to true and falling to false
        if (super.isGrounded()) {
            isJumping = true;
            isFalling = false;
        }
    }

    // method to move the player
    public void moveForce(double xForce, double yForce) {
        // if the player is not grounded, apply half the force
        if (!(super.isGrounded())) {
            xForce /= 2;
            yForce /= 2;
        }
        // apply the force
        applyForce(moveForce * xForce, moveForce * yForce);
    }

    public void grapple(){
        if(grapple.isGrappling()) grapple.endGrapple();
        else grapple.startGrapple();
    }
}