package src;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import src.Objects.*;
import src.Objects.ActiveObjects.DamageObject;
import src.Objects.ActiveObjects.EnergyObject;
import src.Objects.ActiveObjects.HealObject;

public class Level {
    private ArrayList<ArrayList<Object>> objectValues;
    private ArrayList<GameObject> gameObjects;
    private LevelManager levelManager;
    private GameManager gameManager;
    private String levelName;
    private File levelFile;
    private boolean newLevel;
    private boolean isMainLevel;
    
    public Level(String levelName, LevelManager levelManager, GameManager gameManager, boolean newLevel, boolean isMainLevel) {
        this.levelName = levelName;
        this.levelManager = levelManager;
        this.gameManager = gameManager;
        this.levelFile = new File("Levels/"+levelName+".csv");
        this.newLevel = newLevel;
        this.isMainLevel = isMainLevel;
        if(!(newLevel)) this.objectValues = levelManager.decodeLevel(levelFile);
    }

    public void start(){
        if(!(newLevel)){
            gameObjects = pullGameObjects();
            gameManager.setGameObjects(gameObjects);
        }
    }

    public void save(){
        levelManager.encodeLevel(this);
    }

    public ArrayList<GameObject> pullGameObjects(){
        ArrayList<GameObject> objects = new ArrayList<GameObject>();
        for(ArrayList<Object> object : objectValues){
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
