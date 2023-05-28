package src.Components;

import src.Objects.Objective;

public class ObjectiveComponent implements ObjectComponent{
    private Objective objective;

    public ObjectiveComponent(Objective objective) {
        this.objective = objective;
    }

    @Override
    public void update() {
        if(objective.getHitbox().intersects(objective.getGameManager().getPlayer().getHitbox())){
            objective.getGameManager().nextLevel();
        }
    }
}
