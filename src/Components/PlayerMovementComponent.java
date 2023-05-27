package src.Components;

import src.GameManager;
import src.GameObject;

public class PlayerMovementComponent implements ObjectComponent{
    private GameObject player;
    private int moveDirection = 0;
    private double airDrag = 0.9;
    private double groundDrag = 0.8;
    private double moveSpeed = 0.4;
    private GameManager gameManager;
    private boolean isJumping = false;
    private double jumpSpeed = -2;
    private double jumpHeight = 220.0;
    private double initialYPosition = 0.0;


    public PlayerMovementComponent(GameObject player){
        this.player = player;
        gameManager = player.getGameManager();
    }

    @Override
    public void update(){
        moveDirection = gameManager.getMoveDirection();
        boolean jump = gameManager.getJump();
        CollisionComponent collisionComponent = player.getComponent(CollisionComponent.class);

        player.addForce(new double[]{moveDirection * moveSpeed, 0});
        if(jump && collisionComponent.collidingBottom()){
            isJumping = true;
            gameManager.setJump(false);
        }

        if(isJumping){
            if(initialYPosition == 0.0){
                initialYPosition = player.getY();
            }
            double currentYPosition = player.getY();
            double jumpDistance = initialYPosition - currentYPosition;
            if(jumpDistance < jumpHeight-30 && !(collisionComponent.collidingTop())){
                double jumpProgress = jumpDistance / jumpHeight;
                double jumpVelocity = jumpSpeed * (1 - jumpProgress);
                player.setVelocity(new double[]{player.getVelocity()[0], jumpVelocity});
            }
            else{
                isJumping = false;
                initialYPosition = 0.0;
            }
        }

        player.setVelocity(new double[]{player.getVelocity()[0] * GetDrag(), player.getVelocity()[1] * GetDrag()});
    }

    private double GetDrag(){
        if(player.getComponent(CollisionComponent.class).collidingBottom()) return groundDrag;
        else return airDrag;
    }
}
