import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MainMenu extends JPanel{
    private Main main;

    public MainMenu(Main main){
        this.main = main;

        setBackground(Color.GREEN);
        setLayout(new BorderLayout());

        JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the menu panel
                playButton.setFocusable(false);
                playButton.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
                playButton.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
                playButton.removeActionListener(playButton.getActionListeners()[0]);
                main.showGame();
            }
        });
        add(playButton, BorderLayout.CENTER);
    }

    public JButton getPlayButton() {
        return (JButton) getComponent(0);
    }
}
