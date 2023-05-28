package src.Components;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Map;

import src.GameManager;
import src.GameObject;

public class GrappleComponent implements ObjectComponent{
    private GameObject holder;
    private GameManager gameManager;
    private Point mousePos;
    // private GameObject hitObject;
    private double[] hitPoint;
    private boolean isGrappling = false;
    private double[] grapplePos = new double[2];
    private double grappleSpeed = 5;
    Line2D grappleLine = new Line2D.Double();

    public GrappleComponent(GameObject holder, GameManager gameManager){
        this.holder = holder;
        this.gameManager = gameManager;
    }

    public void startGrapple(){
        Point relativeMousePos = getMoisePoint();
        double angle = getAngle(new Point((int) grapplePos[0], (int) grapplePos[1]), relativeMousePos);

        Object[] hit = raycast(grapplePos, angle);
        // hitObject = (GameObject)hit[0];
        hitPoint = (double[])hit[1];
        if(hitPoint != null) isGrappling = true;
    }

    public void endGrapple(){
        isGrappling = false;
    }

    @Override
    public void update(){
        if(hitPoint != null){
            double angle = getAngle(new Point((int) grapplePos[0], (int) grapplePos[1]), new Point((int)hitPoint[0], (int)hitPoint[1]));
            double xDir = Math.cos(angle);
            double yDir = Math.sin(angle);

            if(isGrappling && getDistance(grapplePos, hitPoint) > 100){
                holder.addForce(new double[]{xDir*grappleSpeed, yDir*grappleSpeed*0.075});
            }
        }
    }

    public void draw(Graphics2D g){
        grapplePos[0] = holder.getX() + (int)holder.getHitbox().getWidth();
        grapplePos[1] = holder.getY() + (int)holder.getHitbox().getHeight() / 4;
        
        Point point2 = isGrappling ? new Point((int)hitPoint[0], (int)hitPoint[1]) : getMoisePoint();
        double angle = getAngle(new Point((int) grapplePos[0], (int) grapplePos[1]), point2);

        if(angle*(180/Math.PI) > 90 || angle*(180/Math.PI) < -90){
            grapplePos[0] = holder.getX();
        }
        
        int length = 15;
        
        int xMod = (int)(Math.cos(angle)*length);
        int yMod = (int)(Math.sin(angle)*length);
        
        Stroke stroke = g.getStroke();
        
        if(isGrappling){
            grappleLine.setLine(grapplePos[0], grapplePos[1], hitPoint[0], hitPoint[1]);

            g.setColor(Color.PINK);
            g.setStroke(new BasicStroke(3));
            g.draw(grappleLine);
        }

        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(7));
        g.drawLine((int) grapplePos[0], (int) grapplePos[1], (int) grapplePos[0]+xMod, (int) grapplePos[1]+yMod);
        g.setStroke(stroke);
    }

    private double getAngle(Point point1, Point point2) {
        double xDiff = point2.getX() - point1.getX();
        double yDiff = point2.getY() - point1.getY();

        return Math.atan2(yDiff, xDiff);
    }

    private Point getMoisePoint(){
        mousePos = MouseInfo.getPointerInfo().getLocation();
        Point gamePos = gameManager.getGameWindow().getLocationOnScreen();
        return new Point((int)(mousePos.getX() - gamePos.getX()), (int)(mousePos.getY() - gamePos.getY()));
    }

    public Object[] raycast (double[] point, double angle){
        ArrayList<Map<GameObject, double[]>> hitObjects = new ArrayList<Map<GameObject, double[]>>();

        double xDir = Math.cos(angle);
        double yDir = Math.sin(angle);

        for(GameObject object : gameManager.getGameObjects()){
            if(object != holder){
                double x = point[0];
                double y = point[1];
                boolean hit = false;

                for (int i = 0; i < 1000; i++){
                    if (!hitObject(x, y, object.getHitbox())){
                        x += xDir;
                        y += yDir;
                    }else {
                        hit = true;
                        break;
                    }
                }

                if (hit)hitObjects.add(Map.of(object, new double[]{x, y}));
            }
        }

        if (hitObjects.size() > 0){
            double[] closestHit = hitObjects.get(0).values().iterator().next();
            GameObject closestObject = hitObjects.get(0).keySet().iterator().next();

            for (Map<GameObject, double[]> hitObject : hitObjects){
                double[] hitPoint = hitObject.values().iterator().next();
                GameObject object = hitObject.keySet().iterator().next();

                if (getDistance(point, hitPoint) < getDistance(point, closestHit)){
                    closestHit = hitPoint;
                    closestObject = object;
                }
            }

            return new Object[]{closestObject, closestHit};
        }else return new Object[]{null, null};
    }

    private double getDistance(double[] point1, double[] point2){
        return Math.sqrt(Math.pow(point1[0]-point2[0], 2)+Math.pow(point1[1]-point2[1], 2));
    }

    private boolean hitObject(double x, double y, Rectangle hitbox){
        return x >= hitbox.getX() && x <= hitbox.getX()+hitbox.getWidth() && y >= hitbox.getY() && y <= hitbox.getY()+hitbox.getHeight();
    }

    public boolean isGrappling(){
        return isGrappling;
    }
}