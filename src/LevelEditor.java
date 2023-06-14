package src;

// Imports
import javax.swing.*;

import src.Objects.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

// LevelEditor class
public class LevelEditor extends JPanel{
    // Variables
    private BufferedImage offScreenBuffer;
    private Main main;
    private Thread editorThread;
    private LevelEditorManager levelEditorManager;
    private LevelManager levelManager;
    private LevelMenuBar levelMenuBar;
    private GameManager gameManager;

    private boolean running = false;

    // Constructor
    public LevelEditor(Main main, GameManager gameManager){
        // Set the background color and layout
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setFocusable(true);

        this.main = main;
        this.gameManager = gameManager;
        levelEditorManager = new LevelEditorManager(this);
        levelManager = new LevelManager(gameManager, main, true);
        levelMenuBar = new LevelMenuBar(levelEditorManager);
        
        offScreenBuffer = new BufferedImage(main.getSize().width, main.getSize().height, BufferedImage.TYPE_INT_ARGB);
    }

    // start method
    public void start(boolean restart, boolean newLevel, String levelName){
        // If the level is being restarted
        if(restart){
            // If the editor thread is running, interrupt it
            if(editorThread != null && editorThread.isAlive()){
                editorThread.interrupt();
            }

            // If the level is new start a new level, otherwise start the level with the given name
            if(newLevel){
                levelEditorManager.start(new Level(levelName, levelManager, gameManager, true, false));
            }else{
                levelEditorManager.start(levelManager.getLevelByName(levelName));
            }
            
        }
        editorThread = new Thread(() -> {
            editorLoop();
        });
        running = true;
        editorThread.start();
    }

    // stop method
    public void stop(){
        running = false;
    }

    // editorLoop method
    public void editorLoop(){
        while(running){
            levelEditorManager.update();
            render();
        }
    }

    // render method
    public void render(){
        Graphics2D gb = offScreenBuffer.createGraphics();

        // Clear the screen
        gb.setColor(Color.WHITE);
        gb.fillRect(0, 0, main.getSize().width, main.getSize().height);
        
        ArrayList<EditorObject> editorObjects = levelEditorManager.getEditorObjects();
        // tempEditorObjects is used to avoid a ConcurrentModificationException
        ArrayList<EditorObject> tempEditorObjects = new ArrayList<EditorObject>();
        for(EditorObject editorObject : editorObjects){
            tempEditorObjects.add(editorObject);
        }
        for(EditorObject editorObject : tempEditorObjects){
            editorObject.draw(gb);
        }
        levelMenuBar.draw(gb);

        // Draw the off screen buffer to the screen
        Graphics2D panelGraphics = (Graphics2D) getGraphics();
        if (panelGraphics != null) {
            panelGraphics.drawImage(offScreenBuffer, 0, 0, null);
            panelGraphics.dispose();
        }
    }

    // registerMovementInput method
    public void registerMovementInput(){
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // escape key shows the pause menu
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapeAction");
        actionMap.put("escapeAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
                main.showEditorPauseMenu();
            }
        });

        // mouse click calls the mouse action method in LevelEditorManager
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                // Call the mouse action method in GameManager
                levelEditorManager.mouseAction();
            }
        });
    }

    // Getters and Setters
    public JFrame getGameWindow(){
        return main;
    }

    public LevelMenuBar getLevelMenuBar(){
        return levelMenuBar;
    }

    public LevelManager getLevelManager(){
        return levelManager;
    }

    public LevelEditorManager getLevelEditorManager(){
        return levelEditorManager;
    }
}
