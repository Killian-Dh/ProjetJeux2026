package jumpingthomas;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class GenerateurObstacles {

    private ArrayList<Obstacle> obstacles = new ArrayList<>(); // tous les obstacles à l'écran
    private Random hasard = new Random();
    private double avantProchaineVague = 0;  // distance restante avant de créer la prochaine vague

    // Appelée à chaque image par Game
    public void mettreAJour(double vitesse) {
        // 1. on fait avancer tous les obstacles
        for (Obstacle o : obstacles) {
            o.avancer(vitesse);
        }

        // 2. on supprime ceux qui ont dépassé le joueur
        obstacles.removeIf(o -> o.getDistance() < -20);

        // 3. est-ce qu'il est temps de créer une nouvelle vague ?
        avantProchaineVague = avantProchaineVague - vitesse;
        if (avantProchaineVague <= 0) {
            creerVague();
            avantProchaineVague = 35 + hasard.nextInt(26); // écart aléatoire entre 35 et 60
        }
    }

    // Pour l'instant : un seul obstacle dans un couloir au hasard
    private void creerVague() {
        int couloir = hasard.nextInt(3) - 1;  // nextInt(3) donne 0, 1 ou 2, donc -1, 0 ou 1
        obstacles.add(new Obstacle(couloir, false));
    }

    public void dessiner(Graphics g) {
        for (Obstacle o : obstacles) {
            o.dessiner(g);
        }
    }
}