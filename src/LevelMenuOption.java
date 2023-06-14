package src;

// Imports
import java.awt.*;

// LevelMenuOption class
public class LevelMenuOption {
    // Variables
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private String gameObjectClass;
    private LevelEditorManager levelEditorManager;

    // Constructor
    public LevelMenuOption(int x, int y, int width, int height, Color color, String gameObjectClass, LevelEditorManager levelEditorManager){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.gameObjectClass = gameObjectClass;
        this.levelEditorManager = levelEditorManager;
    }

    // draw method
    public void draw(Graphics2D g){
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    // isMouseOver method
    public boolean isMouseOver(){
        Point mousePos = getMoisePoint();
        boolean checkX = mousePos.getX() > x && mousePos.getX() < x + width;
        boolean checkY = mousePos.getY() > y && mousePos.getY() < y + height;
        
        return checkX && checkY;
    }

    // get mouse point relative to the game window
    private Point getMoisePoint(){
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        Point gamePos = levelEditorManager.getGameWindow().getLocationOnScreen();
        return new Point((int)(mousePos.getX() - gamePos.getX()), (int)(mousePos.getY() - gamePos.getY()));
    }

    // mouseAction method
    public void mouseAction(){
        levelEditorManager.createEditorObect(gameObjectClass);
    }
}
