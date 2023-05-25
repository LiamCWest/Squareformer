package src.Components;

import src.GameObject;

public class GravityComponent implements ObjectComponent{
    private double gravity = 0.2;
    private GameObject object;
    private boolean isGrounded = false;
    private double terminalVelocity = 1;

    public GravityComponent(GameObject object){
        this.object = object;
    }

    @Override
    public void update(){
        isGrounded = object.getComponent(CollisionComponent.class).collidingBottom();

        if(!isGrounded){
            object.addForce(new double[]{0, gravity});
            if(object.getVelocity()[1] > terminalVelocity) object.setVelocity(new double[]{object.getVelocity()[0], terminalVelocity});
        }
        else{
            object.setVelocity(new double[]{object.getVelocity()[0], 0});
        }
    }
}