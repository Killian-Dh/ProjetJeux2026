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
        // Crée une vague : pour chaque couloir, vide, bas ou haut au hasard
    private void creerVague() {
        // 0 = vide, 1 = bas, 2 = haut
        // case 0 = couloir gauche, case 1 = centre, case 2 = droite
        int[] contenu = new int[3];
        for (int i = 0; i < 3; i++) {
            contenu[i] = hasard.nextInt(3);
        }

        // Règle : jamais 3 obstacles hauts, sinon impossible de passer
        if (contenu[0] == 2 && contenu[1] == 2 && contenu[2] == 2) {
            contenu[hasard.nextInt(3)] = 1; // l'un des trois devient un obstacle bas
        }

        // On crée les obstacles
        for (int i = 0; i < 3; i++) {
            int couloir = i - 1; // case 0 -> couloir -1, case 1 -> 0, case 2 -> 1
            if (contenu[i] == 1) {
                obstacles.add(new Obstacle(couloir, true));   // bas
            }
            if (contenu[i] == 2) {
                obstacles.add(new Obstacle(couloir, false));  // haut
            }
        }
    }

    public void dessiner(Graphics g) {
        for (Obstacle o : obstacles) {
            o.dessiner(g);
        }
    }
}