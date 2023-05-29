package src;
// basic imports for swing
import javax.swing.*;
import java.awt.*;

// main class
public class Main extends JFrame{
    // private variables for the panels and card layout
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Game gamePanel;
    private Menu menuPanel;
    private Menu pausePanel;
    private Menu editorPausePanel;
    private LevelEditor levelEditorPanel;
    private JPanel currentPanel;
    
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
        
        // Create the panels
        gamePanel = new Game(this);
        levelEditorPanel = new LevelEditor(this);
        createMainMenu();
        createPauseMenu();
        createEditorPauseMenu();
        
        // Add the panels to the card panel
        cardPanel.add(gamePanel, "game");
        cardPanel.add(levelEditorPanel, "levelEditor");
        cardPanel.add(menuPanel, "menu");
        cardPanel.add(pausePanel, "pause");
        cardPanel.add(editorPausePanel, "editorPause");
        
        // Add the card panel to the frame
        add(cardPanel, BorderLayout.CENTER);

        // Show the menu panel initially
        cardLayout.show(cardPanel, "menu");
        currentPanel = menuPanel;


        // Show the frame
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
    
    // method to create the menu panel
    public void createMainMenu(){
        // Create the menu panel
        menuPanel = new Menu(this, Color.GREEN, null);

        // Add the buttons to the menu panel
        menuPanel.addButton("Play", (a,b) -> {
            showGame(true);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,100}, true);

        menuPanel.addButton("Level Editor", (a,b) -> {
            showLevelEditor(true);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,200}, true);

        menuPanel.addButton("Exit", (a,b) -> {
            System.exit(0);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,300}, true);
    }

    // method to create the pause panel
    public void createPauseMenu(){
        // Create the pause panel
        pausePanel = new Menu(this, Color.BLUE, null);

        // Add the buttons to the pause panel
        pausePanel.addButton("Resume", (a,b) -> {
            showGame(false);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,100}, true);

        pausePanel.addButton("Restart", (a,b) -> {
            showGame(true);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,200}, true);

        pausePanel.addButton("Main Menu", (a,b) -> {
            showMainMenu();
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,300}, true);

        pausePanel.addButton("Exit", (a,b) -> {
            System.exit(0);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,400}, true);
    }

    public void createEditorPauseMenu(){
        // Create the pause panel
        editorPausePanel = new Menu(this, Color.BLUE, null);

        // Add the buttons to the pause panel
        editorPausePanel.addButton("Resume", (a,b) -> {
            showLevelEditor(false);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,100}, true);

        editorPausePanel.addButton("Restart", (a,b) -> {
            showLevelEditor(true);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,200}, true);

        editorPausePanel.addButton("Main Menu", (a,b) -> {
            showMainMenu();
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,300}, true);

        editorPausePanel.addButton("Exit", (a,b) -> {
            System.exit(0);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,400}, true);
    }

    // method to show the game panel
    public void showGame(boolean restart) {
        // Show the game panel
        cardLayout.show(cardPanel, "game");
        currentPanel = gamePanel;

        // Set the focus to the game panel
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        
        registerMovementInput();

        // Start the game
        SwingUtilities.invokeLater(() -> {
            KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            focusManager.focusNextComponent();
            gamePanel.start(restart);
        });
    }

    // method to show the pause panel
    public void showPauseMenu(){
        // Show the pause panel
        cardLayout.show(cardPanel, "pause");
        currentPanel = pausePanel;

        // Set the focus to the pause panel
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.focusNextComponent();
    }

    // method to show the menu panel
    public void showMainMenu(){
        // Show the menu panel
        cardLayout.show(cardPanel, "menu");
        currentPanel = menuPanel;

        // Set the focus to the menu panel
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.focusNextComponent();
    }

    public void showEditorPauseMenu(){
        // Show the pause panel
        cardLayout.show(cardPanel, "editorPause");
        currentPanel = editorPausePanel;

        // Set the focus to the pause panel
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.focusNextComponent();
    }

    public void showLevelEditor(boolean restart){
        // Show the level editor panel
        cardLayout.show(cardPanel, "levelEditor");
        currentPanel = levelEditorPanel;

        // Set the focus to the level editor panel
        levelEditorPanel.setFocusable(true);
        levelEditorPanel.requestFocusInWindow();

        registerMovementInput();

        // Start the level editor
        SwingUtilities.invokeLater(() -> {
            KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            focusManager.focusNextComponent();
            levelEditorPanel.start(restart);
        });
    }

    // call the movement input method in the game panel
    public void registerMovementInput() {
        if(currentPanel == gamePanel) gamePanel.registerMovementInput();
        else if(currentPanel == levelEditorPanel) levelEditorPanel.registerMovementInput();
    }

    // main method
    public static void main(String[] args) {
        // Set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Create the main frame
        SwingUtilities.invokeLater(Main::new);
    }
}


/*
TODO
moving objects - physics system, onCollision(){if(moveable) add force} - 2
movement abilities -3
Class for levels - 4
storage of some sort - 5
level selector - 6
objective, win condition, and just generally what the game is

maybe:
level editor
enemies 
fighting system
*/