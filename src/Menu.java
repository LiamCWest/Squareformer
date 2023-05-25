package src;
// imports for swing and awt
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.function.BinaryOperator;

// Menu class
public class Menu extends JPanel{
    private Main main; // private variable for the main class

    // constructor
    public Menu(Main main, Color color, LayoutManager layout){
        this.main = main; // Set the private variable for the main class

        setBackground(color); // Set the background color
        setLayout(layout); // Set the layout
    }

    // method to add a button to the menu panel
    public <T> void addButton(String name, BinaryOperator<Integer> method, int[] buttonSize, int[] buttonPosition, Boolean isCentered){
        JButton button = new JButton(name); // Create the button

        // Add the action listener
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the menu panel
                SwingUtilities.invokeLater(() -> {
                    // add the method given to the action listener
                    method.apply(1,1);
                });
            }
        });
        // Set the button size and position
        if(isCentered) buttonPosition[0] = (main.getSize().width/2)-buttonSize[0]/2;
        button.setBounds(buttonPosition[0],buttonPosition[1],buttonSize[0],buttonSize[1]);
        add(button, BorderLayout.CENTER); // Add the button to the menu panel
    }
}