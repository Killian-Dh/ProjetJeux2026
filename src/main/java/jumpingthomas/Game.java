package jumpingthomas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {

    public static final int HAUTEUR = 800;
    public static final int LARGEUR = HAUTEUR * 9 / 16;
    public static final int HORIZON = HAUTEUR / 3;
    public static final int TAILLE = 50;

    public static final int JOUEUR_Y = HAUTEUR - 150;
    private int couloir = 0;

    private GenerateurObstacles generateur = new GenerateurObstacles();    
    private double vitesse = 0.5;                       

    public Game() {
        setPreferredSize(new Dimension(LARGEUR, HAUTEUR));

        Timer timer = new Timer(16, e -> {
            vitesse = vitesse + 0.0005;
            generateur.mettreAJour(vitesse);
            repaint();
        });
        timer.start();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    couloir = couloir - 1;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    couloir = couloir + 1;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(135, 206, 235)); // ciel bleu
        g.fillRect(0, 0, LARGEUR, HAUTEUR);

        g.setColor(new Color(34, 139, 34)); // verte
        g.fillRect(0, HORIZON, LARGEUR, HAUTEUR - HORIZON);

        generateur.dessiner(g);

        g.setColor(Color.RED);
        int centreX = LARGEUR / 2 + couloir * LARGEUR / 3;
        g.fillRect(centreX - TAILLE / 2, JOUEUR_Y, TAILLE, TAILLE);
    }
}