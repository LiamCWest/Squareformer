import java.awt.*;

public class Player extends GameObject {
    private double jumpHeight = 200;
    private double currentJumpHeight = 0;
    private boolean isJumping = false;
    private boolean isFalling = false;
    private double jumpForce = -3;
    private double moveForce = 8;
    private double dragOnGround = 0.9;
    private double dragInAir = 0.8;

    Player(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isMoveable, boolean isPhysicsObject, GameManager gameManager) {
        super(x, y, colors, shapes, shapeQ, image, hasGravity, isMoveable, isPhysicsObject, gameManager);
    }

    @Override
    public void move() {
        if (super.isGrounded()) {
            currentJumpHeight = 0;
            isFalling = false;
        }
        else if(!(isJumping)){
            isFalling = true;
        }

        if (isJumping) {
            if (currentJumpHeight < jumpHeight) {
                double jumpForceScaled = jumpForce * (1 - currentJumpHeight / jumpHeight);
                setVelocity(getVelocity()[0], (int) Math.round(jumpForceScaled));
                currentJumpHeight++;
            } else {
                isJumping = false;
                isFalling = true;
            }
        } else if (isFalling) {
            setVelocity(getVelocity()[0], 0.05);
        }

        if (Math.abs(getVelocity()[0]) > 0) {
            applyForce(-getVelocity()[0] * getDrag(), 0);
        }

        super.move();
    }

    private double getDrag() {
        return super.isGrounded() ? dragOnGround : dragInAir;
    }

    public void applyForce(double xForce, double yForce) {
        double[] velocity = getVelocity();
        velocity[0] += xForce;
        velocity[1] += yForce;
        setVelocity((int) Math.round(velocity[0]), (int) Math.round(velocity[1]));
    }

    public void jump() {
        if (super.isGrounded()) {
            isJumping = true;
            isFalling = false;
        }
    }

    public void moveForce(double xForce, double yForce) {
        if (!(super.isGrounded())) {
            xForce /= 2;
            yForce /= 2;
        }
        applyForce(moveForce * xForce, moveForce * yForce);
    }
}
