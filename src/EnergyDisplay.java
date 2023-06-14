package src;

// Import necessary libraries
import java.awt.Graphics2D;

import src.Objects.Player;

// EnergyDisplay class
public class EnergyDisplay {
    // Variables for x, y, width, height, barWidth, barHeight, and energy
    private int x;
    private int y;
    private int width = 100;
    private int height = 30;
    private int barWidth = width - 10;
    private int barHeight = height - 10;

    private double energy;

    private Player player;

    // Constructor
    public EnergyDisplay(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;

        this.energy = player.getEnergy();
    }

    // update method
    public void update() {
        energy = player.getEnergy();
        barWidth = (int) ((energy/10) * (width - 10));
    }

    // draw the energy bar
    public void draw(Graphics2D g){
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(x, y, width, height);
        g.setColor(java.awt.Color.GREEN);
        g.fillRect(x + 5, y + 5, barWidth, barHeight);
    }
}
