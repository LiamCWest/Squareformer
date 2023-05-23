import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Grapple {
    private GameObject holder;
    private GameManager gameManager;
    private Point mousePos;
    private int[] grapplePos = new int[2];
    private Line2D line = new Line2D.Double();

    public Grapple(GameObject holder, GameManager gameManager){
        this.holder = holder;
        this.gameManager = gameManager;
    }

    public void startGrapple(){
        double angle = getAngle();

        Object[] hit = raycast(grapplePos, angle);
        GameObject hitObject = (GameObject)hit[0];
        double[] hitPoint = (double[])hit[1];
        System.out.println(hitObject != null ? hitObject : "null");
    }

    public void draw(Graphics2D g){
        g.setColor(Color.GRAY);
        Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(7));

        double angle = getAngle();
        int length = 15;

        int xMod = (int)(Math.cos(angle)*length);
        int yMod = (int)(Math.sin(angle)*length);
        
        g.draw(line);
        g.drawLine(grapplePos[0], grapplePos[1], grapplePos[0]+xMod, grapplePos[1]+yMod);
        g.setStroke(stroke);
    }

    private double getAngle(){
        mousePos = MouseInfo.getPointerInfo().getLocation();

        grapplePos[0] = holder.getX()+(int)holder.getHitbox().getWidth();
        grapplePos[1] = holder.getY()+(int)holder.getHitbox().getHeight()/4;
        
        return Math.atan2(mousePos.getY()-grapplePos[1], mousePos.getX()-grapplePos[0]);
    }

    // raycast method taking in a point and a direction as an angle
    public Object[] raycast(int[] point, double angle){
        // create a new point with the angle and a large distance
        int[] newPoint = new int[]{(int)(point[0]+Math.cos(angle)*1000), (int)(point[1]+Math.sin(angle)*1000)};
        // create a new line with the point and the new point
        line = new Line2D.Double(point[0], point[1], newPoint[0], newPoint[1]);
        // list of hit objects
        ArrayList<GameObject> hitObjects = new ArrayList<GameObject>();
        // loop through all the game objects
        for(GameObject gameObject : gameManager.getGameObjects()){
            // if the game object is not the holder and the game object is a shape
            if(gameObject != holder && gameObject.isShapeQ()){
                if(line.intersects(gameObject.getHitbox())) {
                    hitObjects.add(gameObject);
                }
            }
        }
        GameObject closestObject = null;
        double[] closestPoint = null;

        // if there are hit objects
        if(!(hitObjects.isEmpty())){
            // get the closest hit object
            for(GameObject gameObject : hitObjects){
                for(Polygon shape : gameObject.getShapes()){
                    for(int i = 0; i < shape.npoints; i++){
                        double[][] shapeLine = new double[][]{{shape.xpoints[i]+gameObject.getX(), shape.ypoints[i]+ gameObject.getY()}, {shape.xpoints[(i+1)%shape.npoints] + gameObject.getX(), shape.ypoints[(i+1)%shape.npoints] + gameObject.getY()}};
                        double[] intersection = calculateIntersection(shapeLine, new double[][]{{point[0], point[1]}, {newPoint[0], newPoint[1]}});
                        if(intersection != null){
                            if(closestPoint == null || Math.sqrt(Math.pow(intersection[0]-point[0], 2)+Math.pow(intersection[1]-point[1], 2)) < Math.sqrt(Math.pow(closestPoint[0]-point[0], 2)+Math.pow(closestPoint[1]-point[1], 2))){
                                closestPoint = intersection;
                                closestObject = gameObject;
                                line = new Line2D.Double(point[0], point[1], intersection[0], intersection[1]);
                                System.out.println("new" + " " + line.getX2() + " " + line.getY2());
                            }
                        }
                    }
                }
            }
            System.out.println("final" + " " + line.getX2() + " " + line.getY2());
            // return the closest hit object
            return new Object[]{closestObject, closestPoint};
        }
        // return null if no game object is found
        return null;
    }

    public static double[] calculateIntersection(double[][] line1, double[][] line2) {
        double x1 = line1[0][0];
        double y1 = line1[0][1];
        double x2 = line1[1][0];
        double y2 = line1[1][1];
    
        double x3 = line2[0][0];
        double y3 = line2[0][1];
        double x4 = line2[1][0];
        double y4 = line2[1][1];
    
        double denominator = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));
    
        if (denominator == 0) {
            // Lines are parallel, no intersection point
            return null;
        }
    
        double intersectionX = (((x1 * y2) - (y1 * x2)) * (x3 - x4) - (x1 - x2) * ((x3 * y4) - (y3 * x4))) / denominator;
        double intersectionY = (((x1 * y2) - (y1 * x2)) * (y3 - y4) - (y1 - y2) * ((x3 * y4) - (y3 * x4))) / denominator;
    
        return new double[]{intersectionX, intersectionY};
    }
    
}