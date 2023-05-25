package src.Components;

import java.awt.*;
import java.util.ArrayList;

import src.GameObject;

public class CollisionComponent implements ObjectComponent{
    private GameObject object;
    private ArrayList<GameObject> leftCollisions = new ArrayList<>();
    private ArrayList<GameObject> rightCollisions = new ArrayList<>();
    private ArrayList<GameObject> topCollisions = new ArrayList<>();
    private ArrayList<GameObject> bottomCollisions = new ArrayList<>();

    public CollisionComponent(GameObject object) {
        this.object = object;
    }

    @Override
    public void update() {
        // Check for collisions with other objects
        ArrayList<GameObject> collisions = new ArrayList<>();
        for (GameObject other : object.getGameManager().getGameObjects()) {
            if (other != object && collidesWith(object, other)) {
                collisions.add(other);
            }
        }

        // Determine which side of the object is colliding with each other object
        double[] velocity = object.getVelocity();
        Rectangle hitbox = object.getHitbox();
        leftCollisions = new ArrayList<>();
        rightCollisions = new ArrayList<>();
        topCollisions = new ArrayList<>();
        bottomCollisions = new ArrayList<>();

        for (GameObject other : collisions) {
            Rectangle otherHitbox = other.getHitbox();
            boolean colRight = hitbox.getMaxX() > otherHitbox.getMinX() && velocity[0] > 0;
            boolean colLeft = hitbox.getMinX() < otherHitbox.getMaxX() && velocity[0] < 0;
            boolean colBottom = hitbox.getMaxY() > otherHitbox.getMinY() && velocity[1] > 0;
            boolean colTop = hitbox.getMinY() < otherHitbox.getMaxY() && velocity[1] < 0;

            if (colLeft) {
                leftCollisions.add(other);
            }
            if (colRight) {
                rightCollisions.add(other);
            }
            if (colTop) {
                topCollisions.add(other);
            }
            if (colBottom) {
                bottomCollisions.add(other);
            }
        }

        handleCollision(leftCollisions, rightCollisions, topCollisions, bottomCollisions);
    }

    public void handleCollision(ArrayList<GameObject> leftCollisions, ArrayList<GameObject> rightCollisions, ArrayList<GameObject> topCollisions, ArrayList<GameObject> bottomCollisions){

    }

    public boolean collidesWith(GameObject object, GameObject other) {
        // Get the hitboxes of both objects
        Rectangle hitbox1 = object.getHitbox();
        Rectangle hitbox2 = other.getHitbox();

        // Check if the hitboxes intersect
        return hitbox1.intersects(hitbox2);
    }

    public boolean collidingTop() {
        return topCollisions.size() > 0;
    }

    public boolean collidingBottom() {
        return bottomCollisions.size() > 0;
    }

    public boolean collidingLeft() {
        return leftCollisions.size() > 0;
    }

    public boolean collidingRight() {
        return rightCollisions.size() > 0;
    }
}
