// basic imports for swing and graphics
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

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

        // Create the off-screen buffer with the same size as the window
        offScreenBuffer = new BufferedImage(main.getSize().width, main.getSize().height, BufferedImage.TYPE_INT_ARGB);
    }

    // method to freeze the game
    public void stop() {
        running = false;
    }

    // method to start the game
    public void start() {
        running = true;
        gameManager.start();
        gameLoop();
    }

    // the game loop that runs the game
    public void gameLoop() {
        // Initialize the time variables
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();

        // The game loop
        while (running) {
            // Calculate the time difference
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                // Update the game state
                gameManager.update();
                delta--;
            }
            // Render the game state
            render();

            // Check if a second has passed
            if (System.currentTimeMillis() - timer > 1000) timer += 1000;
        }
    }

    // method to render the game to the game panel
    public void render() {
        // Get the graphics objects from the off-screen buffer and the game panel
        Graphics g = getGraphics();
        Graphics bbg = offScreenBuffer.getGraphics();

        // Clear the off-screen buffer
        bbg.setColor(new Color(255, 255, 255, 255));
        bbg.fillRect(0, 0, this.getSize().width + 100, this.getSize().height + 100);

        // Draw the game objects
        for (GameObject gameObject : gameManager.getGameObjects()) {
            gameObject.draw(bbg);
        }

        // Draw the off-screen buffer to the game panel
        g.drawImage(offScreenBuffer, 0, 0, this);
    }


    // method to register the movement input
    public void registerMovementInput() {
        System.out.println("Registering movement input");

        // Register the movement actions with the input map and action map
        // The input map maps a key stroke to a string
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "jumpAction");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "leftAction");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "rightAction");

        // The action map maps a string to an action
        getActionMap().put("jumpAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Jump action");
                // Call the jump action method in GameManager
                gameManager.setJump(true);
            }
        });
        getActionMap().put("leftAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Left action");
                // Set the movement vector to move left
                setMovementVector(0, -1);
            }
        });
        getActionMap().put("rightAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Right action");
                // Set the movement vector to move right
                setMovementVector(0, 1);
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
    public void Jump(){
        gameManager.setJump(true);
    }
}