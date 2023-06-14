package src;
// imports
import java.util.ArrayList;
import javax.swing.JFrame;

import src.Objects.*;

// game manager class
public class GameManager {
    // private variables for the game objects, player, game, and jumping
    private ArrayList<GameObject> gameObjects;
    private EnergyDisplay energyDisplay;
    private HealthDisplay healthDisplay;
    private Player player;
    private Game game;
    private int moveDirection = 0;
    private boolean jump = false;

    // constructor
    public GameManager(Game game) {
        this.gameObjects = new ArrayList<GameObject>();
        this.game = game;
    }

    // method to start the game
    public void start(int level) {
        game.getLevelManager().setCurrentLevel(level);
        game.getLevelManager().loadLevel(game.getLevelManager().getCurrentLevel());
        player = (Player) gameObjects.get(0);
        energyDisplay = new EnergyDisplay(30, 30, player);
        healthDisplay = new HealthDisplay(game.getGameWindow().getSize().width-104, 30, player);
    }

    // update method, updates everything each frame
    public void update() {
        // update all the game objects positions
        for (GameObject gameObject : gameObjects) {
            gameObject.update();
        }
        energyDisplay.update();
        healthDisplay.update();
    }

    // end game method
    public void endGame(){
        gameObjects.clear();
        game.stop();
        game.start(true, 0);
    }

    // next level method
    public void nextLevel(){
        gameObjects.clear();
        game.stop();
        game.start(true, game.getLevelManager().getCurrentLevel() + 1);
    }

    // getter method for the player
    public Player getPlayer(){
        return player;
    }

    // getter method for the game objects array list
    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }
    
    // setter method for the game objects array list
    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    // on mouse click, grapple
    public void mouseAction(){
        player.grapple();
    }

    // getters and setter
    public JFrame getGameWindow(){
        return game.getGameWindow();
    }

    public void setMoveDirection(int moveDirection){
        this.moveDirection = moveDirection;
    }

    public int getMoveDirection(){
        return moveDirection;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }

    public boolean getJump(){
        return jump;
    }

    public EnergyDisplay getEnergyDisplay(){
        return energyDisplay;
    }
    
    public HealthDisplay getHealthDisplay(){
        return healthDisplay;
    }
}