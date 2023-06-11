package src;

import javax.swing.*;

import src.Objects.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelEditor extends JPanel{
    private BufferedImage offScreenBuffer;
    private Main main;
    private Thread editorThread;
    private LevelEditorManager levelEditorManager;
    private LevelManager levelManager;
    private LevelMenuBar levelMenuBar;
    private GameManager gameManager;

    private boolean running = false;

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

    public void start(boolean restart, boolean newLevel, String levelName){
        if(restart){
            if(newLevel){
                levelEditorManager.start(new Level(levelName, levelManager, gameManager, true, false));
            }else{
                levelEditorManager.start(levelManager.getLevelByName(levelName));
            }
            
            if(editorThread != null && editorThread.isAlive()){
                editorThread.interrupt();
            }
        }
        editorThread = new Thread(() -> {
            editorLoop();
        });
        running = true;
        editorThread.start();
    }

    public void stop(){
        running = false;
    }

    public void editorLoop(){
        while(running){
            levelEditorManager.update();
            render();
        }
    }

    public void render(){
        Graphics2D gb = offScreenBuffer.createGraphics();

        gb.setColor(Color.WHITE);
        gb.fillRect(0, 0, main.getSize().width, main.getSize().height);
        
        ArrayList<EditorObject> editorObjects = levelEditorManager.getEditorObjects();
        ArrayList<EditorObject> tempEditorObjects = new ArrayList<EditorObject>();
        for(EditorObject editorObject : editorObjects){
            tempEditorObjects.add(editorObject);
        }
        for(EditorObject editorObject : tempEditorObjects){
            editorObject.draw(gb);
        }
        levelMenuBar.draw(gb);

        Graphics2D panelGraphics = (Graphics2D) getGraphics();
        if (panelGraphics != null) {
            panelGraphics.drawImage(offScreenBuffer, 0, 0, null);
            panelGraphics.dispose();
        }
    }

    public void registerMovementInput(){
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapeAction");
        actionMap.put("escapeAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
                main.showEditorPauseMenu();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                // Call the mouse action method in GameManager
                levelEditorManager.mouseAction();
            }
        });
    }

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
