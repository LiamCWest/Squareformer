package src;

// Imports
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import src.Objects.*;
import src.Objects.ActiveObjects.DamageObject;
import src.Objects.ActiveObjects.EnergyObject;
import src.Objects.ActiveObjects.HealObject;

// Level class
public class Level {
    // Variables
    private ArrayList<ArrayList<Object>> objectValues;
    private ArrayList<GameObject> gameObjects;
    private LevelManager levelManager;
    private GameManager gameManager;
    private String levelName;
    private File levelFile;
    private boolean newLevel;
    private boolean isMainLevel;
    
    // Constructor
    public Level(String levelName, LevelManager levelManager, GameManager gameManager, boolean newLevel, boolean isMainLevel) {
        this.levelName = levelName;
        this.levelManager = levelManager;
        this.gameManager = gameManager;
        this.levelFile = isMainLevel ? new File("Levels/"+levelName+".csv") : new File("Levels/UserCreated/"+levelName+".csv");
        this.newLevel = newLevel;
        this.isMainLevel = isMainLevel;
        if(!(newLevel)) this.objectValues = levelManager.decodeLevel(levelFile);
    }

    // start method
    public void start(){
        if(!(newLevel)){ // If the level is not new
            gameObjects = pullGameObjects(); // Pull the game objects from the level file
            gameManager.setGameObjects(gameObjects); // Set the game objects in the game manager
        }
    }

    // save method
    public void save(){
        levelManager.encodeLevel(this); // Encode the level
    }

    // pullGameObjects method
    public ArrayList<GameObject> pullGameObjects(){
        ArrayList<GameObject> objects = new ArrayList<GameObject>();
        // Loop through the object values
        for(ArrayList<Object> object : objectValues){
            // Switch statement for the object type
            switch(object.get(0).toString().toLowerCase()){
                case "gameobject":
                    objects.add(new GameObject((int) object.get(1), (int) object.get(2), (Color[]) object.get(3), (Polygon[]) object.get(4), (boolean) object.get(5), (Image) object.get(6), (boolean) object.get(7), (boolean) object.get(8), (boolean) object.get(9), gameManager));
                    break;
                case "player":
                    objects.add(new Player((int) object.get(1), (int) object.get(2), (Color[]) object.get(3), (Polygon[]) object.get(4), (boolean) object.get(5), (Image) object.get(6), (boolean) object.get(7), (boolean) object.get(8), gameManager));
                    break;
                case "objective":
                    objects.add(new Objective((int) object.get(1), (int) object.get(2), (Color[]) object.get(3), (Polygon[]) object.get(4), (boolean) object.get(5), (Image) object.get(6), gameManager));
                    break;
                case "damageobject":
                    objects.add(new DamageObject((int) object.get(1), (int) object.get(2), (Color[]) object.get(3), (Polygon[]) object.get(4), (boolean) object.get(5), (Image) object.get(6), (boolean) object.get(7), (boolean) object.get(8), gameManager));
                    break;
                case "healobject":
                    objects.add(new HealObject((int) object.get(1), (int) object.get(2), (Color[]) object.get(3), (Polygon[]) object.get(4), (boolean) object.get(5), (Image) object.get(6), (boolean) object.get(7), (boolean) object.get(8), gameManager));
                    break;
                case "energyobject":
                    objects.add(new EnergyObject((int) object.get(1), (int) object.get(2), (Color[]) object.get(3), (Polygon[]) object.get(4), (boolean) object.get(5), (Image) object.get(6), (boolean) object.get(7), (boolean) object.get(8), gameManager));
                    break;
                case "text":
                    objects.add(new TextObject((int) object.get(1), (int) object.get(2), (String) object.get(3), (int) object.get(4)));
                    break;
                default:
                    System.out.println("Error: Object type not found " + object.get(0).toString().toLowerCase());
                    break;
            }
        }
        return objects;
    }

    // Getters and Setters
    public String getLevelName(){
        return levelName;
    }

    public boolean isNewLevel(){
        return newLevel;
    }

    public GameManager getGameManager(){
        return gameManager;
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects){
        this.gameObjects = gameObjects;
    }

    public ArrayList<GameObject> getGameObjects(){
        return gameObjects;
    }

    public boolean isMainLevel(){
        return isMainLevel;
    }
}
