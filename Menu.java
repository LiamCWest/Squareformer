import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.function.BinaryOperator;

public class Menu extends JPanel{
    private Main main;

    public Menu(Main main, Color color, LayoutManager layout){
        this.main = main;

        setBackground(color);
        setLayout(layout);
    }

    public <T> void addButton(String name, BinaryOperator<Integer> method, int[] buttonSize, int[] buttonPosition, Boolean isCentered){
        JButton button = new JButton(name);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the menu panel
                SwingUtilities.invokeLater(() -> {
                    method.apply(1,1);
                });
            }
        });
        if(isCentered) buttonPosition[0] = (main.getSize().width/2)-buttonSize[0]/2;
        button.setBounds(buttonPosition[0],buttonPosition[1],buttonSize[0],buttonSize[1]);
        add(button, BorderLayout.CENTER);
    }
}