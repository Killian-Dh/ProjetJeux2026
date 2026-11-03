package jumpingthomas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {

    public static final int HAUTEUR = 800;
    public static final int LARGEUR = HAUTEUR * 9 / 16;
    public static final int HORIZON = HAUTEUR / 3;
    public static final int TAILLE = 50;

    private double y = HAUTEUR;

     public Game() {
    setPreferredSize(new Dimension(LARGEUR, HAUTEUR));

    Timer timer = new Timer(16, e -> {
        y = y - 5;
        repaint();
    });
    timer.start();
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(135, 206, 235)); // ciel bleu
        g.fillRect(0, 0, LARGEUR, HAUTEUR);

        g.setColor(new Color(34, 139, 34)); // verte
        g.fillRect(0, HORIZON, LARGEUR, HAUTEUR - HORIZON);

        g.setColor(Color.RED);
        g.fillRect(LARGEUR / 2 - TAILLE / 2, (int) y, TAILLE, TAILLE);

}

}

