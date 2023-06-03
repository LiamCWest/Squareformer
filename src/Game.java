package src;
// basic imports for swing and graphics
import javax.swing.*;

import src.Objects.GameObject;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

// game class
public class Game extends JPanel{
    // private variables for the off-screen buffer, movement vector, game manager, and main
    private BufferedImage offScreenBuffer;
    private GameManager gameManager;
    private LevelManager levelManager;
    private Main main;
    private boolean running = false;
    private Thread gameThread;

    public double delta;

    // constructor
    public Game(Main main) {
        // Set the background color and layout
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setFocusable(true);

        // Set the main variable and the game manager
        this.main = main;
        gameManager = new GameManager(this);
        levelManager = new LevelManager(gameManager);

        // Create the off-screen buffer with the same size as the window
        offScreenBuffer = new BufferedImage(main.getSize().width, main.getSize().height, BufferedImage.TYPE_INT_ARGB);
    }

    // method to freeze the game
    public void stop() {
        // Stop the game loop by setting running to false
        running = false;
    }

    // method to start the game
    public void start(Boolean restart, int level) {
        // Start the game loop and restart if needed
        if(restart){
            gameManager.setGameObjects(new ArrayList<GameObject>());
            gameManager.start(level);

            if(gameThread != null && gameThread.isAlive()){
                gameThread.interrupt();
            }
        }
        gameThread = new Thread(() -> {
            gameLoop();
        });
        running = true;
        gameThread.start();
    }

    // the game loop that runs the game
    public void gameLoop() {
        // Initialize the time variables
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        delta = 0;
        long timer = System.currentTimeMillis();


        // The game loop
        while (running) {
            // Calculate the time difference
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            // Update the game state
            gameManager.update();

            // Render the game state
            render();

            // Check if a second has passed
            if (System.currentTimeMillis() - timer > 1000) timer += 1000;
        }
    }

   // Method to render the game to the game panel
    public void render() {
        // Get the graphics object from the off-screen buffer
        Graphics2D g = offScreenBuffer.createGraphics();

        // Clear the off-screen buffer
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getSize().width, getSize().height);

        // Draw the game objects
        for (GameObject gameObject : gameManager.getGameObjects()) {
            gameObject.draw(g);
        }

        // Draw the off-screen buffer to the game panel
        Graphics2D panelGraphics = (Graphics2D) getGraphics();
        if (panelGraphics != null) {
            panelGraphics.drawImage(offScreenBuffer, 0, 0, null);
            panelGraphics.dispose();
        }
    }


    // method to register the movement input
    public void registerMovementInput() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Register the movement actions with the input map and action map
        // The input map maps a key stroke to a string
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "jumpAction");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "leftAction");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "rightAction");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "leftActionRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "rightActionRelease");
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapeAction");

        // The action map maps a string to an action
        actionMap.put("jumpAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the jump action method in GameManager
                gameManager.setJump(true);
            }
        });
        actionMap.put("leftAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the movement vector to move left
                gameManager.setMoveDirection(-1);
            }
        });
        actionMap.put("rightAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the movement vector to move right
                gameManager.setMoveDirection(1);
            }
        });
        actionMap.put("leftActionRelease", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the movement vector to stop moving left
                gameManager.setMoveDirection(0);
            }
        });
        actionMap.put("rightActionRelease", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the movement vector to stop moving right
                gameManager.setMoveDirection(0);
            }
        });
        actionMap.put("escapeAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // stop game and show pause menu
                stop();
                main.showPauseMenu();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                // Call the mouse action method in GameManager
                gameManager.mouseAction();
            }
        });
    }

    public JFrame getGameWindow(){
        return main;
    }

    public LevelManager getLevelManager(){
        return levelManager;
    }

    public GameManager getGameManager(){
        return gameManager;
    }
}