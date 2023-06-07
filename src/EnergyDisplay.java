package src;

import java.awt.Graphics2D;

import src.Objects.Player;

public class EnergyDisplay {
    private int x;
    private int y;
    private int width = 100;
    private int height = 30;
    private int barWidth = width - 10;
    private int barHeight = height - 10;

    private double energy;

    private Player player;

    public EnergyDisplay(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;

        this.energy = player.getEnergy();
    }

    public void update() {
        energy = player.getEnergy();
        barWidth = (int) ((energy/10) * (width - 10));
    }

    public void draw(Graphics2D g){
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(x, y, width, height);
        g.setColor(java.awt.Color.GREEN);
        g.fillRect(x + 5, y + 5, barWidth, barHeight);
    }
}
