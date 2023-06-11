package src;
// basic imports for swing
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// main class
public class Main extends JFrame{
    // private variables for the panels and card layout
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Game gamePanel;
    private Menu menuPanel;
    private Menu pausePanel;
    private Menu editorPausePanel;
    private Menu levelMenu;
    private ArrayList<Menu> levelMenus;
    private LevelEditor levelEditorPanel;
    private JPanel currentPanel;
    private int levelMenuPage = 0;
    
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
        this.levelMenus = new ArrayList<Menu>();
        gamePanel = new Game(this);
        levelEditorPanel = new LevelEditor(this, gamePanel.getGameManager());
        createMainMenu();
        createPauseMenu();
        createEditorPauseMenu();
        
        // Add the panels to the card panel
        cardPanel.add(gamePanel, "game");
        cardPanel.add(levelEditorPanel, "levelEditor");
        cardPanel.add(menuPanel, "menu");
        cardPanel.add(pausePanel, "pause");
        cardPanel.add(editorPausePanel, "editorPause");
        cardPanel.add(levelMenu, "levelMenu");
        
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
            showGame(true, 0);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,100}, true, false);

        menuPanel.addButton("Level Menu", (a,b) -> {
            showLevelMenu();
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,200}, true, false);

        menuPanel.addButton("Exit", (a,b) -> {
            System.exit(0);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,300}, true, false);
    }

    // method to create the pause panel
    public void createPauseMenu(){
        // Create the pause panel
        pausePanel = new Menu(this, Color.BLUE, null);

        // Add the buttons to the pause panel
        pausePanel.addButton("Resume", (a,b) -> {
            showGame(false, 0);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,100}, true, false);

        pausePanel.addButton("Restart", (a,b) -> {
            showGame(true, gamePanel.getLevelManager().getCurrentLevel());
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,200}, true, false);

        pausePanel.addButton("Main Menu", (a,b) -> {
            showMainMenu();
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,300}, true, false);

        pausePanel.addButton("Exit", (a,b) -> {
            System.exit(0);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,400}, true, false);
    }

    public void createEditorPauseMenu(){
        // Create the pause panel
        editorPausePanel = new Menu(this, Color.BLUE, null);

        // Add the buttons to the pause panel
        editorPausePanel.addButton("Resume", (a,b) -> {
            showLevelEditor(false, false, false, "");
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-172,200}, false, false);

        editorPausePanel.addButton("Restart", (a,b) -> {
            showLevelEditor(true, levelEditorPanel.getLevelEditorManager().getLevel().isNewLevel(), false, "");
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)+22,200}, false, false);

        editorPausePanel.addButton("Level Menu", (a,b) -> {
            showLevelMenu();
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-172,300}, false, false);

        editorPausePanel.addButton("Main Menu", (a,b) -> {
            showMainMenu();
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)+22,300}, false, false);

        editorPausePanel.addButton("Save", (a,b) -> {
            levelEditorPanel.getLevelEditorManager().save();
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-172,400}, false, false);

        editorPausePanel.addButton("Exit", (a,b) -> {
            System.exit(0);
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)+22,400}, false, false);
    }

    public void createLevelMenu(ArrayList<ArrayList<Level>> levels){
        for(ArrayList<Level> levelPage : levels){
            levelMenus.add(levelMenu(levelPage));
        }
        levelMenu = levelMenus.get(0);
    }

    public Menu levelMenu(ArrayList<Level> levels){
        Menu newLevelMenu = new Menu(this, Color.MAGENTA, null);
        for(int i = 0; i < levels.size(); i++){
            Level level = levels.get(i);
            newLevelMenu.addButton(level.getLevelName(), (a,b) -> {
                showGame(true, levels.indexOf(level));
                return 1;
            }, new int[]{100,60}, new int[]{150+((i%6)*175),200+(int)((Math.floor(i/6))*100)}, false, false);

            newLevelMenu.addButton("Edit", (a,b) -> {
                showLevelEditor(true, false, false, levels.get(levels.indexOf(level)).getLevelName());
                return 1;
            }, new int[]{50,60}, new int[]{150+100+((i%6)*175),200+(int)((Math.floor(i/6))*100)}, false, true);
        }

        newLevelMenu.addButton("New Level", (a,b) -> {
            showLevelEditor(true, true, true, "");
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,575}, true, false);

        newLevelMenu.addButton("Back", (a,b) -> {
            showMainMenu();
            return 1;
        }, new int[]{150,60}, new int[]{(getSize().width/2)-50,650}, true, false);

        int rightX = 625;
        int rightY = 150;
        JButton rightButton = new TriangleButton(new int[]{rightX,rightX+50,rightX}, new int[]{rightY,rightY+50,rightY+100});
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the menu panel
                SwingUtilities.invokeLater(() -> {
                    // add the method given to the action listener
                    if(levelMenuPage < levelMenus.size()-1){
                        levelMenuPage++;
                        cardPanel.remove(levelMenu);
                        levelMenu = levelMenus.get(levelMenuPage);
                        cardPanel.add(levelMenu, "levelMenu");
                        showLevelMenu();
                    }
                });
            }
        });
        rightButton.setBounds(rightX,rightY,rightX+50,rightY+100);
        newLevelMenu.add(rightButton);
        
        int leftX = 25;
        int leftY = 150;
        JButton leftButton = new TriangleButton(new int[]{leftX+50,leftX,leftX+50}, new int[]{leftY,leftY+50,leftY+100});
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the menu panel
                SwingUtilities.invokeLater(() -> {
                    // add the method given to the action listener
                    if(levelMenuPage > 0){
                        levelMenuPage--;
                        cardPanel.remove(levelMenu);
                        levelMenu = levelMenus.get(levelMenuPage);
                        cardPanel.add(levelMenu, "levelMenu");
                        showLevelMenu();
                    }
                });
            }
        });
        leftButton.setBounds(leftX,leftY,leftX+50,leftY+100);
        newLevelMenu.add(leftButton);


        return newLevelMenu;
    }

    // method to show the game panel
    public void showGame(boolean restart, int levelIndex) {
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
            gamePanel.start(restart, levelIndex);
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

    public void showLevelEditor(boolean restart, boolean newLevel, boolean askForName, String levelName){
        if(askForName) levelName = JOptionPane.showInputDialog(this, "Enter a name for the new level", "New Level", JOptionPane.PLAIN_MESSAGE);
        
        final String levelNameFinal = levelName;

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
            levelEditorPanel.start(restart, newLevel, levelNameFinal);
        });
    }

    public void showLevelMenu(){
        // Show the level menu panel
        cardLayout.show(cardPanel, "levelMenu");
        currentPanel = levelMenu;

        // Set the focus to the level menu panel
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.focusNextComponent();
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
class TriangleButton extends JButton {

    private Polygon triangle;

    public TriangleButton(int[] xPoints, int[] yPoints) {
        super();

        triangle = new Polygon(xPoints, yPoints, 3);

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillPolygon(triangle);
    }

    @Override
    public boolean contains(int x, int y) {
        return triangle.contains(x, y);
    }
}