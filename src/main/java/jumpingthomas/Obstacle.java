package jumpingthomas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import java.awt.Image;

public class Obstacle {

    private int couloir; // -1 = gauche, 0 = centre, 1 = droite

    private double distance; // 100 = à l'horizon, 0 = au niveau du joueur

    private boolean bas; // true = on peut sauter par-dessus
    
    private double distancePrecedente;  // la distance à l'image d'avant

    private Image barriereBas = new ImageIcon("barriere.png").getImage();
    private Image barriereHaut = new ImageIcon("barriereHaute.png").getImage();

    public Obstacle(int couloir, boolean bas) {
        this.couloir = couloir;
        this.bas = bas;
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
        //int hauteur = bas ? largeur / 2 : largeur;   // bas = moitié moins haut
        
        int hauteur = bas ? (int)(largeur/1.5) : (int)(2.0*Game.TAILLE*e);   // bas = moitié moins haut
        //int hauteur = bas ? (int)(largeur/1.5) : (int)(largeur*1.5);   // bas = moitié moins haut
        int x = Perspective.x(couloir, distance);
        int ySol = Perspective.ySol(distance);

       // g.setColor(bas ? Color.ORANGE : Color.DARK_GRAY);
        //g.fillRect(x - largeur / 2, ySol - hauteur, largeur, hauteur);
        if (bas)
        {
            g.drawImage(barriereBas, x - largeur/2, ySol - hauteur, largeur, hauteur, null);
        }
        else 
        {
            g.drawImage(barriereHaut, x - largeur/2, ySol - hauteur, largeur, hauteur, null);
        }
        
    }

    // Est-ce que cet obstacle touche le joueur ?
        public boolean touche(int couloirJoueur, boolean joueurEnSaut) {
        boolean memeCouloir = (couloir == couloirJoueur);
        boolean franchitLeJoueur = (distancePrecedente > 0 && distance <= 0); // passe la "ligne" du joueur à cette image
        boolean evite = (bas && joueurEnSaut);

        return memeCouloir && franchitLeJoueur && !evite;
    }



    
}
