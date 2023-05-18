import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Game gamePanel;
    private MainMenu menuPanel;
    
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
        
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
    
    public void showGame() {
        cardLayout.show(cardPanel, "game");
    
        if (!cardPanel.isAncestorOf(gamePanel)) {
            System.out.println("gamePanel is not added to the panel hierarchy.");
            return;
        }
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        
        SwingUtilities.invokeLater(() -> {
            KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            System.out.println(focusManager.getFocusOwner());
            gamePanel.registerMovementInput();
            gamePanel.start();
        });
    }

    public void registerMovementInput() {
        gamePanel.registerMovementInput();
    }

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