package src.Objects;
// import awt and arraylist
import java.awt.*;
import java.util.ArrayList;

import src.GameManager;
import src.Components.*;

// game object class
public class GameObject{
    // variables for movement, rendering, and collision
    private int x;
    private int y;
    private double[] velocity;
    private Polygon[] shapes;
    private Polygon[] movedShapes;
    private Color[] colors;
    private boolean shapeQ;
    private Image image;
    private boolean hasGravity;
    public boolean isPhysicsObject;
    private GameManager gameManager;
    private boolean isCollisionObject;

    private Rectangle hitboxShape;
    private Rectangle hitbox;
    private ArrayList<ObjectComponent> components = new ArrayList<>();

    // constructor
    public GameObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, boolean isCollisionObject, GameManager gameManager) {
        // set the variables
        this.x = x;
        this.y = y;
        this.colors = colors;
        this.shapes = shapes;
        this.movedShapes = new Polygon[shapes.length];
        this.shapeQ = shapeQ;
        this.image = image;
        this.hasGravity = hasGravity;
        this.isPhysicsObject = isPhysicsObject;
        this.gameManager = gameManager;
        this.velocity = new double[]{0, 0};
        this.isCollisionObject = isCollisionObject;
        // if the game object is a shape, calculate the bounding box and move the shapes
        if(shapeQ){
            this.hitboxShape = new Rectangle();
            calculateBoundingBox();
            this.hitbox = moveHitbox(x, y);
        }
        // else set the hitbox to the image size
        else this.hitbox = new Rectangle(x, y, image != null ? image.getWidth(null) : 0, image != null ? image.getHeight(null) : 0);

        if(hasGravity){
            addComponent(new CollisionComponent(this), -1); // Add the collision component
            addComponent(new GravityComponent(this), -1); // Add the gravity component
        }
        if(isPhysicsObject){
            addComponent(new ForceRegulationComponent(this, 1), -1); // Add the physics component
        }
    }

    // add a component to the components list
    public void addComponent(ObjectComponent component, int index) {
        if(index == -1) components.add(component);
        else components.add(index, component);
    }
    
    public <T extends ObjectComponent> T getComponent(Class<T> componentType) {
        for (ObjectComponent component : components) {
            if (componentType.isInstance(component)) {
                return componentType.cast(component);
            }
        }
        return null;
    }

    public void removeComponent(ObjectComponent component) {
        components.remove(component);
    }

    // method to draw the game object
    public void draw(Graphics2D g) {
        // if the game object is a shape, draw the shape, otherwise draw the image
        if (shapeQ) {
            moveShapes(); // Update the movedShapes array before drawing
            // Draw the shapes
            for (Polygon shape : movedShapes) {
                g.setColor(colors[0]);
                g.fillPolygon(shape);
            }
        } else {
            g.drawImage(image, x, y, null); // Draw the image
        }
    }

    // update method
    public void update(){
        // create a temporary arraylist of components to avoid concurrent modification
        ArrayList<ObjectComponent> tempComponents = new ArrayList<ObjectComponent>();
        for (ObjectComponent component : components) {
            tempComponents.add(component);
        }

        // loop through the components and update them
        for (ObjectComponent component : tempComponents) {
            component.update();
        }

        move();
    }
    
    // move the game object
    public void move(){
        x += velocity[0];
        y += velocity[1];
        hitbox = moveHitbox(x, y);
    }

    // method to move the shapes of the game object
    private void moveShapes() {
        for (int i = 0; i < shapes.length; i++) {
            movedShapes[i] = changePos(this.x, this.y, shapes[i]);
        }
    }

    // getter method for the hitbox
    public Rectangle getHitbox(){
        return hitbox;
    }

    // method to move a polygon a certain distance
    private Polygon changePos(int dx, int dy, Polygon polygon) {
        // Create a new polygon with the same points as the given polygon
        Polygon movedPolygon = new Polygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
        // loop through the points and move them
        for (int i = 0; i < movedPolygon.npoints; i++) {
            movedPolygon.xpoints[i] += dx;
            movedPolygon.ypoints[i] += dy;
        }
        // return the moved polygon
        return movedPolygon;
    }

    // calculate the bounding box of the game object
    private void calculateBoundingBox() {
        // if the game object has no shapes, set the bounding box to 0
        if (shapes.length == 0) {
            hitboxShape = new Rectangle(x, y, 0, 0);
            return;
        }
        // set the min and max x and y values to the max and min integer values
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
    
        // loop through the points of the shapes and set the min and max x and y values
        for (Polygon shape : shapes) {
            for (int i = 0; i < shape.npoints; i++) {
                int x = shape.xpoints[i];
                int y = shape.ypoints[i];
    
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
            }
        }
    
        // calculate the width and height of the bounding box
        int width = maxX - minX;
        int height = maxY - minY;
    
        // set the bounding box
        hitboxShape.setBounds(minX, minY, width, height);
    }

    // move the game object's hitbox
    private Rectangle moveHitbox(int dx, int dy) {
        return new Rectangle(hitboxShape.x + dx, hitboxShape.y + dy, hitboxShape.width, hitboxShape.height);
    }

    // getters and setters
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean isShapeQ(){
        return shapeQ;
    }

    public Polygon[] getShapes(){
        return shapes;
    }

    public boolean hasGravity(){
        return hasGravity;
    }

    public void setHasGravity(boolean hasGravity){
        this.hasGravity = hasGravity;
    }

    public GameManager getGameManager(){
        return this.gameManager;
    }

    public double[] getVelocity(){
        return this.velocity;
    }

    public boolean isPhysicsObject(){
        return this.isPhysicsObject;
    }

    public void setVelocity(double[] velocity){
        this.velocity = velocity;
    }

    public void addForce(double[] force){
        this.velocity[0] += force[0];
        this.velocity[1] += force[1];
    }

    public ArrayList<ObjectComponent> getComponents(){
        return this.components;
    }

    public boolean isCollisionObject(){
        return this.isCollisionObject;
    }

    public Color[] getColors(){
        return this.colors;
    }

    public boolean getShapeQ(){
        return this.shapeQ;
    }

    public Image getImage(){
        return this.image;
    }

    public String ClassName(){
        return this.getClass().getName();
    }
}