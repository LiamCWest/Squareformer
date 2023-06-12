package src.Components;

// Imports
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Map;

import src.GameManager;
import src.Objects.*;

// GrappleComponent class
public class GrappleComponent implements ObjectComponent{
    // Variables
    private Player holder;
    private GameManager gameManager;
    private Point mousePos;
    private double[] hitPoint;
    private boolean isGrappling = false;
    private double[] grapplePos = new double[2];
    private double grappleSpeed = 5;
    Line2D grappleLine = new Line2D.Double();

    // Constructor
    public GrappleComponent(Player holder, GameManager gameManager){
        this.holder = holder;
        this.gameManager = gameManager;
    }

    // Start grappling
    public void startGrapple(){
        // Get the mouse position and the angle between the player and the mouse
        Point relativeMousePos = getMoisePoint();
        double angle = getAngle(new Point((int) grapplePos[0], (int) grapplePos[1]), relativeMousePos);

        // Raycast to find the closest object in the direction of the mouse
        Object[] hit = raycast(grapplePos, angle);
        hitPoint = (double[])hit[1];
        // If the raycast hit something, start grappling
        if(hitPoint != null) isGrappling = true;
    }

    // End grappling
    public void endGrapple(){
        isGrappling = false;
    }

    // Update method
    @Override
    public void update(){
        this.gameManager = holder.getGameManager();

        // If the player is grappling, pull them towards the hit object
        if(hitPoint != null){
            double angle = getAngle(new Point((int) grapplePos[0], (int) grapplePos[1]), new Point((int)hitPoint[0], (int)hitPoint[1]));
            double xDir = Math.cos(angle);
            double yDir = Math.sin(angle);

            if(isGrappling && getDistance(grapplePos, hitPoint) > 100){
                holder.addForce(new double[]{xDir*grappleSpeed, yDir*grappleSpeed*0.075});
                holder.changeEnergy(-0.005);
            }
        }

        // If the player has no energy left, end the grapple
        if(holder.getEnergy() <= 0){
            endGrapple();
            holder.setEnergy(0);
        }
    }

    // Draw the grapple
    public void draw(Graphics2D g){
        // Get the position of the grapple
        grapplePos[0] = holder.getX() + (int)holder.getHitbox().getWidth();
        grapplePos[1] = holder.getY() + (int)holder.getHitbox().getHeight() / 4;
        
        // Get the point that the grapple should be pointing to
        Point point2 = isGrappling ? new Point((int)hitPoint[0], (int)hitPoint[1]) : getMoisePoint();
        double angle = getAngle(new Point((int) grapplePos[0], (int) grapplePos[1]), point2);

        // If the mouse is behind the player, flip the grapple
        if(angle*(180/Math.PI) > 90 || angle*(180/Math.PI) < -90){
            grapplePos[0] = holder.getX();
        }
        
        int length = 15;
        
        int xMod = (int)(Math.cos(angle)*length);
        int yMod = (int)(Math.sin(angle)*length);
        
        Stroke stroke = g.getStroke();
        
        // Draw the grapple line if the player is grappling
        if(isGrappling){
            grappleLine.setLine(grapplePos[0], grapplePos[1], hitPoint[0], hitPoint[1]);

            g.setColor(Color.PINK);
            g.setStroke(new BasicStroke(3));
            g.draw(grappleLine);
        }

        // Draw the grapple
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(7));
        g.drawLine((int) grapplePos[0], (int) grapplePos[1], (int) grapplePos[0]+xMod, (int) grapplePos[1]+yMod);
        g.setStroke(stroke);
    }

    // Get the angle between two points
    private double getAngle(Point point1, Point point2) {
        double xDiff = point2.getX() - point1.getX();
        double yDiff = point2.getY() - point1.getY();

        return Math.atan2(yDiff, xDiff);
    }

    // get the mouse position relative to the game window
    private Point getMoisePoint(){
        mousePos = MouseInfo.getPointerInfo().getLocation();
        Point gamePos = gameManager.getGameWindow().getLocationOnScreen();
        return new Point((int)(mousePos.getX() - gamePos.getX()), (int)(mousePos.getY() - gamePos.getY()));
    }

    // Raycast to find the closest object in the direction of the mouse
    public Object[] raycast (double[] point, double angle){
        ArrayList<Map<GameObject, double[]>> hitObjects = new ArrayList<Map<GameObject, double[]>>();

        double xDir = Math.cos(angle);
        double yDir = Math.sin(angle);

        // Loop through all the objects in the game
        for(GameObject object : gameManager.getGameObjects()){
            // If the object is not the player and is a collision object, raycast to it
            if(object != holder && object.isCollisionObject()){
                double x = point[0];
                double y = point[1];
                boolean hit = false;

                // Raycast 1000 times to find the closest object
                for (int i = 0; i < 1000; i++){
                    if (!hitObject(x, y, object.getHitbox())){
                        x += xDir;
                        y += yDir;
                    }else {
                        hit = true;
                        break;
                    }
                }

                // If the raycast hit the object, add it to the list of hit objects
                if (hit)hitObjects.add(Map.of(object, new double[]{x, y}));
            }
        }

        // If there are any hit objects, find the closest one
        if (hitObjects.size() > 0){
            double[] closestHit = hitObjects.get(0).values().iterator().next();
            GameObject closestObject = hitObjects.get(0).keySet().iterator().next();

            // Loop through all the hit objects
            for (Map<GameObject, double[]> hitObject : hitObjects){
                double[] hitPoint = hitObject.values().iterator().next();
                GameObject object = hitObject.keySet().iterator().next();

                // If the object is closer than the current closest object, set it as the closest object
                if (getDistance(point, hitPoint) < getDistance(point, closestHit)){
                    closestHit = hitPoint;
                    closestObject = object;
                }
            }

            // Return the closest object and the point that it was hit at
            return new Object[]{closestObject, closestHit};
        }else return new Object[]{null, null}; // If no objects were hit, return null
    }

    // Get the distance between two points
    private double getDistance(double[] point1, double[] point2){
        return Math.sqrt(Math.pow(point1[0]-point2[0], 2)+Math.pow(point1[1]-point2[1], 2));
    }

    // Check if a point is inside a rectangle
    private boolean hitObject(double x, double y, Rectangle hitbox){
        return x >= hitbox.getX() && x <= hitbox.getX()+hitbox.getWidth() && y >= hitbox.getY() && y <= hitbox.getY()+hitbox.getHeight();
    }

    // isGrappling getter
    public boolean isGrappling(){
        return isGrappling;
    }
}