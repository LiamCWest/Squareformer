package src.Components;

import src.Objects.ActiveObjects.ActiveObject;

public class HealthChangeComponent implements ObjectComponent{
    private ActiveObject activeObject;
    private double waitTime = 0;

    public HealthChangeComponent(ActiveObject activeObject) {
        this.activeObject = activeObject;
    }

    @Override
    public void update() {
        if(activeObject.getHitbox().intersects(activeObject.getGameManager().getPlayer().getHitbox())){
            double health = activeObject.getGameManager().getPlayer().getHealth();

            if((activeObject.getChange() < 0) && (health > 0) && (waitTime == 0)){
                activeObject.getGameManager().getPlayer().changeHealth(activeObject.getChange());
                waitTime = 1.0;
            }
            else if(waitTime > 0){
                waitTime -= 0.1;
            }
            else if(waitTime < 0){
                waitTime = 0;
            }

            health = activeObject.getGameManager().getPlayer().getHealth();
            
            if(activeObject.getChange() > 0){
                activeObject.getGameManager().getPlayer().changeHealth(activeObject.getChange());
                activeObject.setVisable(false);
                activeObject.removeComponent(this);
            }

            if(health < 0){
                activeObject.getGameManager().endGame();
            }
            else if(health > 20){
                activeObject.getGameManager().getPlayer().setHealth(20);
            }

        }
    }
}
