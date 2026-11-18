package jumpingthomas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
    private double tempsSaut = 700; // vitesse verticale du saut
    private double tempsSautMin = 300;

    private boolean enGlissade = false; // état de glissade
    public boolean intouchable = false; // état d'invincibilité

    // Obstacles (Killian)
    private GenerateurObstacles generateur = new GenerateurObstacles();
    private double vitesse = 0.5; // vitesse de départ, augmente au fil de la partie
    // Les états possibles du jeu : un seul à la fois
    public enum Etat { MENU, JEU, GAME_OVER }
    private Etat etat = Etat.MENU;   // le jeu démarre sur le menu
    private int imagesDepuisExplosion = 0;  // compteur pour animer l'explosion
    private Image[] explosion; // les images de l'animation d'explosion
    private double score = 0;        // double pour garder les petits morceaux, affiché en int
    private int meilleurScore = 0;   // record (tant que le jeu est ouvert)

    // Boutons de l'écran game over
    private JButton boutonRejouer;
    private JButton boutonMenu;
    private Runnable retourMenu;     // action donnée par Main pour revenir au menu
    // Route (Thomas)
    private RouteRenderer route;

     public Game() {
    setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
    chargerExplosion();
    route = new RouteRenderer(LARGEUR, HAUTEUR, HORIZON);   // décor de Thomas
    // Boutons du game over : posés à la main sur le jeu, cachés au départ
    setLayout(null);   // pas de rangement automatique : on place les boutons nous-mêmes
    boutonRejouer = MenuPrincipal.creerBouton("Rejouer", e -> nouvellePartie());
    boutonRejouer.setBounds(LARGEUR / 2 - 125, HAUTEUR / 2 + 90, 250, 55);  // x, y, largeur, hauteur
    boutonMenu = MenuPrincipal.creerBouton("Menu", e -> allerAuMenu());
    boutonMenu.setBounds(LARGEUR / 2 - 125, HAUTEUR / 2 + 165, 250, 55);
    add(boutonRejouer);
    add(boutonMenu);
    afficherBoutons(false);

    setFocusable(true);
        Timer timer = new Timer(16, e -> 
    {
        if (etat == Etat.MENU) {
            return;                    // on est sur le menu : le jeu ne tourne pas
        }

        if (etat == Etat.GAME_OVER) {
            imagesDepuisExplosion++;   // seule l'explosion avance
            if (imagesDepuisExplosion / 4 >= explosion.length) {
                afficherBoutons(true); // explosion finie : on montre les boutons
            }
            repaint();
            return;                    // le reste du jeu est figé
        }

        changerVoie();
        
        positionJoueurX = LARGEUR / 2 + couloir * LARGEUR / 3; 
        
        vitesse = vitesse + 0.0005;          // accélère un peu à chaque image
        generateur.mettreAJour(vitesse);     // fait avancer / crée / supprime les obstacles
        route.mettreAJour(vitesse);          // fait défiler la route (Thomas)
        tempsSaut = Math.max(tempsSautMin, tempsSaut/vitesse); // le saut devient plus rapide au fil de la partie
        if (tempsSaut > 700) {
            tempsSaut = 700; // limite la vitesse du saut
        }

        score = score + vitesse;     // le score augmente avec la distance parcourue
        if (generateur.toucheJoueur(couloir, enSaut)) {
            etat = Etat.GAME_OVER;
            imagesDepuisExplosion = 0;
            if ((int) score > meilleurScore) {
                meilleurScore = (int) score;   // nouveau record !
            }
        }

        repaint();
    });
    timer.start();
    
    
    
      

}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        route.dessiner(g, this);   // fond + route de Thomas (remplace l'ancien ciel et l'herbe)

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
        if (etat == Etat.GAME_OVER) {
            dessinerGameOver(g);
        } else {
            dessinerScore(g);   // le score n'est affiché que pendant la partie
        }   
        
}
public void sauter() {

    if (!enSaut) {
        enSaut = true;
        hauteurSaut = -80;

        Timer timerSaut = new Timer((int)(tempsSaut), e -> {
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

        Timer timerGlisse = new Timer((int)(tempsSaut), e -> {
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

        int numeroImage = imagesDepuisExplosion / 4;  // change d'image toutes les 4 images du jeu
        if (numeroImage < explosion.length) {
            int taille = 250;
            int solJoueur = JOUEUR_Y + 120;  // les roues du cycliste
            // posée au sol : le bas de l'image est au niveau des roues
            g.drawImage(explosion[numeroImage], x - taille / 2, solJoueur - taille, taille, taille, this); 
            }else {
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
            texte = "Score : " + (int) score + "   Record : " + meilleurScore;
            g.drawString(texte, (LARGEUR - fm.stringWidth(texte)) / 2, HAUTEUR / 2 + 50);

        }
    }

        // Score en haut à droite (brouillon, à embellir plus tard)
    private void dessinerScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        String texte = "Score : " + (int) score;
        int largeurTexte = g.getFontMetrics().stringWidth(texte);
        int x = LARGEUR - largeurTexte - 20;   // collé à droite, avec 20 px de marge
        g.setColor(Color.BLACK);
        g.drawString(texte, x + 2, 42);        // l'ombre, décalée de 2 pixels
        g.setColor(Color.WHITE);
        g.drawString(texte, x, 40);            // le texte par-dessus   // collé à droite, avec 20 px de marge
    }

    // Touche Entrée après un game over
    public void rejouer() {
        if (etat != Etat.GAME_OVER) {
            return;  // Entrée ne fait rien pendant la partie
        }
        nouvellePartie();
    }

    // Remet tout à zéro et lance la partie (appelée par le bouton du menu et par rejouer)
    public void nouvellePartie() {
        generateur.reinitialiser();
        vitesse = 0.5;
        score = 0;
        couloir = 0;
        allerGauche = false;
        allerDroite = false;
        route.reinitialiser();   // remet la route à zéro (Thomas)
        etat = Etat.JEU;
        afficherBoutons(false);
    }

    // Bouton "Menu" du game over
    private void allerAuMenu() {
        etat = Etat.MENU;
        afficherBoutons(false);
        retourMenu.run();   // Main s'occupe de montrer la carte du menu
    }

    // Main donne ici l'action pour revenir au menu
    public void setRetourMenu(Runnable action) {
        retourMenu = action;
    }

    private void afficherBoutons(boolean visibles) {
        boutonRejouer.setVisible(visibles);
        boutonMenu.setVisible(visibles);
    }

    private void chargerExplosion() {
        try {
            BufferedImage feuille = ImageIO.read(new File("explosion.png"));
            int nombre = 16;                               // nombre d'images dans la ligne
            int l = feuille.getWidth() / nombre;           // largeur d'une image
            int h = feuille.getHeight();                   // hauteur d'une image

            explosion = new Image[nombre];
            for (int i = 0; i < nombre; i++) {
                explosion[i] = feuille.getSubimage(i * l, 0, l, h);  // découpe la case n° i
            }
        } catch (Exception ex) {
            System.out.println("Image d'explosion introuvable : " + ex.getMessage());
            explosion = new Image[0];  // pas d'explosion, mais le jeu ne plante pas
        }
    }
}