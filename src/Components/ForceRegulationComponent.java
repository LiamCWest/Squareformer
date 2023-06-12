package src.Components;

// Import
import src.Objects.GameObject;

// ForceRegulationComponent class
public class ForceRegulationComponent implements ObjectComponent{
    // Variables
    private double maxVelocity;
    private GameObject object;

    // Constructor
    public ForceRegulationComponent(GameObject object, double maxVelocity){
        this.object = object;
        this.maxVelocity = maxVelocity;
    }

    // Update method
    @Override
    public void update(){
        // Check if the object is moving too fast
        int moveDirection = object.getVelocity()[0] > 0 ? 1 : -1;
        if(Math.abs(object.getVelocity()[0]) < 0.01) object.setVelocity(new double[]{0, object.getVelocity()[1]});
        else if(Math.abs(object.getVelocity()[0]) > maxVelocity){
            object.setVelocity(new double[]{maxVelocity * moveDirection, object.getVelocity()[1]}); // Set the velocity to the max velocity
        }
        if(Math.abs(object.getVelocity()[1]) < 0.01) object.setVelocity(new double[]{object.getVelocity()[0], 0}); // Stop the object if it is moving too slowly
    }
}
