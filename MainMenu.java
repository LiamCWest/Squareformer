import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MainMenu extends JPanel{
    private Main main;
    private JButton playButton;

    public MainMenu(Main main){
        this.main = main;

        setBackground(Color.GREEN);
        setLayout(new BorderLayout());

        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the menu panel
                SwingUtilities.invokeLater(() -> {
                    main.showGame();
                });
            }
        });
        add(playButton, BorderLayout.CENTER);
    }

    public JButton getPlayButton() {
        return (JButton) getComponent(0);
    }
}
