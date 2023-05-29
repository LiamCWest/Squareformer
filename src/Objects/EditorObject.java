package src.Objects;

import java.awt.*;

import src.LevelEditorManager;

public class EditorObject {
    // variables for movement, rendering, and collision
    private int x;
    private int y;
    private Polygon[] shapes;
    private Polygon[] movedShapes;
    private Color[] colors;
    private boolean shapeQ;
    private Image image;
    @SuppressWarnings("unused") private boolean hasGravity;
    public boolean isPhysicsObject;
    @SuppressWarnings("unused") private boolean isCollisionObject;
    private boolean isFocus;
    private LevelEditorManager levelEditorManager;

    private Rectangle hitboxShape;
    private Rectangle hitbox;

    // constructors
    public EditorObject(int x, int y, Color[] colors, Polygon[] shapes, boolean shapeQ, Image image, boolean hasGravity, boolean isPhysicsObject, boolean isCollisionObject, LevelEditorManager levelEditorManager) {
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
        this.isCollisionObject = isCollisionObject;
        this.isFocus = false;
        this.levelEditorManager = levelEditorManager;

        // if the game object is a shape, calculate the bounding box and move the shapes
        if(shapeQ){
            this.hitboxShape = new Rectangle();
            calculateBoundingBox();
            this.hitbox = moveHitbox(x, y);
        }
        // else set the hitbox to the image size
        else this.hitbox = new Rectangle(x, y, image != null ? image.getWidth(null) : 0, image != null ? image.getHeight(null) : 0);

    }

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

    public void update(){
        if(isFocus){
            Point mousePos = getMoisePoint();
            x = (int) (mousePos.getX() - (hitbox.getWidth() / 2));
            y = (int) (mousePos.getY() - (hitbox.getHeight() / 2));
        }
    }

    // method to move the shapes of the game object
    private void moveShapes() {
        for (int i = 0; i < shapes.length; i++) {
            movedShapes[i] = changePos(this.x, this.y, shapes[i]);
        }
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

    // getter method for the x position
    public int getX(){
        return x;
    }

    // getter method for the y position
    public int getY(){
        return y;
    }

    // setter method for the x position
    public void setX(int x){
        this.x = x;
    }

    // setter method for the y position
    public void setY(int y){
        this.y = y;
    }

    public boolean isMouseOver(){
        Point mousePos = getMoisePoint();
        boolean checkX = mousePos.getX() > x && mousePos.getX() < x + hitbox.width;
        boolean checkY = mousePos.getY() > y && mousePos.getY() < y + hitbox.height;
        
        return checkX && checkY;
    }

    private Point getMoisePoint(){
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        Point gamePos = levelEditorManager.getGameWindow().getLocationOnScreen();
        return new Point((int)(mousePos.getX() - gamePos.getX()), (int)(mousePos.getY() - gamePos.getY()));
    }

    public void setFocus(boolean focus){
        this.isFocus = focus;
    }
    
    public boolean getFocus(){
        return isFocus;
    }
}
