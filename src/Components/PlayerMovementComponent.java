package src.Components;

// Import necessary libraries
import src.GameManager;
import src.Objects.GameObject;

// PlayerMovementComponent class
public class PlayerMovementComponent implements ObjectComponent{
    // Variables for objects and movement
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

    // Constructor
    public PlayerMovementComponent(GameObject player){
        this.player = player;
        gameManager = player.getGameManager();
    }

    // Update method
    @Override
    public void update(){
        // Get movement direction and jump and collision component
        moveDirection = gameManager.getMoveDirection();
        boolean jump = gameManager.getJump();
        CollisionComponent collisionComponent = player.getComponent(CollisionComponent.class);

        // move player
        player.addForce(new double[]{moveDirection * moveSpeed, 0});

        // jump player if jump is true and player is on the ground
        if(jump && collisionComponent.collidingBottom()){
            isJumping = true;
            gameManager.setJump(false);
        }

        // if player is jumping, move player up until jump height is reached
        if(isJumping){
            if(initialYPosition == 0.0){
                initialYPosition = player.getY();
            }
            double currentYPosition = player.getY();
            double jumpDistance = initialYPosition - currentYPosition;
            // if player is not at jump height, move player up
            if(jumpDistance < jumpHeight-30 && !(collisionComponent.collidingTop())){
                double jumpProgress = jumpDistance / jumpHeight;
                double jumpVelocity = jumpSpeed * (1 - jumpProgress);
                player.setVelocity(new double[]{player.getVelocity()[0], jumpVelocity});
            }
            // if player is at jump height, stop jumping
            else{
                isJumping = false;
                initialYPosition = 0.0;
            }
        }

        // apply drag to player
        player.setVelocity(new double[]{player.getVelocity()[0] * GetDrag(), player.getVelocity()[1] * GetDrag()});
    }

    // Get drag method
    private double GetDrag(){
        if(player.getComponent(CollisionComponent.class).collidingBottom()) return groundDrag;
        else return airDrag;
    }
}
