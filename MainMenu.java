import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MainMenu extends JPanel{
    private Main main;
    private JButton playButton;

    public MainMenu(Main main){
        this.main = main;

        setBackground(Color.GREEN);
        setLayout(null);

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
        int[] buttonSize = new int[]{100,60};
        playButton.setBounds((main.getSize().width/2)-buttonSize[0]/2,100,buttonSize[0],buttonSize[1]);
        add(playButton, BorderLayout.CENTER);
    }
}
