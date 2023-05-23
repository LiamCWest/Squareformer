import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Map;

public class Grapple {
    private GameObject holder;
    private GameManager gameManager;
    private Point mousePos;
    private int[] grapplePos = new int[2];
    Line2D grappleLine = new Line2D.Double();

    public Grapple(GameObject holder, GameManager gameManager){
        this.holder = holder;
        this.gameManager = gameManager;
    }

    public void startGrapple(){
        double angle = getAngle();

        Object[] hit = raycast(grapplePos, angle);
        GameObject hitObject = (GameObject)hit[0];
        double[] hitPoint = (double[])hit[1];
        grappleLine.setLine(grapplePos[0], grapplePos[1], hitPoint[0], hitPoint[1]);
        System.out.println(hit != null ? hitObject + " " + hitPoint[0] + " " + hitPoint[1] : "null");
    }

    public void draw(Graphics2D g){
        g.setColor(Color.GRAY);
        Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(7));

        double angle = getAngle();
        int length = 15;

        int xMod = (int)(Math.cos(angle)*length);
        int yMod = (int)(Math.sin(angle)*length);
        
        g.draw(grappleLine);
        g.drawLine(grapplePos[0], grapplePos[1], grapplePos[0]+xMod, grapplePos[1]+yMod);
        g.setStroke(stroke);
    }

    private double getAngle(){
        mousePos = MouseInfo.getPointerInfo().getLocation();

        grapplePos[0] = holder.getX()+(int)holder.getHitbox().getWidth();
        grapplePos[1] = holder.getY()+(int)holder.getHitbox().getHeight()/4;
        
        return Math.atan2(mousePos.getY()-grapplePos[1], mousePos.getX()-grapplePos[0]);
    }

    public Object[] raycast (int[] point, double angle){
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

                if (Math.sqrt(Math.pow(point[0]-hitPoint[0], 2)+Math.pow(point[1]-hitPoint[1], 2)) < Math.sqrt(Math.pow(point[0]-closestHit[0], 2)+Math.pow(point[1]-closestHit[1], 2))){
                    closestHit = hitPoint;
                    closestObject = object;
                }
            }

            return new Object[]{closestObject, closestHit};
        }else return new Object[]{null, null};
    }

    private boolean hitObject(double x, double y, Rectangle hitbox){
        return x >= hitbox.getX() && x <= hitbox.getX()+hitbox.getWidth() && y >= hitbox.getY() && y <= hitbox.getY()+hitbox.getHeight();
    }
}