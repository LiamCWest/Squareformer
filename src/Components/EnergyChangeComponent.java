package src.Components;

import src.Objects.Player;
import src.Objects.ActiveObjects.ActiveObject;

public class EnergyChangeComponent implements ObjectComponent{
    private ActiveObject activeObject;

    public EnergyChangeComponent(ActiveObject activeObject) {
        this.activeObject = activeObject;
    }

    @Override
    public void update() {
        Player player = activeObject.getGameManager().getPlayer();
        if(activeObject.getHitbox().intersects(player.getHitbox())){
            if(player.getEnergy() < 10) {
                player.changeEnergy(activeObject.getChange());
                
                if(activeObject.getChange() > 0){
                    activeObject.setVisable(false);
                    activeObject.removeComponent(this);
                }
            }
            
        }
    }
}
