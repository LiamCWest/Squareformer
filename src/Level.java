package src;

import java.io.File;
import java.util.ArrayList;
import src.Objects.*;

public class Level {
    private ArrayList<GameObject> gameObjects;
    private LevelManager levelManager;
    private GameManager gameManager;
    private String levelName;
    private File levelFile;
    
    public Level(String levelName, LevelManager levelManager, GameManager gameManager) {
        this.levelName = levelName;
        this.levelManager = levelManager;
        this.gameManager = gameManager;
        this.levelFile = new File("Levels/"+levelName+".csv");
        this.gameObjects = levelManager.decodeLevel(levelFile);
    }

    public void start(){
        gameManager.setGameObjects(gameObjects);
    }
}
