package jumpingthomas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
//import java.awt.Graphics2D;


import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {

    public static final int HAUTEUR = 800;
    public static final int LARGEUR = HAUTEUR * 9 / 16;
    public static final int HORIZON = HAUTEUR / 3;
    public static final int TAILLE = 50;

    public static final int JOUEUR_Y = HAUTEUR - 150;
    private int couloir = 0; // joueur au centre
    public boolean allerGauche;
    public boolean allerDroite;

     public Game() {
    setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
    setFocusable(true);
    Timer timer = new Timer(16, e -> 
    {
        if (couloir > -1 && allerGauche == true)
        {
            couloir -= 1;
            allerGauche = false;
        }
        if (couloir < 1 && allerDroite == true)
        {
            couloir += 1;
            allerDroite = false;
        }
        repaint();
    });
    timer.start();
    
    
    
    int centreX = LARGEUR / 2 + couloir * LARGEUR / 3;   

}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(135, 206, 235)); // ciel bleu
        g.fillRect(0, 0, LARGEUR, HAUTEUR);

        g.setColor(new Color(34, 139, 34)); // verte
        g.fillRect(0, HORIZON, LARGEUR, HAUTEUR - HORIZON);

        g.setColor(Color.RED);
        g.fillRect(centreX - TAILLE / 2, JOUEUR_Y, TAILLE, TAILLE);
}

}

