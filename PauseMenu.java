import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PauseMenu extends JPanel{
    private Main main;

    public PauseMenu(Main main){
        this.main = main;

        setBackground(Color.GREEN);
        setLayout(null);
    }

    public void button(String name){
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the menu panel
                SwingUtilities.invokeLater(() -> {
                    main.showGame();
                });
            }
        });
        int[] buttonSize = new int[]{100,60};
        button.setBounds((main.getSize().width/2)-buttonSize[0]/2,100,buttonSize[0],buttonSize[1]);
        add(button, BorderLayout.CENTER);
    }
}
