import java.awt.*;
import java.util.ArrayList;

public class GameObject{
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

    public GameObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isMoveable, boolean isPhysicsObject, GameManager gameManager) {
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
        if(shapeQ){
            this.hitboxShape = new Rectangle();
            calculateBoundingBox();
            this.hitbox = moveHitbox(x, y);
        }
        else this.hitbox = new Rectangle(x, y, image != null ? image.getWidth(null) : 0, image != null ? image.getHeight(null) : 0);
    }

    public void draw(Graphics g) {
        if (shapeQ) {
            moveShapes(); // Update the movedShapes array before drawing
            for (Polygon shape : movedShapes) {
                g.setColor(colors[0]);
                g.fillPolygon(shape);
            }
        } else {
            g.drawImage(image, x, y, null);
        }
    }

    private void moveShapes() {
        for (int i = 0; i < shapes.length; i++) {
            movedShapes[i] = changePos(this.x, this.y, shapes[i]);
        }
    }

    public void move(){
        if (Math.abs(getVelocity()[0]) > 0) {
            applyForce(-getVelocity()[0] * getDrag(), 0);
        }

        collidingObjects = new ArrayList<GameObject>();
        if(isMoveable && isColliding()){
            for(GameObject object : collidingObjects){
                if(object.isPhysicsObject){
                    System.out.println(object.getVelocity()[0] + " " + object.getVelocity()[1]);
                    applyForce(object.getVelocity()[0], object.getVelocity()[1]);
                }
            }
        }

        if(!(isGrounded())){
            if(hasGravity) velocity[1] += 1;
        }
        else if (velocity[1] > 0){
            velocity[1] = 0;
        }
        if(collidingUp() && velocity[1] < 0){
            velocity[1] = 0;
        }
        if((collidingLeft() && velocity[0] < 0) || (collidingRight() && velocity[0] > 0)){
            velocity[0] = 0;
        }
        
        this.x += velocity[0];
        this.y += velocity[1];
        this.hitbox = moveHitbox(this.x, this.y);
    }

    public void setVelocity(double x, double y){
        this.velocity = new double[]{x, y};
    }

    public double[] getVelocity(){
        return velocity;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }

    private Polygon changePos(int dx, int dy, Polygon polygon) {
        Polygon movedPolygon = new Polygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
        for (int i = 0; i < movedPolygon.npoints; i++) {
            movedPolygon.xpoints[i] += dx;
            movedPolygon.ypoints[i] += dy;
        }
        return movedPolygon;
    }

    private void calculateBoundingBox() {
        if (shapes.length == 0) {
            hitboxShape = new Rectangle(x, y, 0, 0);
            return;
        }
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
    
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
    
        int width = maxX - minX;
        int height = maxY - minY;
    
        hitboxShape.setBounds(minX, minY, width, height);
    }

    private double getDrag() {
        return isGrounded() ? dragOnGround : dragInAir;
    }
    
    public void applyForce(double xForce, double yForce) {
        double[] velocity = getVelocity();
        velocity[0] += xForce;
        velocity[1] += yForce;
        setVelocity((int) Math.round(velocity[0]), (int) Math.round(velocity[1]));
    }

    private Rectangle moveHitbox(int dx, int dy) {
        return new Rectangle(hitboxShape.x + dx, hitboxShape.y + dy, hitboxShape.width, hitboxShape.height);
    }

    //colllision
    public boolean collision(int[] modifier){
        Rectangle checkBox = new Rectangle(hitbox.x + (int)velocity[0] + modifier[0], hitbox.y + (int)velocity[1] + modifier[1], hitbox.width + modifier[2], hitbox.height + modifier[3]);
        for(GameObject gameObject : gameManager.getGameObjects()){
            if(gameObject != this){
                double[] objectVelocity = gameObject.getVelocity();
                Rectangle objectHitBox = gameObject.getHitbox();
                Rectangle objectCheckBox = new Rectangle(objectHitBox.x + (int)objectVelocity[0], objectHitBox.y + (int)objectVelocity[1], objectHitBox.width, objectHitBox.height);
                if(objectCheckBox.intersects(checkBox)){
                    collidingObjects.add(gameObject);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isColliding(){
        return isGrounded() || collidingUp() || collidingLeft() || collidingRight();
    }
    
    public boolean isGrounded() {
        return collision(new int[]{1, 1, -1, 0});
    }
    
    public boolean collidingUp(){
        return collision(new int[]{1, 0, -2, -1});
    }

    public boolean collidingLeft(){
        return collision(new int[]{0, 1, -2, -3});
    }

    public boolean collidingRight(){
        return collision(new int[]{2, 1, -2, -3});
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}