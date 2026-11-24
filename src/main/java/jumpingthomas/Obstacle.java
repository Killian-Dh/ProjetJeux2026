package jumpingthomas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Obstacle {

    private int couloir; // -1 = gauche, 0 = centre, 1 = droite

    private double distance; // 100 = à l'horizon, 0 = au niveau du joueur

    // Les 3 types d'obstacles, et comment on les évite
    public enum Type {
        BAS,       // barrière basse : on saute
        HAUT,      // barrière haute : on change de couloir
        GLISSADE   // banderole en hauteur : on glisse dessous
    }
    private Type type;
    
    private double distancePrecedente;  // la distance à l'image d'avant

    private Image barriereBas = new ImageIcon("barriere.png").getImage();
    private Image barriereHaut = new ImageIcon("barriereHaute.png").getImage();

    public Obstacle(int couloir, Type type) {
        this.couloir = couloir;
        this.type = type;
        this.distance = 100;   // un nouvel obstacle apparaît toujours à l'horizon
        this.distancePrecedente = 100;
    }

        // Fait avancer l'obstacle vers le joueur
        public void avancer(double vitesse) {
        distancePrecedente = distance;  // on retient où il était avant de bouger
        distance = distance - vitesse;
    }

    public double getDistance() {
        return distance;
    }
    public void dessiner(Graphics g) {
        double e = Perspective.echelle(distance);
        int largeur = (int) (1.5 * Game.TAILLE * e);
        int x = Perspective.x(couloir, distance);
        int ySol = Perspective.ySol(distance);

        if (type == Type.BAS) {
            int hauteur = (int) (largeur / 1.5);
            g.drawImage(barriereBas, x - largeur / 2, ySol - hauteur, largeur, hauteur, null);
        } else if (type == Type.HAUT) {
            int hauteur = (int) (2.0 * Game.TAILLE * e);
            g.drawImage(barriereHaut, x - largeur / 2, ySol - hauteur, largeur, hauteur, null);
        } else {
            // GLISSADE (provisoire) : une banderole tenue par 2 poteaux, on passe dessous
            int hautTotal = (int) (1.5 * Game.TAILLE * e);   // hauteur du haut de la banderole
            int epaisseur = (int) (0.5 * Game.TAILLE * e);   // épaisseur de la banderole
            int poteau = Math.max(2, largeur / 12);          // largeur d'un poteau
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x - largeur / 2, ySol - hautTotal, poteau, hautTotal);           // poteau gauche
            g.fillRect(x + largeur / 2 - poteau, ySol - hautTotal, poteau, hautTotal);  // poteau droit
            g.setColor(Color.RED);
            g.fillRect(x - largeur / 2, ySol - hautTotal, largeur, epaisseur);          // la banderole
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
}