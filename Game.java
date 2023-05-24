// basic imports for swing and graphics
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
// Vector for the movement input
import java.util.Vector;

// game class
public class Game extends JPanel{
    // private variables for the off-screen buffer, movement vector, game manager, and main
    private BufferedImage offScreenBuffer;
    private Vector<Integer> movementVector;
    private GameManager gameManager;
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
        
        // Initialize the movement vector
        movementVector = new Vector<Integer>(2);
        movementVector.add(0);  // Initialize index 0 with value 0
        movementVector.add(0);  // Initialize index 1 with value 0

        // set the game thread
        gameThread = new Thread(() -> {
            while(true){
                gameLoop();
            }
        });

        // Create the off-screen buffer with the same size as the window
        offScreenBuffer = new BufferedImage(main.getSize().width, main.getSize().height, BufferedImage.TYPE_INT_ARGB);
    }

    // method to freeze the game
    public void stop() {
        running = false;
    }

    // method to start the game
    public void start(Boolean restart) {
        running = true; // Set running to true

        // Start the game loop and restart if needed
        if(gameThread.getState() != Thread.State.NEW){
            gameThread = new Thread(() -> {
                while(true){
                    gameLoop();
                }
            });
        }
        if(restart){
            gameManager.setGameObjects(new ArrayList<GameObject>());
            gameManager.start();
        }
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
                setMovementVector(0, -1);
            }
        });
        actionMap.put("rightAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the movement vector to move right
                setMovementVector(0, 1);
            }
        });
        actionMap.put("leftActionRelease", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the movement vector to stop moving left
                setMovementVector(0, 0);
            }
        });
        actionMap.put("rightActionRelease", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the movement vector to stop moving right
                setMovementVector(0, 0);
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

    // method to get the movement vector
    public Vector<Integer> getMovementVector() {
        return movementVector;
    }

    // method to set the movement vector
    public void setMovementVector(int index, int value) {
        movementVector.set(index, value);
    }

    // call the jump method in GameManager
    public void jump(){
        gameManager.setJump(true);
    }

    public JFrame getGameWindow(){
        return main;
    }
}