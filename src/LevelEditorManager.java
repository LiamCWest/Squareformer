package src;

// Imports
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

import src.Objects.*;
import src.Objects.ActiveObjects.DamageObject;
import src.Objects.ActiveObjects.EnergyObject;
import src.Objects.ActiveObjects.HealObject;

// LevelEditorManager class
public class LevelEditorManager {
    // Variables
    private LevelEditor levelEditor;
    private ArrayList<EditorObject> editorObjects;
    private Level level;

    private EditorObject selectedObject;

    // Constructor
    public LevelEditorManager(LevelEditor levelEditor){
        this.levelEditor = levelEditor;
    }
    
    // start method
    public void start(Level level){
        this.level = level;
        editorObjects = new ArrayList<EditorObject>();
        // If the level is new, add the default objects, otherwise add the objects from the level
        if(level.isNewLevel()){
            editorObjects.add(new EditorObject(100, 600, new Color[]{Color.MAGENTA}, new Polygon[]{new Polygon(new int[]{0, 50, 50, 0}, new int[]{0, 0, 50, 50},4)}, true, null, true, true, true, this, "player"));
            editorObjects.add(new EditorObject(0, 718, new Color[]{Color.BLACK}, new Polygon[]{new Polygon(new int[]{0, 1360, 1360, 0}, new int[]{0, 0, 50, 50}, 4)}, true, null, false, false, true, this, "gameObject"));
        } else{
            ArrayList<GameObject> gameObjects = level.pullGameObjects();
            for(GameObject gameObject : gameObjects){
                editorObjects.add(convertToEditorObject(gameObject));
            }
        }
    }

    // update method
    public void update(){
        // tempEditorObjects is used to prevent a ConcurrentModificationException
        ArrayList<EditorObject> tempEditorObjects = new ArrayList<EditorObject>();
        for(EditorObject editorObject : editorObjects){
            tempEditorObjects.add(editorObject);
        }

        for(EditorObject editorObject : tempEditorObjects){
            editorObject.update();
        }
    }

    // save method
    public void save(){
        // Convert the editor objects to game objects
        ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
        for(EditorObject editorObject : editorObjects){
            gameObjects.add(convertToGameObject(editorObject));
        }
        // Save the game objects to the level
        level.setGameObjects(gameObjects);
        level.save();
    }

    // mouseAction method
    public void mouseAction(){
        // If the mouse is over an editor object, select it
        for(EditorObject editorObject : editorObjects){
            if(editorObject.isMouseOver()){
                if(editorObject.getFocus()){
                    editorObject.setFocus(false);
                    selectedObject = null;
                }else {
                    if(selectedObject != null) selectedObject.setFocus(false);
                    editorObject.setFocus(true);
                    selectedObject = editorObject;
                }
            }
        }
        // If the mouse is over a level menu option, perform the action
        for(LevelMenuOption levelMenuOption : levelEditor.getLevelMenuBar().getLevelMenuOptions()){
            if(levelMenuOption.isMouseOver()){
                levelMenuOption.mouseAction();
            }
        }
    }

    // convertToGameObject method
    private GameObject convertToGameObject(EditorObject editorObject){
        String objectClass = editorObject.getGameObjectClass().toLowerCase();
        if(objectClass.contains(".")){
            long count = objectClass.chars().filter(ch -> ch == '.').count();
            objectClass = objectClass.split("\\.")[((int) count)].toLowerCase();
        }

        // Create the game object based on the class
        switch(objectClass){
            case "gameobject":
                return new GameObject(editorObject.getX(), editorObject.getY(), editorObject.getColors(), editorObject.getShapes(), editorObject.getShapeQ(), editorObject.getImage(), editorObject.hasGravity(), editorObject.isPhysicsObject(), editorObject.isCollisionObject(), level.getGameManager());
            case "player":
                return new Player(editorObject.getX(), editorObject.getY(), editorObject.getColors(), editorObject.getShapes(), editorObject.getShapeQ(), editorObject.getImage(), editorObject.hasGravity(), editorObject.isPhysicsObject(), level.getGameManager());
            case "objective":
                return new Objective(editorObject.getX(), editorObject.getY(), editorObject.getColors(), editorObject.getShapes(), editorObject.getShapeQ(), editorObject.getImage(), level.getGameManager());
            case "damageobject":
                return new DamageObject(editorObject.getX(), editorObject.getY(), editorObject.getColors(), editorObject.getShapes(), editorObject.getShapeQ(), editorObject.getImage(), editorObject.hasGravity(), editorObject.isPhysicsObject(), level.getGameManager());
            case "healobject":
                return new HealObject(editorObject.getX(), editorObject.getY(), editorObject.getColors(), editorObject.getShapes(), editorObject.getShapeQ(), editorObject.getImage(), editorObject.hasGravity(), editorObject.isPhysicsObject(), level.getGameManager());
            case "energyobject":
                return new EnergyObject(editorObject.getX(), editorObject.getY(), editorObject.getColors(), editorObject.getShapes(), editorObject.getShapeQ(), editorObject.getImage(), editorObject.hasGravity(), editorObject.isPhysicsObject(), level.getGameManager());
        }
        return null;
    }

    // convertToEditorObject method
    private EditorObject convertToEditorObject(GameObject gameObject){
        EditorObject editorObject = new EditorObject(gameObject.getX(), gameObject.getY(), gameObject.getColors(), gameObject.getShapes(), gameObject.getShapeQ(), gameObject.getImage(), gameObject.hasGravity(), gameObject.isPhysicsObject(), gameObject.isCollisionObject(), this, gameObject.ClassName());
        return editorObject;
    }

    // getters and setters
    public ArrayList<EditorObject> getEditorObjects(){
        return editorObjects;
    }

    public JFrame getGameWindow(){
        return levelEditor.getGameWindow();
    }

    public void setEditorObjects(ArrayList<EditorObject> editorObjects){
        this.editorObjects = editorObjects;
    }

    // createEditorObject method
    public void createEditorObect(String objectClass){
        JTextField[] fields = new JTextField[10];
        for(int i = 0; i < fields.length; i++){
            fields[i] = new JTextField(5);
        }

        JPanel popupPanel = new JPanel();
        popupPanel.add(new JLabel("X:"));
        popupPanel.add(fields[0]);
        popupPanel.add(new JLabel("Y:"));
        popupPanel.add(fields[1]);
        popupPanel.add(new JLabel("Width:"));
        popupPanel.add(fields[2]);
        popupPanel.add(new JLabel("Height:"));
        popupPanel.add(fields[3]);
        popupPanel.add(new JLabel("Color:"));
        popupPanel.add(fields[4]);
        popupPanel.add(new JLabel("Shape or Image:"));
        popupPanel.add(fields[5]);
        popupPanel.add(new JLabel("Image Path:"));
        popupPanel.add(fields[6]);
        popupPanel.add(new JLabel("Has Gravity:"));
        popupPanel.add(fields[7]);
        popupPanel.add(new JLabel("Is Physics Object:"));
        popupPanel.add(fields[8]);
        popupPanel.add(new JLabel("Is Collision Object:"));
        popupPanel.add(fields[9]);

        int result = JOptionPane.showConfirmDialog(null, popupPanel, "Create " + objectClass, JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION){
            int x = Integer.parseInt(fields[0].getText());
            int y = Integer.parseInt(fields[1].getText());
            int width = Integer.parseInt(fields[2].getText());
            int height = Integer.parseInt(fields[3].getText());
            Color color = getColor(fields[4].getText()); //fix this
            boolean shapeQ = Boolean.parseBoolean(fields[5].getText());
            Image image = null;
            if(!(shapeQ)){
                try{
                    image = ImageIO.read(new File(fields[6].getText()));
                }catch(Exception e){
                    System.out.println("Image not found");
                }
            }
            boolean hasGravity = Boolean.parseBoolean(fields[7].getText());
            boolean isPhysicsObject = Boolean.parseBoolean(fields[8].getText());
            boolean isCollisionObject = Boolean.parseBoolean(fields[9].getText());
            editorObjects.add(new EditorObject(x, y, new Color[]{color}, new Polygon[]{new Polygon(new int[]{0,width,width,0}, new int[]{0,0,height,height}, 4)}, shapeQ, image, hasGravity, isPhysicsObject, isCollisionObject, this, objectClass));
        }
    }

    // get color method
    public Color getColor(String color){
        boolean colorQ = color.contains(",");
        if(colorQ){
            String[] colors = color.split(",");
            return new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
        }
        else{
            switch(color){
                case "red":
                    return Color.RED;
                case "blue":
                    return Color.BLUE;
                case "green":
                    return Color.GREEN;
                case "yellow":
                    return Color.YELLOW;
                case "orange":
                    return Color.ORANGE;
                case "pink":
                    return Color.PINK;
                case "black":
                    return Color.BLACK;
                case "white":
                    return Color.WHITE;
                default:
                    return Color.WHITE;
            }
        }
    }

    // getLevel method
    public Level getLevel(){
        return level;
    }
}