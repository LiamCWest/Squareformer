package src.Components;

// Import necessary libraries
import src.Objects.Objective;

// ObjectiveComponent class
public class ObjectiveComponent implements ObjectComponent{
    // Declare private Objective object
    private Objective objective;

    // Constructor
    public ObjectiveComponent(Objective objective) {
        this.objective = objective;
    }

    // Update method
    @Override
    public void update() {
        // Check if player has collided with objective
        if(objective.getHitbox().intersects(objective.getGameManager().getPlayer().getHitbox())){
            objective.getGameManager().nextLevel(); // Go to next level
        }
    }
}
