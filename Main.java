// basic imports for swing
import javax.swing.*;
import java.awt.*;

// main class
public class Main extends JFrame{
    // private variables for the panels and card layout
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Game gamePanel;
    private MainMenu menuPanel;
    
    // constructor
    public Main() {
        // Create the main frame
        setTitle("Game");
        setSize(1360, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create the card panel with CardLayout
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        
        // Create the game panel and menu panel
        gamePanel = new Game(this);
        menuPanel = new MainMenu(this);
        
        // Add the panels to the card panel
        cardPanel.add(gamePanel, "game");
        cardPanel.add(menuPanel, "menu");
        
        // Add the card panel to the frame
        add(cardPanel, BorderLayout.CENTER);
        
        // Show the menu panel initially
        cardLayout.show(cardPanel, "menu");
        
        // Show the frame
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
    
    // method to show the game panel
    public void showGame() {
        // Show the game panel
        cardLayout.show(cardPanel, "game");

        // Set the focus to the game panel
        this.setFocusable(true);
        this.requestFocusInWindow();
        
        // Start the game
        SwingUtilities.invokeLater(() -> {
            KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            System.out.println(focusManager.getFocusOwner());
            registerMovementInput();
            gamePanel.start();
        });
    }

    // call the movement input method in the game panel
    public void registerMovementInput() {
        gamePanel.registerMovementInput();
    }
    

    // main method
    public static void main(String[] args) {
        // Game game = new Game();
        // game.gameLoop();

        SwingUtilities.invokeLater(Main::new);
    }
}


/*
TODO
menu - 1

    level selector
    settings
        keybinds maybe
moving objects - physics system, onCollision(){if(moveable) add force} - 2
enemies - 3
fighting system - 4
Class for levels - 5
storage of some sort - 6
objective, win condition, and just generally what the game is
level editor maybe?
*/