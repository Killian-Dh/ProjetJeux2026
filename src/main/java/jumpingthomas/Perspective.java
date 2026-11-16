package jumpingthomas;

public class Perspective {

    // 1.0 = taille normale (au niveau du joueur), 0.1 = tout petit (à l'horizon)
    public static double echelle(double distance) {
        return 1.0 / (1.0 + 9.0 * distance / 100.0);
    }

    // Position Y du sol à cette distance
    public static int ySol(double distance) {
        double f = (echelle(distance) - 0.1) / 0.9; // 0 à l'horizon, 1 au joueur
        int solJoueur = Game.JOUEUR_Y + Game.TAILLE; // les pieds du joueur
        return (int) (Game.HORIZON + (solJoueur - Game.HORIZON) * f);
    }

    // Position X du centre d'un couloir à cette distance
    public static int x(int couloir, double distance) {
        return (int) (Game.LARGEUR / 2 + couloir * (Game.LARGEUR / 3) * echelle(distance));
    }
}