package jumpingthomas;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        obstacles.removeIf(o -> o.getDistance() < -5);
        // 3. est-ce qu'il est temps de créer une nouvelle vague ?
        avantProchaineVague = avantProchaineVague - vitesse;
        if (avantProchaineVague <= 0) {
            creerVague();
            avantProchaineVague = 35 + hasard.nextInt(26); // écart aléatoire entre 35 et 60
        }
    }

    // Vide la route pour une nouvelle partie
    public void reinitialiser() {
        obstacles.clear();
        avantProchaineVague = 0;
    }

    // Pour l'instant : un seul obstacle dans un couloir au hasard
    // Crée une vague : pour chaque couloir, vide, bas ou haut au hasard
    private void creerVague() {
        // 1. On mélange les 3 couloirs, comme 3 cartes, pour en tirer sans doublon
        List<Integer> couloirs = new ArrayList<>(List.of(-1, 0, 1));
        Collections.shuffle(couloirs, hasard);

        // 2. Combien d'obstacles dans cette vague : 2 ou 3
        int nombre = 2 + hasard.nextInt(2);

        // 3. Un type au hasard pour chacun
        Obstacle.Type[] tousLesTypes = Obstacle.Type.values();   // { BAS, HAUT, GLISSADE }
        Obstacle.Type[] vague = new Obstacle.Type[nombre];
        for (int i = 0; i < nombre; i++) {
            vague[i] = tousLesTypes[hasard.nextInt(tousLesTypes.length)];
        }

        // 4. Règle : jamais 3 obstacles hauts, sinon impossible de passer
        if (nombre == 3 && vague[0] == Obstacle.Type.HAUT
                        && vague[1] == Obstacle.Type.HAUT
                        && vague[2] == Obstacle.Type.HAUT) {
            vague[hasard.nextInt(3)] = Obstacle.Type.BAS;  // l'un des trois devient franchissable
        }

        // 5. On crée les obstacles dans les couloirs tirés
        for (int i = 0; i < nombre; i++) {
            obstacles.add(new Obstacle(couloirs.get(i), vague[i]));
        }
    }

    // Obstacles PLUS LOIN que le joueur : dessinés AVANT lui (ils sont derrière)
    public void dessinerDerriere(Graphics g) {
        // 1. d'abord TOUS les trous : ils sont dans le sol, donc toujours sous le reste
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Obstacle o = obstacles.get(i);
            if (o.estUnTrou()) {
                o.dessiner(g);
            }
        }
        // 2. puis les autres obstacles pas encore atteints, du plus loin au plus proche
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Obstacle o = obstacles.get(i);
            if (!o.estUnTrou() && o.getDistance() > 0) {
                o.dessiner(g);
            }
        }
    }

    // Obstacles que le joueur a DÉJÀ PASSÉS : dessinés APRÈS lui (ils sont devant, côté caméra)
    public void dessinerDevant(Graphics g) {
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Obstacle o = obstacles.get(i);
            if (!o.estUnTrou() && o.getDistance() <= 0) {   // les trous restent sous le joueur {
                o.dessiner(g);
            }
        }
    }

        // Est-ce qu'au moins un obstacle touche le joueur ?
    public boolean toucheJoueur(int couloirJoueur, boolean joueurEnSaut, boolean joueurEnGlissade) {
        for (Obstacle o : obstacles) {
            if (o.touche(couloirJoueur, joueurEnSaut, joueurEnGlissade)) {
                return true;   // on en a trouvé un, pas besoin de regarder les autres
            }
        }
        return false;          // on a tout parcouru, aucun ne touche
    }

    public boolean obstacleProche(int couloir, double distance, double marge) {
        for (Obstacle o : obstacles) {
            if (o.getCouloir() == couloir && Math.abs(o.getDistance() - distance) < marge) {
                return true;
            }
        }
        return false;
    }

    
}