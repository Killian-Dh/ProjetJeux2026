package jumpingthomas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {

    public static final int HAUTEUR = 800;
    public static final int LARGEUR = HAUTEUR * 9 / 16;
    public static final int HORIZON = HAUTEUR / 3;
    public static final int TAILLE = 110;

    // exemple d'appel pour le son : 
    // musique.volume(0.3f); // 30 %
    // musique.boucle();     // boucle indéfiniment
    // saut.jouer(); // joue une fois
    // musique.stop(); // stoppe la lecture

    private Son musiqueFond = new Son("OIIAOIIA_Theme.wav");
    private Son explosionSon = new Son("explosionSong.wav");
    private Son mortSon = new Son("LegoBreak.wav");
    private Son sautSon = new Son("WhooshSound.wav");
    private Son pieceSon = new Son("pieceSon.wav");
    private static Image logoPiece = new ImageIcon("piece.png").getImage();
    private static Image logoScore = new ImageIcon("etoile.png").getImage();
    public static double volumeSon = 0.3; // volume sonore par défaut (0 à 1)
    public static double volumeMusique = 0.3; // volume musique par défaut (0 à 1)

    public static final int JOUEUR_Y = HAUTEUR - 200;
    //Sprite du joueur
    /*private Image idle = new ImageIcon("cycliste_idle.png").getImage();
    private Image idle2 = new ImageIcon("cycliste_idle2.png").getImage();*/

    private Image idle;
    private Image idle2;
    private Image saut;
    private Image glissade;

    

    private boolean imagePedale = false;
    private static int vitessePedale = 500;
    /*private Image saut = new ImageIcon("cycliste_saute.png").getImage();
    private Image glissade = new ImageIcon("cycliste_glissade.png").getImage();*/
    private Image idleDroite = new ImageIcon("cycliste_droite.png").getImage();
    private Image idleGauche = new ImageIcon("cycliste_gauche.png").getImage();
    private Image mort = new ImageIcon("cycliste_death.png").getImage();
    private Image cyclisteVol = new ImageIcon("cycliste_vol.png").getImage();
    private Image cyclisteEcrase = new ImageIcon("cycliste_ecrase.png").getImage();
    private Image vitreFissuree = new ImageIcon("vitre_fissuree.png").getImage();



    private static int dureeVol = 30;
    private static int dureeEcrase = 35;
    private static int dureeGlisse = 50;
    private static int dureeCrash = dureeVol + dureeEcrase + dureeGlisse;
    private static int tailleImpact = 500;
    private static int impactY = 270;
    
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
    // Pièces à collecter (Houda)
    private GenerateurPieces generateurPieces = new GenerateurPieces();
    private double vitesse = 0.5; // vitesse de départ, augmente au fil de la partie
    // Les états possibles du jeu : un seul à la fois
    public enum Etat { MENU, JEU, GAME_OVER }
    private Etat etat = Etat.MENU;   // le jeu démarre sur le menu
    private int imagesDepuisExplosion = 0;  // compteur pour animer l'explosion
    private Image[] explosion; // les images de l'animation d'explosion
    private double score = 0;        // double pour garder les petits morceaux, affiché en int
    private int meilleurScore = 0;   // record (tant que le jeu est ouvert)

    private boolean nouveauRecord = false;   // vrai si la dernière partie a battu le record

    private static final Font PIXEL = chargerPolice("PressStart2P-Regular.ttf");
    private static Image logoRecord = new ImageIcon("etoile.png").getImage();
    private static Image cadreBouton = new ImageIcon("ButtonType.png").getImage();   // le cadre des boutons, réutilisé pour le panneau
    private static final Color SOMBRE = new Color(40, 28, 44);
    private static final Color GRIS = new Color(120, 110, 120);

    public static int totalPieces = 0;
    private static int prevtotalPieces = 0;
   

    
    



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
    boutonRejouer.setBounds(LARGEUR / 2 - 150, HAUTEUR / 2 + 90, 300, 60);  // x, y, largeur, hauteur
    boutonMenu = MenuPrincipal.creerBouton("Menu", e -> allerAuMenu());
    boutonMenu.setBounds(LARGEUR / 2 - 150, HAUTEUR / 2 + 165, 300, 60);
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
            if (imagesDepuisExplosion >= dureeCrash) {  // explosion terminée
                afficherBoutons(true); // explosion finie : on montre les boutons
            }
            repaint();
            return;                    // le reste du jeu est figé
        }

        changerVoie();
        
        positionJoueurX = LARGEUR / 2 + couloir * LARGEUR / 3; 
        
        vitesse = vitesse + 0.0005;          // accélère un peu à chaque image
        generateur.mettreAJour(vitesse);     // fait avancer / crée / supprime les obstacles
                                          // d'abord les obstacles
        int avant = generateurPieces.getNombrePiecesCollectees();
        generateurPieces.mettreAJour(vitesse, couloir, generateur);  // puis les pièces
        
        
        int apres = generateurPieces.getNombrePiecesCollectees();

        
        /*if (apres > avant) {
            pieceSon.volume((float) volumeSon);
            pieceSon.jouer();
        }*/
        if (apres > avant) 
        {
            jouerSonPiece();
        }

        route.mettreAJour(vitesse);          // fait défiler la route (Thomas)
        tempsSaut = Math.max(tempsSautMin, tempsSaut/vitesse); // le saut devient plus rapide au fil de la partie
        if (tempsSaut > 700) {
            tempsSaut = 700; // limite la vitesse du saut
        }

        vitessePedale = (int)(350/ vitesse); // la vitesse de pédalage s'accélère avec la vitesse du jeu

        score = score + vitesse;     // le score augmente avec la distance parcourue
        if (generateur.toucheJoueur(couloir, enSaut, enGlissade)) {
            etat = Etat.GAME_OVER;
            imagesDepuisExplosion = 0;
            musiqueFond.stop();
            explosionSon.volume((float) volumeSon);
            explosionSon.jouer(); // joue une fois
            if (totalPieces < 100)
            {
                totalPieces += generateurPieces.getNombrePiecesCollectees(); 
            }
            else
            {
                totalPieces = 999;
            }
            
            nouveauRecord = ((int) score > meilleurScore);
            if (nouveauRecord) {
                meilleurScore = (int) score;   // nouveau record !
            }
        }

        int styleSelectionne = MenuPrincipal.selectedType;
    switch (styleSelectionne)
    {
        case 0 : 
            idle = new ImageIcon("cycliste_idle.png").getImage();
            idle2 = new ImageIcon("cycliste_idle2.png").getImage();
            saut = new ImageIcon("cycliste_saute.png").getImage();
            glissade = new ImageIcon("cycliste_glissade.png").getImage();
            break;
        case 1 : 
            idle = new ImageIcon("harley_idle.png").getImage();
            idle2 = new ImageIcon("harley_idle2.png").getImage();
            saut = new ImageIcon("harley_saut.png").getImage();
            glissade = new ImageIcon("harley_glissade.png").getImage();
            break;
        case 2 :
            idle = new ImageIcon("cycliste_idle.png").getImage();
            idle2 = new ImageIcon("cycliste_idle2.png").getImage();
            saut = new ImageIcon("cycliste_saute.png").getImage();
            glissade = new ImageIcon("cycliste_glissade.png").getImage();
            break;
     }
     

        repaint();
    });
    timer.start();
    
    
    
      

}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        route.dessiner(g, this);   // fond + route de Thomas (remplace l'ancien ciel et l'herbe)
        generateur.dessinerDerriere(g, generateurPieces);

       
        g.setColor(Color.RED);
        int centreX = positionJoueurX;
        int centreY = JOUEUR_Y;
        //g.fillRect(centreX - TAILLE / 2, JOUEUR_Y, TAILLE, TAILLE);
        if (enGlissade) 
        {
            int largeur = (int)(1.5*TAILLE);
            int hauteur = (int)(2.0*TAILLE * glissade.getWidth(this) / glissade.getHeight(this)); // proportion de l'image
            g.drawImage(glissade, centreX - largeur/2, centreY, largeur, hauteur, this);
        } 
        else if (enSaut) 
        {
            int largeur = TAILLE;
            int hauteur = (int)(2.0*TAILLE * saut.getWidth(this) / saut.getHeight(this)); // proportion de l'image
            g.drawImage(saut, centreX - largeur/2, centreY + hauteurSaut, largeur, hauteur, this);
        } 
        else if (etat == Etat.GAME_OVER)
        {
            // cache le joueur pendant l'animation de défaite
        }
        else
        {
            animationPedale();
            int largeur = TAILLE;
            int hauteur = (int)(2.0*TAILLE * idle.getWidth(this) / idle.getHeight(this)); // proportion de l'image
            if (imagePedale) {
                g.drawImage(idle2, centreX - largeur/2, centreY, largeur, hauteur, this);
            }
            else {
                g.drawImage(idle, centreX - largeur/2, centreY, largeur, hauteur, this);
            }


        }

        generateur.dessinerDevant(g);   // obstacles déjà passés : dessinés PAR-DESSUS le joueur

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
        Graphics2D g2 = (Graphics2D) g;   // version "améliorée" de Graphics, qui sait faire tourner
        int t = imagesDepuisExplosion;    // nombre d'images depuis la collision

        // L'EXPLOSION, au sol, à l'endroit de la collision (dessinée en premier : elle est derrière)
        int numeroImage = t / 4;          // change d'image toutes les 4 images du jeu
        if (numeroImage < explosion.length) {
            int taille = 250;
            int solJoueur = JOUEUR_Y + 120;  // les roues du cycliste
            g.drawImage(explosion[numeroImage], positionJoueurX - taille / 2, solJoueur - taille, taille, taille, this);
        }


        if (t < dureeVol) {
            // PHASE 1 : le cycliste est projeté vers l'écran en tournant
            double p = (double) t / dureeVol;   // avancement de la phase : de 0 à 1
            double e = p * p;                    // au carré : lent au début, très rapide à la fin
            int taille = (int) (TAILLE + (tailleImpact - TAILLE) * e);
            int cx = (int) (positionJoueurX + (LARGEUR / 2 - positionJoueurX) * e);
            int cy = (int) (JOUEUR_Y + 60 + (impactY - JOUEUR_Y - 60) * e);
            double angle = p * 4 * Math.PI;      // 2 tours complets (2 x 2π) : il arrive bien droit
            dessinerImage(g2, cyclisteVol, cx, cy, taille, angle);
            mortSon.volume((float) volumeSon);
            mortSon.jouer(); // joue une fois
            
            return;
        }
        // PHASE 2 et 3 : il est collé à la vitre, puis glisse vers le bas
        int tVitre = t - dureeVol;  // nombre d'images depuis le début de la vitre

        // l'écran tremble pendant les 10 premières images du choc
        int dx = 0;
        int dy = 0;
        if (tVitre < 10) {
            dx = (int) (Math.random() * 16) - 8;
            dy = (int) (Math.random() * 16) - 8;
        }

        // glissade : commence après DUREE_COLLE, de plus en plus vite
        int glisse = 0;
        if (tVitre > dureeEcrase) {
            int s = tVitre - dureeEcrase;  // nombre d'images depuis le début de la glissade
            glisse = s * s / 3;
        }
        dessinerImage(g2, cyclisteEcrase, LARGEUR / 2 + dx, impactY + glisse + dy, tailleImpact, 0);

        // la vitre fissurée PAR-DESSUS le cycliste : il est derrière la vitre
        int hauteurVitre = HAUTEUR;
        int largeurVitre = HAUTEUR * vitreFissuree.getWidth(this) / vitreFissuree.getHeight(this);
        g.drawImage(vitreFissuree, (LARGEUR - largeurVitre) / 2 + dx, dy, largeurVitre, hauteurVitre, this);

        if (t >= dureeCrash) {
            dessinerRecap((Graphics2D) g);
        }
    }

    // Écran de fin
    private void dessinerRecap(Graphics2D g) {
        // voile sombre sur tout l'écran
        g.setColor(new Color(10, 5, 25, 170));
        g.fillRect(0, 0, LARGEUR, HAUTEUR);


        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);


        texteContour(g, "GAME", LARGEUR / 2, 115, PIXEL.deriveFont(52f), new Color(255, 84, 84));
        texteContour(g, "OVER", LARGEUR / 2, 180, PIXEL.deriveFont(52f), new Color(255, 84, 84));


        int px = 45, py = 225, pw = 360, ph = 245;
        dessinerPanneau(g, px, py, pw, ph);


        g.setFont(PIXEL.deriveFont(16f));
        g.setColor(GRIS);
        texteCentre(g, "SCORE", py + 48);
        g.setFont(PIXEL.deriveFont(44f));
        g.setColor(SOMBRE);
        texteCentre(g, String.valueOf((int) score), py + 108);

        g.setColor(new Color(190, 190, 190));
        g.fillRect(px + 30, py + 128, pw - 60, 4);

        g.setFont(PIXEL.deriveFont(16f));
        FontMetrics fm = g.getFontMetrics();
        String record = String.valueOf(meilleurScore);
        String pieces = "+" + generateurPieces.getNombrePiecesCollectees();

        g.drawImage(logoRecord, px + 32, py + 146, 28, 30, this);
        g.setColor(GRIS);
        g.drawString("RECORD", px + 72, py + 170);
        g.setColor(SOMBRE);
        g.drawString(record, px + pw - 34 - fm.stringWidth(record), py + 170);

        g.drawImage(logoPiece, px + 36, py + 190, 21, 28, this);
        g.setColor(GRIS);
        g.drawString("PIECES", px + 72, py + 212);
        g.setColor(new Color(200, 130, 0));
        g.drawString(pieces, px + pw - 34 - fm.stringWidth(pieces), py + 212);

        if (nouveauRecord) {
            int cx = px + pw - 90, cy = py - 14;            
            Graphics2D b = (Graphics2D) g.create();         
            b.rotate(Math.toRadians(-8), cx, cy);
            b.setColor(SOMBRE);
            b.fillRect(cx - 122, cy - 22, 244, 44);
            b.setColor(new Color(255, 212, 60));
            b.fillRect(cx - 118, cy - 18, 236, 36);
            b.setFont(PIXEL.deriveFont(15f));
            b.setColor(SOMBRE);
            String texte = "NOUVEAU RECORD!";
            b.drawString(texte, cx - b.getFontMetrics().stringWidth(texte) / 2, cy + 8);
            b.dispose();
        }

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }


    private void dessinerPanneau(Graphics2D g, int x, int y, int w, int h) {
        int iw = cadreBouton.getWidth(this);    
        int ih = cadreBouton.getHeight(this);   
        int c = 20;                             


        int[] dx = {x, x + c, x + w - c, x + w};
        int[] dy = {y, y + c, y + h - c, y + h};
        int[] sx = {0, c, iw - c, iw};
        int[] sy = {0, c, ih - c, ih};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                
                g.drawImage(cadreBouton, dx[i], dy[j], dx[i + 1], dy[j + 1],
                                         sx[i], sy[j], sx[i + 1], sy[j + 1], this);
            }
        }
    }

    private void texteContour(Graphics2D g, String texte, int cx, int y, Font police, Color couleur) {
        g.setFont(police);
        int x = cx - g.getFontMetrics().stringWidth(texte) / 2;
        g.setColor(SOMBRE);
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 6; dy++) {      // un peu plus bas que haut : ça fait l'ombre
                g.drawString(texte, x + dx, y + dy);
            }
        }
        g.setColor(couleur);
        g.drawString(texte, x, y);
    }

    private void texteCentre(Graphics2D g, String texte, int y) {
        g.drawString(texte, (LARGEUR - g.getFontMetrics().stringWidth(texte)) / 2, y);
    }

    // Charge une police depuis un fichier .ttf (si elle manque : Arial gras, le jeu ne plante pas)
    private static Font chargerPolice(String fichier) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(fichier));
        } catch (Exception ex) {
            System.out.println("Police introuvable : " + fichier);
            return new Font("Arial", Font.BOLD, 12);
        }
    }
    // Dessine une image centrée sur (cx, cy), avec une largeur donnée, tournée d'un angle (en radians)
    private void dessinerImage(Graphics2D g2, Image img, int cx, int cy, int largeur, double angle) {
        int hauteur = largeur * img.getHeight(this) / img.getWidth(this);  // garde les proportions
        AffineTransform avant = g2.getTransform();  // on mémorise la position "normale"
        g2.rotate(angle, cx, cy);                   // on tourne autour du centre de l'image
        g2.drawImage(img, cx - largeur / 2, cy - hauteur / 2, largeur, hauteur, this);
        g2.setTransform(avant);                     // on remet tout droit pour la suite du dessin
    }

        // Score en haut à droite (brouillon, à embellir plus tard)
        private void dessinerScore(Graphics g) {
            int largeurRScore = 35;
            int longueurScore = 80;
            int offsetX1Score = 90;
            int offsetX2Score = 93;
            int offsetlogoScore = 115;
            if (score >= 1000 && score < 10000)
            {
                longueurScore = 105;
                offsetX1Score = 115;
                offsetX2Score = 118;
                offsetlogoScore = 125;
            }
            else if (score < 1000)
            {
                longueurScore = 95;
                offsetX1Score = 105;
                offsetX2Score = 108;
                offsetlogoScore = 110;
            }
            
            g.setColor(new Color(150, 30, 30));
            g.fillRoundRect(Game.LARGEUR - offsetX1Score, 19, longueurScore, largeurRScore, 10, 10);
            g.setColor(new Color(196, 202, 220));
            g.fillRoundRect(Game.LARGEUR - offsetX2Score, 14, longueurScore, largeurRScore, 10, 10);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawImage(logoScore, Game.LARGEUR - offsetlogoScore, 12, 36, 40, this); // remplacer par logoScore

            // Score en haut à droite
            String texteScore = Integer.toString((int)score);
            int largeurScore = g.getFontMetrics().stringWidth(texteScore);
            int xScore = LARGEUR - largeurScore - 20;

            g.setColor(Color.BLACK);
            g.drawString(texteScore, xScore + 2, 42);

            g.setColor(Color.WHITE);
            g.drawString(texteScore, xScore, 40);

            // Pieces collectees en haut a gauche, sur une ligne distincte
            int largeurRPiece = 35;
            int longueurPiece = 80;
            int offsetX1Piece = 90;
            int offsetX2Piece = 93;
            int offsetlogoPiece = 110;
            if (generateurPieces.getNombrePiecesCollectees() >= 1000 && generateurPieces.getNombrePiecesCollectees() < 10000)
            {
                longueurPiece = 105;
                offsetX1Piece = 115;
                offsetX2Piece = 118;
                offsetlogoPiece = 120;
            }
            else if (generateurPieces.getNombrePiecesCollectees() < 1000)
            {
                longueurPiece = 95;
                offsetX1Piece = 105;
                offsetX2Piece = 108;
                offsetlogoPiece = 105;
            }
            
            g.setColor(new Color(150, 30, 30));
            g.fillRoundRect(Game.LARGEUR - offsetX1Piece, 65, longueurPiece, largeurRPiece, 10, 10);
            g.setColor(new Color(196, 202, 220));
            g.fillRoundRect(Game.LARGEUR - offsetX2Piece, 60, longueurPiece, largeurRPiece, 10, 10);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawImage(logoPiece, Game.LARGEUR - offsetlogoPiece, 62, 30, 30, this);
            String textePieces = Integer.toString(generateurPieces.getNombrePiecesCollectees());
            int largeurScorePiece = g.getFontMetrics().stringWidth(textePieces);
            int xScorePiece = LARGEUR - largeurScorePiece - 20;
            

            g.setColor(Color.BLACK);
            g.drawString(textePieces, xScorePiece + 2, 88);

            g.setColor(Color.YELLOW);
            g.drawString(textePieces, xScorePiece, 86);
    }
    /*private void dessinerScore(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 28));
        // Score
        String texteScore = "Score : " + (int) score;
        int largeurScore = g.getFontMetrics().stringWidth(texteScore);
        int xScore = LARGEUR - largeurScore - 20;
        g.setColor(Color.BLACK);
        g.drawString(texteScore, xScore + 2, 42);
        g.setColor(Color.WHITE);
        g.drawString(texteScore, xScore, 40);
        // Pièces collectées
        String textePieces = "Pieces collectees : " + generateurPieces.getNombrePiecesCollectees();
        g.setColor(Color.BLACK);
        g.drawString(textePieces, 22, 40);
}
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
    }*/

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
        generateurPieces.reinitialiser();
        vitesse = 0.5;
        score = 0;
        couloir = 0;
        allerGauche = false;
        allerDroite = false;
        route.reinitialiser();   // remet la route à zéro (Thomas)
        etat = Etat.JEU;
        afficherBoutons(false);
        musiqueFond.boucle();
        musiqueFond.volume((float) volumeMusique);
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
    Timer timerPedale;
    private void animationPedale(){
        
        if (timerPedale != null && timerPedale.isRunning()) {
        return;
        }
        timerPedale = new Timer(vitessePedale, e -> {
        imagePedale = !imagePedale;
        repaint();
        });

        timerPedale.start();
    }

    
    private Son[] sonsPiece = {
        new Son("pieceSon.wav"), new Son("pieceSon.wav"), new Son("pieceSon.wav")
    };
    private int indexSonPiece = 0;

    private void jouerSonPiece() {
        Son s = sonsPiece[indexSonPiece];
        s.volume((float) volumeSon);
        s.jouer();
        indexSonPiece = (indexSonPiece + 1) % sonsPiece.length;
    }

    }
