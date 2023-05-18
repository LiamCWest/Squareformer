import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class Game extends JPanel{
    private BufferedImage offScreenBuffer;
    private Vector<Integer> movementVector;
    private GameManager gameManager;
    private Main main;
    private boolean running = false;

    public Game(Main main) {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setFocusable(true);

        this.main = main;
        gameManager = new GameManager(this);
        
        movementVector = new Vector<Integer>(2);
        movementVector.add(0);  // Initialize index 0 with value 0
        movementVector.add(0);  // Initialize index 1 with value 0

        // Create the off-screen buffer with the same size as the window
        offScreenBuffer = new BufferedImage(main.getSize().width, main.getSize().height, BufferedImage.TYPE_INT_ARGB);
    }

    public void stop() {
        running = false;
    }

    public void start() {
        running = true;
        gameManager.start();
        gameLoop();
    }

    public void gameLoop() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        while (running) {
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

            if (System.currentTimeMillis() - timer > 1000) timer += 1000;
        }
    }

    public void render() {
        Graphics g = getGraphics();
        Graphics bbg = offScreenBuffer.getGraphics();
        bbg.setColor(new Color(255, 255, 255, 255));
        bbg.fillRect(0, 0, this.getSize().width + 100, this.getSize().height + 100);
        for (GameObject gameObject : gameManager.getGameObjects()) {
            gameObject.draw(bbg);
        }
        g.drawImage(offScreenBuffer, 0, 0, this);
    }

    public void registerMovementInput() {
        System.out.println("Registering movement input");

        // Create the input map and action map
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Define the movement input key bindings
        KeyStroke wKey = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0);
        KeyStroke aKey = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0);
        KeyStroke dKey = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0);

        // Register the movement actions with the input map and action map
        inputMap.put(wKey, "jumpAction");
        inputMap.put(aKey, "leftAction");
        inputMap.put(dKey, "rightAction");
        actionMap.put("jumpAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Jump action");
                // Call the jump action method in GameManager
                gameManager.setJump(true);
            }
        });
        actionMap.put("leftAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Left action");
                setMovementVector(0, -1);
            }
        });
        actionMap.put("rightAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Right action");
                setMovementVector(0, 1);
            }
        });
    }

    public Vector<Integer> getMovementVector() {
        return movementVector;
    }

    public void setMovementVector(int index, int value) {
        movementVector.set(index, value);
    }

    public void Jump(){
        gameManager.setJump(true);
    }
}