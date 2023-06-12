package src.Components;

// Imports
import src.Objects.GameObject;

// GravityComponent class
public class GravityComponent implements ObjectComponent{
    // Variables
    private double gravity = 0.2;
    private GameObject object;
    private boolean isGrounded = false;
    private double terminalVelocity = 1;
    private double gravityForce = 0.0;

    // Constructor
    public GravityComponent(GameObject object){
        this.object = object;
    }

    // Update method
    @Override
    public void update(){
        isGrounded = object.getComponent(CollisionComponent.class).collidingBottom();

        // If the object is not grounded, apply gravity
        if(!isGrounded){
            object.addForce(new double[]{0, gravity});
            gravityForce += gravity;
            if(object.getVelocity()[1] > terminalVelocity){
                object.setVelocity(new double[]{object.getVelocity()[0], terminalVelocity});
                gravityForce = terminalVelocity;
            }
        }
        // If the object is grounded, stop applying gravity
        else{
            object.setVelocity(new double[]{object.getVelocity()[0], 0});
            gravityForce = 0.0;
        }
    }

    // Getters
    public double getGravityForce(){
        return gravityForce;
    }
}