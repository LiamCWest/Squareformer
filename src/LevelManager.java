package src;

import java.util.*;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Image;
import java.awt.Polygon;
import java.io.*;

import src.Objects.*;

public class LevelManager {
    private ArrayList<Level> levels;
    private GameManager gameManager;
    private int currentLevel = 0;

    public LevelManager(GameManager gameManager) {
        levels = new ArrayList<Level>();
        this.gameManager = gameManager;
        Level level1 = new Level("Level1", this, gameManager);
        levels.add(level1);
    }

    public ArrayList<GameObject> decodeLevel(File levelFile){
        try{
            ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
            Scanner scanner = new Scanner(levelFile);
            while(scanner.hasNextLine()){
                String[] baseInfo = scanner.nextLine().split(",");
                String type = baseInfo[0];
                int x = Integer.parseInt(baseInfo[1]);
                int y = Integer.parseInt(baseInfo[2]);
                boolean shapeQ = Boolean.parseBoolean(baseInfo[4]);
                Image image = baseInfo[5].equals("null") ? null : ImageIO.read(new File(baseInfo[5]));
                boolean hasGravity = false;
                boolean isPhysicsObject = false;
                boolean isCollisionObject = false;
                if(baseInfo.length > 6){
                    hasGravity = Boolean.parseBoolean(baseInfo[6]);
                }
                if(baseInfo.length > 7){
                    isPhysicsObject = Boolean.parseBoolean(baseInfo[7]);
                }
                if(baseInfo.length > 8){
                    isCollisionObject = Boolean.parseBoolean(baseInfo[8]);
                }

                String[] colorInfo = scanner.nextLine().split(",");
                ArrayList<Color> colors = new ArrayList<Color>();
                int colorIndex = 0;
                while(colorIndex < colorInfo.length){
                    boolean ColorQ = Boolean.parseBoolean(colorInfo[colorIndex]);
                    Color color;
                    if(ColorQ){
                        color = new Color(Integer.parseInt(colorInfo[++colorIndex]), Integer.parseInt(colorInfo[++colorIndex]), Integer.parseInt(colorInfo[++colorIndex]));
                    }
                    else{
                        switch(colorInfo[++colorIndex]){
                            case "red":
                                color = Color.RED;
                                break;
                            case "blue":
                                color = Color.BLUE;
                                break;
                            case "green":
                                color = Color.GREEN;
                                break;
                            case "yellow":
                                color = Color.YELLOW;
                                break;
                            case "orange":
                                color = Color.ORANGE;
                                break;
                            case "pink":
                                color = Color.PINK;
                                break;
                            case "black":
                                color = Color.BLACK;
                                break;
                            case "white":
                                color = Color.WHITE;
                                break;
                            case "gray":
                                color = Color.GRAY;
                                break;
                            case "cyan":
                                color = Color.CYAN;
                                break;
                            case "magenta":
                                color = Color.MAGENTA;
                                break;
                            default:
                                color = Color.WHITE;
                                break;
                        }
                    }
                    colors.add(color);
                    colorIndex++;
                }

                int nPolygons = Integer.parseInt(baseInfo[3]);
                Polygon[] polygons = new Polygon[nPolygons];
                for (int i = 0; i < nPolygons; i++) {
                    String line = scanner.nextLine();

                    int[] polyData = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();

                    int nPoints = polyData[0];
                    int[] xPoints = new int[nPoints];
                    int[] yPoints = new int[nPoints];
                    for(int j = 0; j < nPoints; j++){
                        xPoints[j] = polyData[j+1];
                        yPoints[j] = polyData[j+1+nPoints];
                    }

                    polygons[i] = new Polygon(xPoints, yPoints, nPoints);
                }

                Color[] colorArray = new Color[colors.size()];
                for(int i = 0; i < colors.size(); i++){
                    colorArray[i] = colors.get(i);
                }

                switch(type){
                    case "gameObject":
                        gameObjects.add(new GameObject(x, y, colorArray, polygons, shapeQ, image, hasGravity, isPhysicsObject, isCollisionObject, gameManager));
                        break;
                    case "player":
                        gameObjects.add(new Player(x, y, colorArray, polygons, shapeQ, image, hasGravity, isPhysicsObject, gameManager));
                        break;
                    case "objective":
                        gameObjects.add(new Objective(x, y, colorArray, polygons, shapeQ, image, gameManager));
                        break;
                }

            }
            
            scanner.close();
            return gameObjects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }

    public void loadLevel(int levelIndex){
        levels.get(levelIndex).start();
    }
}