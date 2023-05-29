package src;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import src.Objects.*;

public class Level {
    private ArrayList<ArrayList<Object>> objectValues;
    private ArrayList<GameObject> gameObjects;
    @SuppressWarnings("unused")
    private LevelManager levelManager;
    private GameManager gameManager;
    @SuppressWarnings("unused")
    private String levelName;
    private File levelFile;
    
    public Level(String levelName, LevelManager levelManager, GameManager gameManager) {
        this.levelName = levelName;
        this.levelManager = levelManager;
        this.gameManager = gameManager;
        this.levelFile = new File("Levels/"+levelName+".csv");
        this.objectValues = levelManager.decodeLevel(levelFile);
    }

    public void start(){
        gameObjects = getGameObjects();
        gameManager.setGameObjects(gameObjects);
    }

    public ArrayList<GameObject> getGameObjects(){
        ArrayList<GameObject> objects = new ArrayList<GameObject>();
        for(ArrayList<Object> object : objectValues){
            switch(object.get(0).toString()){
                case "gameObject":
                    objects.add(new GameObject((int) object.get(1), (int) object.get(2), (Color[]) object.get(3), (Polygon[]) object.get(4), (boolean) object.get(5), (Image) object.get(6), (boolean) object.get(7), (boolean) object.get(8), (boolean) object.get(9), gameManager));
                    break;
                case "player":
                    objects.add(new Player((int) object.get(1), (int) object.get(2), (Color[]) object.get(3), (Polygon[]) object.get(4), (boolean) object.get(5), (Image) object.get(6), (boolean) object.get(7), (boolean) object.get(8), gameManager));
                    break;
                case "objective":
                    objects.add(new Objective((int) object.get(1), (int) object.get(2), (Color[]) object.get(3), (Polygon[]) object.get(4), (boolean) object.get(5), (Image) object.get(6), gameManager));
                    break;
            }
        }
        return objects;
    }
}
