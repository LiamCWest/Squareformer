package src.Components;

import src.GameObject;

public class PlayerMovementComponent implements ObjectComponent{
    private GameObject player;
    private int moveDirection = 0;
    private double maxSpeed = 1;
    private double airDrag = 0.9;
    private double groundDrag = 0.8;
    private double moveSpeed = 0.4;

    public PlayerMovementComponent(GameObject player){
        this.player = player;
    }

    @Override
    public void update(){
        moveDirection = player.getGameManager().getMoveDirection();
        player.addForce(new double[]{moveDirection * moveSpeed, 0});
        player.setVelocity(new double[]{player.getVelocity()[0] * GetDrag(), player.getVelocity()[1]});
        if(Math.abs(player.getVelocity()[0]) < 0.01) player.setVelocity(new double[]{0, player.getVelocity()[1]});
        else if(Math.abs(player.getVelocity()[0]) > maxSpeed){
            System.out.println("max speed reached " + player.getVelocity()[0] + " " + moveDirection);
            player.setVelocity(new double[]{maxSpeed * moveDirection, player.getVelocity()[1]});
        }
    }

    private double GetDrag(){
        if(player.getComponent(CollisionComponent.class).collidingBottom()) return groundDrag;
        else return airDrag;
    }
}
