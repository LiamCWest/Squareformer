import java.awt.*;
import java.awt.geom.Line2D;

public class Grapple {
    private GameObject holder;
    private GameManager gameManager;
    private Point mousePos;
    private int[] grapplePos = new int[2];

    public Grapple(GameObject holder, GameManager gameManager){
        this.holder = holder;
        this.gameManager = gameManager;
    }

    public void startGrapple(){
        double angle = getAngle();

        GameObject hitObject = raycast(grapplePos, angle);
        System.out.println(hitObject);
    }

    public void draw(Graphics2D g){
        g.setColor(Color.GRAY);
        Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(7));

        double angle = getAngle();

        int xMod = (int)(Math.cos(angle)*15);
        int yMod = (int)(Math.sin(angle)*15);
        
        g.drawLine(grapplePos[0], grapplePos[1], grapplePos[0]+xMod, grapplePos[1]+yMod);
        g.setStroke(stroke);
    }

    private double getAngle(){
        mousePos = MouseInfo.getPointerInfo().getLocation();

        grapplePos[0] = holder.getX()+(int)holder.getHitbox().getWidth();
        grapplePos[1] = holder.getY()+(int)holder.getHitbox().getHeight()/4;
        
        return Math.atan2(mousePos.getY()-grapplePos[1], mousePos.getX()-grapplePos[0])*5;
    }

    // raycast method taking in a point and a direction as an angle
    public GameObject raycast(int[] point, double angle){
        // create a new point with the angle and a large distance
        int[] newPoint = new int[]{(int)(point[0]+Math.cos(angle)*1000), (int)(point[1]+Math.sin(angle)*1000)};
        // create a new line with the point and the new point
        Line2D line = new Line2D.Double(point[0], point[1], newPoint[0], newPoint[1]);
        // loop through all the game objects
        for(GameObject gameObject : gameManager.getGameObjects()){
            // if the game object is not the holder and the game object is a shape
            if(gameObject != holder && gameObject.isShapeQ()){
                // loop through all the shapes in the game object
                for(Polygon shape : gameObject.getShapes()){
                    // loop through all the points in the shape
                    for(int i = 0; i < shape.npoints; i++){
                        // create a new line with the current point and the next point
                        Line2D line2 = new Line2D.Double(shape.xpoints[i], shape.ypoints[i], shape.xpoints[(i+1)%shape.npoints], shape.ypoints[(i+1)%shape.npoints]);
                        // if the line intersects the shape, return the game object
                        if(line.intersectsLine(line2)) return gameObject;
                    }
                }
            }
        }
        // return null if no game object is found
        return null;
    }
}
