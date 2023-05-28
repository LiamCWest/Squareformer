package src;
// imports
import java.util.ArrayList;
import javax.swing.JFrame;

import src.Objects.*;

// game manager class
public class GameManager {
    // private variables for the game objects, player, game, and jumping
    private ArrayList<GameObject> gameObjects;
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
    public void start(){
        game.getLevelManager().loadLevel(game.getLevelManager().getCurrentLevel());
        player = (Player) gameObjects.get(0);
    }

    // update method, updates everything each frame
    public void update() {
        // update all the game objects positions
        for (GameObject gameObject : gameObjects) {
            gameObject.update();
        }
    }

    public void nextLevel(){
        gameObjects.clear();
        start();
    }

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

    public void mouseAction(){
        player.grapple();
    }

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
}