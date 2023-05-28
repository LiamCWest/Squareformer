package src.Components;

import src.Objects.GameObject;

public class ForceRegulationComponent implements ObjectComponent{
    private double maxVelocity;
    private GameObject object;

    public ForceRegulationComponent(GameObject object, double maxVelocity){
        this.object = object;
        this.maxVelocity = maxVelocity;
    }

    @Override
    public void update(){
        int moveDirection = object.getVelocity()[0] > 0 ? 1 : -1;
        if(Math.abs(object.getVelocity()[0]) < 0.01) object.setVelocity(new double[]{0, object.getVelocity()[1]});
        else if(Math.abs(object.getVelocity()[0]) > maxVelocity){
            object.setVelocity(new double[]{maxVelocity * moveDirection, object.getVelocity()[1]});
        }
        if(Math.abs(object.getVelocity()[1]) < 0.01) object.setVelocity(new double[]{object.getVelocity()[0], 0});
    }
}
