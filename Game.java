import javax.swing.*;
import java.awt.*;
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

    // w - setjump(true), a - setmovementvector(-1), d - setmovementvector(1)

    public Vector<Integer> getMovementVector() {
        return movementVector;
    }
}