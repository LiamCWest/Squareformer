package src.Components;

// Imports
import src.Objects.ActiveObjects.ActiveObject;

// HealthChangeComponent class
public class HealthChangeComponent implements ObjectComponent{
    // Variables
    private ActiveObject activeObject;
    private double waitTime = 0;

    // Constructor
    public HealthChangeComponent(ActiveObject activeObject) {
        this.activeObject = activeObject;
    }

    // Update method
    @Override
    public void update() {
        // Check for collisions with the player
        if(activeObject.getHitbox().intersects(activeObject.getGameManager().getPlayer().getHitbox())){
            double health = activeObject.getGameManager().getPlayer().getHealth();

            // Change the player's health
            if((activeObject.getChange() < 0) && (health > 0) && (waitTime == 0)){
                activeObject.getGameManager().getPlayer().changeHealth(activeObject.getChange());
                waitTime = 1.0;
            }
            // Wait for a second before changing the player's health agains
            else if(waitTime > 0){
                waitTime -= 0.1;
            }
            else if(waitTime < 0){
                waitTime = 0;
            }

            health = activeObject.getGameManager().getPlayer().getHealth();
            // Remove the object if it has been used up            
            if(activeObject.getChange() > 0 && health < 10){
                activeObject.getGameManager().getPlayer().changeHealth(activeObject.getChange());
                activeObject.setVisable(false);
                activeObject.removeComponent(this);
            }

            // End the game if the player's health is too low
            if(health <= 0){
                activeObject.getGameManager().endGame();
            }
            // Set the player's health to 20 if it is too high
            else if(health > 10){
                activeObject.getGameManager().getPlayer().setHealth(10);
            }

        }
    }
}
