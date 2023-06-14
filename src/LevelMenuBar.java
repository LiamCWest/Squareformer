package src;

// Imports
import java.awt.*;
import java.util.ArrayList;

// LevelMenuBar class
public class LevelMenuBar {
    // Variables
    private ArrayList<LevelMenuOption> levelMenuOptions;
    @SuppressWarnings("unused") private LevelEditorManager levelEditorManager;

    // Constructor
    public LevelMenuBar(LevelEditorManager levelEditorManager){
        this.levelEditorManager = levelEditorManager;
        levelMenuOptions = new ArrayList<LevelMenuOption>();
        levelMenuOptions.add(new LevelMenuOption(25, 25, 50, 50, Color.BLACK, "GameObject", levelEditorManager));
        levelMenuOptions.add(new LevelMenuOption(25, 100, 50, 50, Color.MAGENTA, "Player", levelEditorManager));
        levelMenuOptions.add(new LevelMenuOption(25, 175, 50, 50, Color.GREEN, "Objective", levelEditorManager));
        levelMenuOptions.add(new LevelMenuOption(25, 250, 50, 50, Color.RED, "DamageObject", levelEditorManager));
        levelMenuOptions.add(new LevelMenuOption(25, 325, 50, 50, Color.PINK, "HealObject", levelEditorManager));
        levelMenuOptions.add(new LevelMenuOption(25, 400, 50, 50, Color.YELLOW, "EnergyObject", levelEditorManager));
    }

    // draw method
    public void draw(Graphics2D g){
        for(LevelMenuOption levelMenuOption : levelMenuOptions){
            levelMenuOption.draw(g);
        }
    }

    // getLevelMenuOptions method
    public ArrayList<LevelMenuOption> getLevelMenuOptions(){
        return levelMenuOptions;
    }
}