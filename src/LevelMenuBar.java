package src;

import java.awt.*;
import java.util.ArrayList;

public class LevelMenuBar {
    private ArrayList<LevelMenuOption> levelMenuOptions;
    private LevelEditorManager levelEditorManager;

    public LevelMenuBar(LevelEditorManager levelEditorManager){
        this.levelEditorManager = levelEditorManager;
        levelMenuOptions = new ArrayList<LevelMenuOption>();
        levelMenuOptions.add(new LevelMenuOption(25, 25, 50, 50, Color.BLACK, "GameObject", levelEditorManager));
        levelMenuOptions.add(new LevelMenuOption(25, 100, 50, 50, Color.RED, "Player", levelEditorManager));
        levelMenuOptions.add(new LevelMenuOption(25, 175, 50, 50, Color.GREEN, "Objective", levelEditorManager));
    }

    public void update(){

    }

    public void draw(Graphics2D g){
        for(LevelMenuOption levelMenuOption : levelMenuOptions){
            levelMenuOption.draw(g);
        }
    }

    public ArrayList<LevelMenuOption> getLevelMenuOptions(){
        return levelMenuOptions;
    }
}