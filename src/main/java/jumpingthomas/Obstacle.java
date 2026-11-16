package jumpingthomas;
import java.awt.Color;
import java.awt.Graphics;


public class Obstacle {

    private int couloir; // -1 = gauche, 0 = centre, 1 = droite

    private double distance; // 100 = à l'horizon, 0 = au niveau du joueur

    private boolean bas; // true = on peut sauter par-dessus

    public Obstacle(int couloir, boolean bas) {
        this.couloir = couloir;
        this.bas = bas;
        this.distance = 100;   // un nouvel obstacle apparaît toujours à l'horizon
    }

        // Fait avancer l'obstacle vers le joueur
    public void avancer(double vitesse) {
        distance = distance - vitesse;
    }

    public double getDistance() {
        return distance;
    }
        public void dessiner(Graphics g) {
        double e = Perspective.echelle(distance);
        int largeur = (int) (Game.TAILLE * e);
        int hauteur = bas ? largeur / 2 : largeur;   // bas = moitié moins haut
        int x = Perspective.x(couloir, distance);
        int ySol = Perspective.ySol(distance);

        g.setColor(bas ? Color.ORANGE : Color.DARK_GRAY);
        g.fillRect(x - largeur / 2, ySol - hauteur, largeur, hauteur);
    }



    
}
