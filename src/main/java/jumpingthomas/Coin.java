
package jumpingthomas;

import java.awt.Color;
import java.awt.Graphics;

public class Coin {

    private int couloir;
    private double distance;
    private double distancePrecedente;

    private boolean collectee;

    public Coin(int couloir) {
        this.couloir = couloir;
        this.distance = 100;
        this.distancePrecedente = 100;
        this.collectee = false;
    }

    // Faire avancer la pièce vers le joueur
    public void avancer(double vitesse) {
        distancePrecedente = distance;
        distance -= vitesse;
    }

    // Dessiner la pièce
    public void dessiner(Graphics g) {

        if (collectee) {
            return;
        }

        double e = Perspective.echelle(distance);

        int taille = (int) (Game.TAILLE * e);

        int x = Perspective.x(couloir, distance);
        int y = Perspective.ySol(distance) - taille;

        g.setColor(new Color(255, 190, 0));

        g.fillOval(
            x - taille / 2,
            y - taille / 2,
            taille,
            taille
        );

        g.setColor(Color.ORANGE);

        g.drawOval(
            x - taille / 2,
            y - taille / 2,
            taille,
            taille
        );
    }

    // Vérifier si le joueur récupère la pièce
    public boolean toucher(int couloirJoueur) {

        boolean memeCouloir = couloir == couloirJoueur;

        boolean franchitLeJoueur =
            distancePrecedente > 0 && distance <= 0;

        if (memeCouloir && franchitLeJoueur && !collectee) {
            collectee = true;
            return true;
        }

        return false;
    }

    public double getDistance() {
        return distance;
    }

    public boolean estCollectee() {
        return collectee;
    }
}

