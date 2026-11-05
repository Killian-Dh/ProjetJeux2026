package jumpingthomas;

public class Obstacle {

    private int couloir; // -1 = gauche, 0 = centre, 1 = droite

    private double distance; // 100 = à l'horizon, 0 = au niveau du joueur

    private boolean bas; // true = on peut sauter par-dessus

    public Obstacle(int couloir, boolean bas) {
        this.couloir = couloir;
        this.bas = bas;
        this.distance = 100;   // un nouvel obstacle apparaît toujours à l'horizon
    }



    
}
