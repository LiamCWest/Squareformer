// basic swing imports
import java.awt.*;
import java.util.ArrayList;

// game manager class
public class GameManager {
    // private variables for the game objects, player, game, and jumping
    private ArrayList<GameObject> gameObjects;
    private Player player;
    private Game game;
    private boolean isJumping = false;

    // constructor
    public GameManager(Game game) {
        this.gameObjects = new ArrayList<GameObject>();
        this.game = game;
    }

    // method to start the game
    public void start(){
        // Create the player and add it to the game objects
        player = new Player(100, 0, new Color[]{Color.RED}, new Polygon[]{new Polygon(new int[]{0,50,50,0}, new int[]{0,0,50,50}, 4)}, true, null, true, this);
        gameObjects.add(player);

        /// Create the ground and add it to the game objects
        gameObjects.add(new GameObject(0, 718, new Color[]{Color.BLACK}, new Polygon[]{new Polygon(new int[]{0, 1360, 1360, 0}, new int[]{0, 0, 50, 50}, 4)}, true, null, false, this));
        gameObjects.add(new GameObject(500, 643, new Color[]{Color.BLACK}, new Polygon[]{new Polygon(new int[]{0, 75, 75, 0}, new int[]{0, 0, 75, 75}, 4)}, true, null, false, this));
        gameObjects.add(new GameObject(600, 550, new Color[]{Color.BLACK}, new Polygon[]{new Polygon(new int[]{0,150,150,0}, new int[]{0,0,50,50}, 4)}, true, null, false, this));
    }

    public void update() {
        player.moveForce(game.getMovementVector().get(0), 0);
        if (isJumping) {
            player.jump();
            isJumping = false;
        }

        for (GameObject gameObject : gameObjects) {
            gameObject.move();
        }
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setJump(Boolean jump) {
        isJumping = jump;
    }
}
