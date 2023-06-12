package src.Components;

// Imports
import src.Objects.Player;
import src.Objects.ActiveObjects.ActiveObject;

// EnergyChangeComponent class
public class EnergyChangeComponent implements ObjectComponent{
    // Variable
    private ActiveObject activeObject;

    // Constructor
    public EnergyChangeComponent(ActiveObject activeObject) {
        this.activeObject = activeObject;
    }

    // Update method
    @Override
    public void update() {
        Player player = activeObject.getGameManager().getPlayer();
        // Check for collisions with the player
        if(activeObject.getHitbox().intersects(player.getHitbox())){
            // Change the player's energy
            if(player.getEnergy() < 10) {
                player.changeEnergy(activeObject.getChange());
                
                // Remove the object if it has been used up
                if(activeObject.getChange() > 0){
                    activeObject.setVisable(false);
                    activeObject.removeComponent(this);
                }
            }
            
        }
    }
}
