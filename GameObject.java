// import awt and arraylist
import java.awt.*;
import java.util.ArrayList;

// game object class
public class GameObject{
    // variables for movement, rendering, and collision
    private int x;
    private int y;
    public double[] velocity;
    private Polygon[] shapes;
    private Polygon[] movedShapes;
    private Color[] colors;
    private boolean shapeQ;
    private Image image;
    private boolean hasGravity;
    private boolean isMoveable;
    public boolean isPhysicsObject;
    private GameManager gameManager;
    private ArrayList<GameObject> collidingObjects;
    private double dragOnGround = 0.9;
    private double dragInAir = 0.8;

    private Rectangle hitboxShape;
    private Rectangle hitbox;

    // constructors
    public GameObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isMoveable, boolean isPhysicsObject, GameManager gameManager) {
        // set the variables
        this.x = x;
        this.y = y;
        this.colors = colors;
        this.shapes = shapes;
        this.movedShapes = new Polygon[shapes.length];
        this.shapeQ = shapeQ;
        this.image = image;
        this.hasGravity = hasGravity;
        this.isMoveable = isMoveable;
        this.isPhysicsObject = isPhysicsObject;
        this.gameManager = gameManager;
        this.velocity = new double[]{0, 0};
        this.collidingObjects = new ArrayList<GameObject>();
        // if the game object is a shape, calculate the bounding box and move the shapes
        if(shapeQ){
            this.hitboxShape = new Rectangle();
            calculateBoundingBox();
            this.hitbox = moveHitbox(x, y);
        }
        // else set the hitbox to the image size
        else this.hitbox = new Rectangle(x, y, image != null ? image.getWidth(null) : 0, image != null ? image.getHeight(null) : 0);
    }

    // method to draw the game object
    public void draw(Graphics g) {
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

    // method to move the shapes of the game object
    private void moveShapes() {
        for (int i = 0; i < shapes.length; i++) {
            movedShapes[i] = changePos(this.x, this.y, shapes[i]);
        }
    }

    // method to move the game object
    public void move(){
        // apply drag to the game object
        if (Math.abs(getVelocity()[0]) > 0) {
            applyForce(-getVelocity()[0] * getDrag(), 0);
        }

        collidingObjects = new ArrayList<GameObject>(); // Reset the colliding objects array list
        // if the game object is moveable and colliding, apply force to the game object based on the object colliding with it
        if(isMoveable && isColliding()){
            for(GameObject object : collidingObjects){
                // check if the object is a physics object and apply force only if it is
                if(object.isPhysicsObject){
                    System.out.println(object.getVelocity()[0] + " " + object.getVelocity()[1]);
                    applyForce(object.getVelocity()[0], object.getVelocity()[1]);
                }
            }
        }

        // if the game object is not on the ground and it should be effected by gravity, apply gravity to the game object
        if(!(isGrounded())){
            if(hasGravity) velocity[1] += 1;
        }
        // if the game object is on the ground and moving down, set the y velocity to 0
        else if (velocity[1] > 0){
            velocity[1] = 0;
        }

        // if the game object is colliding with the top of another object and moving up, set the y velocity to 0
        if(collidingUp() && velocity[1] < 0){
            velocity[1] = 0;
        }
        // if the game object is colliding with the left or right of another object and moving left or right, set the x velocity to 0
        if((collidingLeft() && velocity[0] < 0) || (collidingRight() && velocity[0] > 0)){
            velocity[0] = 0;
        }
        
        // move the game object
        this.x += velocity[0];
        this.y += velocity[1];
        this.hitbox = moveHitbox(this.x, this.y); // Update the hitbox
    }

    // seter method for the velocity
    public void setVelocity(double x, double y){
        this.velocity = new double[]{x, y};
    }

    // getter method for the velocity
    public double[] getVelocity(){
        return velocity;
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

    // get drag based on if the game object is on the ground or not
    private double getDrag() {
        return isGrounded() ? dragOnGround : dragInAir;
    }
    
    // apply force to the game object
    public void applyForce(double xForce, double yForce) {
        double[] velocity = getVelocity();
        velocity[0] += xForce;
        velocity[1] += yForce;
        setVelocity((int) Math.round(velocity[0]), (int) Math.round(velocity[1]));
    }

    // move the game object's hitbox
    private Rectangle moveHitbox(int dx, int dy) {
        return new Rectangle(hitboxShape.x + dx, hitboxShape.y + dy, hitboxShape.width, hitboxShape.height);
    }

    //colllision
    public boolean collision(int[] modifier){
        // new hitbox with the velocity and modifier
        Rectangle checkBox = new Rectangle(hitbox.x + (int)velocity[0] + modifier[0], hitbox.y + (int)velocity[1] + modifier[1], hitbox.width + modifier[2], hitbox.height + modifier[3]);
        // loop through the game objects and check if the hitbox intersects with any of them
        for(GameObject gameObject : gameManager.getGameObjects()){
            if(gameObject != this){
                double[] objectVelocity = gameObject.getVelocity();
                Rectangle objectHitBox = gameObject.getHitbox();
                Rectangle objectCheckBox = new Rectangle(objectHitBox.x + (int)objectVelocity[0], objectHitBox.y + (int)objectVelocity[1], objectHitBox.width, objectHitBox.height);
                if(objectCheckBox.intersects(checkBox)){
                    collidingObjects.add(gameObject); // Add the object to the colliding objects array list
                    return true;
                }
            }
        }
        return false;
    }

    // check if the game object is colliding with anything
    public boolean isColliding(){
        return isGrounded() || collidingUp() || collidingLeft() || collidingRight();
    }
    
    // check if the game object is on the ground
    public boolean isGrounded() {
        return collision(new int[]{1, 1, -1, 0});
    }
    
    // check if the top of the game object is colliding with anything
    public boolean collidingUp(){
        return collision(new int[]{1, 0, -2, -1});
    }

    // check if the left of the game object is colliding with anything
    public boolean collidingLeft(){
        return collision(new int[]{0, 1, -2, -3});
    }

    // check if the right of the game object is colliding with anything
    public boolean collidingRight(){
        return collision(new int[]{2, 1, -2, -3});
    }

    // getter method for the x position
    public int getX(){
        return x;
    }

    // getter method for the y position
    public int getY(){
        return y;
    }
}