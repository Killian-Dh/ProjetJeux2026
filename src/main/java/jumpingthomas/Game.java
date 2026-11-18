
package jumpingthomas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;

public class Game extends JPanel {

    public static final int HAUTEUR = 800;
    public static final int LARGEUR = HAUTEUR * 9 / 16;
    public static final int HORIZON = HAUTEUR / 3;
    public static final int TAILLE = 110;

    public static final int JOUEUR_Y = HAUTEUR - 200;

    // ==============================
    // JOUEUR (code de l'equipe)
    // ==============================

    private Image idle = new ImageIcon("cycliste_idle.png").getImage();
    private Image saut = new ImageIcon("cycliste_saute.png").getImage();
    private Image glissade = new ImageIcon("cycliste_glissade.png").getImage();

    private int couloir = 0;
    private int positionJoueurX = 0;

    public boolean allerGauche;
    public boolean allerDroite;

    private int hauteurSaut = 0;
    private boolean enSaut = false;

    private boolean enGlissade = false;
    public boolean intouchable = false;

    // ==============================
    // OBSTACLES (Killian)
    // ==============================

    private GenerateurObstacles generateur = new GenerateurObstacles();

    private double vitesse = 0.5;
    private boolean gameOver = false;

    private int imagesDepuisExplosion = 0;
    private Image[] explosion;

    // ==============================
    // NOUVEAU : ROUTE ARC-EN-CIEL
    // ==============================

    private RouteRenderer route;

    // ==============================
    // CONSTRUCTEUR
    // ==============================

    public Game() {

        setPreferredSize(new Dimension(LARGEUR, HAUTEUR));

        chargerExplosion();

        // NOUVEAU : initialisation du decor
        route = new RouteRenderer(LARGEUR, HAUTEUR, HORIZON);

        setFocusable(true);

        Timer timer = new Timer(16, e -> {

            if (gameOver) {
                imagesDepuisExplosion++;
                repaint();
                return;
            }

            changerVoie();

            positionJoueurX =
                LARGEUR / 2 + couloir * LARGEUR / 3;

            // Acceleration du jeu
            vitesse = vitesse + 0.0005;

            // Obstacles
            generateur.mettreAJour(vitesse);

            // NOUVEAU : defilement de la route
            route.mettreAJour(vitesse);

            // Collisions
            if (generateur.toucheJoueur(couloir, enSaut)) {
                gameOver = true;
                imagesDepuisExplosion = 0;
            }

            repaint();
        });

        timer.start();
    }

    // ==============================
    // AFFICHAGE
    // ==============================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // NOUVEAU : fond spatial + Rainbow Road
        // Remplace uniquement l'ancien ciel et sol vert
        route.dessiner(g, this);

        // Obstacles devant la route
        generateur.dessiner(g);

        // ==============================
        // JOUEUR (INCHANGE)
        // ==============================

        g.setColor(Color.RED);

        int centreX = positionJoueurX;
        int centreY = JOUEUR_Y;

        if (enGlissade) {

            int largeur = (int) (1.5 * TAILLE);

            int hauteur = (int) (
                2.0 * TAILLE * 460.0 / 724.0
            );

            g.drawImage(
                glissade,
                centreX - largeur / 2,
                centreY,
                largeur,
                hauteur,
                this
            );

        } else if (enSaut) {

            int largeur = TAILLE;

            int hauteur = (int) (
                2.0 * TAILLE * 424.0 / 724.0
            );

            g.drawImage(
                saut,
                centreX - largeur / 2,
                centreY + hauteurSaut,
                largeur,
                hauteur,
                this
            );

        } else {

            int largeur = TAILLE;

            int hauteur = (int) (
                2.0 * TAILLE * 402.0 / 724.0
            );

            g.drawImage(
                idle,
                centreX - largeur / 2,
                centreY,
                largeur,
                hauteur,
                this
            );
        }

        if (gameOver) {
            dessinerGameOver(g);
        }
    }

    // ==============================
    // SAUT (INCHANGE)
    // ==============================

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

    // ==============================
    // CHANGEMENT DE VOIE
    // ==============================

    private void changerVoie() {

        if (couloir > -1 && allerGauche == true) {
            couloir -= 1;
            allerGauche = false;
        }

        if (couloir < 1 && allerDroite == true) {
            couloir += 1;
            allerDroite = false;
        }
    }

    // ==============================
    // GLISSADE (INCHANGE)
    // ==============================

    public void glisser() {

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

    // ==============================
    // GAME OVER (INCHANGE)
    // ==============================

    private void dessinerGameOver(Graphics g) {

        int x = positionJoueurX;
        int y = JOUEUR_Y + TAILLE / 2;

        int numeroImage = imagesDepuisExplosion / 4;

        if (numeroImage < explosion.length) {

            int taille = 250;
            int solJoueur = JOUEUR_Y + 120;

            g.drawImage(
                explosion[numeroImage],
                x - taille / 2,
                solJoueur - taille,
                taille,
                taille,
                this
            );

        } else {

            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, LARGEUR, HAUTEUR);

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 60));

            FontMetrics fm = g.getFontMetrics();

            String texte = "GAME OVER";

            g.drawString(
                texte,
                (LARGEUR - fm.stringWidth(texte)) / 2,
                HAUTEUR / 2
            );

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 22));

            fm = g.getFontMetrics();

            texte = "Appuie sur Entrée pour rejouer";

            g.drawString(
                texte,
                (LARGEUR - fm.stringWidth(texte)) / 2,
                HAUTEUR / 2 + 50
            );
        }
    }

    // ==============================
    // REJOUER
    // ==============================

    public void rejouer() {

        if (!gameOver) {
            return;
        }

        generateur.reinitialiser();

        vitesse = 0.5;
        couloir = 0;

        allerGauche = false;
        allerDroite = false;

        gameOver = false;

        // NOUVEAU : remettre l'animation de route a zero
        route.reinitialiser();
    }

    // ==============================
    // EXPLOSION (INCHANGE)
    // ==============================

    private void chargerExplosion() {

        try {

            BufferedImage feuille =
                ImageIO.read(new File("explosion.png"));

            int nombre = 16;

            int l = feuille.getWidth() / nombre;
            int h = feuille.getHeight();

            explosion = new Image[nombre];

            for (int i = 0; i < nombre; i++) {

                explosion[i] = feuille.getSubimage(
                    i * l,
                    0,
                    l,
                    h
                );
            }

        } catch (Exception ex) {

            System.out.println(
                "Image d'explosion introuvable : "
                + ex.getMessage()
            );

            explosion = new Image[0];
        }
    }
}
