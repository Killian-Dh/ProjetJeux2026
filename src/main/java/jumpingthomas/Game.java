package jumpingthomas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {

    public static final int HAUTEUR = 800;
    public static final int LARGEUR = HAUTEUR * 9 / 16;
    public static final int HORIZON = HAUTEUR / 3;
    public static final int TAILLE = 110;

    public static final int JOUEUR_Y = HAUTEUR - 200;
    //Sprite du joueur
    private Image idle = new ImageIcon("cycliste_idle.png").getImage();
    private Image saut = new ImageIcon("cycliste_saute.png").getImage();
    private Image glissade = new ImageIcon("cycliste_glissade.png").getImage();
    private int couloir = 0; // joueur au centre
    private int positionJoueurX=0;
    
    public boolean allerGauche;
    public boolean allerDroite;
    
    private int hauteurSaut = 0; // hauteur du saut
    private boolean enSaut = false;

    private boolean enGlissade = false; // état de glissade
    public boolean intouchable = false; // état d'invincibilité

    // Obstacles (Killian)
    private GenerateurObstacles generateur = new GenerateurObstacles();
    private double vitesse = 0.5; // vitesse de départ, augmente au fil de la partie
    private boolean gameOver = false;
    private int imagesDepuisExplosion = 0;  // compteur pour animer l'explosion

     public Game() {
    setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
    

    setFocusable(true);
        Timer timer = new Timer(16, e -> 
    {
        if (gameOver) {
            imagesDepuisExplosion++;   // seule l'explosion avance
            repaint();
            return;                    // le reste du jeu est figé
        }

        changerVoie();
        
        positionJoueurX = LARGEUR / 2 + couloir * LARGEUR / 3; 

        vitesse = vitesse + 0.0005;          // accélère un peu à chaque image
        generateur.mettreAJour(vitesse);     // fait avancer / crée / supprime les obstacles

        if (generateur.toucheJoueur(couloir, enSaut)) {
            gameOver = true;
            imagesDepuisExplosion = 0;
        }

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

        generateur.dessiner(g); // obstacles AVANT le joueur, pour que le joueur passe devant

        g.setColor(Color.RED);
        int centreX = positionJoueurX;
        int centreY = JOUEUR_Y;
        //g.fillRect(centreX - TAILLE / 2, JOUEUR_Y, TAILLE, TAILLE);
        if (enGlissade) 
        {
            int largeur = (int)(1.5*TAILLE);
            int hauteur = (int)(2.0*TAILLE * 460.0 / 724.0); // proportion de l'image
            g.drawImage(glissade, centreX - largeur/2, centreY, largeur, hauteur, this);
        } 
        else if (enSaut) 
        {
            int largeur = TAILLE;
            int hauteur = (int)(2.0*TAILLE * 424.0 / 724.0); // proportion de l'image
            g.drawImage(saut, centreX - largeur/2, centreY + hauteurSaut, largeur, hauteur, this);
        } 
        else 
        {
            int largeur = TAILLE;
            int hauteur = (int)(2.0*TAILLE * 402.0 / 724.0); // proportion de l'image
            g.drawImage(idle, centreX - largeur/2, centreY, largeur, hauteur, this);
        }
            if (gameOver) {
            dessinerGameOver(g);
        }    
        
}
public void sauter() {

    if (!enSaut) {
        enSaut = true;
        hauteurSaut = -80;

        Timer timerSaut = new Timer(700, e -> {
            hauteurSaut = 0;
            enSaut = false;
            repaint();
        });

        timerSaut.setRepeats(false);
        timerSaut.start();
    }
    
}
private void changerVoie(){
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

    }
    public void glisser(){
        if (!enGlissade) {
        enGlissade = true;
        intouchable = true;

        Timer timerGlisse = new Timer(700, e -> {
            enGlissade = false;
            intouchable = false;    
            repaint();
        });

        timerGlisse.setRepeats(false);
        timerGlisse.start();
    }
    }

        private void dessinerGameOver(Graphics g) {
        int x = positionJoueurX;
        int y = JOUEUR_Y + TAILLE / 2;

        if (imagesDepuisExplosion < 40) {
            // Explosion : deux cercles qui grossissent (40 images, environ 0,6 seconde)
            int rayon = imagesDepuisExplosion * 4;
            g.setColor(Color.ORANGE);
            g.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
            g.setColor(Color.YELLOW);
            g.fillOval(x - rayon / 2, y - rayon / 2, rayon, rayon);
        } else {
            // Voile noir transparent sur tout l'écran
            g.setColor(new Color(0, 0, 0, 150));  // le 4e nombre = la transparence (0 à 255)
            g.fillRect(0, 0, LARGEUR, HAUTEUR);

            // "GAME OVER" centré
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            FontMetrics fm = g.getFontMetrics();
            String texte = "GAME OVER";
            g.drawString(texte, (LARGEUR - fm.stringWidth(texte)) / 2, HAUTEUR / 2);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 22));
            fm = g.getFontMetrics();
            texte = "Appuie sur Entrée pour rejouer";
            g.drawString(texte, (LARGEUR - fm.stringWidth(texte)) / 2, HAUTEUR / 2 + 50);
        }
    }

    public void rejouer() {
        if (!gameOver) {
            return;  // Entrée ne fait rien pendant la partie
        }
        generateur.reinitialiser();
        vitesse = 0.5;
        couloir = 0;
        allerGauche = false;
        allerDroite = false;
        gameOver = false;
    }
}