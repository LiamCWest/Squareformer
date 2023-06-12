package src;

import java.util.*;

import javax.imageio.ImageIO;

import src.Objects.*;

import java.awt.Color;
import java.awt.Image;
import java.awt.Polygon;
import java.io.*;

public class LevelManager {
    private ArrayList<Level> levels;
    private ArrayList<Level> userLevels;
    @SuppressWarnings("unused")
    private GameManager gameManager;
    private int currentLevel = 0;
    private File levelFolder;
    private File userLevelFolder;
    @SuppressWarnings("unused")
    private Main main;

    public LevelManager(GameManager gameManager, Main main, boolean editor) {
        levels = new ArrayList<Level>();
        userLevels = new ArrayList<Level>();
        this.gameManager = gameManager;
        this.main = main;
        levelFolder = new File("Levels");
        File[] levelFiles = levelFolder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("csv");
            }
        });
        Arrays.sort(levelFiles);
        for(File levelFile : levelFiles){
            levels.add(new Level(levelFile.getName().split("\\.")[0], this, gameManager, false, true));
        }

        userLevelFolder = new File("Levels/UserCreated");
        File[] userLevelFiles = userLevelFolder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("csv");
            }
        });
        Arrays.sort(userLevelFiles);
        for(File levelFile : userLevelFiles){
            userLevels.add(new Level(levelFile.getName().split("\\.")[0], this, gameManager, true, false));
        }

        if(!(editor)){
            ArrayList<ArrayList<Level>> levelPages = new ArrayList<ArrayList<Level>>();
            ArrayList<Level> tempPage = new ArrayList<Level>();
            for(Level level : levels){
                if(levels.indexOf(level)%18 == 0 && levels.indexOf(level) != 0){
                    System.out.println(levels.indexOf(level));
                    levelPages.add(tempPage);
                    tempPage = new ArrayList<Level>();
                }
                tempPage.add(level);
            }
            levelPages.add(tempPage);
            ArrayList<ArrayList<Level>> customLevelPages = new ArrayList<ArrayList<Level>>();
            if(userLevels != null){
                tempPage = new ArrayList<Level>();
                for(Level level : userLevels){
                    if(userLevels.indexOf(level)%18 == 0 && userLevels.indexOf(level) != 0){
                        System.out.println(userLevels.indexOf(level));
                        customLevelPages.add(tempPage);
                        tempPage = new ArrayList<Level>();
                    }
                    tempPage.add(level);
                }
                customLevelPages.add(tempPage);
            }

            main.createLevelMenu(levelPages, customLevelPages);
        }
    }

    public void encodeLevel(Level level){
        ArrayList<GameObject> objects = level.getGameObjects();
        try {
            File levelFile;
            File levelSaveFolder;
            if(level.isMainLevel()) levelSaveFolder = new File("Levels");
            else levelSaveFolder = new File("Levels/UserCreated");
            levelFile = new File(levelSaveFolder + "/" + level.getLevelName() + ".csv");
            if(levelSaveFolder.exists()) levelFile.createNewFile();
            else levelSaveFolder.mkdir();
            FileWriter writer = new FileWriter(levelFile);
            for(GameObject object : objects){
                String type = object.getClass().getName().toLowerCase();
                if(type.contains(".")){
                    long count = type.chars().filter(ch -> ch == '.').count();
                    type = type.split("\\.")[(int) count];
                }
                int x = object.getX();
                int y = object.getY();
                Color[] colors = object.getColors();
                Polygon[] polygons = object.getShapes();
                boolean shapeQ = object.isShapeQ();
                Image image = object.getImage();
                boolean hasGravity = object.hasGravity();
                boolean isPhysicsObject = object.isPhysicsObject();
                boolean isCollisionObject = object.isCollisionObject();
                writer.write(type + "," + x + "," + y + "," + polygons.length + "," + shapeQ + "," + image + "," + hasGravity + "," + isPhysicsObject + "," + isCollisionObject + "\n");
                for(Color color : colors){
                    writer.write("true" + "," + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ",");
                }
                writer.write("\n");
                for(Polygon polygon : polygons){
                    writer.write(polygon.xpoints.length + ",");
                    for(int i = 0; i < polygon.xpoints.length; i++)writer.write(polygon.xpoints[i] + ",");
                    for(int i = 0; i < polygon.ypoints.length; i++)writer.write(polygon.ypoints[i] + ",");
                }
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> decodeLevel(File levelFile){
        try{
            ArrayList<ArrayList<Object>> objectValues = new ArrayList<ArrayList<Object>>();
            Scanner scanner = new Scanner(levelFile);
            while(scanner.hasNextLine()){
                String[] baseInfo = scanner.nextLine().split(",");
                String type = baseInfo[0];
                if(!(type.equals("text"))){
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
                    objectValues.add(new ArrayList<Object>(Arrays.asList(type, x, y, colorArray, polygons, shapeQ, image, hasGravity, isPhysicsObject, isCollisionObject)));
                } else{
                    int x = Integer.parseInt(baseInfo[1]);
                    int y = Integer.parseInt(baseInfo[2]);
                    String text = baseInfo[3];
                    int size = Integer.parseInt(baseInfo[4]);
                    objectValues.add(new ArrayList<Object>(Arrays.asList(type, x, y, text, size)));
                }
            }
            
            scanner.close();
            return objectValues;
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

    public Level getLevel(int levelIndex){
        return levels.get(levelIndex);
    }

    public void setCurrentLevel(int levelIndex){
        currentLevel = levelIndex;
    }
    
    public ArrayList<Level> getLevels(){
        return levels;
    }

    public Level getLevelByName(String name){
        for(Level level : levels){
            if(level.getLevelName().equals(name)){
                return level;
            }
        }
        return null;
    }
}