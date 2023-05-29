package src;

import java.util.ArrayList;

import javax.swing.JFrame;

import src.Objects.*;

public class LevelEditorManager {
    private LevelEditor levelEditor;
    private ArrayList<EditorObject> editorObjects;
    private EditorObject selectedObject;

    public LevelEditorManager(LevelEditor levelEditor){
        this.levelEditor = levelEditor;
    }
    
    public void start(Level level){
        editorObjects = new ArrayList<EditorObject>();
        ArrayList<GameObject> gameObjects = level.getGameObjects();
        for(GameObject gameObject : gameObjects){
            editorObjects.add(convertToEditorObject(gameObject));
        }
    }

    public void update(){
        for(EditorObject editorObject : editorObjects){
            editorObject.update();
        }
    }

    public void mouseAction(){
        for(EditorObject editorObject : editorObjects){
            if(editorObject.isMouseOver()){
                if(editorObject.getFocus()){
                    editorObject.setFocus(false);
                    selectedObject = null;
                }else {
                    if(selectedObject != null) selectedObject.setFocus(false);
                    editorObject.setFocus(true);
                    selectedObject = editorObject;
                }
            }
        }
        for(LevelMenuOption levelMenuOption : levelEditor.getLevelMenuBar().getLevelMenuOptions()){
            if(levelMenuOption.isMouseOver()){
                levelMenuOption.mouseAction();
            }
        }
    }

    private EditorObject convertToEditorObject(GameObject gameObject){
        EditorObject editorObject = new EditorObject(gameObject.getX(), gameObject.getY(), gameObject.getColors(), gameObject.getShapes(), gameObject.getShapeQ(), gameObject.getImage(), gameObject.hasGravity(), gameObject.isPhysicsObject(), gameObject.isCollisionObject(), this, gameObject.ClassName());
        return editorObject;
    }

    public ArrayList<EditorObject> getEditorObjects(){
        return editorObjects;
    }

    public JFrame getGameWindow(){
        return levelEditor.getGameWindow();
    }

    public void setEditorObjects(ArrayList<EditorObject> editorObjects){
        this.editorObjects = editorObjects;
    }
}