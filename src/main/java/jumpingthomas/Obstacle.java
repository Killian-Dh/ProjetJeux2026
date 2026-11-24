package jumpingthomas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Obstacle {

    private int couloir; // -1 = gauche, 0 = centre, 1 = droite

    private double distance; // 100 = à l'horizon, 0 = au niveau du joueur

    // Les 3 types d'obstacles, et comment on les évite
    public enum Type {
        BAS,       // barrière basse : on saute
        HAUT,      // barrière haute : on change de couloir
        GLISSADE   // oiseau en hauteur : on glisse dessous
    }
    private Type type;
    
    private double distancePrecedente;  // la distance à l'image d'avant

    private Image barriereBas = new ImageIcon("barriere.png").getImage();
    private Image barriereHaut = new ImageIcon("barriereHaute.png").getImage();

    // L'oiseau (obstacle GLISSADE) : chargé UNE seule fois pour tous les obstacles (static)
    private static final Image[] OISEAU = chargerAnimation("oiseau.png", 4);
    private Image TROU = new ImageIcon("trou.png").getImage();  // pour le debug : on voit le trou à franchir
    private boolean estUnTrou;
    private int age = 0;   // nombre d'images depuis la création de l'obstacle (pour l'animation)
    

    public Obstacle(int couloir, Type type) {
        this.couloir = couloir;
        this.type = type;
        this.distance = 100;   // un nouvel obstacle apparaît toujours à l'horizon
        this.distancePrecedente = 100;
        this.estUnTrou = (type == Type.BAS && Math.random() < 0.3);   // 30% des barres basses sont des trous dans le sol, pour varier les obstacles
    }

        // Fait avancer l'obstacle vers le joueur
        public void avancer(double vitesse) {
        distancePrecedente = distance;  // on retient où il était avant de bouger
        distance = distance - vitesse;
        age++;
    }

    public double getDistance() {
        return distance;
    }

    public boolean estUnTrou() {
        return estUnTrou;
    }
    public void dessiner(Graphics g) {
        double e = Perspective.echelle(distance);
        int largeur = (int) (1.5 * Game.TAILLE * e);
        int x = Perspective.x(couloir, distance);
        int ySol = Perspective.ySol(distance);

        if (estUnTrou) {
            // le trou est À PLAT sur le sol : large mais peu haut, centré sur la ligne du sol
            int hauteur = largeur * TROU.getHeight(null) / TROU.getWidth(null);
            g.drawImage(TROU, x - largeur / 2, ySol - hauteur * 2 / 3, largeur, hauteur, null);
        } else if (type == Type.BAS) { 
            int hauteur = (int) (largeur / 1.5);
            g.drawImage(barriereBas, x - largeur / 2, ySol - hauteur, largeur, hauteur, null);
        } else if (type == Type.HAUT) {
            int hauteur = (int) (2.0 * Game.TAILLE * e);
            g.drawImage(barriereHaut, x - largeur / 2, ySol - hauteur, largeur, hauteur, null);
        } else if (OISEAU.length > 0) {
            // GLISSADE : un oiseau qui vole à hauteur de tête, on passe dessous
            Image img = OISEAU[(age / 8) % OISEAU.length];   // change d'image toutes les 8 images, en boucle
            int hauteur = largeur * img.getHeight(null) / img.getWidth(null);
            int basOiseau = ySol - (int) (Game.TAILLE * e);  // le bas de l'oiseau : juste au-dessus du joueur baissé
            g.drawImage(img, x - largeur / 2, basOiseau - hauteur, largeur, hauteur, null);
        } else {
            // image introuvable : on garde la banderole rouge, pour que le jeu reste jouable
            int hautTotal = (int) (1.5 * Game.TAILLE * e);
            int epaisseur = (int) (0.5 * Game.TAILLE * e);
            int poteau = Math.max(2, largeur / 12);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x - largeur / 2, ySol - hautTotal, poteau, hautTotal);
            g.fillRect(x + largeur / 2 - poteau, ySol - hautTotal, poteau, hautTotal);
            g.setColor(Color.RED);
            g.fillRect(x - largeur / 2, ySol - hautTotal, largeur, epaisseur);
        }
    }
    

    // Est-ce que cet obstacle touche le joueur ?
    public boolean touche(int couloirJoueur, boolean joueurEnSaut, boolean joueurEnGlissade) {
        boolean memeCouloir = (couloir == couloirJoueur);
        boolean franchitLeJoueur = (distancePrecedente > 0 && distance <= 0); // passe la "ligne" du joueur à cette image

        // chaque type ne s'évite qu'avec LA bonne action
        boolean evite = (type == Type.BAS && joueurEnSaut)
                     || (type == Type.GLISSADE && joueurEnGlissade);

        return memeCouloir && franchitLeJoueur && !evite;
    }

    // Découpe une spritesheet (images côte à côte sur une ligne) en images séparées
    private static Image[] chargerAnimation(String fichier, int nombre) {
        try {
            BufferedImage feuille = ImageIO.read(new File(fichier));
            int l = feuille.getWidth() / nombre;
            Image[] images = new Image[nombre];
            for (int i = 0; i < nombre; i++) {
                images[i] = feuille.getSubimage(i * l, 0, l, feuille.getHeight());
            }
            return images;
        } catch (Exception ex) {
            System.out.println("Image introuvable : " + fichier);
            return new Image[0];   // tableau vide : on dessinera la banderole à la place
        }
    }
}