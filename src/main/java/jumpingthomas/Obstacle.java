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
        double pixelsParUnite = (Game.JOUEUR_Y - Game.HORIZON) / 100.0;

        int y = Game.JOUEUR_Y - (int) (distance * pixelsParUnite);
        int x = Game.LARGEUR / 2 + couloir * Game.LARGEUR / 3;

                if (bas) {
            g.setColor(Color.ORANGE);  // bas : moitié moins haut, on peut sauter
            g.fillRect(x - Game.TAILLE / 2, y + Game.TAILLE / 2, Game.TAILLE, Game.TAILLE / 2);
        } else {
            g.setColor(Color.DARK_GRAY); // haut : il faut changer de couloir
            g.fillRect(x - Game.TAILLE / 2, y, Game.TAILLE, Game.TAILLE);
        }
    }



    
}
