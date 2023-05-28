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
            boolean rightXCheck = hitbox.getMaxX()+2 > otherHitbox.getMinX() && !(hitbox.getMinX()+1 >= otherHitbox.getMaxX());
            boolean leftXCheck = hitbox.getMinX() < otherHitbox.getMaxX() && !(hitbox.getMaxX() <= otherHitbox.getMinX());
            boolean yCheck = isBetween(hitbox.getMinY(), hitbox.getMaxY(), otherHitbox.getMinY(), otherHitbox.getMaxY());
            
            boolean colRight = yCheck && rightXCheck && velocity[0] > 0;
            boolean colLeft = yCheck && leftXCheck && velocity[0] < 0;
            boolean colBottom = hitbox.getMaxY()+1 > otherHitbox.getMinY() && !(hitbox.getMaxY() > otherHitbox.getMaxY()) && !(colLeft || colRight) && velocity[1] > 0;
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

        handleAllCollisions(leftCollisions, rightCollisions, topCollisions, bottomCollisions);
    }

    private boolean isBetween(double min1, double max1, double min2, double max2){
        return (min1 < max2 && min1 > min2) || (max1 > min2 && max1 < max2);
    }

    public void handleAllCollisions(ArrayList<GameObject> leftCollisions, ArrayList<GameObject> rightCollisions, ArrayList<GameObject> topCollisions, ArrayList<GameObject> bottomCollisions){
        ArrayList<GameObject>[] collisions = new ArrayList[4];
        collisions[0] = leftCollisions;
        collisions[1] = rightCollisions;
        collisions[2] = topCollisions;
        collisions[3] = bottomCollisions;

        for(int i = 0; i < collisions.length; i++){
            ArrayList<GameObject> collideArray = collisions[i];
            for(GameObject object : collideArray){
                handleCollision(object, i);
            }
        }
    }

    private void handleCollision(GameObject colObject, int side){
        switch(side){
            case 0:
            case 1:
                object.setVelocity(new double[]{0, object.getVelocity()[1]});
                break;
            case 2:
            case 3:
                object.setVelocity(new double[]{object.getVelocity()[0], 0});
                break;
        }
    }

    public boolean collidesWith(GameObject object, GameObject other) {
        // Get the hitboxes of both objects
        Rectangle hitbox0 = object.getHitbox();
        Rectangle hitbox1 = new Rectangle(hitbox0.x + (int) object.getVelocity()[0], hitbox0.y + (int) object.getVelocity()[1], hitbox0.width+1, hitbox0.height + 1);
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
